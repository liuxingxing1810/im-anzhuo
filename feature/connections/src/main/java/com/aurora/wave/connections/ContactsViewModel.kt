package com.aurora.wave.connections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aurora.wave.connections.data.FakeContactsProvider
import com.aurora.wave.connections.model.ContactUiModel
import com.aurora.wave.connections.model.FriendRequestUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class ContactsState(
    val contacts: List<ContactUiModel> = emptyList(),
    val groupedContacts: Map<Char, List<ContactUiModel>> = emptyMap(),
    val friendRequests: List<FriendRequestUiModel> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = true,
    val selectedTab: ContactsTab = ContactsTab.CONTACTS,
    val error: String? = null
)

enum class ContactsTab {
    CONTACTS,
    NEW_FRIENDS
}

@HiltViewModel
class ContactsViewModel @Inject constructor() : ViewModel() {
    
    // 原始数据源
    private val _allContacts = MutableStateFlow<List<ContactUiModel>>(emptyList())
    private val _friendRequests = MutableStateFlow<List<FriendRequestUiModel>>(emptyList())
    private val _searchQuery = MutableStateFlow("")
    private val _isLoading = MutableStateFlow(true)
    private val _selectedTab = MutableStateFlow(ContactsTab.CONTACTS)
    
    // 使用 combine 实现响应式过滤
    val state: StateFlow<ContactsState> = combine(
        _allContacts,
        _friendRequests,
        _searchQuery,
        _isLoading,
        _selectedTab
    ) { contacts, friendRequests, query, isLoading, selectedTab ->
        val filteredContacts = if (query.isBlank()) {
            contacts
        } else {
            contacts.filter { contact ->
                contact.name.contains(query, ignoreCase = true) ||
                contact.bio?.contains(query, ignoreCase = true) == true
            }
        }
        
        ContactsState(
            contacts = filteredContacts,
            groupedContacts = FakeContactsProvider.groupContactsByLetter(filteredContacts),
            friendRequests = friendRequests,
            searchQuery = query,
            isLoading = isLoading,
            selectedTab = selectedTab
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ContactsState()
    )
    
    init {
        Timber.d("ContactsViewModel initialized")
        loadContacts()
    }
    
    private fun loadContacts() {
        viewModelScope.launch {
            _isLoading.value = true
            
            // Simulate network delay
            delay(300)
            
            _allContacts.value = FakeContactsProvider.generateContacts()
            _friendRequests.value = FakeContactsProvider.generateFriendRequests()
            _isLoading.value = false
            
            Timber.d("Loaded ${_allContacts.value.size} contacts, ${_friendRequests.value.size} friend requests")
        }
    }
    
    fun onSearchQueryChange(query: String) {
        Timber.d("Search query changed: $query")
        _searchQuery.value = query
    }
    
    fun onTabSelected(tab: ContactsTab) {
        Timber.d("Tab selected: $tab")
        _selectedTab.value = tab
    }
    
    fun acceptFriendRequest(requestId: String) {
        Timber.d("Accept friend request: $requestId")
        viewModelScope.launch {
            _friendRequests.value = _friendRequests.value.filter { it.id != requestId }
            // TODO: Call repository to accept request
        }
    }
    
    fun rejectFriendRequest(requestId: String) {
        Timber.d("Reject friend request: $requestId")
        viewModelScope.launch {
            _friendRequests.value = _friendRequests.value.filter { it.id != requestId }
            // TODO: Call repository to reject request
        }
    }
    
    fun refresh() {
        Timber.d("Refreshing contacts")
        loadContacts()
    }
}

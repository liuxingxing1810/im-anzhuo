package com.aurora.wave.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aurora.wave.messages.data.FakeDataProvider
import com.aurora.wave.messages.model.ConversationUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

data class ConversationListState(
    val conversations: List<ConversationUiModel> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ConversationListViewModel @Inject constructor() : ViewModel() {
    
    // 原始会话列表 (可以从 Repository 获取)
    private val _allConversations = MutableStateFlow(FakeDataProvider.conversations)
    
    // 搜索关键词
    private val _searchQuery = MutableStateFlow("")
    
    // Loading 状态
    private val _isLoading = MutableStateFlow(false)
    
    // 使用 combine 实现响应式过滤，避免每次重建列表
    val state: StateFlow<ConversationListState> = combine(
        _allConversations,
        _searchQuery,
        _isLoading
    ) { conversations, query, isLoading ->
        val filtered = if (query.isBlank()) {
            conversations
        } else {
            conversations.filter { conv ->
                conv.name.contains(query, ignoreCase = true) ||
                conv.lastMessage.contains(query, ignoreCase = true)
            }
        }
        ConversationListState(
            conversations = filtered,
            searchQuery = query,
            isLoading = isLoading
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ConversationListState(isLoading = true)
    )
    
    init {
        Timber.d("ConversationListViewModel initialized")
        loadConversations()
    }
    
    private fun loadConversations() {
        _isLoading.value = true
        // In real app, this would be from repository
        _allConversations.value = FakeDataProvider.conversations
        _isLoading.value = false
        Timber.d("Loaded ${_allConversations.value.size} conversations")
    }
    
    fun onSearchQueryChange(query: String) {
        Timber.d("Search query changed: $query")
        _searchQuery.value = query
    }
    
    fun onPinConversation(conversationId: String) {
        Timber.d("Pin conversation: $conversationId")
        _allConversations.value = _allConversations.value.map { conv ->
            if (conv.id == conversationId) conv.copy(isPinned = !conv.isPinned)
            else conv
        }.sortedWith(compareByDescending { it.isPinned })
    }
    
    fun onMuteConversation(conversationId: String) {
        Timber.d("Mute conversation: $conversationId")
        _allConversations.value = _allConversations.value.map { conv ->
            if (conv.id == conversationId) conv.copy(isMuted = !conv.isMuted)
            else conv
        }
    }
    
    fun onDeleteConversation(conversationId: String) {
        Timber.d("Delete conversation: $conversationId")
        _allConversations.value = _allConversations.value.filter { it.id != conversationId }
    }
}

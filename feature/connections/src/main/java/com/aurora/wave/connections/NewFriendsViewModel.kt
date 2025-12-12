package com.aurora.wave.connections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aurora.wave.connections.data.FakeContactsProvider
import com.aurora.wave.connections.model.FriendRequestUiModel
import com.aurora.wave.connections.model.FriendRequestStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NewFriendsState(
    val friendRequests: List<FriendRequestUiModel> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class NewFriendsViewModel @Inject constructor() : ViewModel() {
    
    private val _state = MutableStateFlow(NewFriendsState())
    val state: StateFlow<NewFriendsState> = _state.asStateFlow()
    
    init {
        loadFriendRequests()
    }
    
    private fun loadFriendRequests() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            // 模拟网络延迟
            delay(300)
            
            val requests = FakeContactsProvider.generateFriendRequests()
            _state.update { 
                it.copy(
                    friendRequests = requests,
                    isLoading = false
                )
            }
        }
    }
    
    fun acceptRequest(requestId: String) {
        viewModelScope.launch {
            _state.update { currentState ->
                val updatedRequests = currentState.friendRequests.map { request ->
                    if (request.id == requestId) {
                        request.copy(status = FriendRequestStatus.ACCEPTED)
                    } else {
                        request
                    }
                }
                currentState.copy(friendRequests = updatedRequests)
            }
        }
    }
    
    fun rejectRequest(requestId: String) {
        viewModelScope.launch {
            _state.update { currentState ->
                val updatedRequests = currentState.friendRequests.map { request ->
                    if (request.id == requestId) {
                        request.copy(status = FriendRequestStatus.REJECTED)
                    } else {
                        request
                    }
                }
                currentState.copy(friendRequests = updatedRequests)
            }
        }
    }
    
    fun refresh() {
        loadFriendRequests()
    }
}

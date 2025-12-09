package com.aurora.wave.messages

import androidx.lifecycle.ViewModel
import com.aurora.wave.data.model.DeliveryStatus
import com.aurora.wave.data.model.MessageContent
import com.aurora.wave.data.model.MessageType
import com.aurora.wave.messages.data.FakeDataProvider
import com.aurora.wave.messages.model.MessageUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

data class ChatDetailState(
    val conversationId: String = "",
    val conversationName: String = "",
    val conversationAvatar: String? = null,
    val isOnline: Boolean = false,
    val messages: List<MessageUiModel> = emptyList(),
    val inputText: String = "",
    val isLoading: Boolean = false,
    val isSending: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ChatDetailViewModel @Inject constructor() : ViewModel() {
    
    private val _state = MutableStateFlow(ChatDetailState())
    val state: StateFlow<ChatDetailState> = _state.asStateFlow()
    
    fun loadConversation(conversationId: String) {
        Timber.d("Loading conversation: $conversationId")
        _state.update { it.copy(isLoading = true, conversationId = conversationId) }
        
        // Find conversation info
        val conversation = FakeDataProvider.conversations.find { it.id == conversationId }
        val messages = FakeDataProvider.getMessagesForConversation(conversationId)
        
        _state.update {
            it.copy(
                conversationName = conversation?.name ?: "Chat",
                conversationAvatar = conversation?.avatarUrl,
                isOnline = conversation?.isOnline ?: false,
                messages = messages.reversed(), // Oldest first for display
                isLoading = false
            )
        }
        Timber.d("Loaded ${messages.size} messages for conversation: $conversationId")
    }
    
    fun onInputTextChange(text: String) {
        _state.update { it.copy(inputText = text) }
    }
    
    fun sendMessage() {
        val text = _state.value.inputText.trim()
        if (text.isBlank()) return
        
        Timber.d("Sending message: $text")
        _state.update { it.copy(isSending = true) }
        
        // Create new message
        val newMessage = MessageUiModel(
            id = UUID.randomUUID().toString(),
            senderId = "user_me",
            senderName = "Me",
            senderAvatar = null,
            type = MessageType.TEXT,
            content = MessageContent.Text(text),
            timestamp = "Just now",
            fullTimestamp = System.currentTimeMillis(),
            status = DeliveryStatus.SENDING,
            isOutgoing = true,
            isFirstInGroup = true,
            isLastInGroup = true
        )
        
        _state.update { currentState ->
            currentState.copy(
                messages = currentState.messages + newMessage,
                inputText = "",
                isSending = false
            )
        }
        
        Timber.d("Message sent successfully")
        // TODO: In real app, send via repository and update status
    }
    
    fun resendMessage(messageId: String) {
        Timber.d("Resending message: $messageId")
        // TODO: Implement resend logic
    }
    
    fun deleteMessage(messageId: String) {
        Timber.d("Deleting message: $messageId")
        _state.update { currentState ->
            currentState.copy(
                messages = currentState.messages.filter { it.id != messageId }
            )
        }
    }
}

package com.aurora.wave.messages

import android.net.Uri
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
                messages = messages, // Oldest first for display (newest at bottom)
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
    
    /**
     * 发送图片消息
     * Send image message
     */
    fun sendImageMessage(uri: Uri) {
        Timber.d("Sending image message: $uri")
        _state.update { it.copy(isSending = true) }
        
        val newMessage = MessageUiModel(
            id = UUID.randomUUID().toString(),
            senderId = "user_me",
            senderName = "Me",
            senderAvatar = null,
            type = MessageType.IMAGE,
            content = MessageContent.Image(
                url = uri.toString(),
                thumbnailUrl = uri.toString(),
                width = 0,
                height = 0
            ),
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
                isSending = false
            )
        }
        
        Timber.d("Image message sent successfully")
        // TODO: In real app, upload image and send via repository
    }
    
    /**
     * 发送视频消息
     * Send video message
     */
    fun sendVideoMessage(uri: Uri) {
        Timber.d("Sending video message: $uri")
        _state.update { it.copy(isSending = true) }
        
        val newMessage = MessageUiModel(
            id = UUID.randomUUID().toString(),
            senderId = "user_me",
            senderName = "Me",
            senderAvatar = null,
            type = MessageType.VIDEO,
            content = MessageContent.Video(
                url = uri.toString(),
                thumbnailUrl = uri.toString(),
                durationSeconds = 0,
                width = 0,
                height = 0
            ),
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
                isSending = false
            )
        }
        
        Timber.d("Video message sent successfully")
        // TODO: In real app, upload video and send via repository
    }
    
    /**
     * 发送位置消息
     * Send location message
     */
    fun sendLocationMessage(latitude: Double, longitude: Double, address: String?, name: String?) {
        Timber.d("Sending location message: lat=$latitude, lng=$longitude")
        _state.update { it.copy(isSending = true) }
        
        val newMessage = MessageUiModel(
            id = UUID.randomUUID().toString(),
            senderId = "user_me",
            senderName = "Me",
            senderAvatar = null,
            type = MessageType.LOCATION,
            content = MessageContent.Location(
                latitude = latitude,
                longitude = longitude,
                address = address,
                name = name
            ),
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
                isSending = false
            )
        }
        
        Timber.d("Location message sent successfully")
    }
    
    /**
     * 撤回消息
     * Recall message (within 2 minutes)
     */
    fun recallMessage(messageId: String) {
        val message = _state.value.messages.find { it.id == messageId }
        if (message == null) {
            Timber.w("Message not found for recall: $messageId")
            return
        }
        
        // 检查是否是自己发送的消息
        if (!message.isOutgoing) {
            Timber.w("Cannot recall other's message: $messageId")
            return
        }
        
        // 检查是否在2分钟内
        val twoMinutesAgo = System.currentTimeMillis() - 2 * 60 * 1000
        if (message.fullTimestamp < twoMinutesAgo) {
            Timber.w("Message too old to recall: $messageId")
            return
        }
        
        Timber.d("Recalling message: $messageId")
        
        // 将消息替换为撤回提示
        val recalledMessage = message.copy(
            type = MessageType.SYSTEM,
            content = MessageContent.System(
                text = "你撤回了一条消息",
                action = "message_recalled"
            ),
            status = DeliveryStatus.READ
        )
        
        _state.update { currentState ->
            currentState.copy(
                messages = currentState.messages.map { 
                    if (it.id == messageId) recalledMessage else it 
                }
            )
        }
        
        Timber.d("Message recalled successfully")
        // TODO: In real app, send recall request via repository
    }
}

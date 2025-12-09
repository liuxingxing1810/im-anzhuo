package com.aurora.wave.data.service

import com.aurora.wave.data.entity.MessageEntity
import com.aurora.wave.data.model.DeliveryStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

/**
 * Connection state for the IM service.
 */
enum class ConnectionState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    RECONNECTING,
    ERROR
}

/**
 * Events emitted by the IM service.
 */
sealed interface ImEvent {
    data class MessageReceived(val message: MessageEntity) : ImEvent
    data class MessageSent(val messageId: String, val status: DeliveryStatus) : ImEvent
    data class MessageDelivered(val messageId: String) : ImEvent
    data class MessageRead(val messageId: String, val readBy: String) : ImEvent
    data class MessageDeleted(val messageId: String) : ImEvent
    data class TypingStarted(val conversationId: String, val userId: String) : ImEvent
    data class TypingStopped(val conversationId: String, val userId: String) : ImEvent
    data class UserOnline(val userId: String) : ImEvent
    data class UserOffline(val userId: String) : ImEvent
    data class ConnectionChanged(val state: ConnectionState, val error: String? = null) : ImEvent
}

/**
 * IM (Instant Messaging) Service interface.
 * Handles real-time messaging via WebSocket or similar protocols.
 */
interface ImService {
    
    /**
     * Current connection state.
     */
    val connectionState: Flow<ConnectionState>
    
    /**
     * Events stream for real-time updates.
     */
    val events: SharedFlow<ImEvent>
    
    /**
     * Connect to the messaging server.
     * @param token Authentication token
     */
    suspend fun connect(token: String): Result<Unit>
    
    /**
     * Disconnect from the messaging server.
     */
    suspend fun disconnect()
    
    /**
     * Send a message to the server.
     * @param message The message entity to send
     * @return Result with the server-assigned message ID or error
     */
    suspend fun sendMessage(message: MessageEntity): Result<String>
    
    /**
     * Mark messages as read on the server.
     * @param conversationId The conversation ID
     * @param messageIds List of message IDs to mark as read
     */
    suspend fun markAsRead(conversationId: String, messageIds: List<String>): Result<Unit>
    
    /**
     * Start typing indicator for a conversation.
     */
    suspend fun startTyping(conversationId: String)
    
    /**
     * Stop typing indicator for a conversation.
     */
    suspend fun stopTyping(conversationId: String)
    
    /**
     * Request message history from server.
     * @param conversationId The conversation ID
     * @param beforeMessageId Load messages before this ID (for pagination)
     * @param limit Maximum number of messages to retrieve
     */
    suspend fun loadHistory(
        conversationId: String,
        beforeMessageId: String? = null,
        limit: Int = 50
    ): Result<List<MessageEntity>>
    
    /**
     * Sync local messages with server.
     * Used after reconnection to sync any missed messages.
     */
    suspend fun syncMessages(): Result<Int>
    
    /**
     * Upload a file for sending in messages.
     * @param filePath Local file path
     * @param mimeType File MIME type
     * @param progressCallback Optional callback for upload progress (0-100)
     * @return Result with the server URL of the uploaded file
     */
    suspend fun uploadFile(
        filePath: String,
        mimeType: String,
        progressCallback: ((Int) -> Unit)? = null
    ): Result<String>
    
    /**
     * Delete a message on the server.
     * @param messageId The message ID to delete
     * @param forEveryone If true, delete for all participants
     */
    suspend fun deleteMessage(messageId: String, forEveryone: Boolean): Result<Unit>
    
    /**
     * Check if connected to server.
     */
    fun isConnected(): Boolean
}

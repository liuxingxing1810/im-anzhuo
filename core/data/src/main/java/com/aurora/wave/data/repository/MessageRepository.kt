package com.aurora.wave.data.repository

import com.aurora.wave.data.entity.MessageEntity
import com.aurora.wave.data.entity.ConversationEntity
import com.aurora.wave.data.model.MessageType
import com.aurora.wave.data.model.DeliveryStatus
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for message-related operations.
 * Abstracts the data layer from ViewModels and use cases.
 */
interface MessageRepository {
    
    // ================== Message Operations ==================
    
    /**
     * Get all messages for a specific conversation as a Flow.
     */
    fun getMessagesForConversation(conversationId: String): Flow<List<MessageEntity>>
    
    /**
     * Get a single message by ID.
     */
    suspend fun getMessageById(messageId: String): MessageEntity?
    
    /**
     * Send a new text message.
     */
    suspend fun sendTextMessage(
        conversationId: String,
        text: String
    ): Result<MessageEntity>
    
    /**
     * Send an image message.
     */
    suspend fun sendImageMessage(
        conversationId: String,
        imageUri: String,
        caption: String? = null
    ): Result<MessageEntity>
    
    /**
     * Send a voice message.
     */
    suspend fun sendVoiceMessage(
        conversationId: String,
        audioUri: String,
        durationSeconds: Int
    ): Result<MessageEntity>
    
    /**
     * Send a file message.
     */
    suspend fun sendFileMessage(
        conversationId: String,
        fileUri: String,
        fileName: String,
        fileSize: Long,
        mimeType: String
    ): Result<MessageEntity>
    
    /**
     * Send a location message.
     */
    suspend fun sendLocationMessage(
        conversationId: String,
        latitude: Double,
        longitude: Double,
        address: String? = null
    ): Result<MessageEntity>
    
    /**
     * Resend a failed message.
     */
    suspend fun resendMessage(messageId: String): Result<MessageEntity>
    
    /**
     * Delete a message locally.
     */
    suspend fun deleteMessage(messageId: String, forEveryone: Boolean = false): Result<Unit>
    
    /**
     * Mark a message as read.
     */
    suspend fun markMessageAsRead(messageId: String): Result<Unit>
    
    /**
     * Mark all messages in a conversation as read.
     */
    suspend fun markAllMessagesAsRead(conversationId: String): Result<Unit>
    
    /**
     * Update message delivery status.
     */
    suspend fun updateDeliveryStatus(messageId: String, status: DeliveryStatus)
    
    /**
     * Search messages by keyword.
     */
    fun searchMessages(query: String): Flow<List<MessageEntity>>
    
    /**
     * Get unread message count for a conversation.
     */
    suspend fun getUnreadCount(conversationId: String): Int
    
    // ================== Conversation Operations ==================
    
    /**
     * Get all conversations as a Flow.
     */
    fun getAllConversations(): Flow<List<ConversationEntity>>
    
    /**
     * Get a single conversation by ID.
     */
    suspend fun getConversationById(conversationId: String): ConversationEntity?
    
    /**
     * Create or get a one-on-one conversation with a user.
     */
    suspend fun getOrCreatePrivateConversation(userId: String): Result<ConversationEntity>
    
    /**
     * Create a new group conversation.
     */
    suspend fun createGroupConversation(
        name: String,
        memberIds: List<String>,
        avatarUrl: String? = null
    ): Result<ConversationEntity>
    
    /**
     * Update conversation settings (mute, pin, etc.)
     */
    suspend fun updateConversation(
        conversationId: String,
        isPinned: Boolean? = null,
        isMuted: Boolean? = null,
        draft: String? = null
    ): Result<Unit>
    
    /**
     * Delete a conversation and optionally all its messages.
     */
    suspend fun deleteConversation(conversationId: String, deleteMessages: Boolean = true): Result<Unit>
    
    /**
     * Clear all messages in a conversation.
     */
    suspend fun clearConversation(conversationId: String): Result<Unit>
    
    /**
     * Archive a conversation.
     */
    suspend fun archiveConversation(conversationId: String): Result<Unit>
    
    /**
     * Unarchive a conversation.
     */
    suspend fun unarchiveConversation(conversationId: String): Result<Unit>
    
    /**
     * Search conversations by name.
     */
    fun searchConversations(query: String): Flow<List<ConversationEntity>>
}

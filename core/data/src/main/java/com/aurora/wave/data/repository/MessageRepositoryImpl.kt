package com.aurora.wave.data.repository

import com.aurora.wave.data.dao.ConversationDao
import com.aurora.wave.data.dao.MessageDao
import com.aurora.wave.data.entity.ConversationEntity
import com.aurora.wave.data.entity.MessageEntity
import com.aurora.wave.data.model.ConversationType
import com.aurora.wave.data.model.DeliveryStatus
import com.aurora.wave.data.model.MessageType
import com.aurora.wave.data.service.ImService
import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * Default implementation of MessageRepository.
 * Coordinates between local database and remote IM service.
 */
class MessageRepositoryImpl(
    private val messageDao: MessageDao,
    private val conversationDao: ConversationDao,
    // private val imService: ImService  // Will be injected when ImService is implemented
) : MessageRepository {
    
    // ================== Message Operations ==================
    
    override fun getMessagesForConversation(conversationId: String): Flow<List<MessageEntity>> {
        return messageDao.getMessagesForConversation(conversationId)
    }
    
    override suspend fun getMessageById(messageId: String): MessageEntity? {
        return messageDao.getMessageById(messageId)
    }
    
    override suspend fun sendTextMessage(
        conversationId: String,
        text: String
    ): Result<MessageEntity> {
        return try {
            val message = createMessageEntity(
                conversationId = conversationId,
                type = MessageType.TEXT,
                content = text
            )
            
            // Save locally first
            messageDao.insertMessage(message)
            
            // Update conversation's last message
            updateConversationLastMessage(conversationId, text)
            
            // TODO: Send to server via ImService
            // val serverMessageId = imService.sendMessage(message)
            // messageDao.updateMessageId(message.id, serverMessageId)
            
            Result.success(message)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun sendImageMessage(
        conversationId: String,
        imageUri: String,
        caption: String?
    ): Result<MessageEntity> {
        return try {
            val content = buildImageContent(imageUri, caption)
            val message = createMessageEntity(
                conversationId = conversationId,
                type = MessageType.IMAGE,
                content = content
            )
            
            messageDao.insertMessage(message)
            updateConversationLastMessage(conversationId, "[Image]")
            
            Result.success(message)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun sendVoiceMessage(
        conversationId: String,
        audioUri: String,
        durationSeconds: Int
    ): Result<MessageEntity> {
        return try {
            val content = buildVoiceContent(audioUri, durationSeconds)
            val message = createMessageEntity(
                conversationId = conversationId,
                type = MessageType.VOICE,
                content = content
            )
            
            messageDao.insertMessage(message)
            updateConversationLastMessage(conversationId, "[Voice ${formatDuration(durationSeconds)}]")
            
            Result.success(message)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun sendFileMessage(
        conversationId: String,
        fileUri: String,
        fileName: String,
        fileSize: Long,
        mimeType: String
    ): Result<MessageEntity> {
        return try {
            val content = buildFileContent(fileUri, fileName, fileSize, mimeType)
            val message = createMessageEntity(
                conversationId = conversationId,
                type = MessageType.FILE,
                content = content
            )
            
            messageDao.insertMessage(message)
            updateConversationLastMessage(conversationId, "[File] $fileName")
            
            Result.success(message)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun sendLocationMessage(
        conversationId: String,
        latitude: Double,
        longitude: Double,
        address: String?
    ): Result<MessageEntity> {
        return try {
            val content = buildLocationContent(latitude, longitude, address)
            val message = createMessageEntity(
                conversationId = conversationId,
                type = MessageType.LOCATION,
                content = content
            )
            
            messageDao.insertMessage(message)
            updateConversationLastMessage(conversationId, "[Location] ${address ?: "Shared location"}")
            
            Result.success(message)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun resendMessage(messageId: String): Result<MessageEntity> {
        return try {
            val message = messageDao.getMessageById(messageId)
                ?: return Result.failure(IllegalArgumentException("Message not found"))
            
            // Update status to sending
            messageDao.updateDeliveryStatus(messageId, DeliveryStatus.SENDING)
            
            // TODO: Resend via ImService
            
            Result.success(message.copy(deliveryStatus = DeliveryStatus.SENDING))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deleteMessage(messageId: String, forEveryone: Boolean): Result<Unit> {
        return try {
            messageDao.deleteMessageById(messageId)
            
            // TODO: If forEveryone, notify server via ImService
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun markMessageAsRead(messageId: String): Result<Unit> {
        return try {
            messageDao.updateDeliveryStatus(messageId, DeliveryStatus.READ)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun markAllMessagesAsRead(conversationId: String): Result<Unit> {
        return try {
            messageDao.markAllAsRead(conversationId)
            conversationDao.updateUnreadCount(conversationId, 0)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateDeliveryStatus(messageId: String, status: DeliveryStatus) {
        messageDao.updateDeliveryStatus(messageId, status)
    }
    
    override fun searchMessages(query: String): Flow<List<MessageEntity>> {
        return messageDao.searchMessages("%$query%")
    }
    
    override suspend fun getUnreadCount(conversationId: String): Int {
        return messageDao.getUnreadCountForConversation(conversationId)
    }
    
    // ================== Conversation Operations ==================
    
    override fun getAllConversations(): Flow<List<ConversationEntity>> {
        return conversationDao.getAllConversations()
    }
    
    override suspend fun getConversationById(conversationId: String): ConversationEntity? {
        return conversationDao.getConversationById(conversationId)
    }
    
    override suspend fun getOrCreatePrivateConversation(userId: String): Result<ConversationEntity> {
        return try {
            // Check if conversation already exists
            var conversation = conversationDao.getPrivateConversationWithUser(userId)
            
            if (conversation == null) {
                // Create new conversation
                conversation = ConversationEntity(
                    id = UUID.randomUUID().toString(),
                    name = "", // Will be filled with user's name
                    type = ConversationType.DIRECT,
                    lastMessage = null,
                    lastMessageTimestamp = System.currentTimeMillis(),
                    unreadCount = 0,
                    isPinned = false,
                    isMuted = false,
                    avatarUrl = null,
                    draft = null,
                    isArchived = false,
                    participantIds = listOf(userId),
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
                conversationDao.insertConversation(conversation)
            }
            
            Result.success(conversation)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun createGroupConversation(
        name: String,
        memberIds: List<String>,
        avatarUrl: String?
    ): Result<ConversationEntity> {
        return try {
            val conversation = ConversationEntity(
                id = UUID.randomUUID().toString(),
                name = name,
                type = ConversationType.GROUP,
                lastMessage = null,
                lastMessageTimestamp = System.currentTimeMillis(),
                unreadCount = 0,
                isPinned = false,
                isMuted = false,
                avatarUrl = avatarUrl,
                draft = null,
                isArchived = false,
                participantIds = memberIds,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            conversationDao.insertConversation(conversation)
            
            Result.success(conversation)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateConversation(
        conversationId: String,
        isPinned: Boolean?,
        isMuted: Boolean?,
        draft: String?
    ): Result<Unit> {
        return try {
            isPinned?.let { conversationDao.updatePinned(conversationId, it) }
            isMuted?.let { conversationDao.updateMuted(conversationId, it) }
            draft?.let { conversationDao.updateDraft(conversationId, it) }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deleteConversation(conversationId: String, deleteMessages: Boolean): Result<Unit> {
        return try {
            if (deleteMessages) {
                messageDao.deleteMessagesForConversation(conversationId)
            }
            conversationDao.deleteConversationById(conversationId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun clearConversation(conversationId: String): Result<Unit> {
        return try {
            messageDao.deleteMessagesForConversation(conversationId)
            conversationDao.updateLastMessage(conversationId, null, System.currentTimeMillis())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun archiveConversation(conversationId: String): Result<Unit> {
        return try {
            conversationDao.updateArchived(conversationId, true)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun unarchiveConversation(conversationId: String): Result<Unit> {
        return try {
            conversationDao.updateArchived(conversationId, false)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override fun searchConversations(query: String): Flow<List<ConversationEntity>> {
        return conversationDao.searchConversations("%$query%")
    }
    
    // ================== Helper Methods ==================
    
    private fun createMessageEntity(
        conversationId: String,
        type: MessageType,
        content: String
    ): MessageEntity {
        // TODO: Get current user ID from auth service
        val currentUserId = "current_user"
        
        return MessageEntity(
            id = UUID.randomUUID().toString(),
            conversationId = conversationId,
            senderId = currentUserId,
            type = type,
            content = content,
            timestamp = System.currentTimeMillis(),
            deliveryStatus = DeliveryStatus.SENDING,
            isEdited = false,
            replyToMessageId = null,
            forwardedFromId = null,
            mentions = null,
            reactions = null
        )
    }
    
    private suspend fun updateConversationLastMessage(conversationId: String, message: String) {
        conversationDao.updateLastMessage(conversationId, message, System.currentTimeMillis())
    }
    
    private fun buildImageContent(uri: String, caption: String?): String {
        // JSON format for image content
        return """{"url":"$uri","caption":${caption?.let { "\"$it\"" } ?: "null"}}"""
    }
    
    private fun buildVoiceContent(uri: String, duration: Int): String {
        return """{"url":"$uri","duration":$duration}"""
    }
    
    private fun buildFileContent(uri: String, name: String, size: Long, mimeType: String): String {
        return """{"url":"$uri","name":"$name","size":$size,"mimeType":"$mimeType"}"""
    }
    
    private fun buildLocationContent(lat: Double, lng: Double, address: String?): String {
        return """{"latitude":$lat,"longitude":$lng,"address":${address?.let { "\"$it\"" } ?: "null"}}"""
    }
    
    private fun formatDuration(seconds: Int): String {
        val mins = seconds / 60
        val secs = seconds % 60
        return if (mins > 0) "$mins:${secs.toString().padStart(2, '0')}" else "0:$secs"
    }
}

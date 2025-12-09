package com.aurora.wave.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aurora.wave.data.entity.MessageEntity
import com.aurora.wave.data.model.DeliveryStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    
    @Query("SELECT * FROM messages WHERE conversationId = :conversationId AND isDeleted = 0 ORDER BY timestamp DESC")
    fun getMessagesFlow(conversationId: String): Flow<List<MessageEntity>>
    
    @Query("SELECT * FROM messages WHERE conversationId = :conversationId AND isDeleted = 0 ORDER BY timestamp ASC")
    fun getMessagesForConversation(conversationId: String): Flow<List<MessageEntity>>
    
    @Query("SELECT * FROM messages WHERE conversationId = :conversationId AND isDeleted = 0 ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentMessages(conversationId: String, limit: Int = 50): Flow<List<MessageEntity>>
    
    @Query("SELECT * FROM messages WHERE conversationId = :conversationId AND timestamp < :beforeTimestamp AND isDeleted = 0 ORDER BY timestamp DESC LIMIT :limit")
    suspend fun loadMoreMessages(conversationId: String, beforeTimestamp: Long, limit: Int = 30): List<MessageEntity>
    
    @Query("SELECT * FROM messages WHERE id = :messageId")
    suspend fun getMessageById(messageId: String): MessageEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntity>)
    
    @Update
    suspend fun updateMessage(message: MessageEntity)
    
    @Query("UPDATE messages SET deliveryStatus = :status WHERE id = :messageId")
    suspend fun updateDeliveryStatus(messageId: String, status: DeliveryStatus)
    
    @Query("UPDATE messages SET deliveryStatus = :status WHERE conversationId = :conversationId AND senderId != :currentUserId")
    suspend fun markAllAsRead(conversationId: String, status: DeliveryStatus = DeliveryStatus.READ, currentUserId: String = "current_user")
    
    @Query("UPDATE messages SET isDeleted = 1 WHERE id = :messageId")
    suspend fun softDeleteMessage(messageId: String)
    
    @Query("DELETE FROM messages WHERE id = :messageId")
    suspend fun deleteMessageById(messageId: String)
    
    @Query("DELETE FROM messages WHERE conversationId = :conversationId")
    suspend fun deleteMessagesForConversation(conversationId: String)
    
    @Query("DELETE FROM messages WHERE conversationId = :conversationId")
    suspend fun deleteAllMessagesInConversation(conversationId: String)
    
    @Query("SELECT COUNT(*) FROM messages WHERE conversationId = :conversationId AND isDeleted = 0")
    suspend fun getMessageCount(conversationId: String): Int
    
    @Query("SELECT COUNT(*) FROM messages WHERE conversationId = :conversationId AND deliveryStatus != 'READ' AND senderId != :currentUserId")
    suspend fun getUnreadCountForConversation(conversationId: String, currentUserId: String = "current_user"): Int
    
    @Query("SELECT * FROM messages WHERE content LIKE :query AND isDeleted = 0 ORDER BY timestamp DESC")
    fun searchMessages(query: String): Flow<List<MessageEntity>>
}

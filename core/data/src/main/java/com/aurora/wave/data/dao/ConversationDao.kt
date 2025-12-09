package com.aurora.wave.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aurora.wave.data.entity.ConversationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationDao {
    
    @Query("SELECT * FROM conversations WHERE isArchived = 0 ORDER BY isPinned DESC, lastMessageTimestamp DESC")
    fun getAllConversations(): Flow<List<ConversationEntity>>
    
    @Query("SELECT * FROM conversations ORDER BY isPinned DESC, lastMessageTimestamp DESC")
    fun getAllConversationsFlow(): Flow<List<ConversationEntity>>
    
    @Query("SELECT * FROM conversations WHERE name LIKE :query ORDER BY isPinned DESC, lastMessageTimestamp DESC")
    fun searchConversations(query: String): Flow<List<ConversationEntity>>
    
    @Query("SELECT * FROM conversations WHERE id = :conversationId")
    suspend fun getConversationById(conversationId: String): ConversationEntity?
    
    @Query("SELECT * FROM conversations WHERE type = 'DIRECT' AND participantIds LIKE '%' || :userId || '%' LIMIT 1")
    suspend fun getPrivateConversationWithUser(userId: String): ConversationEntity?
    
    @Query("SELECT * FROM conversations WHERE id = :conversationId")
    fun getConversationFlow(conversationId: String): Flow<ConversationEntity?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conversation: ConversationEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversations(conversations: List<ConversationEntity>)
    
    @Update
    suspend fun updateConversation(conversation: ConversationEntity)
    
    @Query("UPDATE conversations SET lastMessage = :preview, lastMessageTimestamp = :timestamp, updatedAt = :timestamp WHERE id = :conversationId")
    suspend fun updateLastMessage(conversationId: String, preview: String?, timestamp: Long)
    
    @Query("UPDATE conversations SET unreadCount = unreadCount + 1 WHERE id = :conversationId")
    suspend fun incrementUnreadCount(conversationId: String)
    
    @Query("UPDATE conversations SET unreadCount = :count WHERE id = :conversationId")
    suspend fun updateUnreadCount(conversationId: String, count: Int)
    
    @Query("UPDATE conversations SET unreadCount = 0 WHERE id = :conversationId")
    suspend fun clearUnreadCount(conversationId: String)
    
    @Query("UPDATE conversations SET isPinned = :isPinned WHERE id = :conversationId")
    suspend fun updatePinned(conversationId: String, isPinned: Boolean)
    
    @Query("UPDATE conversations SET isPinned = :isPinned WHERE id = :conversationId")
    suspend fun setPinned(conversationId: String, isPinned: Boolean)
    
    @Query("UPDATE conversations SET isMuted = :isMuted WHERE id = :conversationId")
    suspend fun updateMuted(conversationId: String, isMuted: Boolean)
    
    @Query("UPDATE conversations SET isMuted = :isMuted WHERE id = :conversationId")
    suspend fun setMuted(conversationId: String, isMuted: Boolean)
    
    @Query("UPDATE conversations SET draft = :draft WHERE id = :conversationId")
    suspend fun updateDraft(conversationId: String, draft: String?)
    
    @Query("UPDATE conversations SET draft = :draft WHERE id = :conversationId")
    suspend fun saveDraft(conversationId: String, draft: String?)
    
    @Query("UPDATE conversations SET isArchived = :archived WHERE id = :conversationId")
    suspend fun updateArchived(conversationId: String, archived: Boolean)
    
    @Query("DELETE FROM conversations WHERE id = :conversationId")
    suspend fun deleteConversationById(conversationId: String)
    
    @Query("DELETE FROM conversations WHERE id = :conversationId")
    suspend fun deleteConversation(conversationId: String)
    
    @Query("SELECT SUM(unreadCount) FROM conversations")
    fun getTotalUnreadCount(): Flow<Int?>
}

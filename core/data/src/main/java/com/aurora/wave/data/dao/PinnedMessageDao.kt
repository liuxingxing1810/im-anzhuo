package com.aurora.wave.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aurora.wave.data.entity.PinnedMessageEntity
import kotlinx.coroutines.flow.Flow

/**
 * 置顶消息 DAO
 * 
 * 管理会话中的置顶消息。
 */
@Dao
interface PinnedMessageDao {
    
    // ==================== 查询操作 ====================
    
    @Query("SELECT * FROM pinned_messages WHERE conversationId = :conversationId ORDER BY sortOrder ASC, pinnedAt DESC")
    fun getByConversationId(conversationId: String): Flow<List<PinnedMessageEntity>>
    
    @Query("SELECT * FROM pinned_messages WHERE conversationId = :conversationId AND messageId = :messageId")
    suspend fun getByConversationAndMessage(conversationId: String, messageId: String): PinnedMessageEntity?
    
    @Query("SELECT COUNT(*) FROM pinned_messages WHERE conversationId = :conversationId")
    suspend fun getPinnedCount(conversationId: String): Int
    
    @Query("SELECT EXISTS(SELECT 1 FROM pinned_messages WHERE conversationId = :conversationId AND messageId = :messageId)")
    suspend fun isPinned(conversationId: String, messageId: String): Boolean
    
    // ==================== 插入操作 ====================
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pinnedMessage: PinnedMessageEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pinnedMessages: List<PinnedMessageEntity>)
    
    // ==================== 更新操作 ====================
    
    @Query("UPDATE pinned_messages SET sortOrder = :sortOrder WHERE conversationId = :conversationId AND messageId = :messageId")
    suspend fun updateSortOrder(conversationId: String, messageId: String, sortOrder: Int)
    
    // ==================== 删除操作 ====================
    
    @Query("DELETE FROM pinned_messages WHERE conversationId = :conversationId AND messageId = :messageId")
    suspend fun delete(conversationId: String, messageId: String)
    
    @Query("DELETE FROM pinned_messages WHERE conversationId = :conversationId")
    suspend fun deleteByConversation(conversationId: String)
}

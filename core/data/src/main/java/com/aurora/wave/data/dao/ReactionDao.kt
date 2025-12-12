package com.aurora.wave.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aurora.wave.data.entity.ReactionEntity
import kotlinx.coroutines.flow.Flow

/**
 * 消息反应 DAO
 * 
 * 管理消息的表情反应。
 */
@Dao
interface ReactionDao {
    
    // ==================== 查询操作 ====================
    
    @Query("SELECT * FROM reactions WHERE messageId = :messageId ORDER BY createdAt ASC")
    fun getByMessageId(messageId: String): Flow<List<ReactionEntity>>
    
    @Query("SELECT * FROM reactions WHERE messageId = :messageId AND emoji = :emoji")
    suspend fun getByMessageIdAndEmoji(messageId: String, emoji: String): List<ReactionEntity>
    
    @Query("SELECT * FROM reactions WHERE messageId = :messageId AND userId = :userId")
    suspend fun getByMessageIdAndUserId(messageId: String, userId: String): List<ReactionEntity>
    
    @Query("SELECT DISTINCT emoji FROM reactions WHERE messageId = :messageId")
    suspend fun getEmojisByMessageId(messageId: String): List<String>
    
    @Query("SELECT COUNT(*) FROM reactions WHERE messageId = :messageId AND emoji = :emoji")
    suspend fun getReactionCount(messageId: String, emoji: String): Int
    
    // ==================== 插入操作 ====================
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reaction: ReactionEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(reactions: List<ReactionEntity>)
    
    // ==================== 删除操作 ====================
    
    @Query("DELETE FROM reactions WHERE messageId = :messageId AND userId = :userId AND emoji = :emoji")
    suspend fun delete(messageId: String, userId: String, emoji: String)
    
    @Query("DELETE FROM reactions WHERE messageId = :messageId AND userId = :userId")
    suspend fun deleteAllByUser(messageId: String, userId: String)
    
    @Query("DELETE FROM reactions WHERE messageId = :messageId")
    suspend fun deleteByMessageId(messageId: String)
}

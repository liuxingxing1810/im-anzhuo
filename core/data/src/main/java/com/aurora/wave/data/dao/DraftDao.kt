package com.aurora.wave.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aurora.wave.data.entity.DraftEntity
import kotlinx.coroutines.flow.Flow

/**
 * 草稿 DAO
 * 
 * 管理会话草稿的增删改查。
 */
@Dao
interface DraftDao {
    
    // ==================== 查询操作 ====================
    
    @Query("SELECT * FROM drafts WHERE conversationId = :conversationId")
    suspend fun getByConversationId(conversationId: String): DraftEntity?
    
    @Query("SELECT * FROM drafts WHERE conversationId = :conversationId")
    fun getByConversationIdFlow(conversationId: String): Flow<DraftEntity?>
    
    @Query("SELECT * FROM drafts ORDER BY updatedAt DESC")
    fun getAllDrafts(): Flow<List<DraftEntity>>
    
    @Query("SELECT COUNT(*) FROM drafts")
    suspend fun getDraftCount(): Int
    
    // ==================== 插入/更新操作 ====================
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(draft: DraftEntity)
    
    @Update
    suspend fun update(draft: DraftEntity)
    
    @Query("UPDATE drafts SET content = :content, updatedAt = :timestamp WHERE conversationId = :conversationId")
    suspend fun updateContent(conversationId: String, content: String, timestamp: Long = System.currentTimeMillis())
    
    // ==================== 删除操作 ====================
    
    @Query("DELETE FROM drafts WHERE conversationId = :conversationId")
    suspend fun deleteByConversationId(conversationId: String)
    
    @Query("DELETE FROM drafts")
    suspend fun deleteAll()
}

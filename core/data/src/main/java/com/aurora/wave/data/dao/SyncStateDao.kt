package com.aurora.wave.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aurora.wave.data.entity.SyncStateEntity
import com.aurora.wave.data.model.SyncStatus
import kotlinx.coroutines.flow.Flow

/**
 * 同步状态 DAO
 * 
 * 管理会话的同步状态，支持增量同步机制。
 */
@Dao
interface SyncStateDao {
    
    // ==================== 查询操作 ====================
    
    @Query("SELECT * FROM sync_state WHERE conversationId = :conversationId")
    suspend fun getByConversationId(conversationId: String): SyncStateEntity?
    
    @Query("SELECT * FROM sync_state WHERE conversationId = :conversationId")
    fun getByConversationIdFlow(conversationId: String): Flow<SyncStateEntity?>
    
    @Query("SELECT * FROM sync_state WHERE conversationId = 'global'")
    suspend fun getGlobalSyncState(): SyncStateEntity?
    
    @Query("SELECT * FROM sync_state WHERE syncStatus = :status")
    fun getBySyncStatus(status: SyncStatus): Flow<List<SyncStateEntity>>
    
    @Query("SELECT * FROM sync_state WHERE syncStatus = 'PENDING' OR syncStatus = 'FAILED' ORDER BY updatedAt ASC")
    suspend fun getPendingSyncStates(): List<SyncStateEntity>
    
    // ==================== 插入/更新操作 ====================
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(syncState: SyncStateEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(syncStates: List<SyncStateEntity>)
    
    @Update
    suspend fun update(syncState: SyncStateEntity)
    
    @Query("UPDATE sync_state SET syncStatus = :status, updatedAt = :timestamp WHERE conversationId = :conversationId")
    suspend fun updateSyncStatus(conversationId: String, status: SyncStatus, timestamp: Long = System.currentTimeMillis())
    
    @Query("UPDATE sync_state SET lastSyncTimestamp = :timestamp, serverSeq = :serverSeq, syncStatus = 'SYNCED', updatedAt = :timestamp WHERE conversationId = :conversationId")
    suspend fun markSynced(conversationId: String, timestamp: Long, serverSeq: Long? = null)
    
    @Query("UPDATE sync_state SET syncStatus = 'FAILED', failureCount = failureCount + 1, lastError = :error, updatedAt = :timestamp WHERE conversationId = :conversationId")
    suspend fun markFailed(conversationId: String, error: String?, timestamp: Long = System.currentTimeMillis())
    
    @Query("UPDATE sync_state SET failureCount = 0, lastError = NULL WHERE conversationId = :conversationId")
    suspend fun resetFailureCount(conversationId: String)
    
    // ==================== 删除操作 ====================
    
    @Query("DELETE FROM sync_state WHERE conversationId = :conversationId")
    suspend fun deleteByConversationId(conversationId: String)
    
    @Query("DELETE FROM sync_state")
    suspend fun deleteAll()
}

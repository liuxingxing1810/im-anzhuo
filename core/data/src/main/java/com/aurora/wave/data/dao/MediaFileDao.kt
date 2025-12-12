package com.aurora.wave.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aurora.wave.data.entity.MediaFileEntity
import com.aurora.wave.data.model.DownloadStatus
import com.aurora.wave.data.model.MediaType
import com.aurora.wave.data.model.UploadStatus
import kotlinx.coroutines.flow.Flow

/**
 * 媒体文件 DAO
 * 
 * 提供媒体文件的增删改查操作。
 */
@Dao
interface MediaFileDao {
    
    // ==================== 查询操作 ====================
    
    @Query("SELECT * FROM media_files WHERE id = :id")
    suspend fun getById(id: String): MediaFileEntity?
    
    @Query("SELECT * FROM media_files WHERE messageId = :messageId")
    suspend fun getByMessageId(messageId: String): MediaFileEntity?
    
    @Query("SELECT * FROM media_files WHERE messageId = :messageId")
    fun getByMessageIdFlow(messageId: String): Flow<MediaFileEntity?>
    
    @Query("SELECT * FROM media_files WHERE conversationId = :conversationId ORDER BY createdAt DESC")
    fun getByConversation(conversationId: String): Flow<List<MediaFileEntity>>
    
    @Query("SELECT * FROM media_files WHERE conversationId = :conversationId AND type = :type ORDER BY createdAt DESC")
    fun getByConversationAndType(conversationId: String, type: MediaType): Flow<List<MediaFileEntity>>
    
    @Query("SELECT * FROM media_files WHERE type = :type ORDER BY createdAt DESC LIMIT :limit")
    fun getByType(type: MediaType, limit: Int = 100): Flow<List<MediaFileEntity>>
    
    @Query("SELECT * FROM media_files WHERE downloadStatus = :status ORDER BY createdAt ASC")
    fun getByDownloadStatus(status: DownloadStatus): Flow<List<MediaFileEntity>>
    
    @Query("SELECT * FROM media_files WHERE uploadStatus = :status ORDER BY createdAt ASC")
    fun getByUploadStatus(status: UploadStatus): Flow<List<MediaFileEntity>>
    
    // ==================== 插入操作 ====================
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mediaFile: MediaFileEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(mediaFiles: List<MediaFileEntity>)
    
    // ==================== 更新操作 ====================
    
    @Update
    suspend fun update(mediaFile: MediaFileEntity)
    
    @Query("UPDATE media_files SET downloadStatus = :status, progress = :progress WHERE id = :id")
    suspend fun updateDownloadStatus(id: String, status: DownloadStatus, progress: Int = 0)
    
    @Query("UPDATE media_files SET uploadStatus = :status, progress = :progress WHERE id = :id")
    suspend fun updateUploadStatus(id: String, status: UploadStatus, progress: Int = 0)
    
    @Query("UPDATE media_files SET localPath = :localPath, downloadStatus = :status WHERE id = :id")
    suspend fun updateLocalPath(id: String, localPath: String, status: DownloadStatus = DownloadStatus.COMPLETED)
    
    @Query("UPDATE media_files SET thumbnailPath = :thumbnailPath WHERE id = :id")
    suspend fun updateThumbnailPath(id: String, thumbnailPath: String)
    
    @Query("UPDATE media_files SET lastAccessedAt = :timestamp WHERE id = :id")
    suspend fun updateLastAccessedAt(id: String, timestamp: Long = System.currentTimeMillis())
    
    // ==================== 删除操作 ====================
    
    @Query("DELETE FROM media_files WHERE id = :id")
    suspend fun deleteById(id: String)
    
    @Query("DELETE FROM media_files WHERE messageId = :messageId")
    suspend fun deleteByMessageId(messageId: String)
    
    @Query("DELETE FROM media_files WHERE conversationId = :conversationId")
    suspend fun deleteByConversation(conversationId: String)
    
    // ==================== 统计查询 ====================
    
    @Query("SELECT SUM(fileSize) FROM media_files WHERE conversationId = :conversationId")
    suspend fun getTotalSizeByConversation(conversationId: String): Long?
    
    @Query("SELECT SUM(fileSize) FROM media_files")
    suspend fun getTotalSize(): Long?
    
    @Query("SELECT COUNT(*) FROM media_files WHERE downloadStatus = :status")
    suspend fun getCountByDownloadStatus(status: DownloadStatus): Int
    
    // ==================== 缓存清理 ====================
    
    @Query("SELECT * FROM media_files WHERE lastAccessedAt < :beforeTimestamp AND localPath IS NOT NULL ORDER BY lastAccessedAt ASC LIMIT :limit")
    suspend fun getOldCachedFiles(beforeTimestamp: Long, limit: Int = 100): List<MediaFileEntity>
    
    @Query("UPDATE media_files SET localPath = NULL, thumbnailPath = NULL, downloadStatus = 'PENDING' WHERE id IN (:ids)")
    suspend fun clearLocalPaths(ids: List<String>)
}

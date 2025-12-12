package com.aurora.wave.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.aurora.wave.data.model.SyncStatus

/**
 * 同步状态实体
 * 
 * 记录每个会话的同步状态，用于增量同步机制。
 * 支持离线消息同步、断点续传等场景。
 */
@Entity(
    tableName = "sync_state",
    indices = [
        Index(value = ["syncStatus"])
    ]
)
data class SyncStateEntity(
    @PrimaryKey
    val conversationId: String,  // 会话ID，"global" 表示全局同步状态
    
    /** 上次同步的时间戳 */
    val lastSyncTimestamp: Long = 0L,
    
    /** 服务端序列号（用于增量同步） */
    val serverSeq: Long? = null,
    
    /** 本地序列号 */
    val localSeq: Long? = null,
    
    /** 同步状态 */
    val syncStatus: SyncStatus = SyncStatus.PENDING,
    
    /** 上次同步的消息ID */
    val lastSyncedMessageId: String? = null,
    
    /** 同步失败次数 */
    val failureCount: Int = 0,
    
    /** 最后一次同步错误信息 */
    val lastError: String? = null,
    
    /** 最后更新时间 */
    val updatedAt: Long = System.currentTimeMillis()
)

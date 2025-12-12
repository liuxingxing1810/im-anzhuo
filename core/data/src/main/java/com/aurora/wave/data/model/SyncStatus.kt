package com.aurora.wave.data.model

/**
 * 同步状态
 */
enum class SyncStatus {
    SYNCED,     // 已同步
    SYNCING,    // 同步中
    PENDING,    // 待同步
    FAILED      // 同步失败
}

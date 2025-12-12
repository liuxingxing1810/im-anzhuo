package com.aurora.wave.data.model

/**
 * 媒体文件下载状态
 */
enum class DownloadStatus {
    PENDING,        // 等待下载
    DOWNLOADING,    // 下载中
    COMPLETED,      // 下载完成
    FAILED          // 下载失败
}

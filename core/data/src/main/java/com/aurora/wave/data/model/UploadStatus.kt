package com.aurora.wave.data.model

/**
 * 媒体文件上传状态
 */
enum class UploadStatus {
    PENDING,        // 等待上传
    UPLOADING,      // 上传中
    COMPLETED,      // 上传完成
    FAILED          // 上传失败
}

package com.aurora.wave.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.aurora.wave.data.model.DownloadStatus
import com.aurora.wave.data.model.MediaType
import com.aurora.wave.data.model.UploadStatus

/**
 * 媒体文件实体
 * 
 * 存储媒体文件的索引信息，原文件存储在文件系统中。
 * 支持图片、视频、音频、文档等多种类型。
 */
@Entity(
    tableName = "media_files",
    foreignKeys = [
        ForeignKey(
            entity = MessageEntity::class,
            parentColumns = ["id"],
            childColumns = ["messageId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["messageId"]),
        Index(value = ["conversationId"]),
        Index(value = ["type"]),
        Index(value = ["downloadStatus"])
    ]
)
data class MediaFileEntity(
    @PrimaryKey
    val id: String,
    
    /** 关联的消息ID */
    val messageId: String,
    
    /** 关联的会话ID（冗余字段，便于查询） */
    val conversationId: String,
    
    /** 媒体类型：IMAGE, VIDEO, AUDIO, DOCUMENT, STICKER */
    val type: MediaType,
    
    /** 原始文件名 */
    val fileName: String,
    
    /** 文件大小（字节） */
    val fileSize: Long,
    
    /** MIME 类型，如 "image/jpeg", "video/mp4" */
    val mimeType: String,
    
    /** 本地存储路径（下载完成后） */
    val localPath: String? = null,
    
    /** 远程服务器路径/URL */
    val remotePath: String,
    
    /** 缩略图本地路径 */
    val thumbnailPath: String? = null,
    
    /** 缩略图远程路径 */
    val thumbnailRemotePath: String? = null,
    
    /** 下载状态 */
    val downloadStatus: DownloadStatus = DownloadStatus.PENDING,
    
    /** 上传状态（用于发送） */
    val uploadStatus: UploadStatus = UploadStatus.COMPLETED,
    
    /** 下载/上传进度 (0-100) */
    val progress: Int = 0,
    
    /** 图片/视频宽度（像素） */
    val width: Int? = null,
    
    /** 图片/视频高度（像素） */
    val height: Int? = null,
    
    /** 音视频时长（毫秒） */
    val duration: Long? = null,
    
    /** 文件MD5哈希（用于去重和校验） */
    val md5Hash: String? = null,
    
    /** 创建时间 */
    val createdAt: Long = System.currentTimeMillis(),
    
    /** 最后访问时间（用于缓存清理） */
    val lastAccessedAt: Long = System.currentTimeMillis()
)

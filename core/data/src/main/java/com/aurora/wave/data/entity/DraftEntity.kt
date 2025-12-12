package com.aurora.wave.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 草稿实体
 * 
 * 保存用户在各会话中未发送的消息草稿。
 * 支持文字、@提及、引用消息等内容的草稿保存。
 */
@Entity(
    tableName = "drafts",
    foreignKeys = [
        ForeignKey(
            entity = ConversationEntity::class,
            parentColumns = ["id"],
            childColumns = ["conversationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["conversationId"], unique = true)
    ]
)
data class DraftEntity(
    @PrimaryKey
    val conversationId: String,  // 一个会话只有一个草稿
    
    /** 草稿文本内容 */
    val content: String,
    
    /** 引用的消息ID（如果是回复） */
    val replyToMessageId: String? = null,
    
    /** @提及的用户ID列表（JSON数组） */
    val mentionUserIds: String? = null,
    
    /** 附件ID列表（JSON数组，用于保存未发送的媒体） */
    val attachmentIds: String? = null,
    
    /** 创建时间 */
    val createdAt: Long = System.currentTimeMillis(),
    
    /** 最后更新时间 */
    val updatedAt: Long = System.currentTimeMillis()
)

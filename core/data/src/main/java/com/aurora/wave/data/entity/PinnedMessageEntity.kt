package com.aurora.wave.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * 置顶消息实体
 * 
 * 记录会话中被置顶的消息。
 * 群聊中可以置顶多条消息，私聊一般只置顶一条。
 */
@Entity(
    tableName = "pinned_messages",
    primaryKeys = ["conversationId", "messageId"],
    foreignKeys = [
        ForeignKey(
            entity = ConversationEntity::class,
            parentColumns = ["id"],
            childColumns = ["conversationId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MessageEntity::class,
            parentColumns = ["id"],
            childColumns = ["messageId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["conversationId"]),
        Index(value = ["messageId"])
    ]
)
data class PinnedMessageEntity(
    /** 会话ID */
    val conversationId: String,
    
    /** 被置顶的消息ID */
    val messageId: String,
    
    /** 置顶操作者的用户ID */
    val pinnedByUserId: String,
    
    /** 置顶操作者显示名 */
    val pinnedByUserName: String? = null,
    
    /** 置顶时间 */
    val pinnedAt: Long = System.currentTimeMillis(),
    
    /** 置顶顺序（数字越小越靠前） */
    val sortOrder: Int = 0
)

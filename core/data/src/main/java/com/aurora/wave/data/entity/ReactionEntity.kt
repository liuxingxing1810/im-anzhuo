package com.aurora.wave.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * 消息反应（Reaction）实体
 * 
 * 记录用户对消息的表情反应，如点赞、爱心等。
 * 每个用户对每条消息只能有一个相同类型的反应。
 */
@Entity(
    tableName = "reactions",
    primaryKeys = ["messageId", "userId", "emoji"],
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
        Index(value = ["userId"])
    ]
)
data class ReactionEntity(
    /** 消息ID */
    val messageId: String,
    
    /** 反应的用户ID */
    val userId: String,
    
    /** 表情符号或表情代码 */
    val emoji: String,
    
    /** 用户显示名（冗余字段，便于展示） */
    val userName: String? = null,
    
    /** 创建时间 */
    val createdAt: Long = System.currentTimeMillis()
)

package com.aurora.wave.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aurora.wave.data.model.DeliveryStatus
import com.aurora.wave.data.model.MessageType

/**
 * Room entity for messages stored locally.
 * Content is stored as JSON string and parsed when needed.
 */
@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey
    val id: String,
    
    val conversationId: String,
    
    val senderId: String,
    
    val senderName: String? = null,
    
    val senderAvatar: String? = null,
    
    val type: MessageType,
    
    /** Message content - plain text for TEXT type, JSON for others */
    val content: String,
    
    val timestamp: Long,
    
    val deliveryStatus: DeliveryStatus = DeliveryStatus.SENT,
    
    /** For reply feature: the id of message being replied to */
    val replyToMessageId: String? = null,
    
    /** For forwarded messages */
    val forwardedFromId: String? = null,
    
    /** Local-only: is this message from current user */
    val isOutgoing: Boolean = false,
    
    /** Is message edited */
    val isEdited: Boolean = false,
    
    /** For edits */
    val editedAt: Long? = null,
    
    /** Mentioned user IDs (JSON array) */
    val mentions: String? = null,
    
    /** Reactions (JSON map) */
    val reactions: String? = null,
    
    /** Soft delete */
    val isDeleted: Boolean = false
)

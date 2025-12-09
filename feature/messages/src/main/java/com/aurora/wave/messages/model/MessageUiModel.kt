package com.aurora.wave.messages.model

import com.aurora.wave.data.model.DeliveryStatus
import com.aurora.wave.data.model.MessageContent
import com.aurora.wave.data.model.MessageType

/**
 * UI model for a single message in chat
 */
data class MessageUiModel(
    val id: String,
    val senderId: String,
    val senderName: String?,
    val senderAvatar: String?,
    val type: MessageType,
    val content: MessageContent,
    val timestamp: String,
    val fullTimestamp: Long,
    val status: DeliveryStatus,
    val isOutgoing: Boolean,
    val isFirstInGroup: Boolean = true,  // First message from this sender in a sequence
    val isLastInGroup: Boolean = true,   // Last message from this sender in a sequence
    val replyTo: ReplyPreview? = null
)

data class ReplyPreview(
    val messageId: String,
    val senderName: String,
    val preview: String
)

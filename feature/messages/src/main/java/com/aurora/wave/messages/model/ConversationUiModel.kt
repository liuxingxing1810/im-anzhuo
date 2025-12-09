package com.aurora.wave.messages.model

import com.aurora.wave.data.model.ConversationType

/**
 * UI model for conversation list item
 */
data class ConversationUiModel(
    val id: String,
    val name: String,
    val avatarUrl: String?,
    val lastMessage: String,
    val timestamp: String,
    val unreadCount: Int,
    val isPinned: Boolean,
    val isMuted: Boolean,
    val type: ConversationType,
    val draft: String? = null,
    val isOnline: Boolean = false,
    val lastMessageFromMe: Boolean = false,
    val lastMessageRead: Boolean = false
)

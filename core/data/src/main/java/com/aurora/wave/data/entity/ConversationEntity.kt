package com.aurora.wave.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aurora.wave.data.model.ConversationType

/**
 * Room entity representing a conversation (chat thread).
 */
@Entity(tableName = "conversations")
data class ConversationEntity(
    @PrimaryKey
    val id: String,
    
    val type: ConversationType,
    
    /** Display name (contact name for direct, group name for group) */
    val name: String,
    
    /** Avatar URL */
    val avatarUrl: String? = null,
    
    /** Last message preview text */
    val lastMessage: String? = null,
    
    /** Last message timestamp */
    val lastMessageTimestamp: Long = 0L,
    
    /** Unread message count */
    val unreadCount: Int = 0,
    
    /** Is conversation pinned to top */
    val isPinned: Boolean = false,
    
    /** Is conversation muted (no notifications) */
    val isMuted: Boolean = false,
    
    /** Draft text if user started typing but didn't send */
    val draft: String? = null,
    
    /** Is conversation archived */
    val isArchived: Boolean = false,
    
    /** Participant user IDs (JSON array stored as string for simplicity) */
    val participantIds: List<String> = emptyList(),
    
    /** For group: member count */
    val memberCount: Int? = null,
    
    /** Creation timestamp */
    val createdAt: Long = System.currentTimeMillis(),
    
    /** Last activity timestamp (for sorting) */
    val updatedAt: Long = System.currentTimeMillis()
)

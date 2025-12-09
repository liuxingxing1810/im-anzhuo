package com.aurora.wave.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a user profile (contact or self).
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    
    /** Display name / nickname */
    val displayName: String,
    
    /** Avatar URL */
    val avatarUrl: String? = null,
    
    /** Username / handle (e.g., @johndoe) */
    val username: String? = null,
    
    /** User's status message */
    val statusMessage: String? = null,
    
    /** Phone number (if available) */
    val phoneNumber: String? = null,
    
    /** Is this a contact (friend) */
    val isContact: Boolean = false,
    
    /** Is this blocked */
    val isBlocked: Boolean = false,
    
    /** Contact note / remark set by current user */
    val remark: String? = null,
    
    /** Tags assigned to this contact */
    val tags: String? = null,  // JSON array of tag IDs
    
    /** Last seen timestamp */
    val lastSeenAt: Long? = null,
    
    /** Is currently online */
    val isOnline: Boolean = false
)

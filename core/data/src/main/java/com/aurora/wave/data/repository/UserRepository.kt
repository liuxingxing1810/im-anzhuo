package com.aurora.wave.data.repository

import com.aurora.wave.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for user-related operations.
 */
interface UserRepository {
    
    /**
     * Get the current logged-in user.
     */
    suspend fun getCurrentUser(): UserEntity?
    
    /**
     * Get current user as Flow for reactive updates.
     */
    fun getCurrentUserFlow(): Flow<UserEntity?>
    
    /**
     * Get a user by ID.
     */
    suspend fun getUserById(userId: String): UserEntity?
    
    /**
     * Get multiple users by IDs.
     */
    suspend fun getUsersByIds(userIds: List<String>): List<UserEntity>
    
    /**
     * Search users by name or username.
     */
    fun searchUsers(query: String): Flow<List<UserEntity>>
    
    /**
     * Get all contacts/friends.
     */
    fun getContacts(): Flow<List<UserEntity>>
    
    /**
     * Add a user as contact/friend.
     */
    suspend fun addContact(userId: String): Result<Unit>
    
    /**
     * Remove a user from contacts.
     */
    suspend fun removeContact(userId: String): Result<Unit>
    
    /**
     * Block a user.
     */
    suspend fun blockUser(userId: String): Result<Unit>
    
    /**
     * Unblock a user.
     */
    suspend fun unblockUser(userId: String): Result<Unit>
    
    /**
     * Get blocked users.
     */
    fun getBlockedUsers(): Flow<List<UserEntity>>
    
    /**
     * Update current user's profile.
     */
    suspend fun updateProfile(
        displayName: String? = null,
        avatarUrl: String? = null,
        bio: String? = null
    ): Result<Unit>
    
    /**
     * Update current user's online status.
     */
    suspend fun updateOnlineStatus(isOnline: Boolean)
    
    /**
     * Sync user data with server.
     */
    suspend fun syncUsers(): Result<Int>
    
    /**
     * Get online status of a user.
     */
    suspend fun isUserOnline(userId: String): Boolean
    
    /**
     * Report a user for inappropriate behavior.
     */
    suspend fun reportUser(userId: String, reason: String): Result<Unit>
}

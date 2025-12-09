package com.aurora.wave.connections.model

/**
 * UI model for displaying a contact in the list
 */
data class ContactUiModel(
    val id: String,
    val name: String,
    val avatarUrl: String? = null,
    val initial: Char,
    val bio: String? = null,
    val isOnline: Boolean = false,
    val isFriend: Boolean = true,
    val lastSeenTime: String? = null
)

/**
 * Friend request UI model
 */
data class FriendRequestUiModel(
    val id: String,
    val userId: String,
    val userName: String,
    val userAvatar: String? = null,
    val message: String? = null,
    val timestamp: String,
    val status: FriendRequestStatus = FriendRequestStatus.PENDING
)

enum class FriendRequestStatus {
    PENDING,
    ACCEPTED,
    REJECTED,
    EXPIRED
}

/**
 * Contact group for A-Z sectioned list
 */
data class ContactGroup(
    val letter: Char,
    val contacts: List<ContactUiModel>
)

package com.aurora.wave.discover.model

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Model for Discover menu items
 */
data class DiscoverMenuItem(
    val id: String,
    val title: String,
    val subtitle: String? = null,
    val icon: ImageVector,
    val badge: String? = null,
    val showDot: Boolean = false
)

/**
 * Model for Moments/Timeline posts
 */
data class MomentPost(
    val id: String,
    val authorId: String,
    val authorName: String,
    val authorAvatar: String? = null,
    val content: String,
    val images: List<String> = emptyList(),
    val timestamp: String,
    val likeCount: Int = 0,
    val commentCount: Int = 0,
    val isLikedByMe: Boolean = false
)

/**
 * Model for Nearby People
 */
data class NearbyPerson(
    val id: String,
    val name: String,
    val avatarUrl: String? = null,
    val distance: String,
    val bio: String? = null,
    val isOnline: Boolean = false
)

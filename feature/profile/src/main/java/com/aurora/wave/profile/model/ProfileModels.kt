package com.aurora.wave.profile.model

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * User profile information
 */
data class UserProfile(
    val id: String,
    val displayName: String,
    val username: String,
    val avatarUrl: String? = null,
    val bio: String? = null,
    val phoneNumber: String? = null,
    val email: String? = null,
    val region: String? = null,
    val gender: Gender = Gender.NotSet,
    val qrCodeUrl: String? = null
)

enum class Gender {
    Male, Female, NotSet
}

/**
 * Profile menu item for settings sections
 */
data class ProfileMenuItem(
    val id: String,
    val title: String,
    val subtitle: String? = null,
    val icon: ImageVector,
    val badge: String? = null,
    val showDivider: Boolean = true
)

/**
 * Settings section with grouped menu items
 */
data class SettingsSection(
    val title: String? = null,
    val items: List<ProfileMenuItem>
)

/**
 * Storage and data information
 */
data class StorageInfo(
    val totalStorage: String = "2.5 GB",
    val cacheSize: String = "156 MB",
    val mediaSize: String = "1.8 GB",
    val documentsSize: String = "540 MB"
)

/**
 * About app information
 */
data class AppInfo(
    val appName: String = "星星IM",
    val version: String = "1.0.0",
    val buildNumber: String = "2024.06.001",
    val copyright: String = "© 2024 星星IM"
)

/**
 * ProfileScreen 所需的国际化字符串集合
 * Data class to hold all localized strings for ProfileScreen
 * 用于简化 remember 参数
 */
data class ProfileStrings(
    val defaultBio: String,
    val servicesTitle: String,
    val servicesSub: String,
    val favoritesTitle: String,
    val collectionsTitle: String,
    val settingsTitle: String,
    val notificationsTitle: String,
    val privacyTitle: String,
    val storageTitle: String,
    val appearanceTitle: String,
    val appearanceSubTitle: String,
    val helpTitle: String,
    val aboutTitle: String,
    val aboutVersion: String
)

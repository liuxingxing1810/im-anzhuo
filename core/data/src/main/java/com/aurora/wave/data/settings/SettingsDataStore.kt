package com.aurora.wave.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/**
 * 应用设置数据存储
 * DataStore for app settings
 */
class SettingsDataStore(private val context: Context) {
    
    companion object {
        // 外观设置
        private val KEY_DARK_MODE = booleanPreferencesKey("dark_mode")
        private val KEY_FOLLOW_SYSTEM_THEME = booleanPreferencesKey("follow_system_theme")
        private val KEY_LANGUAGE = stringPreferencesKey("language")
        
        // 通知设置
        private val KEY_PUSH_NOTIFICATIONS = booleanPreferencesKey("push_notifications")
        private val KEY_MESSAGE_NOTIFICATIONS = booleanPreferencesKey("message_notifications")
        private val KEY_SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        private val KEY_VIBRATION_ENABLED = booleanPreferencesKey("vibration_enabled")
        private val KEY_SHOW_NOTIFICATION_PREVIEW = booleanPreferencesKey("show_notification_preview")
        private val KEY_GROUP_NOTIFICATIONS = booleanPreferencesKey("group_notifications")
        private val KEY_MENTION_NOTIFICATIONS = booleanPreferencesKey("mention_notifications")
        
        // 隐私设置
        private val KEY_READ_RECEIPTS = booleanPreferencesKey("read_receipts")
        private val KEY_TYPING_INDICATOR = booleanPreferencesKey("typing_indicator")
        private val KEY_LAST_SEEN = booleanPreferencesKey("last_seen")
        private val KEY_ONLINE_STATUS = booleanPreferencesKey("online_status")
        private val KEY_PROFILE_PHOTO_VISIBILITY = stringPreferencesKey("profile_photo_visibility")
        private val KEY_ADD_BY_PHONE = booleanPreferencesKey("add_by_phone")
        private val KEY_ADD_BY_QR_CODE = booleanPreferencesKey("add_by_qr_code")
        private val KEY_ADD_BY_GROUP = booleanPreferencesKey("add_by_group")
        
        // 存储设置
        private val KEY_AUTO_DOWNLOAD_MEDIA = booleanPreferencesKey("auto_download_media")
        private val KEY_AUTO_DOWNLOAD_ON_WIFI = booleanPreferencesKey("auto_download_on_wifi")
        private val KEY_AUTO_DOWNLOAD_ON_MOBILE = booleanPreferencesKey("auto_download_on_mobile")
        private val KEY_SAVE_TO_GALLERY = booleanPreferencesKey("save_to_gallery")
        private val KEY_DATA_USAGE_OPTIMIZATION = booleanPreferencesKey("data_usage_optimization")
    }
    
    // ========== 语言设置 ==========
    
    val languageFlow: Flow<AppLanguage> = context.dataStore.data.map { preferences ->
        val languageCode = preferences[KEY_LANGUAGE] ?: AppLanguage.SYSTEM.code
        AppLanguage.fromCode(languageCode)
    }
    
    suspend fun setLanguage(language: AppLanguage) {
        context.dataStore.edit { preferences ->
            preferences[KEY_LANGUAGE] = language.code
        }
    }
    
    // ========== 深色模式设置 ==========
    
    val darkModeFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_DARK_MODE] ?: false
    }
    
    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_DARK_MODE] = enabled
        }
    }
    
    // ========== 跟随系统主题设置 ==========
    
    val followSystemThemeFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_FOLLOW_SYSTEM_THEME] ?: true
    }
    
    suspend fun setFollowSystemTheme(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_FOLLOW_SYSTEM_THEME] = enabled
        }
    }
    
    // ========== 通知设置 ==========
    
    val pushNotificationsFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_PUSH_NOTIFICATIONS] ?: true
    }
    
    suspend fun setPushNotifications(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_PUSH_NOTIFICATIONS] = enabled
        }
    }
    
    val messageNotificationsFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_MESSAGE_NOTIFICATIONS] ?: true
    }
    
    suspend fun setMessageNotifications(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_MESSAGE_NOTIFICATIONS] = enabled
        }
    }
    
    val soundEnabledFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_SOUND_ENABLED] ?: true
    }
    
    suspend fun setSoundEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_SOUND_ENABLED] = enabled
        }
    }
    
    val vibrationEnabledFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_VIBRATION_ENABLED] ?: true
    }
    
    suspend fun setVibrationEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_VIBRATION_ENABLED] = enabled
        }
    }
    
    val showNotificationPreviewFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_SHOW_NOTIFICATION_PREVIEW] ?: true
    }
    
    suspend fun setShowNotificationPreview(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_SHOW_NOTIFICATION_PREVIEW] = enabled
        }
    }
    
    val groupNotificationsFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_GROUP_NOTIFICATIONS] ?: true
    }
    
    suspend fun setGroupNotifications(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_GROUP_NOTIFICATIONS] = enabled
        }
    }
    
    val mentionNotificationsFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_MENTION_NOTIFICATIONS] ?: true
    }
    
    suspend fun setMentionNotifications(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_MENTION_NOTIFICATIONS] = enabled
        }
    }
    
    // ========== 隐私设置 ==========
    
    val readReceiptsFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_READ_RECEIPTS] ?: true
    }
    
    suspend fun setReadReceipts(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_READ_RECEIPTS] = enabled
        }
    }
    
    val typingIndicatorFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_TYPING_INDICATOR] ?: true
    }
    
    suspend fun setTypingIndicator(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_TYPING_INDICATOR] = enabled
        }
    }
    
    val lastSeenFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_LAST_SEEN] ?: true
    }
    
    suspend fun setLastSeen(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_LAST_SEEN] = enabled
        }
    }
    
    val onlineStatusFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_ONLINE_STATUS] ?: true
    }
    
    suspend fun setOnlineStatus(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_ONLINE_STATUS] = enabled
        }
    }
    
    val profilePhotoVisibilityFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[KEY_PROFILE_PHOTO_VISIBILITY] ?: "everyone"
    }
    
    suspend fun setProfilePhotoVisibility(visibility: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_PROFILE_PHOTO_VISIBILITY] = visibility
        }
    }
    
    val addByPhoneFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_ADD_BY_PHONE] ?: true
    }
    
    suspend fun setAddByPhone(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_ADD_BY_PHONE] = enabled
        }
    }
    
    val addByQrCodeFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_ADD_BY_QR_CODE] ?: true
    }
    
    suspend fun setAddByQrCode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_ADD_BY_QR_CODE] = enabled
        }
    }
    
    val addByGroupFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_ADD_BY_GROUP] ?: true
    }
    
    suspend fun setAddByGroup(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_ADD_BY_GROUP] = enabled
        }
    }
    
    // ========== 存储设置 ==========
    
    val autoDownloadMediaFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_AUTO_DOWNLOAD_MEDIA] ?: true
    }
    
    suspend fun setAutoDownloadMedia(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_AUTO_DOWNLOAD_MEDIA] = enabled
        }
    }
    
    val autoDownloadOnWifiFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_AUTO_DOWNLOAD_ON_WIFI] ?: true
    }
    
    suspend fun setAutoDownloadOnWifi(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_AUTO_DOWNLOAD_ON_WIFI] = enabled
        }
    }
    
    val autoDownloadOnMobileFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_AUTO_DOWNLOAD_ON_MOBILE] ?: false
    }
    
    suspend fun setAutoDownloadOnMobile(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_AUTO_DOWNLOAD_ON_MOBILE] = enabled
        }
    }
    
    val saveToGalleryFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_SAVE_TO_GALLERY] ?: false
    }
    
    suspend fun setSaveToGallery(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_SAVE_TO_GALLERY] = enabled
        }
    }
    
    val dataUsageOptimizationFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_DATA_USAGE_OPTIMIZATION] ?: false
    }
    
    suspend fun setDataUsageOptimization(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_DATA_USAGE_OPTIMIZATION] = enabled
        }
    }
    
    // ========== 清除所有设置 ==========
    
    suspend fun clearAll() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}

/**
 * 应用支持的语言
 */
enum class AppLanguage(val code: String, val displayName: String, val nativeName: String) {
    SYSTEM("system", "System Default", "跟随系统"),
    ENGLISH("en", "English", "English"),
    SIMPLIFIED_CHINESE("zh-CN", "Simplified Chinese", "简体中文"),
    TRADITIONAL_CHINESE("zh-TW", "Traditional Chinese", "繁體中文");
    
    companion object {
        fun fromCode(code: String): AppLanguage {
            return entries.find { it.code == code } ?: SYSTEM
        }
    }
}

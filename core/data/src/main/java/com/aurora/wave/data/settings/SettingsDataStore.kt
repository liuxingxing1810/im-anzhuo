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
        
        // 隐私设置
        private val KEY_READ_RECEIPTS = booleanPreferencesKey("read_receipts")
        private val KEY_TYPING_INDICATOR = booleanPreferencesKey("typing_indicator")
        private val KEY_LAST_SEEN = booleanPreferencesKey("last_seen")
        
        // 存储设置
        private val KEY_AUTO_DOWNLOAD_MEDIA = booleanPreferencesKey("auto_download_media")
        private val KEY_SAVE_TO_GALLERY = booleanPreferencesKey("save_to_gallery")
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
    
    // ========== 存储设置 ==========
    
    val autoDownloadMediaFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_AUTO_DOWNLOAD_MEDIA] ?: true
    }
    
    suspend fun setAutoDownloadMedia(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_AUTO_DOWNLOAD_MEDIA] = enabled
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

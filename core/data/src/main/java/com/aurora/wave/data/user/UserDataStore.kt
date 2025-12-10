package com.aurora.wave.data.user

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

/**
 * 用户数据存储
 * User data storage for login state and user info
 */
class UserDataStore(private val context: Context) {
    
    companion object {
        private val KEY_IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val KEY_USER_ID = stringPreferencesKey("user_id")
        private val KEY_USERNAME = stringPreferencesKey("username")
        private val KEY_WAVE_ID = stringPreferencesKey("wave_id")
        private val KEY_TOKEN = stringPreferencesKey("token")
        private val KEY_AVATAR_URL = stringPreferencesKey("avatar_url")
    }
    
    // ========== 登录状态 ==========
    
    val isLoggedInFlow: Flow<Boolean> = context.userDataStore.data.map { preferences ->
        preferences[KEY_IS_LOGGED_IN] ?: false
    }
    
    // ========== 用户信息 ==========
    
    val userIdFlow: Flow<String> = context.userDataStore.data.map { preferences ->
        preferences[KEY_USER_ID] ?: ""
    }
    
    val usernameFlow: Flow<String> = context.userDataStore.data.map { preferences ->
        preferences[KEY_USERNAME] ?: ""
    }
    
    val waveIdFlow: Flow<String> = context.userDataStore.data.map { preferences ->
        preferences[KEY_WAVE_ID] ?: ""
    }
    
    val tokenFlow: Flow<String> = context.userDataStore.data.map { preferences ->
        preferences[KEY_TOKEN] ?: ""
    }
    
    val avatarUrlFlow: Flow<String> = context.userDataStore.data.map { preferences ->
        preferences[KEY_AVATAR_URL] ?: ""
    }
    
    // ========== 登录操作 ==========
    
    /**
     * 保存登录信息（Mock数据）
     */
    suspend fun login(
        userId: String,
        username: String,
        waveId: String,
        token: String = "mock_token_${System.currentTimeMillis()}"
    ) {
        context.userDataStore.edit { preferences ->
            preferences[KEY_IS_LOGGED_IN] = true
            preferences[KEY_USER_ID] = userId
            preferences[KEY_USERNAME] = username
            preferences[KEY_WAVE_ID] = waveId
            preferences[KEY_TOKEN] = token
        }
    }
    
    /**
     * 退出登录 - 清除所有用户数据
     */
    suspend fun logout() {
        context.userDataStore.edit { preferences ->
            preferences.clear()
        }
    }
    
    /**
     * 更新用户头像
     */
    suspend fun updateAvatar(avatarUrl: String) {
        context.userDataStore.edit { preferences ->
            preferences[KEY_AVATAR_URL] = avatarUrl
        }
    }
    
    /**
     * 更新用户名
     */
    suspend fun updateUsername(username: String) {
        context.userDataStore.edit { preferences ->
            preferences[KEY_USERNAME] = username
        }
    }
}

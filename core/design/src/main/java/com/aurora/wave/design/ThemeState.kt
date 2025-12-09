package com.aurora.wave.design

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * 全局主题状态管理
 * Global theme state management for Dark Mode toggle
 */
class ThemeState {
    var isDarkMode by mutableStateOf(false)
        private set
    
    var useSystemTheme by mutableStateOf(true)
        private set
    
    fun toggleDarkMode() {
        useSystemTheme = false
        isDarkMode = !isDarkMode
    }
    
    fun updateDarkMode(enabled: Boolean) {
        useSystemTheme = false
        isDarkMode = enabled
    }
    
    fun updateUseSystemTheme(enabled: Boolean) {
        useSystemTheme = enabled
    }
}

/**
 * CompositionLocal for ThemeState
 * 用于在 Compose 树中传递 ThemeState
 */
val LocalThemeState = compositionLocalOf { ThemeState() }

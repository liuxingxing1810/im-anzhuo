package com.aurora.wave.design

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * 微信风格配色方案
 * 
 * 设计特点：
 * - 主色调：微信绿 (#07C160)
 * - 背景：浅灰白 (#EDEDED / #F7F7F7)
 * - 简洁、扁平、高效
 */

// 微信绿色系
private val WeChatGreen = Color(0xFF07C160)      // 微信主绿
private val WeChatGreenLight = Color(0xFF95EC69) // 浅绿
private val WeChatGreenDark = Color(0xFF06AD56)  // 深绿

// 浅色主题 - 微信风格
private val LightColors = lightColorScheme(
    primary = WeChatGreen,
    onPrimary = Color.White,
    primaryContainer = Color.White,              // 卡片背景改为白色
    onPrimaryContainer = Color(0xFF191C1A),
    secondary = Color(0xFF576B5C),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFDAE8DC),
    onSecondaryContainer = Color(0xFF151D17),
    tertiary = Color(0xFF3D6373),
    onTertiary = Color.White,
    background = Color(0xFFEDEDED),              // 微信浅灰背景
    onBackground = Color(0xFF191C1A),
    surface = Color(0xFFFFFFFF),                 // 白色表面
    onSurface = Color(0xFF191C1A),
    surfaceVariant = Color(0xFFF7F7F7),          // 搜索框背景
    onSurfaceVariant = Color(0xFF414941),
    outline = Color(0xFFB4B4B4),                 // 分隔线
    outlineVariant = Color(0xFFE0E0E0),
    error = Color(0xFFFA5151),                   // 微信红
    errorContainer = Color(0xFFFFDAD6)
)

// 深色主题 - 微信风格
private val DarkColors = darkColorScheme(
    primary = WeChatGreenLight,
    onPrimary = Color(0xFF003919),
    primaryContainer = Color(0xFF005227),
    onPrimaryContainer = Color(0xFF95EC69),
    secondary = Color(0xFFBECCC0),
    onSecondary = Color(0xFF29352C),
    secondaryContainer = Color(0xFF3F4C42),
    onSecondaryContainer = Color(0xFFDAE8DC),
    tertiary = Color(0xFFA3CDE0),
    onTertiary = Color(0xFF023544),
    background = Color(0xFF111111),              // 深色背景
    onBackground = Color(0xFFE1E3E0),
    surface = Color(0xFF1E1E1E),                 // 深色表面
    onSurface = Color(0xFFE1E3E0),
    surfaceVariant = Color(0xFF2C2C2C),
    onSurfaceVariant = Color(0xFFC1C9C2),
    outline = Color(0xFF4D4D4D),
    outlineVariant = Color(0xFF3D3D3D),
    error = Color(0xFFFA5151),
    errorContainer = Color(0xFF93000A)
)

/**
 * 星星IM 主题
 * @param themeState 全局主题状态，用于控制 Dark Mode
 * @param content Composable 内容
 */
@Composable
fun AuroraTheme(
    themeState: ThemeState = remember { ThemeState() },
    content: @Composable () -> Unit
) {
    val isSystemDark = isSystemInDarkTheme()
    val useDarkTheme = if (themeState.useSystemTheme) isSystemDark else themeState.isDarkMode
    val colorScheme = if (useDarkTheme) DarkColors else LightColors
    
    CompositionLocalProvider(LocalThemeState provides themeState) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = MaterialTheme.typography,
            shapes = MaterialTheme.shapes,
            content = content
        )
    }
}

/**
 * 设置状态栏图标颜色（Edge-to-Edge 模式）
 * 状态栏背景是透明的，只需要控制图标颜色
 * @param darkIcons 是否使用深色图标（浅色背景时为true）
 */
@Composable
fun StatusBarStyle(darkIcons: Boolean) {
    val view = LocalView.current
    
    if (!view.isInEditMode) {
        DisposableEffect(view, darkIcons) {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkIcons
            onDispose {}
        }
    }
}

/**
 * 灰色/深色背景页面的状态栏 - 用于主页面 (消息、联系人、发现等)
 * Edge-to-Edge 模式：只设置图标颜色，背景由页面内容提供
 */
@Composable
fun GrayStatusBar() {
    val isDark = LocalThemeState.current.let { themeState ->
        val isSystemDark = isSystemInDarkTheme()
        if (themeState.useSystemTheme) isSystemDark else themeState.isDarkMode
    }
    // 浅色模式用深色图标，深色模式用浅色图标
    StatusBarStyle(darkIcons = !isDark)
}

/**
 * 白色/Surface 背景页面的状态栏 - 用于二级页面 (我的、设置、关于等)
 * Edge-to-Edge 模式：只设置图标颜色，背景由页面内容提供
 */
@Composable
fun WhiteStatusBar() {
    val isDark = LocalThemeState.current.let { themeState ->
        val isSystemDark = isSystemInDarkTheme()
        if (themeState.useSystemTheme) isSystemDark else themeState.isDarkMode
    }
    // 浅色模式用深色图标，深色模式用浅色图标
    StatusBarStyle(darkIcons = !isDark)
}

/**
 * 透明状态栏 - 用于全屏页面（开屏页、图片查看等）
 * @param darkIcons 是否使用深色图标
 */
@Composable
fun TransparentStatusBar(darkIcons: Boolean = false) {
    StatusBarStyle(darkIcons = darkIcons)
}

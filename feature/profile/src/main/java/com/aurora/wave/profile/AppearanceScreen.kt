package com.aurora.wave.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aurora.wave.data.settings.SettingsDataStore
import com.aurora.wave.design.LocalThemeState
import com.aurora.wave.design.WhiteStatusBar
import kotlinx.coroutines.launch

/**
 * 外观设置页面
 * Appearance settings screen - Theme, Dark Mode, Font Size
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppearanceScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val settingsDataStore = remember { SettingsDataStore(context) }
    val themeState = LocalThemeState.current
    
    // 从 DataStore 读取设置
    val darkModeEnabled by settingsDataStore.darkModeFlow.collectAsState(initial = false)
    val followSystem by settingsDataStore.followSystemThemeFlow.collectAsState(initial = true)
    
    // 字体大小（本地状态，后续可持久化）
    var fontScale by remember { mutableFloatStateOf(1.0f) }
    
    // 白色状态栏
    WhiteStatusBar()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.appearance_title),
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 主题模式区块
            item {
                AppearanceSection(title = stringResource(R.string.appearance_theme_mode)) {
                    // 跟随系统
                    ThemeModeItem(
                        icon = Icons.Default.BrightnessAuto,
                        title = stringResource(R.string.appearance_follow_system),
                        subtitle = stringResource(R.string.appearance_follow_system_desc),
                        isSelected = followSystem,
                        onClick = {
                            scope.launch {
                                settingsDataStore.setFollowSystemTheme(true)
                                themeState.updateUseSystemTheme(true)
                            }
                        }
                    )
                    
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 56.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )
                    
                    // 浅色模式
                    ThemeModeItem(
                        icon = Icons.Default.LightMode,
                        title = stringResource(R.string.appearance_light_mode),
                        subtitle = stringResource(R.string.appearance_light_mode_desc),
                        isSelected = !followSystem && !darkModeEnabled,
                        onClick = {
                            scope.launch {
                                settingsDataStore.setFollowSystemTheme(false)
                                settingsDataStore.setDarkMode(false)
                                themeState.updateDarkMode(false)
                            }
                        }
                    )
                    
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 56.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )
                    
                    // 深色模式
                    ThemeModeItem(
                        icon = Icons.Default.DarkMode,
                        title = stringResource(R.string.appearance_dark_mode),
                        subtitle = stringResource(R.string.appearance_dark_mode_desc),
                        isSelected = !followSystem && darkModeEnabled,
                        onClick = {
                            scope.launch {
                                settingsDataStore.setFollowSystemTheme(false)
                                settingsDataStore.setDarkMode(true)
                                themeState.updateDarkMode(true)
                            }
                        }
                    )
                }
            }
            
            // 主题预览
            item {
                AppearanceSection(title = stringResource(R.string.appearance_preview)) {
                    ThemePreview(isDark = if (followSystem) themeState.isDarkMode else darkModeEnabled)
                }
            }
            
            // 字体大小
            item {
                AppearanceSection(title = stringResource(R.string.appearance_font_size)) {
                    FontSizeSelector(
                        currentScale = fontScale,
                        onScaleChange = { fontScale = it }
                    )
                }
            }
            
            // 聊天背景（占位）
            item {
                AppearanceSection(title = stringResource(R.string.appearance_chat_background)) {
                    ChatBackgroundSelector()
                }
            }
        }
    }
}

@Composable
private fun AppearanceSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            content()
        }
    }
}

@Composable
private fun ThemeModeItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 图标
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    if (isSelected) Color(0xFFF5F5F5)  // 浅灰色背景
                    else MaterialTheme.colorScheme.surfaceVariant
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(22.dp),
                tint = if (isSelected) Color(0xFF07C160)  // 微信绿
                       else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Spacer(modifier = Modifier.width(14.dp))
        
        // 文字
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        // 选中指示
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
private fun ThemePreview(isDark: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 浅色预览
        PreviewCard(
            title = stringResource(R.string.appearance_light_mode),
            isLight = true,
            isActive = !isDark,
            modifier = Modifier.weight(1f)
        )
        
        // 深色预览
        PreviewCard(
            title = stringResource(R.string.appearance_dark_mode),
            isLight = false,
            isActive = isDark,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun PreviewCard(
    title: String,
    isLight: Boolean,
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isLight) Color(0xFFF5F5F5) else Color(0xFF1E1E1E)
    val surfaceColor = if (isLight) Color.White else Color(0xFF2C2C2C)
    val textColor = if (isLight) Color(0xFF191C1A) else Color(0xFFE1E3E0)
    val accentColor = Color(0xFF07C160)
    
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .then(
                if (isActive) Modifier.border(
                    width = 2.dp,
                    color = accentColor,
                    shape = RoundedCornerShape(12.dp)
                ) else Modifier
            )
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 模拟标题栏
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(surfaceColor)
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // 模拟内容区
        repeat(3) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.3f))
                )
                Spacer(modifier = Modifier.width(4.dp))
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(surfaceColor)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            fontSize = 10.sp
        )
    }
}

@Composable
private fun FontSizeSelector(
    currentScale: Float,
    onScaleChange: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "A",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Text(
                text = stringResource(R.string.appearance_font_preview),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = (16 * currentScale).sp
                )
            )
            
            Text(
                text = "A",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Slider(
            value = currentScale,
            onValueChange = onScaleChange,
            valueRange = 0.85f..1.3f,
            steps = 4,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary
            )
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.appearance_font_small),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = stringResource(R.string.appearance_font_standard),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = stringResource(R.string.appearance_font_large),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ChatBackgroundSelector() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.appearance_chat_background_desc),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // 背景选项
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 默认
            BackgroundOption(
                color = MaterialTheme.colorScheme.background,
                label = stringResource(R.string.appearance_bg_default),
                isSelected = true,
                modifier = Modifier.weight(1f)
            )
            
            // 渐变绿
            BackgroundOption(
                color = Color(0xFFE8F5E9),
                label = stringResource(R.string.appearance_bg_green),
                isSelected = false,
                modifier = Modifier.weight(1f)
            )
            
            // 渐变蓝
            BackgroundOption(
                color = Color(0xFFE3F2FD),
                label = stringResource(R.string.appearance_bg_blue),
                isSelected = false,
                modifier = Modifier.weight(1f)
            )
            
            // 自定义
            BackgroundOption(
                color = MaterialTheme.colorScheme.surfaceVariant,
                label = stringResource(R.string.appearance_bg_custom),
                isSelected = false,
                isCustom = true,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun BackgroundOption(
    color: Color,
    label: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    isCustom: Boolean = false
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(color)
                .then(
                    if (isSelected) Modifier.border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(12.dp)
                    ) else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isCustom) {
                Text(
                    text = "+",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

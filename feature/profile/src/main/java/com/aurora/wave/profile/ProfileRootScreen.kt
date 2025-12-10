package com.aurora.wave.profile

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aurora.wave.design.WhiteStatusBar

/**
 * 我的页面主屏幕
 * 
 * 架构说明：
 * - 不使用内部 Scaffold，由 MainActivity 的 Scaffold 统一管理 WindowInsets
 * - padding 参数来自 MainActivity，包含顶部状态栏和底部导航栏的内边距
 */
@Composable
fun ProfileRootScreen(
    padding: PaddingValues,
    onProfileClick: () -> Unit = {},
    onSettingsClick: (String) -> Unit = {}
) {
    val scrollState = rememberScrollState()
    var showLogoutDialog by remember { mutableStateOf(false) }
    
    // 为我的页面设置状态栏颜色（跟随主题，使用 surface 色）
    WhiteStatusBar()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding) // 应用来自 MainActivity 的 padding
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
    ) {
        // 用户信息卡片
        ProfileHeader(
            userName = "Aurora User",
            waveId = "aurora_001",
            onProfileClick = onProfileClick
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 服务与支付
        WeChatMenuItem(
            icon = Icons.Default.CreditCard,
            iconColor = Color(0xFF07C160),
            title = stringResource(R.string.profile_services),
            onClick = { onSettingsClick("services") }
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 收藏
        WeChatMenuItem(
            icon = Icons.Default.Bookmark,
            iconColor = Color(0xFFFA9D3B),
            title = stringResource(R.string.profile_favorites),
            onClick = { onSettingsClick("favorites") }
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 语言设置 - 放在通知上面
        WeChatMenuItem(
            icon = Icons.Default.Language,
            iconColor = Color(0xFF576B95),
            title = stringResource(R.string.settings_language),
            onClick = { onSettingsClick("language") }
        )
        
        WeChatMenuDivider()
        
        // 通知
        WeChatMenuItem(
            icon = Icons.Default.Notifications,
            iconColor = Color(0xFFFA5151),
            title = stringResource(R.string.profile_notifications),
            onClick = { onSettingsClick("notifications") }
        )
        
        WeChatMenuDivider()
        
        // 隐私与安全
        WeChatMenuItem(
            icon = Icons.Default.Lock,
            iconColor = Color(0xFF07C160),
            title = stringResource(R.string.profile_privacy),
            onClick = { onSettingsClick("privacy") }
        )
        
        WeChatMenuDivider()
        
        // 存储与数据
        WeChatMenuItem(
            icon = Icons.Default.Storage,
            iconColor = Color(0xFF10AEFF),
            title = stringResource(R.string.profile_storage),
            onClick = { onSettingsClick("storage") }
        )
        
        WeChatMenuDivider()
        
        // 外观
        WeChatMenuItem(
            icon = Icons.Default.Palette,
            iconColor = Color(0xFFE75A4D),
            title = stringResource(R.string.profile_appearance),
            subtitle = stringResource(R.string.profile_appearance_sub),
            onClick = { onSettingsClick("appearance") }
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 帮助与支持
        WeChatMenuItem(
            icon = Icons.Default.Help,
            iconColor = Color(0xFF4B8BE5),
            title = stringResource(R.string.profile_help),
            onClick = { onSettingsClick("help") }
        )
        
        WeChatMenuDivider()
        
        // 关于星星IM
        WeChatMenuItem(
            icon = Icons.Default.Info,
            iconColor = Color(0xFF5B6B7D),
            title = stringResource(R.string.profile_about),
            subtitle = stringResource(R.string.profile_about_version),
            onClick = { onSettingsClick("about") }
        )
        
        // 退出登录按钮 - 上边距较大防止误触
        Spacer(modifier = Modifier.height(48.dp))
        
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showLogoutDialog = true }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.settings_logout),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
    
    // 退出登录确认对话框
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text(stringResource(R.string.settings_logout)) },
            text = { Text(stringResource(R.string.logout_confirm)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onSettingsClick("logout")
                        showLogoutDialog = false
                    }
                ) {
                    Text(stringResource(R.string.settings_logout), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

/**
 * 微信风格的用户信息头部
 */
@Composable
private fun ProfileHeader(
    userName: String,
    waveId: String,
    onProfileClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onProfileClick),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 头像 - 稍微放大
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(44.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // 用户信息 - 垂直居中
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = userName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Wave号：$waveId",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // 二维码和箭头 - 垂直居中
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.QrCode2,
                    contentDescription = "QR Code",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
            }
        }
    }
}

/**
 * 微信风格的菜单项
 */
@Composable
private fun WeChatMenuItem(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 图标
            Box(
                modifier = Modifier
                    .size(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp),
                    tint = iconColor
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // 标题
            Text(
                text = title,
                modifier = Modifier.weight(1f),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            // 副标题
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            
            // 箭头
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
        }
    }
}

/**
 * 菜单分隔线
 */
@Composable
private fun WeChatMenuDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 56.dp),
        thickness = 0.5.dp,
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
    )
}

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.DoNotDisturb
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Preview
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aurora.wave.data.settings.SettingsDataStore
import com.aurora.wave.design.WhiteStatusBar
import kotlinx.coroutines.launch

/**
 * 通知设置页面
 * Notifications settings screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val settingsDataStore = remember { SettingsDataStore(context) }
    
    // 通知设置状态
    val pushNotificationsEnabled by settingsDataStore.pushNotificationsFlow.collectAsState(initial = true)
    val messageNotificationsEnabled by settingsDataStore.messageNotificationsFlow.collectAsState(initial = true)
    val soundEnabled by settingsDataStore.soundEnabledFlow.collectAsState(initial = true)
    val vibrationEnabled by settingsDataStore.vibrationEnabledFlow.collectAsState(initial = true)
    val showPreview by settingsDataStore.showNotificationPreviewFlow.collectAsState(initial = true)
    val groupNotifications by settingsDataStore.groupNotificationsFlow.collectAsState(initial = true)
    val mentionNotifications by settingsDataStore.mentionNotificationsFlow.collectAsState(initial = true)
    
    // 白色状态栏
    WhiteStatusBar()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.notifications_title),
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
            // 总开关
            item {
                NotificationSection(title = stringResource(R.string.notifications_general)) {
                    NotificationToggleItem(
                        icon = Icons.Default.Notifications,
                        iconColor = Color(0xFFFA5151),
                        title = stringResource(R.string.notifications_push),
                        subtitle = stringResource(R.string.notifications_push_desc),
                        checked = pushNotificationsEnabled,
                        onCheckedChange = { enabled ->
                            scope.launch {
                                settingsDataStore.setPushNotifications(enabled)
                            }
                        }
                    )
                }
            }
            
            // 消息通知
            item {
                NotificationSection(title = stringResource(R.string.notifications_messages)) {
                    NotificationToggleItem(
                        icon = Icons.AutoMirrored.Filled.Message,
                        iconColor = Color(0xFF07C160),
                        title = stringResource(R.string.notifications_new_messages),
                        subtitle = stringResource(R.string.notifications_new_messages_desc),
                        checked = messageNotificationsEnabled,
                        enabled = pushNotificationsEnabled,
                        onCheckedChange = { enabled ->
                            scope.launch {
                                settingsDataStore.setMessageNotifications(enabled)
                            }
                        }
                    )
                    
                    NotificationDivider()
                    
                    NotificationToggleItem(
                        icon = Icons.Default.Group,
                        iconColor = Color(0xFF4B8BE5),
                        title = stringResource(R.string.notifications_group),
                        subtitle = stringResource(R.string.notifications_group_desc),
                        checked = groupNotifications,
                        enabled = pushNotificationsEnabled,
                        onCheckedChange = { enabled ->
                            scope.launch {
                                settingsDataStore.setGroupNotifications(enabled)
                            }
                        }
                    )
                    
                    NotificationDivider()
                    
                    NotificationToggleItem(
                        icon = Icons.Default.Person,
                        iconColor = Color(0xFFFA9D3B),
                        title = stringResource(R.string.notifications_mentions),
                        subtitle = stringResource(R.string.notifications_mentions_desc),
                        checked = mentionNotifications,
                        enabled = pushNotificationsEnabled,
                        onCheckedChange = { enabled ->
                            scope.launch {
                                settingsDataStore.setMentionNotifications(enabled)
                            }
                        }
                    )
                }
            }
            
            // 通知方式
            item {
                NotificationSection(title = stringResource(R.string.notifications_style)) {
                    NotificationToggleItem(
                        icon = Icons.AutoMirrored.Filled.VolumeUp,
                        iconColor = Color(0xFF10AEFF),
                        title = stringResource(R.string.notifications_sound),
                        subtitle = stringResource(R.string.notifications_sound_desc),
                        checked = soundEnabled,
                        enabled = pushNotificationsEnabled,
                        onCheckedChange = { enabled ->
                            scope.launch {
                                settingsDataStore.setSoundEnabled(enabled)
                            }
                        }
                    )
                    
                    NotificationDivider()
                    
                    NotificationToggleItem(
                        icon = Icons.Default.Vibration,
                        iconColor = Color(0xFF8B5CF6),
                        title = stringResource(R.string.notifications_vibration),
                        subtitle = stringResource(R.string.notifications_vibration_desc),
                        checked = vibrationEnabled,
                        enabled = pushNotificationsEnabled,
                        onCheckedChange = { enabled ->
                            scope.launch {
                                settingsDataStore.setVibrationEnabled(enabled)
                            }
                        }
                    )
                    
                    NotificationDivider()
                    
                    NotificationToggleItem(
                        icon = Icons.Default.Preview,
                        iconColor = Color(0xFFE75A4D),
                        title = stringResource(R.string.notifications_preview),
                        subtitle = stringResource(R.string.notifications_preview_desc),
                        checked = showPreview,
                        enabled = pushNotificationsEnabled,
                        onCheckedChange = { enabled ->
                            scope.launch {
                                settingsDataStore.setShowNotificationPreview(enabled)
                            }
                        }
                    )
                }
            }
            
            // 免打扰说明
            item {
                NotificationSection(title = stringResource(R.string.notifications_dnd)) {
                    NotificationInfoItem(
                        icon = Icons.Default.DoNotDisturb,
                        iconColor = Color(0xFF5B6B7D),
                        title = stringResource(R.string.notifications_dnd_title),
                        subtitle = stringResource(R.string.notifications_dnd_desc),
                        onClick = { /* TODO: 跳转到系统免打扰设置 */ }
                    )
                }
            }
            
            // 底部间距
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun NotificationSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                content()
            }
        }
    }
}

@Composable
private fun NotificationToggleItem(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    subtitle: String? = null,
    checked: Boolean,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled) { onCheckedChange(!checked) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = iconColor.copy(alpha = if (enabled) 0.1f else 0.05f),
                    shape = RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (enabled) iconColor else iconColor.copy(alpha = 0.4f),
                modifier = Modifier.size(22.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = if (enabled) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                }
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (enabled) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                    }
                )
            }
        }
        
        Switch(
            checked = checked && enabled,
            onCheckedChange = { if (enabled) onCheckedChange(it) },
            enabled = enabled,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.surface,
                checkedTrackColor = Color(0xFF07C160),
                uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )
    }
}

@Composable
private fun NotificationInfoItem(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = iconColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(22.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        )
    }
}

@Composable
private fun NotificationDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 68.dp),
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    )
}

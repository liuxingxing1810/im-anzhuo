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
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NoAccounts
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
 * 隐私与安全设置页面
 * Privacy & Security settings screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacySecurityScreen(
    onBackClick: () -> Unit,
    onBlockedUsersClick: () -> Unit = {},
    onDevicesClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val settingsDataStore = remember { SettingsDataStore(context) }
    
    // 隐私设置状态
    val readReceipts by settingsDataStore.readReceiptsFlow.collectAsState(initial = true)
    val typingIndicator by settingsDataStore.typingIndicatorFlow.collectAsState(initial = true)
    val lastSeen by settingsDataStore.lastSeenFlow.collectAsState(initial = true)
    val onlineStatus by settingsDataStore.onlineStatusFlow.collectAsState(initial = true)
    val profilePhotoVisibility by settingsDataStore.profilePhotoVisibilityFlow.collectAsState(initial = "everyone")
    val addByPhone by settingsDataStore.addByPhoneFlow.collectAsState(initial = true)
    val addByQrCode by settingsDataStore.addByQrCodeFlow.collectAsState(initial = true)
    val addByGroup by settingsDataStore.addByGroupFlow.collectAsState(initial = true)
    
    // 白色状态栏
    WhiteStatusBar()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.privacy_title),
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
            // 账户安全
            item {
                PrivacySection(title = stringResource(R.string.privacy_account_security)) {
                    PrivacyNavigationItem(
                        icon = Icons.Default.Lock,
                        iconColor = Color(0xFF07C160),
                        title = stringResource(R.string.privacy_password),
                        subtitle = stringResource(R.string.privacy_password_desc),
                        onClick = { /* TODO: 跳转到密码设置 */ }
                    )
                    
                    PrivacyDivider()
                    
                    PrivacyNavigationItem(
                        icon = Icons.Default.Fingerprint,
                        iconColor = Color(0xFF4B8BE5),
                        title = stringResource(R.string.privacy_biometric),
                        subtitle = stringResource(R.string.privacy_biometric_desc),
                        onClick = { /* TODO: 跳转到生物识别设置 */ }
                    )
                    
                    PrivacyDivider()
                    
                    PrivacyNavigationItem(
                        icon = Icons.Default.PhoneAndroid,
                        iconColor = Color(0xFFFA9D3B),
                        title = stringResource(R.string.privacy_devices),
                        subtitle = stringResource(R.string.privacy_devices_desc),
                        onClick = onDevicesClick
                    )
                }
            }
            
            // 可见性设置
            item {
                PrivacySection(title = stringResource(R.string.privacy_visibility)) {
                    PrivacyToggleItem(
                        icon = Icons.Default.RemoveRedEye,
                        iconColor = Color(0xFF10AEFF),
                        title = stringResource(R.string.privacy_last_seen),
                        subtitle = stringResource(R.string.privacy_last_seen_desc),
                        checked = lastSeen,
                        onCheckedChange = { enabled ->
                            scope.launch {
                                settingsDataStore.setLastSeen(enabled)
                            }
                        }
                    )
                    
                    PrivacyDivider()
                    
                    PrivacyToggleItem(
                        icon = Icons.Default.Visibility,
                        iconColor = Color(0xFF07C160),
                        title = stringResource(R.string.privacy_online_status),
                        subtitle = stringResource(R.string.privacy_online_status_desc),
                        checked = onlineStatus,
                        onCheckedChange = { enabled ->
                            scope.launch {
                                settingsDataStore.setOnlineStatus(enabled)
                            }
                        }
                    )
                    
                    PrivacyDivider()
                    
                    PrivacyNavigationItem(
                        icon = Icons.Default.PersonOff,
                        iconColor = Color(0xFF8B5CF6),
                        title = stringResource(R.string.privacy_profile_photo),
                        subtitle = when (profilePhotoVisibility) {
                            "everyone" -> stringResource(R.string.privacy_photo_everyone)
                            "contacts" -> stringResource(R.string.privacy_photo_contacts)
                            "nobody" -> stringResource(R.string.privacy_photo_nobody)
                            else -> stringResource(R.string.privacy_photo_everyone)
                        },
                        onClick = { /* TODO: 显示选择对话框 */ }
                    )
                }
            }
            
            // 消息隐私
            item {
                PrivacySection(title = stringResource(R.string.privacy_messages)) {
                    PrivacyToggleItem(
                        icon = Icons.Default.Check,
                        iconColor = Color(0xFF07C160),
                        title = stringResource(R.string.privacy_read_receipts),
                        subtitle = stringResource(R.string.privacy_read_receipts_desc),
                        checked = readReceipts,
                        onCheckedChange = { enabled ->
                            scope.launch {
                                settingsDataStore.setReadReceipts(enabled)
                            }
                        }
                    )
                    
                    PrivacyDivider()
                    
                    PrivacyToggleItem(
                        icon = Icons.Default.Key,
                        iconColor = Color(0xFFFA5151),
                        title = stringResource(R.string.privacy_typing),
                        subtitle = stringResource(R.string.privacy_typing_desc),
                        checked = typingIndicator,
                        onCheckedChange = { enabled ->
                            scope.launch {
                                settingsDataStore.setTypingIndicator(enabled)
                            }
                        }
                    )
                }
            }
            
            // 添加好友方式
            item {
                PrivacySection(title = stringResource(R.string.privacy_add_me)) {
                    PrivacyToggleItem(
                        icon = Icons.Default.PersonAdd,
                        iconColor = Color(0xFF4B8BE5),
                        title = stringResource(R.string.privacy_add_by_phone),
                        subtitle = stringResource(R.string.privacy_add_by_phone_desc),
                        checked = addByPhone,
                        onCheckedChange = { enabled ->
                            scope.launch {
                                settingsDataStore.setAddByPhone(enabled)
                            }
                        }
                    )
                    
                    PrivacyDivider()
                    
                    PrivacyToggleItem(
                        icon = Icons.Default.QrCode,
                        iconColor = Color(0xFFFA9D3B),
                        title = stringResource(R.string.privacy_add_by_qr),
                        subtitle = stringResource(R.string.privacy_add_by_qr_desc),
                        checked = addByQrCode,
                        onCheckedChange = { enabled ->
                            scope.launch {
                                settingsDataStore.setAddByQrCode(enabled)
                            }
                        }
                    )
                    
                    PrivacyDivider()
                    
                    PrivacyToggleItem(
                        icon = Icons.Default.Groups,
                        iconColor = Color(0xFF07C160),
                        title = stringResource(R.string.privacy_add_by_group),
                        subtitle = stringResource(R.string.privacy_add_by_group_desc),
                        checked = addByGroup,
                        onCheckedChange = { enabled ->
                            scope.launch {
                                settingsDataStore.setAddByGroup(enabled)
                            }
                        }
                    )
                }
            }
            
            // 黑名单
            item {
                PrivacySection(title = stringResource(R.string.privacy_blocked)) {
                    PrivacyNavigationItem(
                        icon = Icons.Default.Block,
                        iconColor = Color(0xFFFA5151),
                        title = stringResource(R.string.privacy_blocked_users),
                        subtitle = stringResource(R.string.privacy_blocked_users_desc),
                        onClick = onBlockedUsersClick
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
private fun PrivacySection(
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
private fun PrivacyToggleItem(
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
        
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
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
private fun PrivacyNavigationItem(
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
private fun PrivacyDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 68.dp),
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    )
}

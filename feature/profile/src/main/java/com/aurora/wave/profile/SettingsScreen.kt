package com.aurora.wave.profile

import android.content.Context
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
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aurora.wave.data.settings.AppLanguage
import com.aurora.wave.data.settings.LanguageManager
import com.aurora.wave.data.settings.SettingsDataStore
import com.aurora.wave.data.settings.StorageInfo
import com.aurora.wave.data.settings.StorageManager
import com.aurora.wave.design.LocalThemeState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onItemClick: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    
    // 获取 ThemeState 用于实时更新主题
    val themeState = LocalThemeState.current
    
    // 设置数据存储
    val settingsDataStore = remember { SettingsDataStore(context) }
    val storageManager = remember { StorageManager(context) }
    
    // 设置状态
    val darkModeEnabled by settingsDataStore.darkModeFlow.collectAsState(initial = false)
    val currentLanguage by settingsDataStore.languageFlow.collectAsState(initial = AppLanguage.SYSTEM)
    val pushNotificationsEnabled by settingsDataStore.pushNotificationsFlow.collectAsState(initial = true)
    val messageNotificationsEnabled by settingsDataStore.messageNotificationsFlow.collectAsState(initial = true)
    val soundEnabled by settingsDataStore.soundEnabledFlow.collectAsState(initial = true)
    val vibrationEnabled by settingsDataStore.vibrationEnabledFlow.collectAsState(initial = true)
    val readReceipts by settingsDataStore.readReceiptsFlow.collectAsState(initial = true)
    val typingIndicator by settingsDataStore.typingIndicatorFlow.collectAsState(initial = true)
    val lastSeen by settingsDataStore.lastSeenFlow.collectAsState(initial = true)
    val autoDownloadMedia by settingsDataStore.autoDownloadMediaFlow.collectAsState(initial = true)
    val saveToGallery by settingsDataStore.saveToGalleryFlow.collectAsState(initial = false)
    
    // 存储信息
    var storageInfo by remember { mutableStateOf<StorageInfo?>(null) }
    var isLoadingStorage by remember { mutableStateOf(true) }
    
    // 对话框状态
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showStorageDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showClearCacheDialog by remember { mutableStateOf(false) }
    
    // 加载存储信息
    LaunchedEffect(Unit) {
        storageInfo = storageManager.getStorageInfo()
        isLoadingStorage = false
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = stringResource(R.string.settings_title), 
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
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 账户区块
            item {
                SettingsSection(title = stringResource(R.string.settings_account)) {
                    SettingsItem(
                        icon = Icons.Default.AccountCircle,
                        title = stringResource(R.string.settings_account_management),
                        subtitle = stringResource(R.string.settings_account_management_sub),
                        onClick = { onItemClick("account") }
                    )
                    SettingsDivider()
                    SettingsItem(
                        icon = Icons.Default.Lock,
                        title = stringResource(R.string.settings_security),
                        subtitle = stringResource(R.string.settings_security_sub),
                        onClick = { onItemClick("security") }
                    )
                    SettingsDivider()
                    SettingsItem(
                        icon = Icons.Default.Block,
                        title = stringResource(R.string.settings_blocked_users),
                        onClick = { onItemClick("blocked") }
                    )
                }
            }
            
            // 外观区块
            item {
                SettingsSection(title = stringResource(R.string.settings_appearance)) {
                    SettingsToggleItem(
                        icon = Icons.Default.DarkMode,
                        title = stringResource(R.string.settings_dark_mode),
                        subtitle = stringResource(R.string.settings_dark_mode_sub),
                        checked = darkModeEnabled,
                        onCheckedChange = { enabled ->
                            // 实时更新主题
                            themeState.updateDarkMode(enabled)
                            // 持久化设置
                            scope.launch {
                                settingsDataStore.setDarkMode(enabled)
                            }
                        }
                    )
                    SettingsDivider()
                    SettingsItem(
                        icon = Icons.Default.Language,
                        title = stringResource(R.string.settings_language),
                        subtitle = currentLanguage.nativeName,
                        onClick = { showLanguageDialog = true }
                    )
                }
            }
            
            // 通知区块
            item {
                SettingsSection(title = stringResource(R.string.settings_notifications)) {
                    SettingsToggleItem(
                        icon = Icons.Default.Notifications,
                        title = stringResource(R.string.settings_push_notifications),
                        subtitle = stringResource(R.string.settings_push_notifications_sub),
                        checked = pushNotificationsEnabled,
                        onCheckedChange = { enabled ->
                            scope.launch {
                                settingsDataStore.setPushNotifications(enabled)
                            }
                        }
                    )
                    SettingsDivider()
                    SettingsToggleItem(
                        icon = Icons.AutoMirrored.Filled.VolumeUp,
                        title = stringResource(R.string.settings_sound),
                        checked = soundEnabled,
                        onCheckedChange = { enabled ->
                            scope.launch {
                                settingsDataStore.setSoundEnabled(enabled)
                            }
                        }
                    )
                    SettingsDivider()
                    SettingsToggleItem(
                        icon = Icons.Default.Vibration,
                        title = stringResource(R.string.settings_vibration),
                        checked = vibrationEnabled,
                        onCheckedChange = { enabled ->
                            scope.launch {
                                settingsDataStore.setVibrationEnabled(enabled)
                            }
                        }
                    )
                }
            }
            
            // 隐私区块
            item {
                SettingsSection(title = stringResource(R.string.settings_privacy)) {
                    SettingsToggleItem(
                        icon = Icons.Default.Check,
                        title = stringResource(R.string.settings_read_receipts),
                        subtitle = stringResource(R.string.settings_read_receipts_sub),
                        checked = readReceipts,
                        onCheckedChange = { enabled ->
                            scope.launch {
                                settingsDataStore.setReadReceipts(enabled)
                            }
                        }
                    )
                    SettingsDivider()
                    SettingsToggleItem(
                        icon = Icons.Default.RemoveRedEye,
                        title = stringResource(R.string.settings_last_seen),
                        subtitle = stringResource(R.string.settings_last_seen_sub),
                        checked = lastSeen,
                        onCheckedChange = { enabled ->
                            scope.launch {
                                settingsDataStore.setLastSeen(enabled)
                            }
                        }
                    )
                }
            }
            
            // 存储区块
            item {
                SettingsSection(title = stringResource(R.string.settings_storage)) {
                    SettingsItem(
                        icon = Icons.Default.Storage,
                        title = stringResource(R.string.settings_storage_usage),
                        subtitle = if (isLoadingStorage) {
                            stringResource(R.string.loading)
                        } else {
                            storageInfo?.formatTotal() ?: "0 B"
                        },
                        onClick = { showStorageDialog = true }
                    )
                    SettingsDivider()
                    SettingsToggleItem(
                        icon = Icons.Default.Download,
                        title = stringResource(R.string.settings_auto_download),
                        subtitle = stringResource(R.string.settings_auto_download_sub),
                        checked = autoDownloadMedia,
                        onCheckedChange = { enabled ->
                            scope.launch {
                                settingsDataStore.setAutoDownloadMedia(enabled)
                            }
                        }
                    )
                    SettingsDivider()
                    SettingsToggleItem(
                        icon = Icons.Default.Photo,
                        title = stringResource(R.string.settings_save_to_gallery),
                        subtitle = stringResource(R.string.settings_save_to_gallery_sub),
                        checked = saveToGallery,
                        onCheckedChange = { enabled ->
                            scope.launch {
                                settingsDataStore.setSaveToGallery(enabled)
                            }
                        }
                    )
                    SettingsDivider()
                    SettingsItem(
                        icon = Icons.Default.CleaningServices,
                        title = stringResource(R.string.settings_clear_cache),
                        subtitle = if (isLoadingStorage) {
                            stringResource(R.string.loading)
                        } else {
                            storageInfo?.formatTotalCache() ?: "0 B"
                        },
                        onClick = { showClearCacheDialog = true }
                    )
                }
            }
            
            // 退出登录
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
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
            }
            
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
    
    // 语言选择对话框
    if (showLanguageDialog) {
        LanguageSelectionDialog(
            currentLanguage = currentLanguage,
            onLanguageSelected = { language ->
                scope.launch {
                    settingsDataStore.setLanguage(language)
                    LanguageManager.setLanguage(context, language)
                }
                showLanguageDialog = false
            },
            onDismiss = { showLanguageDialog = false }
        )
    }
    
    // 存储详情对话框
    if (showStorageDialog && storageInfo != null) {
        StorageDetailsDialog(
            storageInfo = storageInfo!!,
            onClearCache = { type ->
                scope.launch {
                    val success = when (type) {
                        "all" -> storageManager.clearAllCache()
                        "images" -> storageManager.clearImageCache()
                        "videos" -> storageManager.clearVideoCache()
                        "audio" -> storageManager.clearAudioCache()
                        "documents" -> storageManager.clearDocumentCache()
                        else -> false
                    }
                    if (success) {
                        storageInfo = storageManager.getStorageInfo()
                        snackbarHostState.showSnackbar(context.getString(R.string.cache_cleared))
                    }
                }
            },
            onDismiss = { showStorageDialog = false }
        )
    }
    
    // 清除缓存确认对话框
    if (showClearCacheDialog) {
        AlertDialog(
            onDismissRequest = { showClearCacheDialog = false },
            title = { Text(stringResource(R.string.settings_clear_cache)) },
            text = { Text(stringResource(R.string.clear_cache_confirm)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            val success = storageManager.clearAllCache()
                            if (success) {
                                storageInfo = storageManager.getStorageInfo()
                                snackbarHostState.showSnackbar(context.getString(R.string.cache_cleared))
                            }
                        }
                        showClearCacheDialog = false
                    }
                ) {
                    Text(stringResource(R.string.confirm), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearCacheDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
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
                        onItemClick("logout")
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

@Composable
private fun LanguageSelectionDialog(
    currentLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.settings_language)) },
        text = {
            Column {
                AppLanguage.entries.forEach { language ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onLanguageSelected(language) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = language == currentLanguage,
                            onClick = { onLanguageSelected(language) }
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = language.nativeName,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            if (language != AppLanguage.SYSTEM) {
                                Text(
                                    text = language.displayName,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Composable
private fun StorageDetailsDialog(
    storageInfo: StorageInfo,
    onClearCache: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.settings_storage_usage)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                StorageItem(
                    icon = Icons.Default.Image,
                    title = stringResource(R.string.storage_images),
                    size = storageInfo.formatImageCache(),
                    onClear = { onClearCache("images") }
                )
                StorageItem(
                    icon = Icons.Default.Image, // 用视频图标替换
                    title = stringResource(R.string.storage_videos),
                    size = storageInfo.formatVideoCache(),
                    onClear = { onClearCache("videos") }
                )
                StorageItem(
                    icon = Icons.AutoMirrored.Filled.VolumeUp,
                    title = stringResource(R.string.storage_audio),
                    size = storageInfo.formatAudioCache(),
                    onClear = { onClearCache("audio") }
                )
                StorageItem(
                    icon = Icons.Default.Storage,
                    title = stringResource(R.string.storage_documents),
                    size = storageInfo.formatDocumentCache(),
                    onClear = { onClearCache("documents") }
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.storage_total),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = storageInfo.formatTotalCache(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { 
                    onClearCache("all")
                    onDismiss()
                }
            ) {
                Text(stringResource(R.string.clear_all), color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.close))
            }
        }
    )
}

@Composable
private fun StorageItem(
    icon: ImageVector,
    title: String,
    size: String,
    onClear: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = size,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        IconButton(onClick = onClear, modifier = Modifier.size(32.dp)) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column {
                content()
            }
        }
    }
}

@Composable
private fun SettingsDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 56.dp),
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    )
}

@Composable
private fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
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
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun SettingsToggleItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
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
            onCheckedChange = onCheckedChange
        )
    }
}

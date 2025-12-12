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
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aurora.wave.data.settings.SettingsDataStore
import com.aurora.wave.data.settings.StorageInfo
import com.aurora.wave.data.settings.StorageManager
import com.aurora.wave.design.WhiteStatusBar
import kotlinx.coroutines.launch

/**
 * 存储与数据设置页面
 * Storage & Data settings screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorageDataScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val settingsDataStore = remember { SettingsDataStore(context) }
    val storageManager = remember { StorageManager(context) }
    val snackbarHostState = remember { SnackbarHostState() }
    
    // 存储设置状态
    val autoDownloadMedia by settingsDataStore.autoDownloadMediaFlow.collectAsState(initial = true)
    val autoDownloadOnWifi by settingsDataStore.autoDownloadOnWifiFlow.collectAsState(initial = true)
    val autoDownloadOnMobile by settingsDataStore.autoDownloadOnMobileFlow.collectAsState(initial = false)
    val saveToGallery by settingsDataStore.saveToGalleryFlow.collectAsState(initial = false)
    val dataUsageOptimization by settingsDataStore.dataUsageOptimizationFlow.collectAsState(initial = false)
    
    // 存储信息
    var storageInfo by remember { mutableStateOf<StorageInfo?>(null) }
    var isLoadingStorage by remember { mutableStateOf(true) }
    var isClearing by remember { mutableStateOf(false) }
    
    // 对话框状态
    var showClearCacheDialog by remember { mutableStateOf(false) }
    var showClearAllDialog by remember { mutableStateOf(false) }
    
    // 加载存储信息
    LaunchedEffect(Unit) {
        storageInfo = storageManager.getStorageInfo()
        isLoadingStorage = false
    }
    
    // 白色状态栏
    WhiteStatusBar()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.storage_title),
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
                .padding(padding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 存储使用情况
            item {
                StorageSection(title = stringResource(R.string.storage_usage)) {
                    StorageUsageCard(
                        storageInfo = storageInfo,
                        isLoading = isLoadingStorage
                    )
                }
            }
            
            // 存储详情
            item {
                StorageSection(title = stringResource(R.string.storage_details)) {
                    StorageDetailItem(
                        icon = Icons.Default.Image,
                        iconColor = Color(0xFF4B8BE5),
                        title = stringResource(R.string.storage_images),
                        size = storageInfo?.formatImages() ?: "0 B",
                        isLoading = isLoadingStorage
                    )
                    
                    StorageDivider()
                    
                    StorageDetailItem(
                        icon = Icons.Default.VideoFile,
                        iconColor = Color(0xFFFA5151),
                        title = stringResource(R.string.storage_videos),
                        size = storageInfo?.formatVideos() ?: "0 B",
                        isLoading = isLoadingStorage
                    )
                    
                    StorageDivider()
                    
                    StorageDetailItem(
                        icon = Icons.Default.AudioFile,
                        iconColor = Color(0xFFFA9D3B),
                        title = stringResource(R.string.storage_audio),
                        size = storageInfo?.formatAudio() ?: "0 B",
                        isLoading = isLoadingStorage
                    )
                    
                    StorageDivider()
                    
                    StorageDetailItem(
                        icon = Icons.Default.Description,
                        iconColor = Color(0xFF07C160),
                        title = stringResource(R.string.storage_documents),
                        size = storageInfo?.formatDocuments() ?: "0 B",
                        isLoading = isLoadingStorage
                    )
                    
                    StorageDivider()
                    
                    StorageDetailItem(
                        icon = Icons.Default.FolderOpen,
                        iconColor = Color(0xFF8B5CF6),
                        title = stringResource(R.string.storage_cache),
                        size = storageInfo?.formatTotalCache() ?: "0 B",
                        isLoading = isLoadingStorage
                    )
                }
            }
            
            // 自动下载
            item {
                StorageSection(title = stringResource(R.string.storage_auto_download)) {
                    StorageToggleItem(
                        icon = Icons.Default.Download,
                        iconColor = Color(0xFF10AEFF),
                        title = stringResource(R.string.storage_auto_download_media),
                        subtitle = stringResource(R.string.storage_auto_download_desc),
                        checked = autoDownloadMedia,
                        onCheckedChange = { enabled ->
                            scope.launch {
                                settingsDataStore.setAutoDownloadMedia(enabled)
                            }
                        }
                    )
                    
                    StorageDivider()
                    
                    StorageToggleItem(
                        icon = Icons.Default.Wifi,
                        iconColor = Color(0xFF07C160),
                        title = stringResource(R.string.storage_download_wifi),
                        subtitle = stringResource(R.string.storage_download_wifi_desc),
                        checked = autoDownloadOnWifi,
                        enabled = autoDownloadMedia,
                        onCheckedChange = { enabled ->
                            scope.launch {
                                settingsDataStore.setAutoDownloadOnWifi(enabled)
                            }
                        }
                    )
                    
                    StorageDivider()
                    
                    StorageToggleItem(
                        icon = Icons.Default.SignalCellularAlt,
                        iconColor = Color(0xFFFA9D3B),
                        title = stringResource(R.string.storage_download_mobile),
                        subtitle = stringResource(R.string.storage_download_mobile_desc),
                        checked = autoDownloadOnMobile,
                        enabled = autoDownloadMedia,
                        onCheckedChange = { enabled ->
                            scope.launch {
                                settingsDataStore.setAutoDownloadOnMobile(enabled)
                            }
                        }
                    )
                }
            }
            
            // 媒体设置
            item {
                StorageSection(title = stringResource(R.string.storage_media_settings)) {
                    StorageToggleItem(
                        icon = Icons.Default.PhotoLibrary,
                        iconColor = Color(0xFF4B8BE5),
                        title = stringResource(R.string.storage_save_gallery),
                        subtitle = stringResource(R.string.storage_save_gallery_desc),
                        checked = saveToGallery,
                        onCheckedChange = { enabled ->
                            scope.launch {
                                settingsDataStore.setSaveToGallery(enabled)
                            }
                        }
                    )
                    
                    StorageDivider()
                    
                    StorageToggleItem(
                        icon = Icons.Default.NetworkCheck,
                        iconColor = Color(0xFF8B5CF6),
                        title = stringResource(R.string.storage_data_saver),
                        subtitle = stringResource(R.string.storage_data_saver_desc),
                        checked = dataUsageOptimization,
                        onCheckedChange = { enabled ->
                            scope.launch {
                                settingsDataStore.setDataUsageOptimization(enabled)
                            }
                        }
                    )
                }
            }
            
            // 清理操作
            item {
                StorageSection(title = stringResource(R.string.storage_cleanup)) {
                    StorageActionItem(
                        icon = Icons.Default.CleaningServices,
                        iconColor = Color(0xFFFA9D3B),
                        title = stringResource(R.string.storage_clear_cache),
                        subtitle = stringResource(R.string.storage_clear_cache_desc),
                        onClick = { showClearCacheDialog = true }
                    )
                    
                    StorageDivider()
                    
                    StorageActionItem(
                        icon = Icons.Default.Delete,
                        iconColor = Color(0xFFFA5151),
                        title = stringResource(R.string.storage_clear_all),
                        subtitle = stringResource(R.string.storage_clear_all_desc),
                        onClick = { showClearAllDialog = true }
                    )
                }
            }
            
            // 底部间距
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
    
    // 清除缓存确认对话框
    if (showClearCacheDialog) {
        AlertDialog(
            onDismissRequest = { showClearCacheDialog = false },
            title = { Text(stringResource(R.string.storage_clear_cache)) },
            text = { Text(stringResource(R.string.storage_clear_cache_confirm)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showClearCacheDialog = false
                        scope.launch {
                            isClearing = true
                            storageManager.clearCache()
                            storageInfo = storageManager.getStorageInfo()
                            isClearing = false
                            snackbarHostState.showSnackbar(context.getString(R.string.cache_cleared))
                        }
                    }
                ) {
                    Text(stringResource(R.string.confirm), color = Color(0xFFFA5151))
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearCacheDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
    
    // 清除所有数据确认对话框
    if (showClearAllDialog) {
        AlertDialog(
            onDismissRequest = { showClearAllDialog = false },
            title = { Text(stringResource(R.string.storage_clear_all)) },
            text = { Text(stringResource(R.string.storage_clear_all_confirm)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showClearAllDialog = false
                        scope.launch {
                            isClearing = true
                            storageManager.clearAllData()
                            storageInfo = storageManager.getStorageInfo()
                            isClearing = false
                            snackbarHostState.showSnackbar(context.getString(R.string.data_cleared))
                        }
                    }
                ) {
                    Text(stringResource(R.string.confirm), color = Color(0xFFFA5151))
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearAllDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

@Composable
private fun StorageSection(
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
private fun StorageUsageCard(
    storageInfo: StorageInfo?,
    isLoading: Boolean
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
            Column {
                Text(
                    text = stringResource(R.string.storage_total_used),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = storageInfo?.formatTotal() ?: "0 B",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            
            Icon(
                imageVector = Icons.Default.Storage,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = Color(0xFF10AEFF)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 存储使用进度条
        if (!isLoading && storageInfo != null) {
            LinearProgressIndicator(
                progress = { (storageInfo.totalBytes.toFloat() / (1024 * 1024 * 1024)).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = Color(0xFF10AEFF),
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StorageLegendItem(color = Color(0xFF4B8BE5), label = stringResource(R.string.storage_images))
                StorageLegendItem(color = Color(0xFFFA5151), label = stringResource(R.string.storage_videos))
                StorageLegendItem(color = Color(0xFFFA9D3B), label = stringResource(R.string.storage_audio))
                StorageLegendItem(color = Color(0xFF07C160), label = stringResource(R.string.storage_documents))
            }
        }
    }
}

@Composable
private fun StorageLegendItem(
    color: Color,
    label: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color, RoundedCornerShape(2.dp))
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun StorageDetailItem(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    size: String,
    isLoading: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
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
        
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = size,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun StorageToggleItem(
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
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF07C160),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFE5E5E5)
            )
        )
    }
}

@Composable
private fun StorageActionItem(
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
private fun StorageDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 68.dp),
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    )
}

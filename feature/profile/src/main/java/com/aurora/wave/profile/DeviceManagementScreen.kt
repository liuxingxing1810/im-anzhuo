package com.aurora.wave.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Tablet
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aurora.wave.design.GrayStatusBar

/**
 * 设备类型
 */
enum class DeviceType {
    PHONE,
    TABLET,
    COMPUTER
}

/**
 * 设备数据模型
 */
data class DeviceInfo(
    val id: String,
    val name: String,
    val type: DeviceType,
    val lastActive: String,
    val location: String,
    val isCurrent: Boolean = false
)

/**
 * 设备管理页面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceManagementScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Mock 设备数据
    var devices by remember {
        mutableStateOf(
            listOf(
                DeviceInfo(
                    id = "1",
                    name = "当前设备",
                    type = DeviceType.PHONE,
                    lastActive = "在线",
                    location = "北京",
                    isCurrent = true
                ),
                DeviceInfo(
                    id = "2",
                    name = "iPad Pro",
                    type = DeviceType.TABLET,
                    lastActive = "2小时前",
                    location = "北京"
                ),
                DeviceInfo(
                    id = "3",
                    name = "MacBook Pro",
                    type = DeviceType.COMPUTER,
                    lastActive = "昨天 18:30",
                    location = "上海"
                ),
                DeviceInfo(
                    id = "4",
                    name = "Windows 电脑",
                    type = DeviceType.COMPUTER,
                    lastActive = "3天前",
                    location = "广州"
                )
            )
        )
    }
    
    var showLogoutDialog by remember { mutableStateOf<DeviceInfo?>(null) }
    
    GrayStatusBar()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "登录设备管理",
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            item {
                Text(
                    text = "当前登录的设备",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }
            
            // 当前设备
            item {
                val currentDevice = devices.find { it.isCurrent }
                currentDevice?.let {
                    DeviceItem(
                        device = it,
                        onClick = { },
                        showLogout = false
                    )
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "其他登录设备",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }
            
            // 其他设备
            items(devices.filter { !it.isCurrent }) { device ->
                DeviceItem(
                    device = device,
                    onClick = { showLogoutDialog = device },
                    showLogout = true
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "点击设备可以将其退出登录",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
    
    // 退出登录对话框
    showLogoutDialog?.let { device ->
        AlertDialog(
            onDismissRequest = { showLogoutDialog = null },
            title = { Text("退出登录") },
            text = { Text("确定要将 \"${device.name}\" 退出登录吗？") },
            confirmButton = {
                TextButton(
                    onClick = {
                        devices = devices.filter { it.id != device.id }
                        showLogoutDialog = null
                    }
                ) {
                    Text("确定", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = null }) {
                    Text("取消")
                }
            }
        )
    }
}

@Composable
private fun DeviceItem(
    device: DeviceInfo,
    onClick: () -> Unit,
    showLogout: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 设备图标
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        if (device.isCurrent) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (device.type) {
                        DeviceType.PHONE -> Icons.Default.PhoneAndroid
                        DeviceType.TABLET -> Icons.Default.Tablet
                        DeviceType.COMPUTER -> Icons.Default.Computer
                    },
                    contentDescription = null,
                    tint = if (device.isCurrent) {
                        Color.White
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = device.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    if (device.isCurrent) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "本机",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${device.location} · ${device.lastActive}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            if (showLogout) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "更多",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

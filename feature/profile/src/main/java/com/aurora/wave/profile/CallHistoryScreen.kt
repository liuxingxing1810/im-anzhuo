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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallMade
import androidx.compose.material.icons.filled.CallMissed
import androidx.compose.material.icons.filled.CallReceived
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.aurora.wave.design.GrayStatusBar

/**
 * 通话类型
 */
enum class CallType {
    OUTGOING,       // 拨出
    INCOMING,       // 呼入
    MISSED          // 未接
}

/**
 * 通话记录数据模型
 */
data class CallRecord(
    val id: String,
    val contactId: String,
    val contactName: String,
    val callType: CallType,
    val isVideo: Boolean,
    val timestamp: String,
    val duration: String? = null  // null 表示未接通
)

/**
 * 通话记录页面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CallHistoryScreen(
    onBackClick: () -> Unit,
    onCallClick: (String, String, Boolean) -> Unit = { _, _, _ -> },
    modifier: Modifier = Modifier
) {
    // Mock 通话记录
    var callRecords by remember {
        mutableStateOf(
            listOf(
                CallRecord(
                    id = "1",
                    contactId = "contact_1",
                    contactName = "张三",
                    callType = CallType.OUTGOING,
                    isVideo = false,
                    timestamp = "今天 14:30",
                    duration = "5分23秒"
                ),
                CallRecord(
                    id = "2",
                    contactId = "contact_2",
                    contactName = "李四",
                    callType = CallType.MISSED,
                    isVideo = true,
                    timestamp = "今天 12:15",
                    duration = null
                ),
                CallRecord(
                    id = "3",
                    contactId = "contact_3",
                    contactName = "王五",
                    callType = CallType.INCOMING,
                    isVideo = false,
                    timestamp = "昨天 18:45",
                    duration = "12分08秒"
                ),
                CallRecord(
                    id = "4",
                    contactId = "contact_1",
                    contactName = "张三",
                    callType = CallType.OUTGOING,
                    isVideo = true,
                    timestamp = "昨天 15:20",
                    duration = "2分45秒"
                ),
                CallRecord(
                    id = "5",
                    contactId = "contact_4",
                    contactName = "赵六",
                    callType = CallType.MISSED,
                    isVideo = false,
                    timestamp = "3天前",
                    duration = null
                ),
                CallRecord(
                    id = "6",
                    contactId = "contact_5",
                    contactName = "钱七",
                    callType = CallType.INCOMING,
                    isVideo = true,
                    timestamp = "3天前",
                    duration = "8分32秒"
                )
            )
        )
    }
    
    var showClearDialog by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    
    GrayStatusBar()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "通话记录",
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
                actions = {
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "更多"
                            )
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("清空通话记录") },
                                onClick = {
                                    showMenu = false
                                    showClearDialog = true
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null
                                    )
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        if (callRecords.isEmpty()) {
            // 空状态
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "暂无通话记录",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                items(callRecords, key = { it.id }) { record ->
                    CallRecordItem(
                        record = record,
                        onClick = {
                            onCallClick(record.contactId, record.contactName, record.isVideo)
                        },
                        onDelete = {
                            callRecords = callRecords.filter { it.id != record.id }
                        }
                    )
                }
            }
        }
    }
    
    // 清空确认对话框
    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("清空通话记录") },
            text = { Text("确定要清空所有通话记录吗？此操作不可恢复。") },
            confirmButton = {
                TextButton(
                    onClick = {
                        callRecords = emptyList()
                        showClearDialog = false
                    }
                ) {
                    Text("确定", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("取消")
                }
            }
        )
    }
}

@Composable
private fun CallRecordItem(
    record: CallRecord,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 头像
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = record.contactName.first().toString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = record.contactName,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = if (record.callType == CallType.MISSED) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 通话类型图标
                    Icon(
                        imageVector = when (record.callType) {
                            CallType.OUTGOING -> Icons.Default.CallMade
                            CallType.INCOMING -> Icons.Default.CallReceived
                            CallType.MISSED -> Icons.Default.CallMissed
                        },
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = when (record.callType) {
                            CallType.OUTGOING -> MaterialTheme.colorScheme.primary
                            CallType.INCOMING -> MaterialTheme.colorScheme.tertiary
                            CallType.MISSED -> MaterialTheme.colorScheme.error
                        }
                    )
                    
                    Spacer(modifier = Modifier.width(4.dp))
                    
                    // 视频/语音图标
                    Icon(
                        imageVector = if (record.isVideo) Icons.Default.Videocam else Icons.Default.Call,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Text(
                        text = if (record.duration != null) {
                            "${record.timestamp} · ${record.duration}"
                        } else {
                            "${record.timestamp} · 未接听"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            // 回拨按钮
            IconButton(
                onClick = onClick
            ) {
                Icon(
                    imageVector = if (record.isVideo) Icons.Default.Videocam else Icons.Default.Call,
                    contentDescription = if (record.isVideo) "视频通话" else "语音通话",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

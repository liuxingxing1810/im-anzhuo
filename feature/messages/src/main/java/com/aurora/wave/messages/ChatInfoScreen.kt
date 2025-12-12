package com.aurora.wave.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aurora.wave.design.GrayStatusBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatInfoScreen(
    conversationId: String,
    onBackClick: () -> Unit,
    onAddMemberClick: () -> Unit = {},
    onSearchChatClick: () -> Unit = {},
    onSetBackgroundClick: () -> Unit = {},
    onClearChatClick: () -> Unit = {},
    onReportClick: () -> Unit = {},
    viewModel: ChatDetailViewModel = viewModel()
) {
    LaunchedEffect(conversationId) {
        viewModel.loadConversation(conversationId)
    }
    
    val state by viewModel.state.collectAsState()
    
    // 本地开关状态
    var muteNotifications by remember { mutableStateOf(false) }
    var pinChat by remember { mutableStateOf(false) }
    var reminder by remember { mutableStateOf(false) }
    
    // 灰色状态栏
    GrayStatusBar()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "聊天信息",
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
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                .verticalScroll(rememberScrollState())
        ) {
            // 成员区域
            MemberSection(
                memberName = state.conversationName,
                memberAvatar = state.conversationName.firstOrNull()?.toString() ?: "?",
                onAddMemberClick = onAddMemberClick
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 查找聊天记录
            ClickableItem(
                title = "查找聊天记录",
                onClick = onSearchChatClick
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 开关选项组
            SwitchSection(
                muteNotifications = muteNotifications,
                onMuteChange = { muteNotifications = it },
                pinChat = pinChat,
                onPinChange = { pinChat = it },
                reminder = reminder,
                onReminderChange = { reminder = it }
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 设置聊天背景
            ClickableItem(
                title = "设置当前聊天背景",
                onClick = onSetBackgroundClick
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 清空聊天记录
            ClickableItem(
                title = "清空聊天记录",
                onClick = onClearChatClick
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 投诉
            ClickableItem(
                title = "投诉",
                onClick = onReportClick
            )
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

/**
 * 成员区域
 */
@Composable
private fun MemberSection(
    memberName: String,
    memberAvatar: String,
    onAddMemberClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        // 成员头像和名字
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(60.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = memberAvatar,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = memberName,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                textAlign = TextAlign.Center
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        // 添加按钮
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(60.dp)
                .clickable(onClick = onAddMemberClick)
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(4.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "添加成员",
                    tint = MaterialTheme.colorScheme.outlineVariant,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            // 占位，保持对齐
            Text(
                text = "",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

/**
 * 开关选项区域
 */
@Composable
private fun SwitchSection(
    muteNotifications: Boolean,
    onMuteChange: (Boolean) -> Unit,
    pinChat: Boolean,
    onPinChange: (Boolean) -> Unit,
    reminder: Boolean,
    onReminderChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        SwitchItem(
            title = "消息免打扰",
            checked = muteNotifications,
            onCheckedChange = onMuteChange
        )
        
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        )
        
        SwitchItem(
            title = "置顶聊天",
            checked = pinChat,
            onCheckedChange = onPinChange
        )
        
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        )
        
        SwitchItem(
            title = "提醒",
            checked = reminder,
            onCheckedChange = onReminderChange
        )
    }
}

/**
 * 开关项
 */
@Composable
private fun SwitchItem(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF4CAF50),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                uncheckedBorderColor = Color.Transparent
            )
        )
    }
}

/**
 * 可点击项（带箭头）
 */
@Composable
private fun ClickableItem(
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        )
    }
}

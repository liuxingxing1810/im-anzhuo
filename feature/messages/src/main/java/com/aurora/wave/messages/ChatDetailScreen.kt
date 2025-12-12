package com.aurora.wave.messages

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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aurora.wave.design.GrayStatusBar
import com.aurora.wave.messages.ui.ChatInputBar
import com.aurora.wave.messages.ui.MessageBubble

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(
    conversationId: String,
    onBackClick: () -> Unit,
    onContactClick: (String) -> Unit = {},
    onCallClick: () -> Unit = {},
    onVideoCallClick: () -> Unit = {},
    onMoreClick: () -> Unit = {},
    viewModel: ChatDetailViewModel = viewModel()
) {
    LaunchedEffect(conversationId) {
        viewModel.loadConversation(conversationId)
    }
    
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()
    
    // 灰色状态栏
    GrayStatusBar()
    
    // 首次加载完成后滚动到底部（不使用动画，立即跳转）
    LaunchedEffect(state.isLoading, state.messages.size) {
        if (!state.isLoading && state.messages.isNotEmpty()) {
            // 使用 scrollToItem 而非 animateScrollToItem，首次加载立即定位
            listState.scrollToItem(state.messages.lastIndex)
        }
    }
    
    // 新消息到达时使用动画滚动
    LaunchedEffect(state.messages.lastOrNull()?.id) {
        if (state.messages.isNotEmpty() && !state.isLoading) {
            // 只有在接近底部时才自动滚动
            val lastVisibleIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            if (lastVisibleIndex >= state.messages.size - 3) {
                listState.animateScrollToItem(state.messages.lastIndex)
            }
        }
    }
    
    // 监听键盘状态，键盘弹出时自动滚动到底部
    val density = LocalDensity.current
    val imeHeight = WindowInsets.ime.getBottom(density)
    val currentMessages by rememberUpdatedState(state.messages)
    
    LaunchedEffect(imeHeight) {
        // 键盘弹出时 (imeHeight > 0) 滚动到底部
        if (imeHeight > 0 && currentMessages.isNotEmpty()) {
            listState.animateScrollToItem(currentMessages.lastIndex)
        }
    }
    
    // 面板打开状态
    var isPanelOpen by remember { mutableStateOf(false) }
    
    // 面板打开时滚动到底部（等待面板动画完成）
    LaunchedEffect(isPanelOpen) {
        if (isPanelOpen && currentMessages.isNotEmpty()) {
            // 等待面板展开动画完成（动画是250ms，等400ms确保布局稳定）
            kotlinx.coroutines.delay(400)
            // 使用scrollToItem确保立即到位，不使用动画避免与面板动画冲突
            listState.scrollToItem(currentMessages.lastIndex)
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        Scaffold(
            topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onContactClick(conversationId) }
                    ) {
                        // Avatar
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = state.conversationName.firstOrNull()?.toString() ?: "?",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Column {
                            Text(
                                text = state.conversationName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            
                            // Online status
                            Text(
                                text = if (state.isOnline) "Online" else "Last seen recently",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (state.isOnline) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onCallClick) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Voice call"
                        )
                    }
                    IconButton(onClick = onVideoCallClick) {
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = "Video call"
                        )
                    }
                    IconButton(onClick = onMoreClick) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More options"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .navigationBarsPadding()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // 消息列表区域 - 占据剩余空间
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when {
                    state.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    
                    state.error != null -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Error loading chat",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.error
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = state.error ?: "",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                    
                    state.messages.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "👋",
                                    style = MaterialTheme.typography.displayLarge
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Say hello to ${state.conversationName}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                    
                    else -> {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                horizontal = 12.dp,
                                vertical = 8.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            // Date header - 动态计算日期
                            val firstMessageTimestamp = state.messages.firstOrNull()?.fullTimestamp ?: System.currentTimeMillis()
                            item {
                                DateHeader(date = formatDateHeader(firstMessageTimestamp))
                            }
                            
                            items(
                                items = state.messages,
                                key = { it.id }
                            ) { message ->
                                MessageBubble(
                                    message = message,
                                    onAvatarClick = { senderId -> onContactClick(senderId) },
                                    modifier = Modifier.padding(vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
            
            // 输入栏和面板 - 放在底部
            ChatInputBar(
                text = state.inputText,
                onTextChange = viewModel::onInputTextChange,
                onSendClick = viewModel::sendMessage,
                onVoiceClick = { /* TODO */ },
                onEmojiClick = { /* TODO */ },
                onAttachClick = { /* TODO */ },
                onCameraClick = { /* TODO */ },
                isSending = state.isSending,
                onPanelStateChange = { isOpen -> isPanelOpen = isOpen }
            )
        }
    }
    }  // 关闭 imePadding Box
}

@Composable
private fun DateHeader(
    date: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = date,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    shape = CircleShape
                )
                .padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}

/**
 * 根据时间戳计算日期显示文本
 * Calculate date display text based on timestamp
 */
private fun formatDateHeader(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val messageDate = java.util.Calendar.getInstance().apply { timeInMillis = timestamp }
    val today = java.util.Calendar.getInstance()
    val yesterday = java.util.Calendar.getInstance().apply { add(java.util.Calendar.DAY_OF_YEAR, -1) }
    
    return when {
        isSameDay(messageDate, today) -> "Today"
        isSameDay(messageDate, yesterday) -> "Yesterday"
        else -> {
            val formatter = java.text.SimpleDateFormat("MMM d, yyyy", java.util.Locale.getDefault())
            formatter.format(java.util.Date(timestamp))
        }
    }
}

private fun isSameDay(cal1: java.util.Calendar, cal2: java.util.Calendar): Boolean {
    return cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR) &&
           cal1.get(java.util.Calendar.DAY_OF_YEAR) == cal2.get(java.util.Calendar.DAY_OF_YEAR)
}

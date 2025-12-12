package com.aurora.wave.messages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aurora.wave.design.GrayStatusBar
import com.aurora.wave.messages.ui.SwipeableConversationItem

/**
 * 消息列表主页面
 * 
 * 架构说明：
 * - 不使用内部 Scaffold，由 MainActivity 的 Scaffold 统一管理 WindowInsets
 * - padding 参数来自 MainActivity，包含顶部状态栏和底部导航栏的内边距
 * - TopAppBar 设置 windowInsets = WindowInsets(0, 0, 0, 0) 禁用自动 insets 处理
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesRootScreen(
    padding: PaddingValues,
    onConversationClick: (String) -> Unit = {},
    onSearchClick: () -> Unit = {},
    onGroupChatClick: () -> Unit = {},
    onAddFriendClick: () -> Unit = {},
    onScanClick: () -> Unit = {},
    viewModel: ConversationListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()
    
    // 菜单展开状态
    var showMenu by remember { mutableStateOf(false) }
    
    // 灰色状态栏
    GrayStatusBar()
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding) // 应用来自 MainActivity 的 padding
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // TopAppBar - 禁用自动 WindowInsets 处理，因为外部 Scaffold 已处理
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.messages_title),
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "搜索"
                        )
                    }
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "更多"
                        )
                    }
                },
                windowInsets = WindowInsets(0, 0, 0, 0), // 禁用自动 insets
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.background
                )
            )
            
            // 内容区域
            when {
                state.isLoading -> {
                    LoadingContent()
                }
                state.conversations.isEmpty() -> {
                    EmptyConversationsPlaceholder(
                        searchQuery = state.searchQuery,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                else -> {
                    ConversationList(
                        conversations = state.conversations,
                        onConversationClick = onConversationClick,
                        onPinConversation = viewModel::onPinConversation,
                        onDeleteConversation = viewModel::onDeleteConversation,
                        listState = listState
                    )
                }
            }
        }
        
        // 下拉菜单覆盖层
        PlusMenuOverlay(
            visible = showMenu,
            onDismiss = { showMenu = false },
            onGroupChatClick = { 
                showMenu = false
                onGroupChatClick()
            },
            onAddFriendClick = { 
                showMenu = false
                onAddFriendClick()
            },
            onScanClick = { 
                showMenu = false
                onScanClick()
            },
            onPaymentClick = { 
                showMenu = false
                // TODO: 收付款
            }
        )
    }
}

/**
 * "+" 按钮下拉菜单覆盖层
 */
@Composable
private fun PlusMenuOverlay(
    visible: Boolean,
    onDismiss: () -> Unit,
    onGroupChatClick: () -> Unit,
    onAddFriendClick: () -> Unit,
    onScanClick: () -> Unit,
    onPaymentClick: () -> Unit
) {
    // 使用 MutableTransitionState 来实现进入和退出动画
    val expandedState = remember { MutableTransitionState(false) }
    expandedState.targetState = visible
    
    if (expandedState.currentState || expandedState.targetState) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss
                )
        ) {
            // 菜单卡片，定位在右上角
            AnimatedVisibility(
                visibleState = expandedState,
                enter = fadeIn(animationSpec = tween(150)) + 
                        expandVertically(
                            animationSpec = tween(200),
                            expandFrom = Alignment.Top
                        ),
                exit = fadeOut(animationSpec = tween(150)) + 
                       shrinkVertically(
                           animationSpec = tween(200),
                           shrinkTowards = Alignment.Top
                       ),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 56.dp, end = 12.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF4A4A4A)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { /* 阻止点击穿透 */ }
                        )
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        PlusMenuItem(
                            icon = Icons.Default.GroupAdd,
                            text = "发起群聊",
                            onClick = onGroupChatClick
                        )
                        PlusMenuItem(
                            icon = Icons.Default.PersonAdd,
                            text = "添加朋友",
                            onClick = onAddFriendClick
                        )
                        PlusMenuItem(
                            icon = Icons.Default.QrCodeScanner,
                            text = "扫一扫",
                            onClick = onScanClick
                        )
                        PlusMenuItem(
                            icon = Icons.Default.Payment,
                            text = "收付款",
                            onClick = onPaymentClick
                        )
                    }
                }
            }
        }
    }
}

/**
 * 菜单项
 */
@Composable
private fun PlusMenuItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            color = Color.White,
            fontSize = 15.sp
        )
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ConversationList(
    conversations: List<com.aurora.wave.messages.model.ConversationUiModel>,
    onConversationClick: (String) -> Unit,
    onPinConversation: (String) -> Unit,
    onDeleteConversation: (String) -> Unit,
    listState: androidx.compose.foundation.lazy.LazyListState
) {
    // 跟踪当前展开的会话ID（一次只能展开一个）
    var expandedConversationId by remember { mutableStateOf<String?>(null) }
    
    // 微信风格：白色背景、无间隔、分隔线
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        items(
            items = conversations,
            key = { it.id }
        ) { conversation ->
            SwipeableConversationItem(
                conversation = conversation,
                isExpanded = expandedConversationId == conversation.id,
                onExpandChange = { expanded ->
                    expandedConversationId = if (expanded) conversation.id else null
                },
                onClick = { 
                    // 如果有展开的，先收起来
                    if (expandedConversationId != null) {
                        expandedConversationId = null
                    } else {
                        onConversationClick(conversation.id) 
                    }
                },
                onPinClick = { 
                    expandedConversationId = null
                    onPinConversation(conversation.id) 
                },
                onDeleteClick = { 
                    expandedConversationId = null
                    onDeleteConversation(conversation.id) 
                }
            )
            // 分隔线
            if (conversation != conversations.last()) {
                HorizontalDivider(
                    modifier = Modifier.padding(start = 76.dp),
                    thickness = 0.5.dp,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Composable
private fun EmptyConversationsPlaceholder(
    searchQuery: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = if (searchQuery.isNotBlank()) "🔍" else "💬",
                style = MaterialTheme.typography.displayLarge
            )
            Text(
                text = if (searchQuery.isNotBlank()) {
                    stringResource(R.string.messages_search_empty)
                } else {
                    stringResource(R.string.messages_empty_title)
                },
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = if (searchQuery.isNotBlank()) {
                    stringResource(R.string.messages_search_hint)
                } else {
                    stringResource(R.string.messages_empty_subtitle)
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

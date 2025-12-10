package com.aurora.wave.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aurora.wave.design.GrayStatusBar
import com.aurora.wave.messages.ui.ConversationItem

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
    viewModel: ConversationListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()
    
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
                    IconButton(onClick = { /* TODO: More */ }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
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
                        listState = listState
                    )
                }
            }
        }
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
    listState: androidx.compose.foundation.lazy.LazyListState
) {
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
            ConversationItem(
                conversation = conversation,
                onClick = { onConversationClick(conversation.id) }
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

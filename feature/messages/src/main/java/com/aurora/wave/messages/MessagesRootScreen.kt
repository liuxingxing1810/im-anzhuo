package com.aurora.wave.messages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aurora.wave.messages.ui.ConversationItem
import com.aurora.wave.messages.ui.SearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesRootScreen(
    padding: PaddingValues,
    onConversationClick: (String) -> Unit = {},
    viewModel: ConversationListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val listState = rememberLazyListState()
    
    // FAB 可见性
    val showFab by remember {
        derivedStateOf { listState.firstVisibleItemIndex < 3 }
    }
    
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = padding.calculateBottomPadding())
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MessagesTopBar(scrollBehavior = scrollBehavior)
        },
        floatingActionButton = {
            ModernFAB(
                visible = showFab,
                onClick = { /* TODO: New chat */ }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 微信风格搜索栏 - 放在白色区域内
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                SearchBar(
                    query = state.searchQuery,
                    onQueryChange = viewModel::onSearchQueryChange
                )
            }
            
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

/**
 * 现代化 FAB
 */
@Composable
private fun ModernFAB(
    visible: Boolean,
    onClick: () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(spring()) + fadeIn(),
        exit = scaleOut(spring()) + fadeOut()
    ) {
        Surface(
            onClick = onClick,
            shape = CircleShape,
            shadowElevation = 8.dp,
            modifier = Modifier.size(56.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.85f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Create,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MessagesTopBar(
    scrollBehavior: TopAppBarScrollBehavior
) {
    // 现代化顶部栏
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.messages_title),
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleLarge
            )
        },
        actions = {
            IconButton(onClick = { /* TODO: Search */ }) {
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
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.background
        )
    )
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

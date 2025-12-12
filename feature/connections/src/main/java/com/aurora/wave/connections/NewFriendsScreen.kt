package com.aurora.wave.connections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aurora.wave.connections.model.FriendRequestUiModel
import com.aurora.wave.connections.model.FriendRequestStatus
import com.aurora.wave.design.WhiteStatusBar
import kotlinx.coroutines.delay

/**
 * 新的朋友页面
 * 显示好友请求列表，支持接受/拒绝操作
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewFriendsScreen(
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit = {},
    viewModel: NewFriendsViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    
    WhiteStatusBar()
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "新的朋友",
                        fontWeight = FontWeight.Medium,
                        fontSize = 17.sp
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
                    IconButton(onClick = onSearchClick) {
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = "添加朋友"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // 搜索入口
            SearchEntryCard(
                onClick = onSearchClick,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                state.friendRequests.isEmpty() -> {
                    EmptyFriendRequestsPlaceholder()
                }
                else -> {
                    FriendRequestsList(
                        requests = state.friendRequests,
                        onAccept = viewModel::acceptRequest,
                        onReject = viewModel::rejectRequest
                    )
                }
            }
        }
    }
}

/**
 * 搜索入口卡片
 */
@Composable
private fun SearchEntryCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PersonSearch,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "添加朋友",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "搜索 Wave号/手机号/昵称",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * 好友请求列表
 */
@Composable
private fun FriendRequestsList(
    requests: List<FriendRequestUiModel>,
    onAccept: (String) -> Unit,
    onReject: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        // 最近三天
        val recentRequests = requests.filter { it.isRecent }
        if (recentRequests.isNotEmpty()) {
            item { 
                SectionHeader(title = "最近三天")
            }
            itemsIndexed(
                items = recentRequests,
                key = { _, item -> item.id }
            ) { index, request ->
                FriendRequestItem(
                    request = request,
                    onAccept = { onAccept(request.id) },
                    onReject = { onReject(request.id) },
                    showDivider = index < recentRequests.size - 1
                )
            }
        }
        
        // 更早
        val olderRequests = requests.filter { !it.isRecent }
        if (olderRequests.isNotEmpty()) {
            item { 
                SectionHeader(title = "更早")
            }
            itemsIndexed(
                items = olderRequests,
                key = { _, item -> item.id }
            ) { index, request ->
                FriendRequestItem(
                    request = request,
                    onAccept = { onAccept(request.id) },
                    onReject = { onReject(request.id) },
                    showDivider = index < olderRequests.size - 1
                )
            }
        }
    }
}

/**
 * 分组标题
 */
@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        fontSize = 13.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

/**
 * 好友请求项
 */
@Composable
private fun FriendRequestItem(
    request: FriendRequestUiModel,
    onAccept: () -> Unit,
    onReject: () -> Unit,
    showDivider: Boolean = false
) {
    var isProcessing by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isProcessing) 0.95f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "scale"
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .animateContentSize(),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 头像
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            when (request.avatarColor) {
                                "blue" -> Color(0xFF4B8BE5)
                                "green" -> Color(0xFF07C160)
                                "orange" -> Color(0xFFFA9D3B)
                                "purple" -> Color(0xFF9B59B6)
                                else -> MaterialTheme.colorScheme.primaryContainer
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = request.name.firstOrNull()?.toString() ?: "?",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                // 信息
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = request.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = request.message,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                // 操作按钮
                when (request.status) {
                    FriendRequestStatus.PENDING -> {
                        Button(
                            onClick = {
                                isProcessing = true
                                onAccept()
                            },
                            modifier = Modifier.height(32.dp),
                            shape = RoundedCornerShape(4.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            contentPadding = PaddingValues(horizontal = 16.dp)
                        ) {
                            Text(
                                text = "接受",
                                fontSize = 13.sp
                            )
                        }
                    }
                    FriendRequestStatus.ACCEPTED -> {
                        Text(
                            text = "已添加",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    FriendRequestStatus.REJECTED -> {
                        Text(
                            text = "已拒绝",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                    FriendRequestStatus.EXPIRED -> {
                        Text(
                            text = "已过期",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
            
            if (showDivider) {
                HorizontalDivider(
                    modifier = Modifier.padding(start = 76.dp),
                    thickness = 0.5.dp,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                )
            }
        }
    }
}

/**
 * 空状态占位符
 */
@Composable
private fun EmptyFriendRequestsPlaceholder() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.PersonAdd,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
            )
            Text(
                text = "暂无新的好友请求",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "点击上方搜索添加新朋友",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

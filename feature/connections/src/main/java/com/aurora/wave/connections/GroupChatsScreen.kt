package com.aurora.wave.connections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.sp
import com.aurora.wave.design.WhiteStatusBar
import kotlinx.coroutines.delay

/**
 * 群聊UI模型
 */
data class GroupChatUiModel(
    val id: String,
    val name: String,
    val memberCount: Int,
    val avatarColors: List<String> = emptyList(),
    val lastMessage: String? = null,
    val lastMessageTime: String? = null,
    val isMuted: Boolean = false,
    val isTop: Boolean = false
)

/**
 * 群聊列表页面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupChatsScreen(
    onBackClick: () -> Unit,
    onGroupClick: (String) -> Unit = {},
    onCreateGroupClick: () -> Unit = {},
    onSearchClick: () -> Unit = {}
) {
    var isLoading by remember { mutableStateOf(true) }
    var groups by remember { mutableStateOf<List<GroupChatUiModel>>(emptyList()) }
    
    // 加载群聊数据
    LaunchedEffect(Unit) {
        delay(300)
        groups = generateFakeGroups()
        isLoading = false
    }
    
    WhiteStatusBar()
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "群聊",
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
                            imageVector = Icons.Default.Search,
                            contentDescription = "搜索"
                        )
                    }
                    IconButton(onClick = onCreateGroupClick) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "创建群聊"
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
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            groups.isEmpty() -> {
                EmptyGroupsPlaceholder(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    onCreateGroupClick = onCreateGroupClick
                )
            }
            else -> {
                GroupsList(
                    groups = groups,
                    onGroupClick = onGroupClick,
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}

/**
 * 群聊列表
 */
@Composable
private fun GroupsList(
    groups: List<GroupChatUiModel>,
    onGroupClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        // 置顶群聊
        val topGroups = groups.filter { it.isTop }
        val normalGroups = groups.filter { !it.isTop }
        
        if (topGroups.isNotEmpty()) {
            item {
                SectionHeader(title = "置顶群聊")
            }
            itemsIndexed(
                items = topGroups,
                key = { _, group -> group.id }
            ) { index, group ->
                GroupChatItem(
                    group = group,
                    onClick = { onGroupClick(group.id) },
                    showDivider = index < topGroups.size - 1
                )
            }
        }
        
        // 全部群聊
        if (normalGroups.isNotEmpty()) {
            item {
                SectionHeader(title = "全部群聊 (${groups.size})")
            }
            itemsIndexed(
                items = normalGroups,
                key = { _, group -> group.id }
            ) { index, group ->
                GroupChatItem(
                    group = group,
                    onClick = { onGroupClick(group.id) },
                    showDivider = index < normalGroups.size - 1
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
 * 群聊列表项
 */
@Composable
private fun GroupChatItem(
    group: GroupChatUiModel,
    onClick: () -> Unit,
    showDivider: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .animateContentSize(
                animationSpec = spring(stiffness = Spring.StiffnessLow)
            ),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (group.isTop) {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            } else {
                MaterialTheme.colorScheme.surface
            }
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
                // 群头像（九宫格风格）
                GroupAvatar(
                    colors = group.avatarColors,
                    modifier = Modifier.size(48.dp)
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                // 群信息
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = group.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "(${group.memberCount})",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    if (group.lastMessage != null) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = group.lastMessage,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                
                // 时间和状态
                if (group.lastMessageTime != null) {
                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = group.lastMessageTime,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.outline
                        )
                        if (group.isMuted) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Icon(
                                imageVector = Icons.Default.Groups,
                                contentDescription = "免打扰",
                                modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colorScheme.outline
                            )
                        }
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
 * 九宫格群头像
 */
@Composable
private fun GroupAvatar(
    colors: List<String>,
    modifier: Modifier = Modifier
) {
    val avatarColors = colors.take(4).map { colorName ->
        when (colorName) {
            "blue" -> Color(0xFF4B8BE5)
            "green" -> Color(0xFF07C160)
            "orange" -> Color(0xFFFA9D3B)
            "purple" -> Color(0xFF9B59B6)
            "pink" -> Color(0xFFE91E63)
            "cyan" -> Color(0xFF00BCD4)
            else -> Color(0xFF4B8BE5)
        }
    }
    
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        when (avatarColors.size) {
            1 -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(avatarColors[0])
                )
            }
            2 -> {
                Row(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .background(avatarColors[0])
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .background(avatarColors[1])
                    )
                }
            }
            3 -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(modifier = Modifier.weight(1f)) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                                .background(avatarColors[0])
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                                .background(avatarColors[1])
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .background(avatarColors[2])
                    )
                }
            }
            else -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(modifier = Modifier.weight(1f)) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                                .background(avatarColors.getOrElse(0) { Color.Gray })
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                                .background(avatarColors.getOrElse(1) { Color.Gray })
                        )
                    }
                    Row(modifier = Modifier.weight(1f)) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                                .background(avatarColors.getOrElse(2) { Color.Gray })
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                                .background(avatarColors.getOrElse(3) { Color.Gray })
                        )
                    }
                }
            }
        }
    }
}

/**
 * 空状态占位符
 */
@Composable
private fun EmptyGroupsPlaceholder(
    modifier: Modifier = Modifier,
    onCreateGroupClick: () -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Groups,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
            )
            Text(
                text = "暂无群聊",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "点击右上角创建或加入群聊",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

/**
 * 生成假数据
 */
private fun generateFakeGroups(): List<GroupChatUiModel> {
    val colorOptions = listOf("blue", "green", "orange", "purple", "pink", "cyan")
    
    return listOf(
        GroupChatUiModel(
            id = "group_1",
            name = "技术交流群",
            memberCount = 128,
            avatarColors = listOf("blue", "green", "orange", "purple"),
            lastMessage = "大家好，今天讨论一下新项目的技术选型",
            lastMessageTime = "10:30",
            isMuted = false,
            isTop = true
        ),
        GroupChatUiModel(
            id = "group_2",
            name = "产品设计部",
            memberCount = 45,
            avatarColors = listOf("green", "pink"),
            lastMessage = "设计稿已经更新，请大家查看",
            lastMessageTime = "昨天",
            isMuted = true,
            isTop = true
        ),
        GroupChatUiModel(
            id = "group_3",
            name = "项目Alpha讨论",
            memberCount = 23,
            avatarColors = listOf("orange", "cyan", "purple"),
            lastMessage = "[图片]",
            lastMessageTime = "周一",
            isMuted = false,
            isTop = false
        ),
        GroupChatUiModel(
            id = "group_4",
            name = "周末活动群",
            memberCount = 67,
            avatarColors = listOf("pink", "blue", "green", "orange"),
            lastMessage = "这周六有人一起爬山吗？",
            lastMessageTime = "周日",
            isMuted = false,
            isTop = false
        ),
        GroupChatUiModel(
            id = "group_5",
            name = "读书分享会",
            memberCount = 34,
            avatarColors = listOf("purple", "cyan"),
            lastMessage = "本月推荐书目已更新",
            lastMessageTime = "3天前",
            isMuted = true,
            isTop = false
        ),
        GroupChatUiModel(
            id = "group_6",
            name = "公司通知群",
            memberCount = 256,
            avatarColors = listOf("blue"),
            lastMessage = "[公告] 关于下周放假安排的通知",
            lastMessageTime = "上周",
            isMuted = false,
            isTop = false
        )
    )
}

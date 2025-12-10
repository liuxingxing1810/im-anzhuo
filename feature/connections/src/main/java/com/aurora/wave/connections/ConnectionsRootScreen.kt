package com.aurora.wave.connections

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Badge
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aurora.wave.connections.ui.AlphabetIndex
import com.aurora.wave.connections.ui.rememberAlphabetLetters
import com.aurora.wave.design.GrayStatusBar
import kotlinx.coroutines.launch

/**
 * 联系人列表主页面
 * 
 * 架构说明：
 * - 不使用内部 Scaffold，由 MainActivity 的 Scaffold 统一管理 WindowInsets
 * - padding 参数来自 MainActivity，包含顶部状态栏和底部导航栏的内边距
 * - TopAppBar 设置 windowInsets = WindowInsets(0, 0, 0, 0) 禁用自动 insets 处理
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectionsRootScreen(
    padding: PaddingValues,
    onContactClick: (String) -> Unit = {},
    onSearchClick: () -> Unit = {},
    viewModel: ContactsViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    
    // 灰色状态栏
    GrayStatusBar()
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding) // 应用来自 MainActivity 的 padding
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // TopAppBar - 禁用自动 WindowInsets 处理
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.connections_title),
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }
                    IconButton(onClick = { /* TODO: Add contact */ }) {
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = "Add Contact"
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
            val letters = rememberAlphabetLetters(state.groupedContacts)
            
            Row(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.weight(1f).fillMaxSize()
                ) {
                    // 微信风格功能入口
                    item(key = "function_entries") {
                        WeChatFunctionEntries(
                            newFriendCount = state.friendRequests.size,
                            onNewFriendsClick = { viewModel.onTabSelected(ContactsTab.NEW_FRIENDS) },
                            onGroupsClick = { /* TODO */ },
                            onTagsClick = { /* TODO */ },
                            onOfficialClick = { /* TODO */ }
                        )
                    }
                    
                    // 联系人列表带字母分组
                    if (state.groupedContacts.isEmpty()) {
                        item {
                            EmptyContactsHint()
                        }
                    } else {
                        state.groupedContacts.forEach { (letter, contacts) ->
                            // 字母分组标题
                            item(key = "header_$letter") {
                                WeChatSectionHeader(letter = letter)
                            }
                            
                            // 该字母下的联系人
                            contacts.forEachIndexed { index, contact ->
                                item(key = contact.id) {
                                    WeChatContactItem(
                                        name = contact.name,
                                        avatarInitial = contact.name.firstOrNull()?.toString() ?: "?",
                                        onClick = { onContactClick(contact.id) },
                                        showDivider = index < contacts.size - 1
                                    )
                                }
                            }
                        }
                    }
                }
                
                // 右侧字母索引
                if (state.groupedContacts.isNotEmpty()) {
                    AlphabetIndex(
                        letters = letters,
                        onLetterSelected = { letter ->
                            val keys = state.groupedContacts.keys.toList()
                            var index = 1 // 从1开始，因为功能入口占据了位置0
                            for (key in keys) {
                                if (key == letter) break
                                index += 1 + (state.groupedContacts[key]?.size ?: 0)
                            }
                            scope.launch {
                                listState.animateScrollToItem(index)
                            }
                        },
                        modifier = Modifier.padding(end = 4.dp, top = 8.dp)
                    )
                }
            }
        }
    }
}

/**
 * 微信风格功能入口区域
 */
@Composable
private fun WeChatFunctionEntries(
    newFriendCount: Int,
    onNewFriendsClick: () -> Unit,
    onGroupsClick: () -> Unit,
    onTagsClick: () -> Unit,
    onOfficialClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column {
            // 新的朋友
            WeChatFunctionItem(
                icon = Icons.Default.PersonAdd,
                iconColor = Color(0xFFFA9D3B),
                title = stringResource(R.string.new_friends_entry),
                badgeCount = newFriendCount,
                onClick = onNewFriendsClick
            )
            
            WeChatMenuDivider()
            
            // 群聊
            WeChatFunctionItem(
                icon = Icons.Default.Groups,
                iconColor = Color(0xFF07C160),
                title = stringResource(R.string.groups_entry),
                onClick = onGroupsClick
            )
            
            WeChatMenuDivider()
            
            // 标签
            WeChatFunctionItem(
                icon = Icons.Default.Label,
                iconColor = Color(0xFF4B8BE5),
                title = stringResource(R.string.tags_entry),
                onClick = onTagsClick
            )
            
            WeChatMenuDivider()
            
            // 公众号
            WeChatFunctionItem(
                icon = Icons.Default.Storefront,
                iconColor = Color(0xFF5B6B7D),
                title = stringResource(R.string.official_accounts_entry),
                onClick = onOfficialClick
            )
        }
    }
    
    Spacer(modifier = Modifier.height(8.dp))
}

/**
 * 微信风格功能菜单项
 */
@Composable
private fun WeChatFunctionItem(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    badgeCount: Int = 0,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 图标
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(iconColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.White
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        // 标题
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        // 红点数字
        if (badgeCount > 0) {
            Badge(
                containerColor = Color(0xFFFA5151),
                contentColor = Color.White
            ) {
                Text(
                    text = if (badgeCount > 99) "99+" else badgeCount.toString(),
                    fontSize = 10.sp
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

/**
 * 微信风格字母分组标题
 */
@Composable
private fun WeChatSectionHeader(letter: Char) {
    Text(
        text = letter.toString(),
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 6.dp),
        fontSize = 13.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

/**
 * 微信风格联系人项
 */
@Composable
private fun WeChatContactItem(
    name: String,
    avatarInitial: String,
    onClick: () -> Unit,
    showDivider: Boolean = false
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 头像
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = avatarInitial,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                // 名称
                Text(
                    text = name,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            // 分隔线
            if (showDivider) {
                HorizontalDivider(
                    modifier = Modifier.padding(start = 72.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                )
            }
        }
    }
}

/**
 * 菜单分隔线
 */
@Composable
private fun WeChatMenuDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 68.dp),
        thickness = 0.5.dp,
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
    )
}

/**
 * 空联系人提示
 */
@Composable
private fun EmptyContactsHint() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
            Text(
                text = stringResource(R.string.no_contacts),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

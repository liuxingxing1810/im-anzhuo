package com.aurora.wave.connections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aurora.wave.design.WhiteStatusBar

/**
 * 公众号列表页面 - 微信风格
 * 
 * 特性：
 * - 搜索功能
 * - 未读消息角标
 * - 消息免打扰状态
 * - 认证标识
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfficialAccountsScreen(
    onBackClick: () -> Unit = {},
    onAccountClick: (String) -> Unit = {}
) {
    WhiteStatusBar()
    
    // Mock data
    val accounts = remember { generateMockOfficialAccounts() }
    var searchQuery by remember { mutableStateOf("") }
    
    val filteredAccounts = remember(searchQuery, accounts) {
        if (searchQuery.isBlank()) accounts
        else accounts.filter { 
            it.name.contains(searchQuery, ignoreCase = true) ||
            it.description.contains(searchQuery, ignoreCase = true)
        }
    }
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "公众号",
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // 搜索栏
            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                placeholder = "搜索公众号"
            )
            
            if (filteredAccounts.isEmpty()) {
                // 空状态
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Storefront,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = if (searchQuery.isBlank()) "暂无关注的公众号" else "未找到相关公众号",
                            color = MaterialTheme.colorScheme.outline,
                            fontSize = 16.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(filteredAccounts, key = { it.id }) { account ->
                        OfficialAccountItem(
                            account = account,
                            onClick = { onAccountClick(account.id) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * 搜索栏
 */
@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(20.dp)
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.weight(1f),
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 15.sp
                ),
                singleLine = true,
                cursorBrush = SolidColor(Color(0xFF07C160)),
                decorationBox = { innerTextField ->
                    Box {
                        if (query.isEmpty()) {
                            Text(
                                text = placeholder,
                                color = MaterialTheme.colorScheme.outline,
                                fontSize = 15.sp
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
    }
}

/**
 * 单个公众号列表项
 */
@Composable
private fun OfficialAccountItem(
    account: OfficialAccount,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.surface
    ) {
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
                    .clip(RoundedCornerShape(8.dp))
                    .background(account.avatarColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    account.icon,
                    contentDescription = null,
                    tint = account.avatarColor,
                    modifier = Modifier.size(28.dp)
                )
                
                // 未读角标
                if (account.unreadCount > 0) {
                    Badge(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(2.dp),
                        containerColor = Color(0xFFE53935)
                    ) {
                        Text(
                            text = if (account.unreadCount > 99) "99+" else account.unreadCount.toString(),
                            fontSize = 10.sp
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = account.name,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    
                    // 认证标识
                    if (account.isVerified) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            Icons.Default.Verified,
                            contentDescription = "已认证",
                            tint = Color(0xFF1DA1F2),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    
                    // 免打扰标识
                    if (account.isMuted) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            Icons.Default.NotificationsOff,
                            contentDescription = "消息免打扰",
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = account.lastMessage.ifBlank { account.description },
                    color = MaterialTheme.colorScheme.outline,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            // 时间
            if (account.lastTime.isNotBlank()) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = account.lastTime,
                    color = MaterialTheme.colorScheme.outline,
                    fontSize = 12.sp
                )
            }
        }
    }
}

// Data class
data class OfficialAccount(
    val id: String,
    val name: String,
    val description: String,
    val avatarColor: Color,
    val icon: androidx.compose.ui.graphics.vector.ImageVector = Icons.Default.Storefront,
    val isVerified: Boolean = false,
    val isMuted: Boolean = false,
    val unreadCount: Int = 0,
    val lastMessage: String = "",
    val lastTime: String = ""
)

// Mock data generator
private fun generateMockOfficialAccounts(): List<OfficialAccount> = listOf(
    OfficialAccount(
        id = "oa_1",
        name = "微信支付",
        description = "微信支付官方账号",
        avatarColor = Color(0xFF07C160),
        isVerified = true,
        lastMessage = "您收到一笔转账",
        lastTime = "09:30",
        unreadCount = 2
    ),
    OfficialAccount(
        id = "oa_2",
        name = "腾讯新闻",
        description = "分享有价值的新闻资讯",
        avatarColor = Color(0xFF1DA1F2),
        isVerified = true,
        lastMessage = "今日要闻：科技创新引领发展",
        lastTime = "08:15",
        unreadCount = 5
    ),
    OfficialAccount(
        id = "oa_3",
        name = "京东",
        description = "京东购物官方账号",
        avatarColor = Color(0xFFE53935),
        isVerified = true,
        lastMessage = "您关注的商品降价啦！",
        lastTime = "昨天",
        unreadCount = 0,
        isMuted = true
    ),
    OfficialAccount(
        id = "oa_4",
        name = "美团外卖",
        description = "美食不等待",
        avatarColor = Color(0xFFFFB300),
        isVerified = true,
        lastMessage = "午餐时间到，看看今天吃什么",
        lastTime = "12:00"
    ),
    OfficialAccount(
        id = "oa_5",
        name = "滴滴出行",
        description = "滴滴一下，美好出行",
        avatarColor = Color(0xFFFF6D00),
        isVerified = true,
        lastMessage = "行程已完成，欢迎评价",
        lastTime = "周一"
    ),
    OfficialAccount(
        id = "oa_6",
        name = "得到",
        description = "知识就在得到",
        avatarColor = Color(0xFF8E24AA),
        isVerified = true,
        lastMessage = "新课上线：如何高效学习",
        lastTime = "周日",
        isMuted = true
    ),
    OfficialAccount(
        id = "oa_7",
        name = "小红书",
        description = "标记我的生活",
        avatarColor = Color(0xFFE91E63),
        isVerified = true,
        lastMessage = "发现好物推荐",
        lastTime = "3天前"
    ),
    OfficialAccount(
        id = "oa_8",
        name = "程序员小灰",
        description = "漫画讲解技术知识",
        avatarColor = Color(0xFF607D8B),
        isVerified = false,
        lastMessage = "什么是分布式系统？",
        lastTime = "1周前"
    )
)

package com.aurora.wave.messages

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 搜索结果类型
 */
enum class SearchResultType {
    CONTACT,        // 联系人
    GROUP,          // 群组
    CHAT_RECORD     // 聊天记录
}

/**
 * 搜索结果数据模型
 */
data class SearchResult(
    val id: String,
    val type: SearchResultType,
    val title: String,           // 名称/标题
    val subtitle: String,        // 副标题/最近消息
    val avatarColor: Color,      // 头像背景色
    val matchedText: String? = null  // 匹配的文本（高亮用）
)

/**
 * Mock 搜索数据
 */
private fun getMockSearchResults(query: String): List<SearchResult> {
    if (query.isBlank()) return emptyList()
    
    val allResults = listOf(
        // 联系人
        SearchResult(
            id = "contact_1",
            type = SearchResultType.CONTACT,
            title = "张三",
            subtitle = "ID: zhangsan_001",
            avatarColor = Color(0xFF4CAF50)
        ),
        SearchResult(
            id = "contact_2",
            type = SearchResultType.CONTACT,
            title = "李四",
            subtitle = "ID: lisi_002",
            avatarColor = Color(0xFF2196F3)
        ),
        SearchResult(
            id = "contact_3",
            type = SearchResultType.CONTACT,
            title = "王五",
            subtitle = "ID: wangwu_003",
            avatarColor = Color(0xFFFF9800)
        ),
        SearchResult(
            id = "contact_4",
            type = SearchResultType.CONTACT,
            title = "John",
            subtitle = "ID: john_en",
            avatarColor = Color(0xFF9C27B0)
        ),
        SearchResult(
            id = "contact_5",
            type = SearchResultType.CONTACT,
            title = "Alice",
            subtitle = "ID: alice_en",
            avatarColor = Color(0xFFE91E63)
        ),
        // 群组
        SearchResult(
            id = "group_1",
            type = SearchResultType.GROUP,
            title = "技术交流群",
            subtitle = "50人 · 最近活跃",
            avatarColor = Color(0xFF00BCD4)
        ),
        SearchResult(
            id = "group_2",
            type = SearchResultType.GROUP,
            title = "产品讨论组",
            subtitle = "28人 · 最近活跃",
            avatarColor = Color(0xFF8BC34A)
        ),
        SearchResult(
            id = "group_3",
            type = SearchResultType.GROUP,
            title = "星星IM官方群",
            subtitle = "1000人 · 最近活跃",
            avatarColor = Color(0xFF07C160)
        ),
        // 聊天记录
        SearchResult(
            id = "chat_1",
            type = SearchResultType.CHAT_RECORD,
            title = "张三",
            subtitle = "明天下午3点开会，记得准时参加",
            avatarColor = Color(0xFF4CAF50),
            matchedText = "开会"
        ),
        SearchResult(
            id = "chat_2",
            type = SearchResultType.CHAT_RECORD,
            title = "技术交流群",
            subtitle = "有人分享了一篇关于Kotlin协程的文章",
            avatarColor = Color(0xFF00BCD4),
            matchedText = "Kotlin"
        ),
        SearchResult(
            id = "chat_3",
            type = SearchResultType.CHAT_RECORD,
            title = "李四",
            subtitle = "周末有空一起吃饭吗？",
            avatarColor = Color(0xFF2196F3),
            matchedText = "吃饭"
        ),
        SearchResult(
            id = "chat_4",
            type = SearchResultType.CHAT_RECORD,
            title = "Alice",
            subtitle = "The meeting has been rescheduled to Friday",
            avatarColor = Color(0xFFE91E63),
            matchedText = "meeting"
        )
    )
    
    // 简单的搜索过滤
    val lowerQuery = query.lowercase()
    return allResults.filter { result ->
        result.title.lowercase().contains(lowerQuery) ||
        result.subtitle.lowercase().contains(lowerQuery) ||
        result.matchedText?.lowercase()?.contains(lowerQuery) == true
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBackClick: () -> Unit,
    onResultClick: (SearchResult) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    val searchResults = remember(searchQuery) { getMockSearchResults(searchQuery) }
    val focusRequester = remember { FocusRequester() }
    
    // 自动聚焦搜索框
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    
    Scaffold(
        topBar = {
            SearchTopBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onBackClick = onBackClick,
                onClearClick = { searchQuery = "" },
                focusRequester = focusRequester
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (searchQuery.isBlank()) {
                // 显示搜索提示
                SearchHint()
            } else if (searchResults.isEmpty()) {
                // 无结果
                EmptySearchResult(query = searchQuery)
            } else {
                // 显示搜索结果
                SearchResultList(
                    results = searchResults,
                    onResultClick = onResultClick
                )
            }
        }
    }
}

@Composable
private fun SearchTopBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onClearClick: () -> Unit,
    focusRequester: FocusRequester
) {
    Surface(
        color = Color(0xFFEDEDED),
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 返回按钮
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "返回",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            
            // 搜索框
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    BasicTextField(
                        value = query,
                        onValueChange = onQueryChange,
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRequester),
                        singleLine = true,
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 16.sp
                        ),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        decorationBox = { innerTextField ->
                            if (query.isEmpty()) {
                                Text(
                                    text = "搜索联系人、群组、聊天记录",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 14.sp
                                )
                            }
                            innerTextField()
                        }
                    )
                    
                    // 清除按钮
                    if (query.isNotEmpty()) {
                        IconButton(
                            onClick = onClearClick,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "清除",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
private fun SearchHint() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        )
        
        Text(
            text = "搜索联系人、群组或聊天记录",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        // 搜索建议
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "试试搜索：",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
            
            listOf("张三", "技术交流群", "开会", "Kotlin").forEach { suggestion ->
                Text(
                    text = "• $suggestion",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun EmptySearchResult(query: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "🔍",
                style = MaterialTheme.typography.displayLarge
            )
            Text(
                text = "未找到 \"$query\" 相关结果",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "试试其他关键词",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
private fun SearchResultList(
    results: List<SearchResult>,
    onResultClick: (SearchResult) -> Unit
) {
    // 按类型分组
    val contacts = results.filter { it.type == SearchResultType.CONTACT }
    val groups = results.filter { it.type == SearchResultType.GROUP }
    val chatRecords = results.filter { it.type == SearchResultType.CHAT_RECORD }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // 联系人
        if (contacts.isNotEmpty()) {
            item {
                SectionHeader(title = "联系人", count = contacts.size)
            }
            items(contacts, key = { it.id }) { result ->
                SearchResultItem(result = result, onClick = { onResultClick(result) })
                HorizontalDivider(
                    modifier = Modifier.padding(start = 76.dp),
                    thickness = 0.5.dp,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                )
            }
        }
        
        // 群组
        if (groups.isNotEmpty()) {
            item {
                SectionHeader(title = "群组", count = groups.size)
            }
            items(groups, key = { it.id }) { result ->
                SearchResultItem(result = result, onClick = { onResultClick(result) })
                HorizontalDivider(
                    modifier = Modifier.padding(start = 76.dp),
                    thickness = 0.5.dp,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                )
            }
        }
        
        // 聊天记录
        if (chatRecords.isNotEmpty()) {
            item {
                SectionHeader(title = "聊天记录", count = chatRecords.size)
            }
            items(chatRecords, key = { it.id }) { result ->
                SearchResultItem(result = result, onClick = { onResultClick(result) })
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
private fun SectionHeader(title: String, count: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "($count)",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
private fun SearchResultItem(
    result: SearchResult,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 头像
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(result.avatarColor),
            contentAlignment = Alignment.Center
        ) {
            val icon: ImageVector = when (result.type) {
                SearchResultType.CONTACT -> Icons.Default.Person
                SearchResultType.GROUP -> Icons.Default.Group
                SearchResultType.CHAT_RECORD -> Icons.Default.History
            }
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        // 内容
        Column(
            modifier = Modifier.weight(1f)
        ) {
            // 标题
            Text(
                text = result.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(2.dp))
            
            // 副标题
            Text(
                text = result.subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        
        // 类型标签
        val typeLabel = when (result.type) {
            SearchResultType.CONTACT -> "联系人"
            SearchResultType.GROUP -> "群组"
            SearchResultType.CHAT_RECORD -> "聊天记录"
        }
        Text(
            text = typeLabel,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

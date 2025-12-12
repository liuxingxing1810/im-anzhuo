package com.aurora.wave.connections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Radar
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aurora.wave.design.WhiteStatusBar
import kotlinx.coroutines.delay

/**
 * 搜索结果用户模型
 */
data class SearchUserResult(
    val id: String,
    val name: String,
    val waveId: String,
    val avatarColor: String = "blue",
    val bio: String? = null,
    val isFriend: Boolean = false
)

/**
 * 添加朋友页面
 * 包含搜索和多种添加方式入口
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFriendScreen(
    onBackClick: () -> Unit,
    onScanClick: () -> Unit = {},
    onMyQrCodeClick: () -> Unit = {},
    onContactsClick: () -> Unit = {},
    onNearbyClick: () -> Unit = {},
    onGroupSearchClick: () -> Unit = {},
    onUserClick: (String) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    var searchResults by remember { mutableStateOf<List<SearchUserResult>>(emptyList()) }
    var hasSearched by remember { mutableStateOf(false) }
    
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    
    // 执行搜索
    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotBlank()) {
            isSearching = true
            delay(500) // 模拟网络延迟
            searchResults = performSearch(searchQuery)
            isSearching = false
            hasSearched = true
        } else {
            searchResults = emptyList()
            hasSearched = false
        }
    }
    
    WhiteStatusBar()
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "添加朋友",
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
                .imePadding()
        ) {
            // 搜索框
            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onSearch = { 
                    focusManager.clearFocus()
                },
                isSearching = isSearching,
                focusRequester = focusRequester,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            
            // 我的Wave号
            MyWaveIdCard(
                waveId = "aurora_001",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            
            when {
                isSearching -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(32.dp))
                    }
                }
                hasSearched && searchResults.isEmpty() -> {
                    EmptySearchResult(query = searchQuery)
                }
                searchResults.isNotEmpty() -> {
                    SearchResultsList(
                        results = searchResults,
                        onUserClick = onUserClick
                    )
                }
                else -> {
                    // 添加方式入口
                    AddMethodsList(
                        onScanClick = onScanClick,
                        onMyQrCodeClick = onMyQrCodeClick,
                        onContactsClick = onContactsClick,
                        onNearbyClick = onNearbyClick,
                        onGroupSearchClick = onGroupSearchClick
                    )
                }
            }
        }
    }
}

/**
 * 搜索框
 */
@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    isSearching: Boolean,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
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
                textStyle = TextStyle(
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSearch() }),
                decorationBox = { innerTextField ->
                    Box {
                        if (query.isEmpty()) {
                            Text(
                                text = "搜索 Wave号/手机号",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 15.sp
                            )
                        }
                        innerTextField()
                    }
                }
            )
            
            if (query.isNotEmpty()) {
                if (isSearching) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    IconButton(
                        onClick = { onQueryChange("") },
                        modifier = Modifier.size(18.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "清除",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * 我的Wave号卡片
 */
@Composable
private fun MyWaveIdCard(
    waveId: String,
    modifier: Modifier = Modifier
) {
    Card(
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
            Text(
                text = "我的Wave号：",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = waveId,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

/**
 * 添加方式列表
 */
@Composable
private fun AddMethodsList(
    onScanClick: () -> Unit,
    onMyQrCodeClick: () -> Unit,
    onContactsClick: () -> Unit,
    onNearbyClick: () -> Unit,
    onGroupSearchClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        item {
            AddMethodItem(
                icon = Icons.Default.QrCodeScanner,
                iconColor = Color(0xFF07C160),
                title = "扫一扫",
                subtitle = "扫描二维码添加好友",
                onClick = onScanClick,
                showDivider = true
            )
        }
        
        item {
            AddMethodItem(
                icon = Icons.Default.QrCode,
                iconColor = Color(0xFF4B8BE5),
                title = "我的二维码",
                subtitle = "让对方扫描添加",
                onClick = onMyQrCodeClick,
                showDivider = true
            )
        }
        
        item {
            AddMethodItem(
                icon = Icons.Default.ContactPhone,
                iconColor = Color(0xFFFA9D3B),
                title = "手机联系人",
                subtitle = "从通讯录添加好友",
                onClick = onContactsClick,
                showDivider = true
            )
        }
        
        item {
            AddMethodItem(
                icon = Icons.Default.Radar,
                iconColor = Color(0xFF9B59B6),
                title = "附近的人",
                subtitle = "发现附近的朋友",
                onClick = onNearbyClick,
                showDivider = true
            )
        }
        
        item {
            AddMethodItem(
                icon = Icons.Default.Group,
                iconColor = Color(0xFF00BCD4),
                title = "搜索群聊",
                subtitle = "通过群号加入群聊",
                onClick = onGroupSearchClick,
                showDivider = false
            )
        }
    }
}

/**
 * 添加方式项
 */
@Composable
private fun AddMethodItem(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    showDivider: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
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
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(iconColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            if (showDivider) {
                HorizontalDivider(
                    modifier = Modifier.padding(start = 72.dp),
                    thickness = 0.5.dp,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                )
            }
        }
    }
}

/**
 * 搜索结果列表
 */
@Composable
private fun SearchResultsList(
    results: List<SearchUserResult>,
    onUserClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        item {
            Text(
                text = "搜索结果",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        items(results, key = { it.id }) { user ->
            SearchResultItem(
                user = user,
                onClick = { onUserClick(user.id) }
            )
        }
    }
}

/**
 * 搜索结果项
 */
@Composable
private fun SearchResultItem(
    user: SearchUserResult,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
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
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        when (user.avatarColor) {
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
                    text = user.name.firstOrNull()?.toString() ?: "?",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Wave号：${user.waveId}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            if (user.isFriend) {
                Text(
                    text = "已添加",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.outline
                )
            } else {
                Button(
                    onClick = onClick,
                    modifier = Modifier.height(32.dp),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    Text(
                        text = "添加",
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

/**
 * 空搜索结果
 */
@Composable
private fun EmptySearchResult(query: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
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
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
            )
            Text(
                text = "未找到「$query」相关用户",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "请检查Wave号或手机号是否正确",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

/**
 * 模拟搜索
 */
private fun performSearch(query: String): List<SearchUserResult> {
    // 模拟搜索结果
    if (query.length < 2) return emptyList()
    
    val mockUsers = listOf(
        SearchUserResult(
            id = "user_search_1",
            name = "张三",
            waveId = "zhangsan_123",
            avatarColor = "blue",
            bio = "Life is beautiful",
            isFriend = false
        ),
        SearchUserResult(
            id = "user_search_2",
            name = "李四",
            waveId = "lisi_456",
            avatarColor = "green",
            bio = "Coding all day",
            isFriend = true
        ),
        SearchUserResult(
            id = "user_search_3",
            name = "王五",
            waveId = "wangwu_789",
            avatarColor = "orange",
            bio = null,
            isFriend = false
        )
    )
    
    return mockUsers.filter {
        it.name.contains(query, ignoreCase = true) ||
        it.waveId.contains(query, ignoreCase = true)
    }
}

package com.aurora.wave.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aurora.wave.design.WhiteStatusBar
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items as lazyRowItems

/**
 * 收藏页面 - 微信风格
 * 
 * 特性：
 * - 类型筛选（全部、链接、图片、视频、音乐、文件）
 * - 列表/网格视图切换
 * - 长按删除
 * - 搜索功能
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    onBackClick: () -> Unit = {},
    onItemClick: (String) -> Unit = {}
) {
    WhiteStatusBar()
    
    // Mock data
    val favorites = remember { mutableStateListOf(*generateMockFavorites().toTypedArray()) }
    var selectedType by remember { mutableStateOf(FavoriteType.ALL) }
    var isGridView by remember { mutableStateOf(false) }
    var deleteTarget by remember { mutableStateOf<FavoriteItem?>(null) }
    
    val filteredFavorites = remember(selectedType, favorites.toList()) {
        if (selectedType == FavoriteType.ALL) favorites
        else favorites.filter { it.type == selectedType }
    }
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "收藏",
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    IconButton(onClick = { isGridView = !isGridView }) {
                        Icon(
                            imageVector = if (isGridView) Icons.Default.ViewList else Icons.Default.GridView,
                            contentDescription = if (isGridView) "列表视图" else "网格视图"
                        )
                    }
                    IconButton(onClick = { /* TODO: Search */ }) {
                        Icon(Icons.Default.Search, contentDescription = "搜索")
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
            // 类型筛选
            TypeFilterRow(
                selectedType = selectedType,
                onTypeSelected = { selectedType = it }
            )
            
            if (filteredFavorites.isEmpty()) {
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
                            Icons.Default.Bookmark,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "暂无收藏内容",
                            color = MaterialTheme.colorScheme.outline,
                            fontSize = 16.sp
                        )
                    }
                }
            } else {
                if (isGridView) {
                    // 网格视图
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(filteredFavorites.toList(), key = { it.id }) { item ->
                            FavoriteGridItem(
                                item = item,
                                onClick = { onItemClick(item.id) },
                                onLongClick = { deleteTarget = item }
                            )
                        }
                    }
                } else {
                    // 列表视图
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(filteredFavorites.toList(), key = { it.id }) { item ->
                            FavoriteListItem(
                                item = item,
                                onClick = { onItemClick(item.id) },
                                onLongClick = { deleteTarget = item }
                            )
                        }
                    }
                }
            }
        }
    }
    
    // 删除确认对话框
    deleteTarget?.let { item ->
        AlertDialog(
            onDismissRequest = { deleteTarget = null },
            title = { Text("删除收藏") },
            text = { Text("确定要删除「${item.title}」吗？") },
            confirmButton = {
                TextButton(
                    onClick = {
                        favorites.remove(item)
                        deleteTarget = null
                    }
                ) {
                    Text("删除", color = Color(0xFFE53935))
                }
            },
            dismissButton = {
                TextButton(onClick = { deleteTarget = null }) {
                    Text("取消")
                }
            }
        )
    }
}

/**
 * 类型筛选行
 */
@Composable
private fun TypeFilterRow(
    selectedType: FavoriteType,
    onTypeSelected: (FavoriteType) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(FavoriteType.entries.toTypedArray()) { type ->
            FilterChip(
                selected = selectedType == type,
                onClick = { onTypeSelected(type) },
                label = { Text(type.label) },
                leadingIcon = if (selectedType == type) {
                    { Icon(type.icon, null, modifier = Modifier.size(16.dp)) }
                } else null,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF07C160).copy(alpha = 0.15f),
                    selectedLabelColor = Color(0xFF07C160),
                    selectedLeadingIconColor = Color(0xFF07C160)
                )
            )
        }
    }
}

/**
 * 列表项
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FavoriteListItem(
    item: FavoriteItem,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 类型图标/缩略图
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(item.type.color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    item.type.icon,
                    contentDescription = null,
                    tint = item.type.color,
                    modifier = Modifier.size(28.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.source,
                        color = MaterialTheme.colorScheme.outline,
                        fontSize = 12.sp
                    )
                    Text(
                        text = " · ",
                        color = MaterialTheme.colorScheme.outline,
                        fontSize = 12.sp
                    )
                    Text(
                        text = item.time,
                        color = MaterialTheme.colorScheme.outline,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

/**
 * 网格项
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FavoriteGridItem(
    item: FavoriteItem,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            // 缩略图区域
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.2f)
                    .background(item.type.color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    item.type.icon,
                    contentDescription = null,
                    tint = item.type.color,
                    modifier = Modifier.size(48.dp)
                )
            }
            
            // 信息区域
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = item.time,
                    color = MaterialTheme.colorScheme.outline,
                    fontSize = 11.sp
                )
            }
        }
    }
}

// Data classes
enum class FavoriteType(
    val label: String,
    val icon: ImageVector,
    val color: Color
) {
    ALL("全部", Icons.Default.Bookmark, Color(0xFF607D8B)),
    LINK("链接", Icons.Default.Link, Color(0xFF1DA1F2)),
    IMAGE("图片", Icons.Default.Image, Color(0xFF4CAF50)),
    VIDEO("视频", Icons.Default.PlayCircle, Color(0xFFE91E63)),
    MUSIC("音乐", Icons.Default.MusicNote, Color(0xFFFF9800)),
    FILE("文件", Icons.AutoMirrored.Filled.Article, Color(0xFF9C27B0))
}

data class FavoriteItem(
    val id: String,
    val title: String,
    val type: FavoriteType,
    val source: String,
    val time: String
)

// Mock data generator
private fun generateMockFavorites(): List<FavoriteItem> = listOf(
    FavoriteItem(
        id = "fav_1",
        title = "如何提高编程效率的10个技巧",
        type = FavoriteType.LINK,
        source = "技术博客",
        time = "今天 10:30"
    ),
    FavoriteItem(
        id = "fav_2",
        title = "风景照片.jpg",
        type = FavoriteType.IMAGE,
        source = "小明",
        time = "昨天"
    ),
    FavoriteItem(
        id = "fav_3",
        title = "年会精彩瞬间",
        type = FavoriteType.VIDEO,
        source = "公司群",
        time = "3天前"
    ),
    FavoriteItem(
        id = "fav_4",
        title = "周杰伦 - 稻香",
        type = FavoriteType.MUSIC,
        source = "音乐分享",
        time = "1周前"
    ),
    FavoriteItem(
        id = "fav_5",
        title = "项目需求文档v2.0.pdf",
        type = FavoriteType.FILE,
        source = "工作群",
        time = "2周前"
    ),
    FavoriteItem(
        id = "fav_6",
        title = "2024年科技趋势预测",
        type = FavoriteType.LINK,
        source = "科技公众号",
        time = "2周前"
    ),
    FavoriteItem(
        id = "fav_7",
        title = "美食菜谱大全",
        type = FavoriteType.LINK,
        source = "美食频道",
        time = "1个月前"
    ),
    FavoriteItem(
        id = "fav_8",
        title = "团队合影.png",
        type = FavoriteType.IMAGE,
        source = "同事",
        time = "1个月前"
    )
)

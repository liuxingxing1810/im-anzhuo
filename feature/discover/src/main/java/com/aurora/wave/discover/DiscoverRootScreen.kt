package com.aurora.wave.discover

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Games
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aurora.wave.discover.model.DiscoverMenuItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverRootScreen(
    padding: PaddingValues,
    onItemClick: (String) -> Unit = {}
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    
    val menuItems = listOf(
        // Social section
        DiscoverMenuItem(
            id = "moments",
            title = stringResource(R.string.discover_moments_title),
            subtitle = stringResource(R.string.discover_moments_subtitle),
            icon = Icons.Default.PhotoLibrary
        ),
        // Tools section
        DiscoverMenuItem(
            id = "scan",
            title = stringResource(R.string.discover_scan_title),
            subtitle = stringResource(R.string.discover_scan_subtitle),
            icon = Icons.Default.QrCodeScanner
        ),
        // Location section
        DiscoverMenuItem(
            id = "nearby",
            title = stringResource(R.string.discover_nearby_title),
            subtitle = stringResource(R.string.discover_nearby_subtitle),
            icon = Icons.Default.LocationOn
        ),
        // Entertainment section
        DiscoverMenuItem(
            id = "explore",
            title = stringResource(R.string.discover_explore_title),
            subtitle = stringResource(R.string.discover_explore_subtitle),
            icon = Icons.Default.Explore,
            badge = "5"
        ),
        DiscoverMenuItem(
            id = "mini_apps",
            title = stringResource(R.string.discover_mini_apps_title),
            subtitle = stringResource(R.string.discover_mini_apps_subtitle),
            icon = Icons.Default.Widgets
        ),
        DiscoverMenuItem(
            id = "games",
            title = stringResource(R.string.discover_games_title),
            subtitle = stringResource(R.string.discover_games_subtitle),
            icon = Icons.Default.Games
        ),
        // Services section
        DiscoverMenuItem(
            id = "shop",
            title = stringResource(R.string.discover_shop_title),
            subtitle = stringResource(R.string.discover_shop_subtitle),
            icon = Icons.Default.ShoppingBag
        ),
        DiscoverMenuItem(
            id = "news",
            title = stringResource(R.string.discover_news_title),
            subtitle = stringResource(R.string.discover_news_subtitle),
            icon = Icons.AutoMirrored.Filled.Article,
            showDot = true
        )
    )
    
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = padding.calculateBottomPadding())
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.discover_title),
                        fontWeight = FontWeight.Bold
                    )
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 8.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Search bar
            item {
                SearchBar(
                    onClick = { onItemClick("search") }
                )
            }
            
            item { Spacer(modifier = Modifier.height(4.dp)) }
            
            // Menu sections
            item {
                MenuSection(
                    items = menuItems.take(1),
                    onItemClick = onItemClick
                )
            }
            
            item {
                MenuSection(
                    items = menuItems.subList(1, 2),
                    onItemClick = onItemClick
                )
            }
            
            item {
                MenuSection(
                    items = menuItems.subList(2, 3),
                    onItemClick = onItemClick
                )
            }
            
            item {
                MenuSection(
                    items = menuItems.subList(3, 6),
                    onItemClick = onItemClick
                )
            }
            
            item {
                MenuSection(
                    items = menuItems.subList(6, 8),
                    onItemClick = onItemClick
                )
            }
        }
    }
}

@Composable
private fun SearchBar(
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.discover_search_label),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(R.string.discover_search_label),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun MenuSection(
    items: List<DiscoverMenuItem>,
    onItemClick: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column {
            items.forEachIndexed { index, item ->
                DiscoverMenuRow(
                    item = item,
                    onClick = { onItemClick(item.id) }
                )
                
                if (index < items.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 56.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

@Composable
private fun DiscoverMenuRow(
    item: DiscoverMenuItem,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon with colored background
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(getIconBackgroundColor(item.id)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(14.dp))
        
        // Title and subtitle
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                
                if (item.showDot) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.error)
                    )
                }
            }
            
            item.subtitle?.let { subtitle ->
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        // Badge
        item.badge?.let { badge ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.error)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = badge,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * 图标背景颜色映射表
 * Icon background color mapping using Map for better readability and lower complexity
 */
private val iconBackgroundColors = mapOf(
    "moments" to Color(0xFF5C6BC0),    // Indigo
    "scan" to Color(0xFF26A69A),        // Teal
    "nearby" to Color(0xFF42A5F5),      // Blue
    "explore" to Color(0xFFEF5350),     // Red
    "mini_apps" to Color(0xFF7E57C2),   // Purple
    "games" to Color(0xFFFFB300),       // Amber
    "shop" to Color(0xFFFF7043),        // Deep Orange
    "news" to Color(0xFF66BB6A)         // Green
)

private val defaultIconBackgroundColor = Color(0xFF78909C) // Blue Grey

@Composable
private fun getIconBackgroundColor(itemId: String): Color {
    return iconBackgroundColors[itemId] ?: defaultIconBackgroundColor
}

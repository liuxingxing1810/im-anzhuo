package com.aurora.wave.profile

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
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aurora.wave.profile.model.ProfileMenuItem
import com.aurora.wave.profile.model.ProfileStrings
import com.aurora.wave.profile.model.SettingsSection
import com.aurora.wave.profile.model.UserProfile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileRootScreen(
    padding: PaddingValues,
    onProfileClick: () -> Unit = {},
    onSettingsClick: (String) -> Unit = {}
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    // 使用 ProfileStrings data class 简化 remember 参数
    val strings = ProfileStrings(
        defaultBio = stringResource(R.string.profile_header_bio_default),
        servicesTitle = stringResource(R.string.profile_services),
        servicesSub = stringResource(R.string.profile_services_sub),
        favoritesTitle = stringResource(R.string.profile_favorites),
        collectionsTitle = stringResource(R.string.profile_collections),
        settingsTitle = stringResource(R.string.profile_settings),
        notificationsTitle = stringResource(R.string.profile_notifications),
        privacyTitle = stringResource(R.string.profile_privacy),
        storageTitle = stringResource(R.string.profile_storage),
        appearanceTitle = stringResource(R.string.profile_appearance),
        appearanceSubTitle = stringResource(R.string.profile_appearance_sub),
        helpTitle = stringResource(R.string.profile_help),
        aboutTitle = stringResource(R.string.profile_about),
        aboutVersion = stringResource(R.string.profile_about_version)
    )
    
    val currentUser = remember(strings) {
        UserProfile(
            id = "user_001",
            displayName = "Aurora User",
            username = "aurora_wave_001",
            bio = strings.defaultBio
        )
    }
    
    val settingsSections = remember(strings) {
        buildSettingsSections(strings)
    }
    
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = padding.calculateBottomPadding())
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.profile_title),
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
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile header card
            item {
                ProfileHeaderCard(
                    user = currentUser,
                    onClick = onProfileClick
                )
            }
            
            // Settings sections
            items(settingsSections) { section ->
                SettingsSectionCard(
                    section = section,
                    onItemClick = onSettingsClick
                )
            }
            
            // Bottom spacer
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun ProfileHeaderCard(
    user: UserProfile,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Surface(
                modifier = Modifier.size(72.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // User info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.displayName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                        text = stringResource(R.string.profile_header_wave_id, user.username),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (!user.bio.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = user.bio,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        maxLines = 1
                    )
                }
            }
            
            // QR code button
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.QrCode2,
                        contentDescription = "QR Code",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
private fun SettingsSectionCard(
    section: SettingsSection,
    onItemClick: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            section.items.forEachIndexed { index, item ->
                SettingsMenuItem(
                    item = item,
                    onClick = { onItemClick(item.id) }
                )
                if (item.showDivider && index < section.items.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 56.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsMenuItem(
    item: ProfileMenuItem,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        // Title and subtitle
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyLarge
            )
            if (!item.subtitle.isNullOrEmpty()) {
                Text(
                    text = item.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        // Badge
        if (!item.badge.isNullOrEmpty()) {
            Badge(
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Text(
                    text = item.badge,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
        
        // Arrow
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.size(20.dp)
        )
    }
}

/**
 * 构建设置菜单区块
 * Build settings sections from localized strings
 */
private fun buildSettingsSections(strings: ProfileStrings): List<SettingsSection> = listOf(
    SettingsSection(
        items = listOf(
            ProfileMenuItem(
                id = "services",
                title = strings.servicesTitle,
                subtitle = strings.servicesSub,
                icon = Icons.Default.CreditCard
            )
        )
    ),
    SettingsSection(
        items = listOf(
            ProfileMenuItem(
                id = "favorites",
                title = strings.favoritesTitle,
                icon = Icons.Default.Bookmark
            ),
            ProfileMenuItem(
                id = "collections",
                title = strings.collectionsTitle,
                icon = Icons.Default.Favorite,
                badge = "3"
            )
        )
    ),
    SettingsSection(
        items = listOf(
            ProfileMenuItem(
                id = "settings",
                title = strings.settingsTitle,
                icon = Icons.Default.Settings
            ),
            ProfileMenuItem(
                id = "notifications",
                title = strings.notificationsTitle,
                icon = Icons.Default.Notifications
            ),
            ProfileMenuItem(
                id = "privacy",
                title = strings.privacyTitle,
                icon = Icons.Default.Security
            ),
            ProfileMenuItem(
                id = "storage",
                title = strings.storageTitle,
                icon = Icons.Default.Storage
            ),
            ProfileMenuItem(
                id = "appearance",
                title = strings.appearanceTitle,
                subtitle = strings.appearanceSubTitle,
                icon = Icons.Default.Palette
            )
        )
    ),
    SettingsSection(
        items = listOf(
            ProfileMenuItem(
                id = "help",
                title = strings.helpTitle,
                icon = Icons.AutoMirrored.Filled.Help
            ),
            ProfileMenuItem(
                id = "about",
                title = strings.aboutTitle,
                subtitle = strings.aboutVersion,
                icon = Icons.Default.Info,
                showDivider = false
            )
        )
    )
)

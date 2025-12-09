package com.aurora.wave

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.CompassCalibration
import androidx.compose.material.icons.outlined.Contacts
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aurora.wave.design.AuroraTheme
import com.aurora.wave.design.ThemeState
import com.aurora.wave.data.settings.SettingsDataStore
import com.aurora.wave.messages.MessagesRootScreen
import com.aurora.wave.messages.ChatDetailScreen
import com.aurora.wave.connections.ConnectionsRootScreen
import com.aurora.wave.connections.ContactDetailScreen
import com.aurora.wave.discover.DiscoverRootScreen
import com.aurora.wave.discover.MomentsScreen
import com.aurora.wave.discover.ScanScreen
import com.aurora.wave.discover.MyQrCodeScreen
import com.aurora.wave.profile.ProfileRootScreen
import com.aurora.wave.profile.SettingsScreen
import com.aurora.wave.profile.AppearanceScreen
import com.aurora.wave.profile.AboutScreen
import com.aurora.wave.navigation.WaveRoute
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("MainActivity onCreate")
        setContent {
            val context = LocalContext.current
            val settingsDataStore = remember { SettingsDataStore(context) }
            val darkModeEnabled by settingsDataStore.darkModeFlow.collectAsState(initial = false)
            val followSystem by settingsDataStore.followSystemThemeFlow.collectAsState(initial = true)
            val isSystemDark = isSystemInDarkTheme()
            
            val themeState = remember { ThemeState() }
            
            // 计算实际是否为深色模式
            val actualDarkMode = if (followSystem) isSystemDark else darkModeEnabled
            
            // 同步设置到 ThemeState
            LaunchedEffect(followSystem, darkModeEnabled, isSystemDark) {
                if (followSystem) {
                    themeState.updateUseSystemTheme(true)
                } else {
                    themeState.updateDarkMode(darkModeEnabled)
                }
            }
            
            // 更新状态栏颜色
            UpdateStatusBarColor(isDark = actualDarkMode)
            
            AuroraTheme(themeState = themeState) {
                MainRoot(themeState = themeState)
            }
        }
    }
}

/**
 * 根据深色模式更新状态栏颜色
 */
@Composable
private fun UpdateStatusBarColor(isDark: Boolean) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        DisposableEffect(isDark) {
            val window = (view.context as Activity).window
            // 设置状态栏图标颜色（深色模式用浅色图标，浅色模式用深色图标）
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDark
            // 设置状态栏背景色
            window.statusBarColor = if (isDark) {
                Color(0xFF1E1E1E).toArgb()  // 深色模式 - 深色背景
            } else {
                Color.White.toArgb()  // 浅色模式 - 白色背景
            }
            onDispose {}
        }
    }
}

data class NavItem(
    val route: String,
    val label: String,
    val icon: @Composable () -> Unit
)

@Composable
private fun MainRoot(themeState: ThemeState) {
    val navController = rememberNavController()
    val items = listOf(
        NavItem(WaveRoute.Messages.route, stringResource(R.string.nav_messages)) { Icon(Icons.AutoMirrored.Outlined.Chat, contentDescription = null) },
        NavItem(WaveRoute.Connections.route, stringResource(R.string.nav_connections)) { Icon(Icons.Outlined.Contacts, contentDescription = null) },
        NavItem(WaveRoute.Discover.route, stringResource(R.string.nav_discover)) { Icon(Icons.Outlined.CompassCalibration, contentDescription = null) },
        NavItem(WaveRoute.Profile.route, stringResource(R.string.nav_profile)) { Icon(Icons.Outlined.AccountCircle, contentDescription = null) }
    )
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    // Only show bottom bar on main tabs
    val showBottomBar = currentRoute in WaveRoute.mainTabRoutes

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                BottomNavBar(currentRoute, items) { route ->
                    Timber.d("Navigate to: $route")
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = WaveRoute.Messages.route,
            modifier = Modifier.fillMaxSize(),
            builder = {
                composable(WaveRoute.Messages.route) { 
                    MessagesRootScreen(
                        padding = padding,
                        onConversationClick = { conversationId ->
                            navController.navigate(WaveRoute.Chat(conversationId).route)
                        }
                    ) 
                }
                
                composable(
                    route = WaveRoute.Chat.ROUTE_PATTERN,
                    arguments = listOf(navArgument(WaveRoute.Chat.ARG_CONVERSATION_ID) { type = NavType.StringType })
                ) { backStackEntry ->
                    val conversationId = backStackEntry.arguments?.getString(WaveRoute.Chat.ARG_CONVERSATION_ID) ?: ""
                    ChatDetailScreen(
                        conversationId = conversationId,
                        onBackClick = { navController.popBackStack() }
                    )
                }
                
                composable(WaveRoute.Connections.route) { 
                    ConnectionsRootScreen(
                        padding = padding,
                        onContactClick = { contactId ->
                            navController.navigate(WaveRoute.Contact(contactId).route)
                        }
                    ) 
                }
                
                composable(
                    route = WaveRoute.Contact.ROUTE_PATTERN,
                    arguments = listOf(navArgument(WaveRoute.Contact.ARG_CONTACT_ID) { type = NavType.StringType })
                ) { backStackEntry ->
                    val contactId = backStackEntry.arguments?.getString(WaveRoute.Contact.ARG_CONTACT_ID) ?: ""
                    ContactDetailScreen(
                        contactId = contactId,
                        onBackClick = { navController.popBackStack() },
                        onChatClick = {
                            navController.navigate(WaveRoute.Chat(contactId).route) {
                                popUpTo(WaveRoute.Connections.route)
                            }
                        }
                    )
                }
                
                composable(WaveRoute.Discover.route) { 
                    DiscoverRootScreen(
                        padding = padding,
                        onItemClick = { route ->
                            navController.navigate(route)
                        }
                    ) 
                }
                
                composable(WaveRoute.Moments.route) {
                    MomentsScreen(
                        onBackClick = { navController.popBackStack() }
                    )
                }
                
                composable(WaveRoute.Scan.route) {
                    ScanScreen(
                        onBackClick = { navController.popBackStack() },
                        onMyQrCodeClick = { navController.navigate(WaveRoute.MyQrCode.route) }
                    )
                }
                
                composable(WaveRoute.MyQrCode.route) {
                    MyQrCodeScreen(
                        onBackClick = { navController.popBackStack() }
                    )
                }
                
                composable(WaveRoute.Profile.route) { 
                    ProfileRootScreen(
                        padding = padding,
                        onSettingsClick = { settingId ->
                            Timber.d("Profile menu clicked: $settingId")
                            when (settingId) {
                                "settings" -> navController.navigate(WaveRoute.Settings.route)
                                "appearance" -> navController.navigate(WaveRoute.Appearance.route)
                                "about" -> navController.navigate(WaveRoute.About.route)
                                else -> { /* Handle other menu items */ }
                            }
                        }
                    ) 
                }
                
                composable(WaveRoute.Settings.route) {
                    SettingsScreen(
                        onBackClick = { navController.popBackStack() }
                    )
                }
                
                composable(WaveRoute.Appearance.route) {
                    AppearanceScreen(
                        onBackClick = { navController.popBackStack() }
                    )
                }
                
                composable(WaveRoute.About.route) {
                    AboutScreen(
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
        )
    }
}

/**
 * 微信风格底部导航栏
 * 特点：白色背景、简洁图标、绿色选中
 */
@Composable
private fun BottomNavBar(currentRoute: String?, items: List<NavItem>, onSelect: (String) -> Unit) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = { onSelect(item.route) },
                icon = item.icon,
                label = { 
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall
                    ) 
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.outline,
                    unselectedTextColor = MaterialTheme.colorScheme.outline,
                    indicatorColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    }
}

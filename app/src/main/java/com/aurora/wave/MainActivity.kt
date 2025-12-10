package com.aurora.wave

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
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
import com.aurora.wave.data.user.UserDataStore
import com.aurora.wave.auth.LoginScreen
import com.aurora.wave.auth.RegisterScreen
import com.aurora.wave.messages.MessagesRootScreen
import com.aurora.wave.messages.ChatDetailScreen
import com.aurora.wave.messages.SearchScreen
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
import com.aurora.wave.profile.NotificationsScreen
import com.aurora.wave.profile.PrivacySecurityScreen
import com.aurora.wave.profile.StorageDataScreen
import com.aurora.wave.profile.LanguageScreen
import com.aurora.wave.navigation.WaveRoute
import com.aurora.wave.splash.SplashScreen
import com.aurora.wave.data.settings.LanguageManager
import com.aurora.wave.data.settings.AppLanguage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    override fun attachBaseContext(newBase: Context) {
        // 先从 DataStore 加载语言设置
        val settingsDataStore = SettingsDataStore(newBase)
        val savedLanguage = runBlocking { 
            settingsDataStore.languageFlow.first() 
        }
        LanguageManager.initLanguage(savedLanguage)
        
        // 包装 Context 以应用语言设置
        super.attachBaseContext(LanguageManager.wrapContext(newBase))
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        // 启用 Edge-to-Edge，让内容延伸到系统栏下方
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        Timber.d("MainActivity onCreate")
        setContent {
            val context = LocalContext.current
            val settingsDataStore = remember { SettingsDataStore(context) }
            val userDataStore = remember { UserDataStore(context) }
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
            
            AuroraTheme(themeState = themeState) {
                MainRoot(themeState = themeState, userDataStore = userDataStore)
            }
        }
    }
}

data class NavItem(
    val route: String,
    val label: String,
    val icon: @Composable () -> Unit
)

@Composable
private fun MainRoot(themeState: ThemeState, userDataStore: UserDataStore) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    
    // 监听登录状态
    val isLoggedIn by userDataStore.isLoggedInFlow.collectAsState(initial = null)
    
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

    // 起始页面始终是 Splash
    val startDestination = WaveRoute.Splash.route
    
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
        // 定义页面切换动画
        val enterTransition = slideInHorizontally(
            initialOffsetX = { fullWidth -> fullWidth },
            animationSpec = tween(300)
        ) + fadeIn(animationSpec = tween(300))
        
        val exitTransition = slideOutHorizontally(
            targetOffsetX = { fullWidth -> -fullWidth / 4 },
            animationSpec = tween(300)
        ) + fadeOut(animationSpec = tween(150))
        
        val popEnterTransition = slideInHorizontally(
            initialOffsetX = { fullWidth -> -fullWidth / 4 },
            animationSpec = tween(300)
        ) + fadeIn(animationSpec = tween(300))
        
        val popExitTransition = slideOutHorizontally(
            targetOffsetX = { fullWidth -> fullWidth },
            animationSpec = tween(300)
        ) + fadeOut(animationSpec = tween(150))
        
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.fillMaxSize(),
            enterTransition = { enterTransition },
            exitTransition = { exitTransition },
            popEnterTransition = { popEnterTransition },
            popExitTransition = { popExitTransition },
            builder = {
                // 闪屏页
                composable(WaveRoute.Splash.route) {
                    SplashScreen(
                        onSplashFinished = {
                            // 动画结束后，根据登录状态导航
                            val destination = if (isLoggedIn == true) {
                                WaveRoute.Messages.route
                            } else {
                                WaveRoute.Login.route
                            }
                            navController.navigate(destination) {
                                popUpTo(WaveRoute.Splash.route) { inclusive = true }
                            }
                        }
                    )
                }
                
                // 登录页
                composable(WaveRoute.Login.route) {
                    LoginScreen(
                        onLoginSuccess = {
                            scope.launch {
                                // Mock 登录成功 - 保存用户数据
                                userDataStore.login(
                                    userId = "user_${System.currentTimeMillis()}",
                                    username = "Aurora User",
                                    waveId = "aurora_${(1000..9999).random()}"
                                )
                            }
                            navController.navigate(WaveRoute.Messages.route) {
                                popUpTo(WaveRoute.Login.route) { inclusive = true }
                            }
                        },
                        onNavigateToRegister = {
                            navController.navigate(WaveRoute.Register.route)
                        }
                    )
                }
                
                // 注册页
                composable(WaveRoute.Register.route) {
                    RegisterScreen(
                        onRegisterSuccess = {
                            scope.launch {
                                // Mock 注册成功 - 保存用户数据
                                userDataStore.login(
                                    userId = "user_${System.currentTimeMillis()}",
                                    username = "Aurora User",
                                    waveId = "aurora_${(1000..9999).random()}"
                                )
                            }
                            navController.navigate(WaveRoute.Messages.route) {
                                popUpTo(WaveRoute.Login.route) { inclusive = true }
                            }
                        },
                        onNavigateToLogin = {
                            navController.popBackStack()
                        },
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                }
                
                composable(WaveRoute.Messages.route) { 
                    MessagesRootScreen(
                        padding = padding,
                        onConversationClick = { conversationId ->
                            navController.navigate(WaveRoute.Chat(conversationId).route)
                        },
                        onSearchClick = {
                            navController.navigate(WaveRoute.Search.route)
                        }
                    ) 
                }
                
                composable(WaveRoute.Search.route) {
                    SearchScreen(
                        onBackClick = { navController.popBackStack() },
                        onResultClick = { result ->
                            // 根据结果类型导航到不同页面
                            when (result.type.name) {
                                "CONTACT" -> navController.navigate(WaveRoute.Contact(result.id).route)
                                "GROUP", "CHAT_RECORD" -> navController.navigate(WaveRoute.Chat(result.id).route)
                            }
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
                        onBackClick = { navController.popBackStack() },
                        onContactClick = { contactId ->
                            navController.navigate(WaveRoute.Contact(contactId).route)
                        }
                    )
                }
                
                composable(WaveRoute.Connections.route) { 
                    ConnectionsRootScreen(
                        padding = padding,
                        onContactClick = { contactId ->
                            navController.navigate(WaveRoute.Contact(contactId).route)
                        },
                        onSearchClick = {
                            navController.navigate(WaveRoute.Search.route)
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
                                "notifications" -> navController.navigate(WaveRoute.Notifications.route)
                                "privacy" -> navController.navigate(WaveRoute.Privacy.route)
                                "storage" -> navController.navigate(WaveRoute.Storage.route)
                                "language" -> navController.navigate(WaveRoute.Language.route)
                                "logout" -> {
                                    // 退出登录 - 清除缓存并返回登录页
                                    scope.launch {
                                        userDataStore.logout()
                                    }
                                    navController.navigate(WaveRoute.Login.route) {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
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
                
                composable(WaveRoute.Notifications.route) {
                    NotificationsScreen(
                        onBackClick = { navController.popBackStack() }
                    )
                }
                
                composable(WaveRoute.Privacy.route) {
                    PrivacySecurityScreen(
                        onBackClick = { navController.popBackStack() },
                        onBlockedUsersClick = { /* TODO: Navigate to blocked users */ }
                    )
                }
                
                composable(WaveRoute.Storage.route) {
                    StorageDataScreen(
                        onBackClick = { navController.popBackStack() }
                    )
                }
                
                composable(WaveRoute.Language.route) {
                    LanguageScreen(
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
    // Material 3 NavigationBar 默认已正确处理 navigationBars insets
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

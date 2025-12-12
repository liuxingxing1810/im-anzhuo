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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
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
import com.aurora.wave.messages.ChatInfoScreen
import com.aurora.wave.messages.SearchScreen
import com.aurora.wave.messages.call.VoiceCallScreen
import com.aurora.wave.messages.call.VideoCallScreen
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
import com.aurora.wave.profile.EditProfileScreen
import com.aurora.wave.profile.FavoritesScreen
import com.aurora.wave.profile.ServicesScreen
import com.aurora.wave.profile.HelpSupportScreen
import com.aurora.wave.connections.NewFriendsScreen
import com.aurora.wave.connections.GroupChatsScreen
import com.aurora.wave.connections.AddFriendScreen
import com.aurora.wave.connections.CreateGroupScreen
import com.aurora.wave.connections.TagsScreen
import com.aurora.wave.connections.OfficialAccountsScreen
import com.aurora.wave.navigation.WaveRoute
import com.aurora.wave.navigation.navigateSafely
import com.aurora.wave.navigation.popBackStackSafely
import com.aurora.wave.splash.SplashScreen
import com.aurora.wave.data.settings.LanguageManager
import com.aurora.wave.data.settings.AppLanguage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.net.URLEncoder
import java.net.URLDecoder

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
        NavItem(WaveRoute.Messages.route, stringResource(R.string.nav_messages)) { Icon(Icons.AutoMirrored.Outlined.Chat, contentDescription = null, modifier = Modifier.size(24.dp)) },
        NavItem(WaveRoute.Connections.route, stringResource(R.string.nav_connections)) { Icon(Icons.Outlined.Contacts, contentDescription = null, modifier = Modifier.size(24.dp)) },
        NavItem(WaveRoute.Discover.route, stringResource(R.string.nav_discover)) { Icon(Icons.Outlined.CompassCalibration, contentDescription = null, modifier = Modifier.size(24.dp)) },
        NavItem(WaveRoute.Profile.route, stringResource(R.string.nav_profile)) { Icon(Icons.Outlined.AccountCircle, contentDescription = null, modifier = Modifier.size(24.dp)) }
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
                    navController.navigateSafely(route) {
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
                            navController.navigateSafely(WaveRoute.Chat(conversationId).route)
                        },
                        onSearchClick = {
                            navController.navigateSafely(WaveRoute.Search.route)
                        },
                        onGroupChatClick = {
                            navController.navigateSafely(WaveRoute.CreateGroup.route)
                        },
                        onAddFriendClick = {
                            navController.navigateSafely(WaveRoute.AddFriend.route)
                        },
                        onScanClick = {
                            navController.navigateSafely(WaveRoute.Scan.route)
                        }
                    ) 
                }
                
                composable(WaveRoute.Search.route) {
                    SearchScreen(
                        onBackClick = { navController.popBackStackSafely() },
                        onResultClick = { result ->
                            // 根据结果类型导航到不同页面
                            when (result.type.name) {
                                "CONTACT" -> navController.navigateSafely(WaveRoute.Contact(result.id).route)
                                "GROUP", "CHAT_RECORD" -> navController.navigateSafely(WaveRoute.Chat(result.id).route)
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
                        onBackClick = { navController.popBackStackSafely() },
                        onContactClick = { contactId ->
                            navController.navigateSafely(WaveRoute.Contact(contactId).route)
                        },
                        onMoreClick = {
                            navController.navigateSafely(WaveRoute.ChatInfo(conversationId).route)
                        },
                        onCallClick = {
                            val encodedName = URLEncoder.encode("对方", "UTF-8")
                            val route = WaveRoute.VoiceCall(conversationId, encodedName).route
                            Timber.d("MainActivity: navigating to voice call route=$route")
                            navController.navigateSafely(route)
                        },
                        onVideoCallClick = {
                            val encodedName = URLEncoder.encode("对方", "UTF-8")
                            val route = WaveRoute.VideoCall(conversationId, encodedName).route
                            Timber.d("MainActivity: navigating to video call route=$route")
                            navController.navigateSafely(route)
                        }
                    )
                }
                
                composable(
                    route = WaveRoute.ChatInfo.ROUTE_PATTERN,
                    arguments = listOf(navArgument(WaveRoute.ChatInfo.ARG_CONVERSATION_ID) { type = NavType.StringType })
                ) { backStackEntry ->
                    val conversationId = backStackEntry.arguments?.getString(WaveRoute.ChatInfo.ARG_CONVERSATION_ID) ?: ""
                    ChatInfoScreen(
                        conversationId = conversationId,
                        onBackClick = { navController.popBackStackSafely() }
                    )
                }
                
                // 语音通话页面
                composable(
                    route = WaveRoute.VoiceCall.ROUTE_PATTERN,
                    arguments = listOf(
                        navArgument(WaveRoute.VoiceCall.ARG_CONVERSATION_ID) { type = NavType.StringType },
                        navArgument(WaveRoute.VoiceCall.ARG_CONTACT_NAME) { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val conversationId = backStackEntry.arguments?.getString(WaveRoute.VoiceCall.ARG_CONVERSATION_ID) ?: ""
                    val rawContactName = backStackEntry.arguments?.getString(WaveRoute.VoiceCall.ARG_CONTACT_NAME) ?: ""
                    val contactName = try { URLDecoder.decode(rawContactName, "UTF-8") } catch (e: Exception) { rawContactName }
                    VoiceCallScreen(
                        conversationId = conversationId,
                        contactName = contactName,
                        onEndCall = { navController.popBackStackSafely() }
                    )
                }
                
                // 视频通话页面
                composable(
                    route = WaveRoute.VideoCall.ROUTE_PATTERN,
                    arguments = listOf(
                        navArgument(WaveRoute.VideoCall.ARG_CONVERSATION_ID) { type = NavType.StringType },
                        navArgument(WaveRoute.VideoCall.ARG_CONTACT_NAME) { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val conversationId = backStackEntry.arguments?.getString(WaveRoute.VideoCall.ARG_CONVERSATION_ID) ?: ""
                    val rawContactName = backStackEntry.arguments?.getString(WaveRoute.VideoCall.ARG_CONTACT_NAME) ?: ""
                    val contactName = try { URLDecoder.decode(rawContactName, "UTF-8") } catch (e: Exception) { rawContactName }
                    VideoCallScreen(
                        conversationId = conversationId,
                        contactName = contactName,
                        onEndCall = { navController.popBackStackSafely() }
                    )
                }
                
                composable(WaveRoute.Connections.route) { 
                    ConnectionsRootScreen(
                        padding = padding,
                        onContactClick = { contactId ->
                            navController.navigateSafely(WaveRoute.Contact(contactId).route)
                        },
                        onSearchClick = {
                            navController.navigateSafely(WaveRoute.Search.route)
                        },
                        onNewFriendsClick = {
                            navController.navigateSafely(WaveRoute.NewFriends.route)
                        },
                        onGroupsClick = {
                            navController.navigateSafely(WaveRoute.GroupChats.route)
                        },
                        onAddFriendClick = {
                            navController.navigateSafely(WaveRoute.AddFriend.route)
                        },
                        onTagsClick = {
                            navController.navigateSafely(WaveRoute.Tags.route)
                        },
                        onOfficialAccountsClick = {
                            navController.navigateSafely(WaveRoute.OfficialAccounts.route)
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
                        onBackClick = { navController.popBackStackSafely() },
                        onChatClick = {
                            navController.navigateSafely(WaveRoute.Chat(contactId).route) {
                                popUpTo(WaveRoute.Connections.route)
                            }
                        }
                    )
                }
                
                composable(WaveRoute.Discover.route) { 
                    DiscoverRootScreen(
                        padding = padding,
                        onItemClick = { route ->
                            navController.navigateSafely(route)
                        }
                    ) 
                }
                
                composable(WaveRoute.Moments.route) {
                    MomentsScreen(
                        onBackClick = { navController.popBackStackSafely() }
                    )
                }
                
                composable(WaveRoute.Scan.route) {
                    ScanScreen(
                        onBackClick = { navController.popBackStackSafely() },
                        onMyQrCodeClick = { navController.navigateSafely(WaveRoute.MyQrCode.route) }
                    )
                }
                
                composable(WaveRoute.MyQrCode.route) {
                    MyQrCodeScreen(
                        onBackClick = { navController.popBackStackSafely() }
                    )
                }
                
                composable(WaveRoute.Profile.route) { 
                    ProfileRootScreen(
                        padding = padding,
                        onProfileClick = {
                            navController.navigateSafely(WaveRoute.EditProfile.route)
                        },
                        onSettingsClick = { settingId ->
                            Timber.d("Profile menu clicked: $settingId")
                            when (settingId) {
                                "settings" -> navController.navigateSafely(WaveRoute.Settings.route)
                                "appearance" -> navController.navigateSafely(WaveRoute.Appearance.route)
                                "about" -> navController.navigateSafely(WaveRoute.About.route)
                                "notifications" -> navController.navigateSafely(WaveRoute.Notifications.route)
                                "privacy" -> navController.navigateSafely(WaveRoute.Privacy.route)
                                "storage" -> navController.navigateSafely(WaveRoute.Storage.route)
                                "language" -> navController.navigateSafely(WaveRoute.Language.route)
                                "favorites" -> navController.navigateSafely(WaveRoute.Favorites.route)
                                "services" -> navController.navigateSafely(WaveRoute.Services.route)
                                "help" -> navController.navigateSafely(WaveRoute.HelpSupport.route)
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
                        onBackClick = { navController.popBackStackSafely() }
                    )
                }
                
                composable(WaveRoute.Appearance.route) {
                    AppearanceScreen(
                        onBackClick = { navController.popBackStackSafely() }
                    )
                }
                
                composable(WaveRoute.About.route) {
                    AboutScreen(
                        onBackClick = { navController.popBackStackSafely() }
                    )
                }
                
                composable(WaveRoute.Notifications.route) {
                    NotificationsScreen(
                        onBackClick = { navController.popBackStackSafely() }
                    )
                }
                
                composable(WaveRoute.Privacy.route) {
                    PrivacySecurityScreen(
                        onBackClick = { navController.popBackStackSafely() },
                        onBlockedUsersClick = { /* TODO: Navigate to blocked users */ }
                    )
                }
                
                composable(WaveRoute.Storage.route) {
                    StorageDataScreen(
                        onBackClick = { navController.popBackStackSafely() }
                    )
                }
                
                composable(WaveRoute.Language.route) {
                    LanguageScreen(
                        onBackClick = { navController.popBackStackSafely() }
                    )
                }
                
                // 收付款页面
                composable(WaveRoute.PayCode.route) {
                    com.aurora.wave.profile.PayCodeScreen(
                        onBackClick = { navController.popBackStackSafely() }
                    )
                }
                
                // 设备管理页面
                composable(WaveRoute.DeviceManagement.route) {
                    com.aurora.wave.profile.DeviceManagementScreen(
                        onBackClick = { navController.popBackStackSafely() }
                    )
                }
                
                // 修改密码页面
                composable(WaveRoute.ChangePassword.route) {
                    com.aurora.wave.profile.ChangePasswordScreen(
                        onBackClick = { navController.popBackStackSafely() }
                    )
                }
                
                // 通话记录页面
                composable(WaveRoute.CallHistory.route) {
                    com.aurora.wave.profile.CallHistoryScreen(
                        onBackClick = { navController.popBackStackSafely() },
                        onCallClick = { callId, contactName, isVideo ->
                            if (isVideo) {
                                navController.navigateSafely(WaveRoute.VideoCall(callId, contactName).route)
                            } else {
                                navController.navigateSafely(WaveRoute.VoiceCall(callId, contactName).route)
                            }
                        }
                    )
                }
                
                // 通讯录子页面
                composable(WaveRoute.NewFriends.route) {
                    NewFriendsScreen(
                        onBackClick = { navController.popBackStackSafely() }
                    )
                }
                
                composable(WaveRoute.GroupChats.route) {
                    GroupChatsScreen(
                        onBackClick = { navController.popBackStackSafely() },
                        onGroupClick = { groupId ->
                            navController.navigateSafely(WaveRoute.Chat(groupId).route)
                        }
                    )
                }
                
                composable(WaveRoute.AddFriend.route) {
                    AddFriendScreen(
                        onBackClick = { navController.popBackStackSafely() },
                        onScanClick = {
                            navController.navigateSafely(WaveRoute.Scan.route)
                        }
                    )
                }
                
                composable(WaveRoute.CreateGroup.route) {
                    CreateGroupScreen(
                        onBackClick = { navController.popBackStackSafely() },
                        onCreateGroup = { contactIds ->
                            // 创建群聊后导航到聊天页面
                            val groupId = "group_${System.currentTimeMillis()}"
                            navController.navigateSafely(WaveRoute.Chat(groupId).route) {
                                popUpTo(WaveRoute.Messages.route)
                            }
                        }
                    )
                }
                
                composable(WaveRoute.Tags.route) {
                    TagsScreen(
                        onBackClick = { navController.popBackStackSafely() },
                        onContactClick = { contactId ->
                            navController.navigateSafely(WaveRoute.Contact(contactId).route)
                        }
                    )
                }
                
                composable(WaveRoute.OfficialAccounts.route) {
                    OfficialAccountsScreen(
                        onBackClick = { navController.popBackStackSafely() },
                        onAccountClick = { accountId ->
                            navController.navigateSafely(WaveRoute.Chat(accountId).route)
                        }
                    )
                }
                
                // 个人资料子页面
                composable(WaveRoute.EditProfile.route) {
                    EditProfileScreen(
                        onBackClick = { navController.popBackStackSafely() },
                        onQrCodeClick = {
                            navController.navigateSafely(WaveRoute.MyQrCode.route)
                        }
                    )
                }
                
                composable(WaveRoute.Favorites.route) {
                    FavoritesScreen(
                        onBackClick = { navController.popBackStackSafely() }
                    )
                }
                
                composable(WaveRoute.Services.route) {
                    ServicesScreen(
                        onBackClick = { navController.popBackStackSafely() },
                        onServiceClick = { serviceId ->
                            when (serviceId) {
                                "scan" -> navController.navigateSafely(WaveRoute.Scan.route)
                                else -> { /* Handle other services */ }
                            }
                        }
                    )
                }
                
                composable(WaveRoute.HelpSupport.route) {
                    HelpSupportScreen(
                        onBackClick = { navController.popBackStackSafely() }
                    )
                }
            }
        )
    }
}

/**
 * 微信风格底部导航栏
 * 特点：紧凑高度(56dp)、24dp图标、10sp文字
 */
@Composable
private fun BottomNavBar(currentRoute: String?, items: List<NavItem>, onSelect: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // 顶部分隔线
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        )
        // 导航栏内容
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp), // 微信风格紧凑高度
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val selected = currentRoute == item.route
                val color = if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outline
                }
                
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onSelect(item.route) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        androidx.compose.runtime.CompositionLocalProvider(
                            androidx.compose.material3.LocalContentColor provides color
                        ) {
                            item.icon()
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.label,
                        color = color,
                        fontSize = 10.sp,
                        fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal
                    )
                }
            }
        }
        // 系统导航栏安全区
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

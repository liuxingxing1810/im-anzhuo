package com.aurora.wave.navigation

/**
 * 路由 sealed class
 * Type-safe navigation routes using sealed class
 */
sealed class WaveRoute(val route: String) {
    // Splash
    data object Splash : WaveRoute("splash")
    
    // Auth
    data object Login : WaveRoute("login")
    data object Register : WaveRoute("register")
    
    // Main tabs
    data object Messages : WaveRoute("messages")
    data object Connections : WaveRoute("connections")
    data object Discover : WaveRoute("discover")
    data object Profile : WaveRoute("profile")
    
    // Chat
    data class Chat(val conversationId: String = "{conversationId}") : WaveRoute("chat/$conversationId") {
        companion object {
            const val ROUTE_PATTERN = "chat/{conversationId}"
            const val ARG_CONVERSATION_ID = "conversationId"
        }
    }
    
    // Chat Info (聊天信息页面)
    data class ChatInfo(val conversationId: String = "{conversationId}") : WaveRoute("chat_info/$conversationId") {
        companion object {
            const val ROUTE_PATTERN = "chat_info/{conversationId}"
            const val ARG_CONVERSATION_ID = "conversationId"
        }
    }
    
    // Contact
    data class Contact(val contactId: String = "{contactId}") : WaveRoute("contact/$contactId") {
        companion object {
            const val ROUTE_PATTERN = "contact/{contactId}"
            const val ARG_CONTACT_ID = "contactId"
        }
    }
    
    // Contacts sub-routes
    data object NewFriends : WaveRoute("new_friends")
    data object GroupChats : WaveRoute("group_chats")
    data object AddFriend : WaveRoute("add_friend")
    data object CreateGroup : WaveRoute("create_group")
    data object Tags : WaveRoute("tags")
    data object OfficialAccounts : WaveRoute("official_accounts")
    
    // Discover sub-routes
    data object Moments : WaveRoute("moments")
    data object Scan : WaveRoute("scan")
    data object MyQrCode : WaveRoute("my_qr_code")
    data object Search : WaveRoute("search")
    
    // Profile sub-routes
    data object Settings : WaveRoute("settings")
    data object Appearance : WaveRoute("appearance")
    data object About : WaveRoute("about")
    data object Notifications : WaveRoute("notifications")
    data object Privacy : WaveRoute("privacy")
    data object Storage : WaveRoute("storage")
    data object Language : WaveRoute("language")
    data object EditProfile : WaveRoute("edit_profile")
    data object Favorites : WaveRoute("favorites")
    data object Services : WaveRoute("services")
    data object HelpSupport : WaveRoute("help_support")
    data object PayCode : WaveRoute("pay_code") // 收付款页面
    data object DeviceManagement : WaveRoute("device_management") // 设备管理
    data object ChangePassword : WaveRoute("change_password") // 修改密码
    data object CallHistory : WaveRoute("call_history") // 通话记录
    
    // Call routes
    data class VoiceCall(val conversationId: String = "{conversationId}", val contactName: String = "{contactName}") : 
        WaveRoute("voice_call/$conversationId/$contactName") {
        companion object {
            const val ROUTE_PATTERN = "voice_call/{conversationId}/{contactName}"
            const val ARG_CONVERSATION_ID = "conversationId"
            const val ARG_CONTACT_NAME = "contactName"
        }
    }
    
    data class VideoCall(val conversationId: String = "{conversationId}", val contactName: String = "{contactName}") : 
        WaveRoute("video_call/$conversationId/$contactName") {
        companion object {
            const val ROUTE_PATTERN = "video_call/{conversationId}/{contactName}"
            const val ARG_CONVERSATION_ID = "conversationId"
            const val ARG_CONTACT_NAME = "contactName"
        }
    }
    
    companion object {
        // Auth routes
        val authRoutes: Set<String> by lazy {
            setOf(Login.route, Register.route)
        }
        
        // 使用 lazy 避免静态初始化循环依赖
        val mainTabRoutes: Set<String> by lazy {
            setOf(
                Messages.route,
                Connections.route,
                Discover.route,
                Profile.route
            )
        }
    }
}

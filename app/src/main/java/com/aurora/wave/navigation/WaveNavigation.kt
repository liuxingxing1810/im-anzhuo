package com.aurora.wave.navigation

/**
 * 路由 sealed class
 * Type-safe navigation routes using sealed class
 */
sealed class WaveRoute(val route: String) {
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
    
    // Contact
    data class Contact(val contactId: String = "{contactId}") : WaveRoute("contact/$contactId") {
        companion object {
            const val ROUTE_PATTERN = "contact/{contactId}"
            const val ARG_CONTACT_ID = "contactId"
        }
    }
    
    // Discover sub-routes
    data object Moments : WaveRoute("moments")
    data object Scan : WaveRoute("scan")
    data object MyQrCode : WaveRoute("my_qr_code")
    data object Search : WaveRoute("search")
    
    // Profile sub-routes
    data object Settings : WaveRoute("settings")
    data object Appearance : WaveRoute("appearance")
    data object About : WaveRoute("about")
    
    companion object {
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

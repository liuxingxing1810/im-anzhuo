package com.aurora.wave.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

/**
 * 防止快速重复点击导航的工具类
 * 使用节流机制，在指定时间内只响应第一次点击
 */
object NavigationUtils {
    // 上次导航的时间戳
    @Volatile
    private var lastNavigationTime = 0L
    
    // 导航间隔阈值（毫秒）
    private const val NAVIGATION_THROTTLE_MS = 500L
    
    /**
     * 检查是否可以进行导航
     * @return true 如果可以导航，false 如果需要节流
     */
    @Synchronized
    fun canNavigate(): Boolean {
        val currentTime = System.currentTimeMillis()
        return if (currentTime - lastNavigationTime > NAVIGATION_THROTTLE_MS) {
            lastNavigationTime = currentTime
            true
        } else {
            false
        }
    }
    
    /**
     * 重置导航时间戳（用于特殊情况，如强制导航）
     */
    fun reset() {
        lastNavigationTime = 0L
    }
}

/**
 * NavController 扩展函数：安全导航，防止重复点击
 * 
 * @param route 目标路由
 * @param builder 可选的导航选项构建器
 */
fun NavController.navigateSafely(
    route: String,
    builder: (NavOptionsBuilder.() -> Unit)? = null
) {
    if (NavigationUtils.canNavigate()) {
        if (builder != null) {
            navigate(route, builder)
        } else {
            navigate(route)
        }
    }
}

/**
 * NavController 扩展函数：安全返回上一页，防止重复点击
 */
fun NavController.popBackStackSafely(): Boolean {
    return if (NavigationUtils.canNavigate()) {
        popBackStack()
    } else {
        false
    }
}

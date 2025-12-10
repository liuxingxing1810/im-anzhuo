package com.aurora.wave.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.aurora.wave.design.TransparentStatusBar
import kotlin.math.hypot

/**
 * 闪屏页面
 * 
 * 功能：
 * 1. 显示 Lottie 加载动画
 * 2. 动画结束后执行圆形扩散过渡效果
 * 3. 过渡完成后导航到登录页/主页
 */
@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    // 沉浸式状态栏 - 深色背景使用浅色图标
    val isDarkTheme = isSystemInDarkTheme()
    TransparentStatusBar(darkIcons = !isDarkTheme)
    
    // 加载 Lottie 动画 (JSON 格式)
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset("splash_animation.json")
    )
    
    // 动画播放进度
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1,
        isPlaying = true
    )
    
    // 圆形扩散动画状态
    var showCircleReveal by remember { mutableStateOf(false) }
    val circleRadius = remember { Animatable(0f) }
    
    // 获取屏幕尺寸用于计算圆形最大半径
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenWidthPx = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }
    // 计算对角线长度作为最大半径，确保圆形能覆盖整个屏幕
    val maxRadius = hypot(screenWidthPx, screenHeightPx)
    
    // 背景色 - 深色主题
    val backgroundColor = MaterialTheme.colorScheme.background
    val revealColor = MaterialTheme.colorScheme.surface
    
    // 监听 Lottie 动画完成
    LaunchedEffect(progress) {
        if (progress >= 1f && !showCircleReveal) {
            showCircleReveal = true
        }
    }
    
    // 执行圆形扩散动画
    LaunchedEffect(showCircleReveal) {
        if (showCircleReveal) {
            circleRadius.animateTo(
                targetValue = maxRadius,
                animationSpec = tween(
                    durationMillis = 600,
                    easing = FastOutSlowInEasing
                )
            )
            // 动画完成后回调
            onSplashFinished()
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        // Lottie 动画
        if (!showCircleReveal || circleRadius.value < maxRadius * 0.3f) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.size(200.dp)
            )
        }
        
        // 圆形扩散效果
        if (showCircleReveal) {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                val centerX = size.width / 2
                val centerY = size.height / 2
                
                drawCircle(
                    color = revealColor,
                    radius = circleRadius.value,
                    center = Offset(centerX, centerY)
                )
            }
        }
    }
}

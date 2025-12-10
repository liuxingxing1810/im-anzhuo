package com.aurora.wave.auth

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.sin

/**
 * 静态波浪线条背景
 * 简约风格，轻量级装饰
 */
@Composable
fun AnimatedWaveBackground(
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
    val secondaryColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.06f)
    val tertiaryColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f)
    
    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        
        // 绘制静态曲线装饰
        drawWaveLine(
            width = width,
            baseY = height * 0.25f,
            amplitude = 60f,
            frequency = 0.008f,
            phase = 0f,
            color = primaryColor,
            strokeWidth = 2f
        )
        
        drawWaveLine(
            width = width,
            baseY = height * 0.5f,
            amplitude = 80f,
            frequency = 0.006f,
            phase = 90f,
            color = secondaryColor,
            strokeWidth = 1.5f
        )
        
        drawWaveLine(
            width = width,
            baseY = height * 0.75f,
            amplitude = 50f,
            frequency = 0.01f,
            phase = 180f,
            color = tertiaryColor,
            strokeWidth = 1.5f
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawWaveLine(
    width: Float,
    baseY: Float,
    amplitude: Float,
    frequency: Float,
    phase: Float,
    color: androidx.compose.ui.graphics.Color,
    strokeWidth: Float
) {
    val path = Path()
    val phaseRad = Math.toRadians(phase.toDouble()).toFloat()
    
    path.moveTo(0f, baseY + amplitude * sin(phaseRad))
    
    // 使用更大的步长减少计算量
    var x = 0f
    while (x <= width) {
        val y = baseY + amplitude * sin((x * frequency + phaseRad).toDouble()).toFloat()
        path.lineTo(x, y)
        x += 8f  // 增大步长
    }
    
    drawPath(
        path = path,
        color = color,
        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
    )
}

/**
 * 静态圆圈装饰背景
 */
@Composable
fun GradientCirclesBackground(
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
    val tertiaryColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.03f)
    
    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        
        // 右上角大圆
        drawCircle(
            color = primaryColor,
            radius = width * 0.4f,
            center = Offset(width * 0.9f, height * 0.1f)
        )
        
        // 左下角圆
        drawCircle(
            color = tertiaryColor,
            radius = width * 0.35f,
            center = Offset(width * 0.1f, height * 0.85f)
        )
    }
}

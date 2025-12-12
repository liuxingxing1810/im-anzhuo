package com.aurora.wave.messages.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * 语音录制状态
 */
enum class VoiceRecordingState {
    IDLE,       // 空闲状态
    RECORDING,  // 录制中
    CANCEL,     // 准备取消
    CONVERT     // 准备转文字
}

/**
 * 语音录制面板
 * 类似微信的语音录制交互：长按录制，上滑取消，右滑转文字
 */
@Composable
fun VoiceRecordingPanel(
    isRecording: Boolean,
    recordingDuration: Int,
    currentAmplitude: Float,
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit,
    onCancelRecording: () -> Unit,
    onConvertToText: () -> Unit,
    modifier: Modifier = Modifier
) {
    var recordingState by remember { mutableStateOf(VoiceRecordingState.IDLE) }
    var dragOffsetY by remember { mutableFloatStateOf(0f) }
    var dragOffsetX by remember { mutableFloatStateOf(0f) }
    val hapticFeedback = LocalHapticFeedback.current
    val density = LocalDensity.current
    
    // 取消阈值（上滑超过100dp触发取消）
    val cancelThreshold = with(density) { 100.dp.toPx() }
    // 转文字阈值（右滑超过80dp触发转文字）
    val convertThreshold = with(density) { 80.dp.toPx() }
    
    // 根据拖动距离判断状态
    LaunchedEffect(dragOffsetY, dragOffsetX) {
        recordingState = when {
            !isRecording -> VoiceRecordingState.IDLE
            dragOffsetY < -cancelThreshold -> VoiceRecordingState.CANCEL
            dragOffsetX > convertThreshold -> VoiceRecordingState.CONVERT
            else -> VoiceRecordingState.RECORDING
        }
    }
    
    // 录制时长格式化
    val formattedDuration = remember(recordingDuration) {
        val minutes = recordingDuration / 60
        val seconds = recordingDuration % 60
        String.format("%d:%02d", minutes, seconds)
    }
    
    // 声波动画
    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    val waveScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wave_scale"
    )
    
    AnimatedVisibility(
        visible = isRecording,
        enter = fadeIn(tween(150)) + slideInVertically { it },
        exit = fadeOut(tween(100)) + slideOutVertically { it }
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // 录制状态指示
                RecordingIndicator(
                    state = recordingState,
                    duration = formattedDuration,
                    amplitude = currentAmplitude,
                    waveScale = waveScale
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // 提示文字
                Text(
                    text = when (recordingState) {
                        VoiceRecordingState.IDLE -> ""
                        VoiceRecordingState.RECORDING -> "松开 发送"
                        VoiceRecordingState.CANCEL -> "松开 取消"
                        VoiceRecordingState.CONVERT -> "松开 转文字"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = when (recordingState) {
                        VoiceRecordingState.CANCEL -> MaterialTheme.colorScheme.error
                        else -> Color.White
                    },
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(48.dp))
                
                // 操作区域
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 取消按钮区域
                    CancelButton(
                        isActive = recordingState == VoiceRecordingState.CANCEL
                    )
                    
                    // 中间麦克风按钮（主录制按钮）
                    MicrophoneButton(
                        amplitude = currentAmplitude,
                        waveScale = waveScale,
                        offsetY = dragOffsetY.roundToInt(),
                        offsetX = dragOffsetX.roundToInt(),
                        onDragStart = {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        onDrag = { x, y ->
                            dragOffsetX = x
                            dragOffsetY = y
                        },
                        onDragEnd = {
                            when (recordingState) {
                                VoiceRecordingState.CANCEL -> onCancelRecording()
                                VoiceRecordingState.CONVERT -> onConvertToText()
                                else -> onStopRecording()
                            }
                            dragOffsetX = 0f
                            dragOffsetY = 0f
                        }
                    )
                    
                    // 转文字按钮区域
                    ConvertButton(
                        isActive = recordingState == VoiceRecordingState.CONVERT
                    )
                }
            }
        }
    }
}

/**
 * 录制状态指示器
 */
@Composable
private fun RecordingIndicator(
    state: VoiceRecordingState,
    duration: String,
    amplitude: Float,
    waveScale: Float
) {
    val backgroundColor = when (state) {
        VoiceRecordingState.CANCEL -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.primary
    }
    
    val scale by animateFloatAsState(
        targetValue = when (state) {
            VoiceRecordingState.CANCEL -> 1.1f
            else -> 1f
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "indicator_scale"
    )
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 声波圆圈
        Box(
            contentAlignment = Alignment.Center
        ) {
            // 外层声波
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .scale(waveScale * (0.8f + amplitude * 0.4f))
                    .alpha(0.3f)
                    .clip(CircleShape)
                    .background(backgroundColor)
            )
            
            // 内层声波
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .scale(1f + amplitude * 0.2f)
                    .alpha(0.5f)
                    .clip(CircleShape)
                    .background(backgroundColor)
            )
            
            // 中心图标
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .scale(scale)
                    .clip(CircleShape)
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (state == VoiceRecordingState.CANCEL) {
                        Icons.Default.Close
                    } else {
                        Icons.Default.Mic
                    },
                    contentDescription = null,
                    modifier = Modifier.size(36.dp),
                    tint = Color.White
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 时长显示
        Text(
            text = duration,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * 麦克风主按钮
 */
@Composable
private fun MicrophoneButton(
    amplitude: Float,
    waveScale: Float,
    offsetY: Int,
    offsetX: Int,
    onDragStart: () -> Unit,
    onDrag: (Float, Float) -> Unit,
    onDragEnd: () -> Unit
) {
    var totalOffsetX by remember { mutableFloatStateOf(0f) }
    var totalOffsetY by remember { mutableFloatStateOf(0f) }
    
    Box(
        modifier = Modifier
            .offset { IntOffset(offsetX, offsetY) }
            .size(72.dp)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                    )
                )
            )
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        totalOffsetX = 0f
                        totalOffsetY = 0f
                        onDragStart()
                    },
                    onDrag = { _, dragAmount ->
                        totalOffsetX += dragAmount.x
                        totalOffsetY += dragAmount.y
                        onDrag(totalOffsetX, totalOffsetY)
                    },
                    onDragEnd = onDragEnd,
                    onDragCancel = onDragEnd
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Mic,
            contentDescription = "录制语音",
            modifier = Modifier.size(32.dp),
            tint = Color.White
        )
    }
}

/**
 * 取消按钮
 */
@Composable
private fun CancelButton(
    isActive: Boolean
) {
    val scale by animateFloatAsState(
        targetValue = if (isActive) 1.3f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "cancel_scale"
    )
    
    val alpha by animateFloatAsState(
        targetValue = if (isActive) 1f else 0.6f,
        animationSpec = tween(150),
        label = "cancel_alpha"
    )
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .scale(scale)
                .alpha(alpha)
                .clip(CircleShape)
                .background(
                    if (isActive) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "取消",
                modifier = Modifier.size(24.dp),
                tint = if (isActive) Color.White else Color.White.copy(alpha = 0.7f)
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "取消",
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = alpha),
            fontSize = 10.sp
        )
        
        // 上滑提示
        if (!isActive) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp)
                    .alpha(0.5f),
                tint = Color.White
            )
        }
    }
}

/**
 * 转文字按钮
 */
@Composable
private fun ConvertButton(
    isActive: Boolean
) {
    val scale by animateFloatAsState(
        targetValue = if (isActive) 1.3f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "convert_scale"
    )
    
    val alpha by animateFloatAsState(
        targetValue = if (isActive) 1f else 0.6f,
        animationSpec = tween(150),
        label = "convert_alpha"
    )
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .scale(scale)
                .alpha(alpha)
                .clip(CircleShape)
                .background(
                    if (isActive) {
                        MaterialTheme.colorScheme.secondary
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Translate,
                contentDescription = "转文字",
                modifier = Modifier.size(24.dp),
                tint = if (isActive) Color.White else Color.White.copy(alpha = 0.7f)
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "转文字",
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = alpha),
            fontSize = 10.sp
        )
    }
}

/**
 * 语音录制按钮组件（用于输入栏）
 * 长按开始录制，松开发送
 */
@Composable
fun VoiceRecordButton(
    isRecording: Boolean,
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hapticFeedback = LocalHapticFeedback.current
    
    val backgroundColor by animateFloatAsState(
        targetValue = if (isRecording) 1f else 0f,
        animationSpec = tween(150),
        label = "bg_alpha"
    )
    
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        onStartRecording()
                    },
                    onPress = {
                        try {
                            awaitRelease()
                            if (isRecording) {
                                onStopRecording()
                            }
                        } catch (e: Exception) {
                            // 被取消
                        }
                    }
                )
            },
        shape = RoundedCornerShape(6.dp),
        color = if (isRecording) {
            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
        } else {
            MaterialTheme.colorScheme.surfaceContainerHighest
        }
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = if (isRecording) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = if (isRecording) "松开 发送" else "按住 说话",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isRecording) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    fontWeight = if (isRecording) FontWeight.Medium else FontWeight.Normal
                )
            }
        }
    }
}

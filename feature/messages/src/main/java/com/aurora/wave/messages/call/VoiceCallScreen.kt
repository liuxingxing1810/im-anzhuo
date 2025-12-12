package com.aurora.wave.messages.call

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay
import timber.log.Timber

@Composable
fun VoiceCallScreen(
    conversationId: String,
    contactName: String,
    onEndCall: () -> Unit
) {
    Timber.d("VoiceCallScreen: started with conversationId=$conversationId, contactName=$contactName")
    val context = LocalContext.current
    var hasPermission by remember { mutableStateOf(false) }
    var permissionDenied by remember { mutableStateOf(false) }
    
    // 音效管理器
    val soundManager = remember { CallSoundManager(context) }
    
    // 释放音效资源
    DisposableEffect(Unit) {
        onDispose {
            soundManager.release()
        }
    }
    
    // 权限请求
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        Timber.d("VoiceCallScreen: permission result granted=$granted")
        if (granted) {
            hasPermission = true
        } else {
            permissionDenied = true
            onEndCall()
        }
    }
    
    // 检查权限
    LaunchedEffect(Unit) {
        Timber.d("VoiceCallScreen: checking permission")
        val permission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        )
        if (permission == PackageManager.PERMISSION_GRANTED) {
            Timber.d("VoiceCallScreen: permission already granted")
            hasPermission = true
        } else {
            Timber.d("VoiceCallScreen: requesting permission")
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }
    
    // 控制状态
    var isMuted by remember { mutableStateOf(false) }
    var isSpeakerOn by remember { mutableStateOf(false) }
    var callDuration by remember { mutableIntStateOf(0) }
    var isConnecting by remember { mutableStateOf(true) }
    
    // 模拟通话计时
    LaunchedEffect(hasPermission) {
        if (hasPermission) {
            Timber.d("VoiceCallScreen: starting call timer")
            // 开始播放呼叫等待音
            soundManager.startRingbackTone()
            // 模拟等待接通
            delay(3000)
            // 接通后停止呼叫音
            soundManager.stopRingbackTone()
            isConnecting = false
            // 开始计时
            while (true) {
                delay(1000)
                callDuration++
            }
        }
    }
    
    // 挂断处理函数
    val handleEndCall: () -> Unit = {
        soundManager.stopRingbackTone()
        soundManager.playHangupToneAndThen {
            onEndCall()
        }
    }
    
    Timber.d("VoiceCallScreen: rendering, hasPermission=$hasPermission")
    if (hasPermission) {
        VoiceCallContent(
            contactName = contactName,
            isConnecting = isConnecting,
            callDuration = callDuration,
            isMuted = isMuted,
            isSpeakerOn = isSpeakerOn,
            onMuteToggle = { isMuted = !isMuted },
            onSpeakerToggle = { isSpeakerOn = !isSpeakerOn },
            onEndCall = handleEndCall
        )
    }
}

@Composable
private fun VoiceCallContent(
    contactName: String,
    isConnecting: Boolean,
    callDuration: Int,
    isMuted: Boolean,
    isSpeakerOn: Boolean,
    onMuteToggle: () -> Unit,
    onSpeakerToggle: () -> Unit,
    onEndCall: () -> Unit
) {
    // 渐变背景色
    val gradientColors = listOf(
        Color(0xFF1A1A2E),
        Color(0xFF16213E),
        Color(0xFF0F3460)
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(colors = gradientColors)
            )
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))
            
            // 头像带波纹效果
            AvatarWithRipple(
                name = contactName,
                isConnecting = isConnecting
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // 联系人名称
            Text(
                text = contactName,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // 通话状态
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = if (isConnecting) {
                        "正在呼叫..."
                    } else {
                        formatDuration(callDuration)
                    },
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
            
            // 连接动画点
            if (isConnecting) {
                Spacer(modifier = Modifier.height(20.dp))
                ConnectingDots()
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // 控制按钮
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // 静音按钮
                CallControlButton(
                    icon = if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                    label = if (isMuted) "取消静音" else "静音",
                    isActive = isMuted,
                    onClick = onMuteToggle
                )
                
                // 扬声器按钮
                CallControlButton(
                    icon = if (isSpeakerOn) Icons.AutoMirrored.Filled.VolumeUp else Icons.Default.Hearing,
                    label = if (isSpeakerOn) "听筒" else "扬声器",
                    isActive = isSpeakerOn,
                    onClick = onSpeakerToggle
                )
            }
            
            Spacer(modifier = Modifier.height(60.dp))
            
            // 挂断按钮
            EndCallButton(onClick = onEndCall)
            
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
private fun AvatarWithRipple(
    name: String,
    isConnecting: Boolean
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ripple")
    
    // 波纹动画
    val rippleScale1 by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ripple1"
    )
    
    val rippleAlpha1 by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rippleAlpha1"
    )
    
    val rippleScale2 by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, delayMillis = 600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ripple2"
    )
    
    val rippleAlpha2 by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, delayMillis = 600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rippleAlpha2"
    )
    
    val rippleScale3 by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, delayMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ripple3"
    )
    
    val rippleAlpha3 by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, delayMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rippleAlpha3"
    )
    
    Box(
        contentAlignment = Alignment.Center
    ) {
        // 波纹效果 (仅在呼叫中显示)
        if (isConnecting) {
            // 第一层波纹
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .scale(rippleScale1)
                    .alpha(rippleAlpha1)
                    .border(
                        width = 2.dp,
                        color = Color(0xFF4CAF50),
                        shape = CircleShape
                    )
            )
            
            // 第二层波纹
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .scale(rippleScale2)
                    .alpha(rippleAlpha2)
                    .border(
                        width = 2.dp,
                        color = Color(0xFF4CAF50),
                        shape = CircleShape
                    )
            )
            
            // 第三层波纹
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .scale(rippleScale3)
                    .alpha(rippleAlpha3)
                    .border(
                        width = 2.dp,
                        color = Color(0xFF4CAF50),
                        shape = CircleShape
                    )
            )
        }
        
        // 头像
        Box(
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF667eea),
                            Color(0xFF764ba2)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name.firstOrNull()?.uppercase() ?: "?",
                fontSize = 56.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun ConnectingDots() {
    val infiniteTransition = rememberInfiniteTransition(label = "dots")
    
    val dot1Alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot1"
    )
    
    val dot2Alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = 200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot2"
    )
    
    val dot3Alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = 400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot3"
    )
    
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .alpha(dot1Alpha)
                .clip(CircleShape)
                .background(Color(0xFF4CAF50))
        )
        Box(
            modifier = Modifier
                .size(10.dp)
                .alpha(dot2Alpha)
                .clip(CircleShape)
                .background(Color(0xFF4CAF50))
        )
        Box(
            modifier = Modifier
                .size(10.dp)
                .alpha(dot3Alpha)
                .clip(CircleShape)
                .background(Color(0xFF4CAF50))
        )
    }
}

@Composable
private fun CallControlButton(
    icon: ImageVector,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(
                    if (isActive) Color.White else Color.White.copy(alpha = 0.2f)
                )
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isActive) Color(0xFF1A1A2E) else Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun EndCallButton(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // 波纹扩散效果
    val infiniteTransition = rememberInfiniteTransition(label = "endCall")
    
    val rippleScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "endCallRipple"
    )
    
    val rippleAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "endCallRippleAlpha"
    )
    
    Box(
        contentAlignment = Alignment.Center
    ) {
        // 波纹效果
        Box(
            modifier = Modifier
                .size(80.dp)
                .scale(rippleScale)
                .alpha(rippleAlpha)
                .clip(CircleShape)
                .background(Color(0xFFE53935))
        )
        
        // 挂断按钮
        Box(
            modifier = Modifier
                .size(80.dp)
                .scale(if (isPressed) 0.95f else 1f)
                .clip(CircleShape)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFE53935),
                            Color(0xFFC62828)
                        )
                    )
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CallEnd,
                contentDescription = "挂断",
                tint = Color.White,
                modifier = Modifier.size(36.dp)
            )
        }
    }
}

private fun formatDuration(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}

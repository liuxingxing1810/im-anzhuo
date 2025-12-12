package com.aurora.wave.messages.call

import android.Manifest
import android.content.pm.PackageManager
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.math.roundToInt

@Composable
fun VideoCallScreen(
    conversationId: String,
    contactName: String,
    onEndCall: () -> Unit
) {
    val context = LocalContext.current
    var hasPermission by remember { mutableStateOf(false) }
    var permissionDenied by remember { mutableStateOf(false) }
    
    // 需要的权限
    val permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )
    
    // 权限请求
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        val allGranted = results.values.all { it }
        if (allGranted) {
            hasPermission = true
        } else {
            permissionDenied = true
            onEndCall()
        }
    }
    
    // 检查权限
    LaunchedEffect(Unit) {
        val allGranted = permissions.all { permission ->
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
        if (allGranted) {
            hasPermission = true
        } else {
            permissionLauncher.launch(permissions)
        }
    }
    
    // 控制状态
    var isMuted by remember { mutableStateOf(false) }
    var isSpeakerOn by remember { mutableStateOf(true) } // 视频通话默认开启扬声器
    var isCameraOn by remember { mutableStateOf(true) }
    var isUsingFrontCamera by remember { mutableStateOf(true) }
    var callDuration by remember { mutableIntStateOf(0) }
    var isConnecting by remember { mutableStateOf(true) }
    var showControls by remember { mutableStateOf(true) }
    
    // 新增：是否交换大小窗口内容（true时本地摄像头显示在大屏，对方头像显示在小窗口）
    var isSwapped by remember { mutableStateOf(false) }
    
    // 音效管理器
    val soundManager = remember { CallSoundManager(context) }
    
    // 释放音效资源
    DisposableEffect(Unit) {
        onDispose {
            soundManager.release()
        }
    }
    
    // 模拟通话计时
    LaunchedEffect(hasPermission) {
        if (hasPermission) {
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
    
    // 自动隐藏控制栏
    LaunchedEffect(showControls, isConnecting) {
        if (showControls && !isConnecting) {
            delay(5000)
            showControls = false
        }
    }
    
    if (hasPermission) {
        VideoCallContent(
            contactName = contactName,
            isConnecting = isConnecting,
            callDuration = callDuration,
            isMuted = isMuted,
            isSpeakerOn = isSpeakerOn,
            isCameraOn = isCameraOn,
            isUsingFrontCamera = isUsingFrontCamera,
            showControls = showControls,
            isSwapped = isSwapped,
            onToggleControls = { showControls = !showControls },
            onMuteToggle = { isMuted = !isMuted },
            onSpeakerToggle = { isSpeakerOn = !isSpeakerOn },
            onCameraToggle = { isCameraOn = !isCameraOn },
            onSwitchCamera = { isUsingFrontCamera = !isUsingFrontCamera },
            onSwapViews = { isSwapped = !isSwapped },
            onEndCall = handleEndCall
        )
    }
}

@Composable
private fun VideoCallContent(
    contactName: String,
    isConnecting: Boolean,
    callDuration: Int,
    isMuted: Boolean,
    isSpeakerOn: Boolean,
    isCameraOn: Boolean,
    isUsingFrontCamera: Boolean,
    showControls: Boolean,
    isSwapped: Boolean,
    onToggleControls: () -> Unit,
    onMuteToggle: () -> Unit,
    onSpeakerToggle: () -> Unit,
    onCameraToggle: () -> Unit,
    onSwitchCamera: () -> Unit,
    onSwapViews: () -> Unit,
    onEndCall: () -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onToggleControls() }
    ) {
        val density = LocalDensity.current
        val maxWidthPx = with(density) { maxWidth.toPx() }
        val maxHeightPx = with(density) { maxHeight.toPx() }
        
        // 小窗口尺寸
        val smallWindowWidth = 120.dp
        val smallWindowHeight = 170.dp
        val smallWindowWidthPx = with(density) { smallWindowWidth.toPx() }
        val smallWindowHeightPx = with(density) { smallWindowHeight.toPx() }
        val padding = with(density) { 16.dp.toPx() }
        val topPadding = with(density) { 100.dp.toPx() }
        
        // 大屏内容：默认显示对方画面（模拟），交换后显示我的摄像头
        if (isSwapped) {
            // 交换后：大屏显示我的摄像头
            if (isCameraOn) {
                CameraPreview(
                    isUsingFrontCamera = isUsingFrontCamera,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                CameraOffPlaceholder(name = "我")
            }
        } else {
            // 默认：大屏显示对方画面（模拟）
            RemoteVideoPlaceholder(
                name = contactName,
                modifier = Modifier.fillMaxSize()
            )
        }
        
        // 顶部信息栏
        AnimatedVisibility(
            visible = showControls || isConnecting,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically(),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            TopInfoBar(
                contactName = contactName,
                isConnecting = isConnecting,
                callDuration = callDuration
            )
        }
        
        // 正在呼叫动画
        if (isConnecting) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CallingAnimation()
            }
        }
        
        // 可拖动的小窗口 - 通话接通后显示
        if (!isConnecting) {
            DraggableSmallVideoPreview(
                contactName = contactName,
                isCameraOn = isCameraOn,
                isUsingFrontCamera = isUsingFrontCamera,
                isSwapped = isSwapped,
                windowWidth = smallWindowWidth,
                windowHeight = smallWindowHeight,
                maxWidthPx = maxWidthPx,
                maxHeightPx = maxHeightPx,
                initialX = maxWidthPx - smallWindowWidthPx - padding,
                initialY = topPadding,
                padding = padding,
                onSwapClick = onSwapViews
            )
        }
        
        // 底部控制栏
        AnimatedVisibility(
            visible = showControls || isConnecting,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            BottomControlBar(
                isMuted = isMuted,
                isSpeakerOn = isSpeakerOn,
                isCameraOn = isCameraOn,
                onMuteToggle = onMuteToggle,
                onSpeakerToggle = onSpeakerToggle,
                onCameraToggle = onCameraToggle,
                onSwitchCamera = onSwitchCamera,
                onEndCall = onEndCall
            )
        }
    }
}

@Composable
private fun CameraPreview(
    isUsingFrontCamera: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }
    
    val previewView = remember {
        PreviewView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }
    
    LaunchedEffect(isUsingFrontCamera) {
        val provider = suspendCoroutine<ProcessCameraProvider> { continuation ->
            ProcessCameraProvider.getInstance(context).apply {
                addListener({
                    continuation.resume(get())
                }, ContextCompat.getMainExecutor(context))
            }
        }
        
        cameraProvider = provider
        
        val cameraSelector = if (isUsingFrontCamera) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }
        
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }
        
        try {
            provider.unbindAll()
            provider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    DisposableEffect(Unit) {
        onDispose {
            cameraProvider?.unbindAll()
        }
    }
    
    AndroidView(
        factory = { previewView },
        modifier = modifier
    )
}

@Composable
private fun TopInfoBar(
    contactName: String,
    isConnecting: Boolean,
    callDuration: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.7f),
                        Color.Transparent
                    )
                )
            )
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = contactName,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = if (isConnecting) "视频通话呼叫中..." else formatDuration(callDuration),
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun CallingAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "calling")
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "callingScale"
    )
    
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "callingAlpha"
    )
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .scale(scale)
                .alpha(alpha)
                .clip(CircleShape)
                .border(3.dp, Color(0xFF4CAF50), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Videocam,
                contentDescription = null,
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(48.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // 连接动画点
        ConnectingDotsVideo()
    }
}

@Composable
private fun ConnectingDotsVideo() {
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
                .size(12.dp)
                .alpha(dot1Alpha)
                .clip(CircleShape)
                .background(Color(0xFF4CAF50))
        )
        Box(
            modifier = Modifier
                .size(12.dp)
                .alpha(dot2Alpha)
                .clip(CircleShape)
                .background(Color(0xFF4CAF50))
        )
        Box(
            modifier = Modifier
                .size(12.dp)
                .alpha(dot3Alpha)
                .clip(CircleShape)
                .background(Color(0xFF4CAF50))
        )
    }
}

// 可拖动的小视频预览窗口
@Composable
private fun DraggableSmallVideoPreview(
    contactName: String,
    isCameraOn: Boolean,
    isUsingFrontCamera: Boolean,
    isSwapped: Boolean,
    windowWidth: androidx.compose.ui.unit.Dp,
    windowHeight: androidx.compose.ui.unit.Dp,
    maxWidthPx: Float,
    maxHeightPx: Float,
    initialX: Float,
    initialY: Float,
    padding: Float,
    onSwapClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val windowWidthPx = with(density) { windowWidth.toPx() }
    val windowHeightPx = with(density) { windowHeight.toPx() }
    
    // 记住当前位置
    var offsetX by remember { mutableFloatStateOf(initialX) }
    var offsetY by remember { mutableFloatStateOf(initialY) }
    
    // 用于吸附动画
    val animatedOffsetX = remember { Animatable(initialX) }
    val animatedOffsetY = remember { Animatable(initialY) }
    
    // 是否正在拖动
    var isDragging by remember { mutableStateOf(false) }
    
    Box(
        modifier = Modifier
            .offset {
                IntOffset(
                    if (isDragging) offsetX.roundToInt() else animatedOffsetX.value.roundToInt(),
                    if (isDragging) offsetY.roundToInt() else animatedOffsetY.value.roundToInt()
                )
            }
            .size(windowWidth, windowHeight)
            .shadow(8.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1A1A2E))
            .border(2.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { isDragging = true },
                    onDragEnd = {
                        isDragging = false
                        // 计算吸附位置
                        val centerX = offsetX + windowWidthPx / 2
                        val targetX = if (centerX < maxWidthPx / 2) {
                            padding // 吸附到左边
                        } else {
                            maxWidthPx - windowWidthPx - padding // 吸附到右边
                        }
                        // 限制Y轴范围
                        val targetY = offsetY.coerceIn(padding, maxHeightPx - windowHeightPx - padding)
                        
                        scope.launch {
                            launch {
                                animatedOffsetX.snapTo(offsetX)
                                animatedOffsetX.animateTo(
                                    targetX,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessMedium
                                    )
                                )
                                offsetX = targetX
                            }
                            launch {
                                animatedOffsetY.snapTo(offsetY)
                                animatedOffsetY.animateTo(
                                    targetY,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessMedium
                                    )
                                )
                                offsetY = targetY
                            }
                        }
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        offsetX = (offsetX + dragAmount.x).coerceIn(0f, maxWidthPx - windowWidthPx)
                        offsetY = (offsetY + dragAmount.y).coerceIn(0f, maxHeightPx - windowHeightPx)
                    }
                )
            }
            .clickable { onSwapClick() }
            .zIndex(10f)
    ) {
        // 小窗口内容：默认显示我的摄像头，交换后显示对方头像
        if (isSwapped) {
            // 交换后：小窗显示对方头像
            RemoteVideoPlaceholderContent(name = contactName)
        } else {
            // 默认：小窗显示我的摄像头
            if (isCameraOn) {
                CameraPreview(
                    isUsingFrontCamera = isUsingFrontCamera,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                SmallCameraOffPlaceholder()
            }
        }
        
        // 切换提示图标
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(4.dp)
                .size(24.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.SwapHoriz,
                contentDescription = "点击切换",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

// 对方视频占位符（全屏）
@Composable
private fun RemoteVideoPlaceholder(
    name: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A1A2E),
                        Color(0xFF16213E),
                        Color(0xFF0F3460)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        RemoteVideoPlaceholderContent(name = name)
    }
}

// 对方视频占位符内容（复用）
@Composable
private fun RemoteVideoPlaceholderContent(name: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
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
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

// 摄像头关闭时的全屏占位符
@Composable
private fun CameraOffPlaceholder(name: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A1A2E),
                        Color(0xFF16213E),
                        Color(0xFF0F3460)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "摄像头已关闭",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}

// 小窗口摄像头关闭占位符
@Composable
private fun SmallCameraOffPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2A2A3E)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.VideocamOff,
            contentDescription = "摄像头已关闭",
            tint = Color.White.copy(alpha = 0.5f),
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
private fun BottomControlBar(
    isMuted: Boolean,
    isSpeakerOn: Boolean,
    isCameraOn: Boolean,
    onMuteToggle: () -> Unit,
    onSpeakerToggle: () -> Unit,
    onCameraToggle: () -> Unit,
    onSwitchCamera: () -> Unit,
    onEndCall: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Black.copy(alpha = 0.8f)
                    )
                )
            )
            .navigationBarsPadding()
            .padding(vertical = 24.dp, horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 上排控制按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // 静音
                VideoCallControlButton(
                    icon = if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                    label = if (isMuted) "取消静音" else "静音",
                    isActive = isMuted,
                    onClick = onMuteToggle
                )
                
                // 扬声器/听筒
                VideoCallControlButton(
                    icon = if (isSpeakerOn) Icons.AutoMirrored.Filled.VolumeUp else Icons.Default.Hearing,
                    label = if (isSpeakerOn) "听筒" else "扬声器",
                    isActive = !isSpeakerOn,
                    onClick = onSpeakerToggle
                )
                
                // 摄像头开关
                VideoCallControlButton(
                    icon = if (isCameraOn) Icons.Default.Videocam else Icons.Default.VideocamOff,
                    label = if (isCameraOn) "关闭摄像头" else "打开摄像头",
                    isActive = !isCameraOn,
                    onClick = onCameraToggle
                )
                
                // 切换摄像头
                VideoCallControlButton(
                    icon = Icons.Default.Cameraswitch,
                    label = "切换摄像头",
                    isActive = false,
                    onClick = onSwitchCamera
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // 挂断按钮
            VideoEndCallButton(onClick = onEndCall)
        }
    }
}

@Composable
private fun VideoCallControlButton(
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
                .size(52.dp)
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
                tint = if (isActive) Color.Black else Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(6.dp))
        
        Text(
            text = label,
            fontSize = 10.sp,
            color = Color.White.copy(alpha = 0.7f),
            maxLines = 1
        )
    }
}

@Composable
private fun VideoEndCallButton(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // 波纹动画
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
                .size(72.dp)
                .scale(rippleScale)
                .alpha(rippleAlpha)
                .clip(CircleShape)
                .background(Color(0xFFE53935))
        )
        
        // 挂断按钮
        Box(
            modifier = Modifier
                .size(72.dp)
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
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

private fun formatDuration(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}

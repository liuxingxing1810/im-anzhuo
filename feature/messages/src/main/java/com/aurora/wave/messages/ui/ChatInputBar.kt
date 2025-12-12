package com.aurora.wave.messages.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.ContactPage
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 底部面板类型
 */
enum class BottomPanelType {
    NONE,       // 无面板
    EMOJI,      // 表情面板
    EXTENSION,  // 扩展功能面板
    VOICE       // 语音输入模式
}

@Composable
fun ChatInputBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSendClick: () -> Unit,
    onVoiceClick: () -> Unit,
    onEmojiClick: () -> Unit,
    onAttachClick: () -> Unit,
    onCameraClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSending: Boolean = false,
    onPanelStateChange: (Boolean) -> Unit = {},  // 面板状态变化回调：true=打开, false=关闭
    onVoiceRecordStart: () -> Unit = {},
    onVoiceRecordStop: () -> Unit = {}
) {
    val hasText = text.isNotBlank()
    
    // 面板状态
    var currentPanel by remember { mutableStateOf(BottomPanelType.NONE) }
    
    // 语音模式状态
    var isVoiceMode by remember { mutableStateOf(false) }
    
    // 键盘控制器
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    
    // 检测键盘高度来判断键盘是否打开
    val density = LocalDensity.current
    val imeHeight = WindowInsets.ime.getBottom(density)
    val isKeyboardVisible = imeHeight > 0
    
    // 键盘打开时，关闭面板并切换到文字模式
    LaunchedEffect(isKeyboardVisible) {
        if (isKeyboardVisible && currentPanel != BottomPanelType.NONE) {
            currentPanel = BottomPanelType.NONE
        }
        if (isKeyboardVisible && isVoiceMode) {
            isVoiceMode = false
        }
    }
    
    // 通知面板状态变化
    LaunchedEffect(currentPanel) {
        onPanelStateChange(currentPanel != BottomPanelType.NONE)
    }
    
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // 输入栏
            InputRow(
                text = text,
                onTextChange = onTextChange,
                hasText = hasText,
                isSending = isSending,
                currentPanel = currentPanel,
                isVoiceMode = isVoiceMode,
                onVoiceClick = {
                    // 切换语音/键盘模式
                    isVoiceMode = !isVoiceMode
                    if (isVoiceMode) {
                        // 切换到语音模式时，隐藏键盘和面板
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        currentPanel = BottomPanelType.NONE
                    }
                },
                onVoiceRecordStart = {
                    currentPanel = BottomPanelType.VOICE
                    onVoiceRecordStart()
                },
                onVoiceRecordStop = {
                    currentPanel = BottomPanelType.NONE
                    onVoiceRecordStop()
                },
                onEmojiClick = {
                    if (currentPanel == BottomPanelType.EMOJI) {
                        currentPanel = BottomPanelType.NONE
                    } else {
                        // 打开表情面板前，先隐藏键盘
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        isVoiceMode = false
                        currentPanel = BottomPanelType.EMOJI
                    }
                },
                onAttachClick = {
                    if (currentPanel == BottomPanelType.EXTENSION) {
                        currentPanel = BottomPanelType.NONE
                    } else {
                        // 打开扩展面板前，先隐藏键盘
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        currentPanel = BottomPanelType.EXTENSION
                    }
                },
                onSendClick = onSendClick
            )
            
            // 表情面板
            AnimatedVisibility(
                visible = currentPanel == BottomPanelType.EMOJI,
                enter = expandVertically(
                    animationSpec = tween(250),
                    expandFrom = Alignment.Top
                ) + fadeIn(animationSpec = tween(200)),
                exit = shrinkVertically(
                    animationSpec = tween(200),
                    shrinkTowards = Alignment.Top
                ) + fadeOut(animationSpec = tween(150))
            ) {
                EmojiPanel(
                    onEmojiClick = { emoji ->
                        onTextChange(text + emoji)
                    }
                )
            }
            
            // 扩展功能面板
            AnimatedVisibility(
                visible = currentPanel == BottomPanelType.EXTENSION,
                enter = expandVertically(
                    animationSpec = tween(250),
                    expandFrom = Alignment.Top
                ) + fadeIn(animationSpec = tween(200)),
                exit = shrinkVertically(
                    animationSpec = tween(200),
                    shrinkTowards = Alignment.Top
                ) + fadeOut(animationSpec = tween(150))
            ) {
                ExtensionPanel(
                    onPhotoClick = { /* TODO */ },
                    onCameraClick = onCameraClick,
                    onVideoCallClick = { /* TODO */ },
                    onVoiceCallClick = { /* TODO */ },
                    onLocationClick = { /* TODO */ },
                    onRedPacketClick = { /* TODO */ },
                    onGiftClick = { /* TODO */ },
                    onTransferClick = { /* TODO */ },
                    onVoiceInputClick = { /* TODO */ },
                    onFavoriteClick = { /* TODO */ },
                    onContactClick = { /* TODO */ },
                    onFileClick = { /* TODO */ },
                    onMusicClick = { /* TODO */ }
                )
            }
        }
    }
}

@Composable
private fun InputRow(
    text: String,
    onTextChange: (String) -> Unit,
    hasText: Boolean,
    isSending: Boolean,
    currentPanel: BottomPanelType,
    isVoiceMode: Boolean,
    onVoiceClick: () -> Unit,
    onVoiceRecordStart: () -> Unit,
    onVoiceRecordStop: () -> Unit,
    onEmojiClick: () -> Unit,
    onAttachClick: () -> Unit,
    onSendClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Voice/Keyboard toggle button
        IconButton(
            onClick = onVoiceClick,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = if (isVoiceMode) Icons.Default.Keyboard else Icons.Default.Mic,
                contentDescription = if (isVoiceMode) "Switch to keyboard" else "Switch to voice",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        // Input field or Voice record button
        if (isVoiceMode) {
            // 语音录制按钮
            VoiceRecordButton(
                isRecording = currentPanel == BottomPanelType.VOICE,
                onStartRecording = onVoiceRecordStart,
                onStopRecording = onVoiceRecordStop,
                modifier = Modifier.weight(1f)
            )
        } else {
            // 文字输入框
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp),
                shape = RoundedCornerShape(6.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHighest
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicTextField(
                        value = text,
                        onValueChange = onTextChange,
                        modifier = Modifier.weight(1f),
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize
                        ),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        decorationBox = { innerTextField ->
                            Box(
                                contentAlignment = Alignment.CenterStart
                            ) {
                                if (text.isEmpty()) {
                                    Text(
                                        text = "",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                    )
                                }
                                innerTextField()
                            }
                        },
                        maxLines = 4
                    )
                }
            }
        }
        
        // Emoji button - 切换显示键盘或表情图标
        IconButton(
            onClick = onEmojiClick,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = if (currentPanel == BottomPanelType.EMOJI) {
                    Icons.Default.Keyboard
                } else {
                    Icons.Default.EmojiEmotions
                },
                contentDescription = "Emoji",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        // Add/Send button
        if (hasText && !isVoiceMode) {
            // Send button
            IconButton(
                onClick = onSendClick,
                enabled = !isSending && hasText,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    modifier = Modifier.size(20.dp)
                )
            }
        } else {
            // Add button
            IconButton(
                onClick = onAttachClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "More",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * 表情面板
 */
@Composable
private fun EmojiPanel(
    onEmojiClick: (String) -> Unit
) {
    val emojis = listOf(
        "😀", "😃", "😄", "😁", "😅", "😂", "🤣", "😊",
        "😇", "🙂", "🙃", "😉", "😌", "😍", "🥰", "😘",
        "😗", "😙", "😚", "😋", "😛", "😜", "🤪", "😝",
        "🤑", "🤗", "🤭", "🤫", "🤔", "🤐", "🤨", "😐",
        "😑", "😶", "😏", "😒", "🙄", "😬", "🤥", "😌",
        "😔", "😪", "🤤", "😴", "😷", "🤒", "🤕", "🤢",
        "🤮", "🤧", "🥵", "🥶", "🥴", "😵", "🤯", "🤠",
        "🥳", "🥸", "😎", "🤓", "🧐", "😕", "😟", "🙁",
        "☹️", "😮", "😯", "😲", "😳", "🥺", "😦", "😧",
        "😨", "😰", "😥", "😢", "😭", "😱", "😖", "😣",
        "😞", "😓", "😩", "😫", "🥱", "😤", "😡", "😠",
        "🤬", "😈", "👿", "💀", "☠️", "💩", "🤡", "👹",
        "👺", "👻", "👽", "👾", "🤖", "😺", "😸", "😹",
        "😻", "😼", "😽", "🙀", "😿", "😾", "🙈", "🙉",
        "🙊", "💋", "💌", "💘", "💝", "💖", "💗", "💓",
        "💞", "💕", "💟", "❣️", "💔", "❤️", "🧡", "💛",
        "💚", "💙", "💜", "🤎", "🖤", "🤍", "💯", "💢",
        "💥", "💫", "💦", "💨", "🕳️", "💣", "💬", "👁️‍🗨️",
        "🗨️", "🗯️", "💭", "💤", "👋", "🤚", "🖐️", "✋",
        "🖖", "👌", "🤌", "🤏", "✌️", "🤞", "🤟", "🤘"
    )
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .padding(top = 8.dp)
    ) {
        // Emoji grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(8),
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(horizontal = 8.dp),
            contentPadding = PaddingValues(4.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(emojis) { emoji ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onEmojiClick(emoji) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = emoji,
                        fontSize = 24.sp
                    )
                }
            }
        }
    }
}

/**
 * 扩展功能面板
 */
@Composable
private fun ExtensionPanel(
    onPhotoClick: () -> Unit,
    onCameraClick: () -> Unit,
    onVideoCallClick: () -> Unit,
    onVoiceCallClick: () -> Unit,
    onLocationClick: () -> Unit,
    onRedPacketClick: () -> Unit,
    onGiftClick: () -> Unit,
    onTransferClick: () -> Unit,
    onVoiceInputClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onContactClick: () -> Unit,
    onFileClick: () -> Unit,
    onMusicClick: () -> Unit
) {
    // 第一页功能
    val page1Items = listOf(
        ExtensionItem(Icons.Default.Image, "相册", onPhotoClick),
        ExtensionItem(Icons.Default.CameraAlt, "拍摄", onCameraClick),
        ExtensionItem(Icons.Default.Videocam, "视频通话", onVideoCallClick),
        ExtensionItem(Icons.Default.Phone, "语音通话", onVoiceCallClick),
        ExtensionItem(Icons.Default.LocationOn, "位置", onLocationClick),
        ExtensionItem(Icons.Default.Redeem, "红包", onRedPacketClick),
        ExtensionItem(Icons.Default.CardGiftcard, "礼物", onGiftClick),
        ExtensionItem(Icons.Default.SwapHoriz, "转账", onTransferClick)
    )
    
    // 第二页功能
    val page2Items = listOf(
        ExtensionItem(Icons.Default.Mic, "语音输入", onVoiceInputClick),
        ExtensionItem(Icons.Default.Folder, "收藏", onFavoriteClick),
        ExtensionItem(Icons.Default.ContactPage, "个人名片", onContactClick),
        ExtensionItem(Icons.Default.Folder, "文件", onFileClick),
        ExtensionItem(Icons.Default.MusicNote, "音乐", onMusicClick)
    )
    
    val pages = listOf(page1Items, page2Items)
    val scrollState = rememberScrollState()
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val pageWidthPx = with(androidx.compose.ui.platform.LocalDensity.current) { screenWidth.dp.toPx() }
    val coroutineScope = rememberCoroutineScope()
    
    // 记录上一页位置，用于判断滑动方向
    var lastPage by remember { mutableIntStateOf(0) }
    
    // 计算当前页面 (四分之一阈值)
    val threshold = pageWidthPx / 4
    val currentPage = when {
        scrollState.value < threshold -> 0
        scrollState.value > pageWidthPx - threshold -> 1
        scrollState.value > lastPage * pageWidthPx -> 1 // 向右滑，超过1/4就切换
        else -> 0 // 向左滑，超过1/4就切换
    }
    
    // 滑动停止后自动吸附到目标页面
    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.isScrollInProgress }
            .distinctUntilChanged()
            .filter { !it } // 滑动停止时
            .collect {
                // 根据滑动距离判断目标页面（四分之一阈值）
                val targetPage = when {
                    scrollState.value < threshold -> 0
                    scrollState.value > pageWidthPx - threshold -> 1
                    scrollState.value > lastPage * pageWidthPx -> 1
                    else -> 0
                }
                lastPage = targetPage
                val targetScroll = (targetPage * pageWidthPx).toInt()
                if (scrollState.value != targetScroll) {
                    scrollState.animateScrollTo(
                        targetScroll,
                        animationSpec = tween(durationMillis = 200, easing = androidx.compose.animation.core.FastOutSlowInEasing)
                    )
                }
            }
    }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .padding(vertical = 16.dp)
    ) {
        // 使用 horizontalScroll 实现左右滑动
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .horizontalScroll(scrollState)
        ) {
            pages.forEach { pageItems ->
                Box(
                    modifier = Modifier.width(screenWidth.dp)
                ) {
                    ExtensionGrid(items = pageItems)
                }
            }
        }
        
        // 页面指示器
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pages.size) { index ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(
                            if (currentPage == index) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                            }
                        )
                )
            }
        }
    }
}

/**
 * 扩展功能网格
 */
@Composable
private fun ExtensionGrid(
    items: List<ExtensionItem>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 第一行 4个
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items.take(4).forEach { item ->
                ExtensionButton(
                    icon = item.icon,
                    label = item.label,
                    onClick = item.onClick
                )
            }
            // 填充空白
            repeat(4 - minOf(4, items.size)) {
                Spacer(modifier = Modifier.size(60.dp))
            }
        }
        
        // 第二行
        if (items.size > 4) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                items.drop(4).take(4).forEach { item ->
                    ExtensionButton(
                        icon = item.icon,
                        label = item.label,
                        onClick = item.onClick
                    )
                }
                // 填充空白
                repeat(4 - minOf(4, items.size - 4)) {
                    Spacer(modifier = Modifier.size(60.dp))
                }
            }
        }
    }
}

/**
 * 扩展功能按钮
 */
@Composable
private fun ExtensionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(60.dp)
            .clickable(onClick = onClick)
    ) {
        // 图标背景
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Spacer(modifier = Modifier.height(6.dp))
        
        // 标签
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 11.sp
        )
    }
}

/**
 * 扩展功能项数据类
 */
private data class ExtensionItem(
    val icon: ImageVector,
    val label: String,
    val onClick: () -> Unit
)

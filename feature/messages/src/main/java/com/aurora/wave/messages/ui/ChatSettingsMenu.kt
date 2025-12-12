package com.aurora.wave.messages.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VerticalAlignTop
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

/**
 * 聊天设置菜单操作类型
 */
enum class ChatSettingsAction {
    SEARCH_CHAT,        // 查找聊天记录
    MUTE_NOTIFICATIONS, // 消息免打扰
    PIN_CHAT,           // 置顶聊天
    SET_BACKGROUND,     // 设置聊天背景
    CLEAR_CHAT,         // 清空聊天记录
    REPORT,             // 投诉举报
    BLOCK               // 加入黑名单
}

/**
 * 聊天设置菜单
 * 从聊天详情页右上角更多按钮弹出
 */
@Composable
fun ChatSettingsMenu(
    isVisible: Boolean,
    isMuted: Boolean,
    isPinned: Boolean,
    onDismiss: () -> Unit,
    onAction: (ChatSettingsAction) -> Unit,
    onMuteChange: (Boolean) -> Unit,
    onPinChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.9f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "menu_scale"
    )
    
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(150),
        label = "menu_alpha"
    )
    
    if (isVisible) {
        Popup(
            alignment = Alignment.TopEnd,
            onDismissRequest = onDismiss,
            properties = PopupProperties(
                focusable = true,
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures { onDismiss() }
                    }
            ) {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(150)) + scaleIn(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        ),
                        initialScale = 0.8f,
                        transformOrigin = TransformOrigin(1f, 0f) // 从右上角展开
                    ),
                    exit = fadeOut(tween(100)) + scaleOut(
                        animationSpec = tween(100),
                        targetScale = 0.9f,
                        transformOrigin = TransformOrigin(1f, 0f)
                    ),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 56.dp, end = 8.dp)
                ) {
                    Surface(
                        modifier = modifier
                            .width(220.dp)
                            .pointerInput(Unit) {
                                detectTapGestures { }
                            },
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = 8.dp,
                        shadowElevation = 8.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            // 查找聊天记录
                            ChatSettingsMenuItem(
                                icon = Icons.Default.Search,
                                title = "查找聊天记录",
                                onClick = {
                                    onAction(ChatSettingsAction.SEARCH_CHAT)
                                    onDismiss()
                                }
                            )
                            
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                            )
                            
                            // 消息免打扰
                            ChatSettingsToggleItem(
                                icon = if (isMuted) Icons.Default.NotificationsOff else Icons.Default.Notifications,
                                title = "消息免打扰",
                                isChecked = isMuted,
                                onCheckedChange = onMuteChange
                            )
                            
                            // 置顶聊天
                            ChatSettingsToggleItem(
                                icon = Icons.Default.PushPin,
                                title = "置顶聊天",
                                isChecked = isPinned,
                                onCheckedChange = onPinChange
                            )
                            
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                            )
                            
                            // 设置聊天背景
                            ChatSettingsMenuItem(
                                icon = Icons.Default.Image,
                                title = "设置聊天背景",
                                onClick = {
                                    onAction(ChatSettingsAction.SET_BACKGROUND)
                                    onDismiss()
                                }
                            )
                            
                            // 清空聊天记录
                            ChatSettingsMenuItem(
                                icon = Icons.Default.Delete,
                                title = "清空聊天记录",
                                tint = MaterialTheme.colorScheme.error,
                                onClick = {
                                    onAction(ChatSettingsAction.CLEAR_CHAT)
                                    onDismiss()
                                }
                            )
                            
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                            )
                            
                            // 投诉举报
                            ChatSettingsMenuItem(
                                icon = Icons.Default.Flag,
                                title = "投诉举报",
                                onClick = {
                                    onAction(ChatSettingsAction.REPORT)
                                    onDismiss()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * 聊天设置菜单项
 */
@Composable
private fun ChatSettingsMenuItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    tint: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(22.dp),
            tint = tint
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = tint
        )
    }
}

/**
 * 聊天设置切换项（带开关）
 */
@Composable
private fun ChatSettingsToggleItem(
    icon: ImageVector,
    title: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!isChecked) }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(22.dp),
            tint = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            )
        )
    }
}

/**
 * 清空聊天记录确认对话框
 */
@Composable
fun ClearChatDialog(
    isVisible: Boolean,
    contactName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (isVisible) {
        Popup(
            alignment = Alignment.Center,
            onDismissRequest = onDismiss,
            properties = PopupProperties(
                focusable = true,
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .pointerInput(Unit) {
                        detectTapGestures { onDismiss() }
                    },
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier
                        .width(300.dp)
                        .pointerInput(Unit) {
                            detectTapGestures { }
                        },
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "清空聊天记录",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "确定要清空与「$contactName」的聊天记录吗？此操作不可恢复。",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // 取消按钮
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable(onClick = onDismiss),
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Box(
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "取消",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            
                            // 确认按钮
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable(onClick = {
                                        onConfirm()
                                        onDismiss()
                                    }),
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.error
                            ) {
                                Box(
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "清空",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.onError
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * 聊天背景选择器
 */
@Composable
fun ChatBackgroundPicker(
    isVisible: Boolean,
    currentBackground: Int,
    onSelectBackground: (Int) -> Unit,
    onSelectCustom: () -> Unit,
    onDismiss: () -> Unit
) {
    // 预设背景
    val presetBackgrounds = listOf(
        0 to "默认",
        1 to "星空",
        2 to "森林",
        3 to "海洋",
        4 to "简约白",
        5 to "深邃黑"
    )
    
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(tween(200)) + slideInVertically { it },
        exit = fadeOut(tween(150)) + slideOutVertically { it }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .pointerInput(Unit) {
                    detectTapGestures { onDismiss() }
                },
            contentAlignment = Alignment.BottomCenter
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectTapGestures { }
                    },
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // 标题
                    Text(
                        text = "选择聊天背景",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    // 预设背景网格
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        presetBackgrounds.chunked(3).forEach { row ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                row.forEach { (id, name) ->
                                    BackgroundPreviewCard(
                                        id = id,
                                        name = name,
                                        isSelected = currentBackground == id,
                                        onClick = {
                                            onSelectBackground(id)
                                            onDismiss()
                                        },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                // 填充空白
                                repeat(3 - row.size) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // 自定义背景选项
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = {
                                onSelectCustom()
                                onDismiss()
                            }),
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "从相册选择",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

/**
 * 背景预览卡片
 */
@Composable
private fun BackgroundPreviewCard(
    id: Int,
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when (id) {
        0 -> MaterialTheme.colorScheme.surfaceVariant
        1 -> Color(0xFF1A237E) // 星空 - 深蓝
        2 -> Color(0xFF2E7D32) // 森林 - 绿色
        3 -> Color(0xFF0288D1) // 海洋 - 蓝色
        4 -> Color(0xFFFAFAFA) // 简约白
        5 -> Color(0xFF212121) // 深邃黑
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "card_scale"
    )
    
    Surface(
        modifier = modifier
            .scale(scale)
            .height(80.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        color = backgroundColor,
        border = if (isSelected) {
            androidx.compose.foundation.BorderStroke(
                2.dp,
                MaterialTheme.colorScheme.primary
            )
        } else null,
        tonalElevation = if (isSelected) 4.dp else 0.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.labelSmall,
                color = if (id == 4) Color.Black else Color.White,
                fontSize = 10.sp
            )
        }
    }
}

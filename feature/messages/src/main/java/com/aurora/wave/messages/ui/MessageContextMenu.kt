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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Forward
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

/**
 * 消息操作类型
 */
enum class MessageAction {
    COPY,       // 复制
    FORWARD,    // 转发
    REPLY,      // 回复
    QUOTE,      // 引用
    FAVORITE,   // 收藏
    RECALL,     // 撤回（仅自己的消息）
    DELETE,     // 删除
    MORE        // 更多
}

/**
 * 消息操作数据类
 */
data class MessageActionItem(
    val action: MessageAction,
    val icon: ImageVector,
    val label: String,
    val tint: Color? = null
)

/**
 * 消息长按上下文菜单
 */
@Composable
fun MessageContextMenu(
    isVisible: Boolean,
    isOutgoing: Boolean,
    canRecall: Boolean,
    onDismiss: () -> Unit,
    onAction: (MessageAction) -> Unit,
    modifier: Modifier = Modifier
) {
    // 根据是否是自己发送的消息，决定显示哪些操作
    val actions = buildList {
        add(MessageActionItem(MessageAction.COPY, Icons.Default.ContentCopy, "复制"))
        add(MessageActionItem(MessageAction.FORWARD, Icons.Default.Forward, "转发"))
        add(MessageActionItem(MessageAction.REPLY, Icons.Default.Reply, "回复"))
        add(MessageActionItem(MessageAction.QUOTE, Icons.Default.FormatQuote, "引用"))
        add(MessageActionItem(MessageAction.FAVORITE, Icons.Default.Star, "收藏"))
        if (isOutgoing && canRecall) {
            add(MessageActionItem(MessageAction.RECALL, Icons.Default.Undo, "撤回"))
        }
        add(MessageActionItem(
            MessageAction.DELETE,
            Icons.Default.Delete,
            "删除",
            MaterialTheme.colorScheme.error
        ))
    }
    
    if (isVisible) {
        Popup(
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
                    .background(Color.Black.copy(alpha = 0.3f))
                    .pointerInput(Unit) {
                        detectTapGestures { onDismiss() }
                    },
                contentAlignment = Alignment.Center
            ) {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(150)) + scaleIn(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        ),
                        initialScale = 0.8f,
                        transformOrigin = TransformOrigin(0.5f, 0.5f)
                    ),
                    exit = fadeOut(tween(100)) + scaleOut(
                        animationSpec = tween(100),
                        targetScale = 0.9f
                    )
                ) {
                    Surface(
                        modifier = modifier
                            .pointerInput(Unit) {
                                // 阻止点击穿透
                                detectTapGestures { }
                            },
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = 8.dp,
                        shadowElevation = 8.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            actions.forEach { item ->
                                MessageActionButton(
                                    item = item,
                                    onClick = {
                                        onAction(item.action)
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
}

/**
 * 消息操作按钮
 */
@Composable
private fun MessageActionButton(
    item: MessageActionItem,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.label,
            modifier = Modifier.size(22.dp),
            tint = item.tint ?: MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = item.label,
            style = MaterialTheme.typography.labelSmall,
            color = item.tint ?: MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 10.sp
        )
    }
}

/**
 * 消息菜单浮层 - 带气泡预览
 * 类似微信的消息操作菜单，在消息上方显示操作选项
 */
@Composable
fun MessageMenuPopup(
    isVisible: Boolean,
    isOutgoing: Boolean,
    canRecall: Boolean,
    onDismiss: () -> Unit,
    onAction: (MessageAction) -> Unit,
    messageContent: @Composable () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.95f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "menu_scale"
    )
    
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(150),
        label = "menu_alpha"
    )
    
    // 根据是否是自己发送的消息，决定显示哪些操作
    val actions = buildList {
        add(MessageActionItem(MessageAction.COPY, Icons.Default.ContentCopy, "复制"))
        add(MessageActionItem(MessageAction.FORWARD, Icons.Default.Forward, "转发"))
        add(MessageActionItem(MessageAction.REPLY, Icons.Default.Reply, "回复"))
        add(MessageActionItem(MessageAction.QUOTE, Icons.Default.FormatQuote, "引用"))
        add(MessageActionItem(MessageAction.FAVORITE, Icons.Default.Star, "收藏"))
        if (isOutgoing && canRecall) {
            add(MessageActionItem(MessageAction.RECALL, Icons.Default.Undo, "撤回"))
        }
        add(MessageActionItem(
            MessageAction.DELETE,
            Icons.Default.Delete,
            "删除",
            MaterialTheme.colorScheme.error
        ))
    }
    
    if (isVisible) {
        Popup(
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
                    .background(Color.Black.copy(alpha = 0.5f * alpha))
                    .pointerInput(Unit) {
                        detectTapGestures { onDismiss() }
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .scale(scale)
                        .alpha(alpha)
                        .pointerInput(Unit) {
                            detectTapGestures { }
                        },
                    horizontalAlignment = if (isOutgoing) Alignment.End else Alignment.Start
                ) {
                    // 操作菜单
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = 8.dp,
                        shadowElevation = 8.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            actions.forEach { item ->
                                MessageActionButton(
                                    item = item,
                                    onClick = {
                                        onAction(item.action)
                                        onDismiss()
                                    }
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // 消息预览
                    Box(
                        modifier = Modifier.padding(horizontal = 12.dp)
                    ) {
                        messageContent()
                    }
                }
            }
        }
    }
}

package com.aurora.wave.messages.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aurora.wave.data.model.ConversationType
import com.aurora.wave.messages.model.ConversationUiModel
import kotlin.math.roundToInt

// 滑动菜单宽度常量
private val ACTION_BUTTON_WIDTH = 72.dp
private val TOTAL_ACTION_WIDTH = 144.dp // 两个按钮

/**
 * 可滑动会话列表项 - 类似 Telegram 左滑菜单
 * 左滑显示：置顶、删除
 * 
 * @param isExpanded 外部控制的展开状态
 * @param onExpandChange 展开状态变化回调
 */
@Composable
fun SwipeableConversationItem(
    conversation: ConversationUiModel,
    isExpanded: Boolean,
    onExpandChange: (Boolean) -> Unit,
    onClick: () -> Unit,
    onPinClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 最大滑动距离（像素）
    val density = androidx.compose.ui.platform.LocalDensity.current
    val maxSwipeDistance = with(density) { TOTAL_ACTION_WIDTH.toPx() }
    
    // 内部滑动偏移量（用于拖动时）
    var dragOffset by remember { mutableFloatStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }
    
    // 目标偏移量：展开时为 -maxSwipeDistance，否则为 0
    val targetOffset = if (isExpanded) -maxSwipeDistance else 0f
    
    // 动画效果：非拖动时使用动画
    val animatedOffsetX by animateFloatAsState(
        targetValue = if (isDragging) dragOffset else targetOffset,
        animationSpec = tween(durationMillis = 200),
        label = "offsetX"
    )
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(76.dp)
    ) {
        // 背景操作按钮（固定在右侧）
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 置顶按钮
            Box(
                modifier = Modifier
                    .width(ACTION_BUTTON_WIDTH)
                    .fillMaxHeight()
                    .background(
                        if (conversation.isPinned) Color(0xFFFF9800) else Color(0xFF2196F3)
                    )
                    .clickable { onPinClick() },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PushPin,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (conversation.isPinned) "取消" else "置顶",
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 11.sp
                    )
                }
            }
            
            // 删除按钮
            Box(
                modifier = Modifier
                    .width(ACTION_BUTTON_WIDTH)
                    .fillMaxHeight()
                    .background(Color(0xFFF44336))
                    .clickable { onDeleteClick() },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "删除",
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 11.sp
                    )
                }
            }
        }
        
        // 前景内容（可滑动）- 必须有背景色才能遮住后面的按钮
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(animatedOffsetX.roundToInt(), 0) }
                .background(MaterialTheme.colorScheme.surface)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragStart = {
                            isDragging = true
                            // 初始化拖动位置
                            dragOffset = if (isExpanded) -maxSwipeDistance else 0f
                        },
                        onDragEnd = {
                            isDragging = false
                            // 松手时决定是展开还是收回
                            val shouldExpand = dragOffset < -maxSwipeDistance / 2
                            onExpandChange(shouldExpand)
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            // 限制在 [-maxSwipeDistance, 0] 范围内
                            dragOffset = (dragOffset + dragAmount).coerceIn(-maxSwipeDistance, 0f)
                        }
                    )
                }
        ) {
            ConversationItemContent(
                conversation = conversation,
                onClick = onClick
            )
        }
    }
}

/**
 * 现代化会话列表项内容 - 参考 Telegram/WhatsApp 设计
 */
@Composable
private fun ConversationItemContent(
    conversation: ConversationUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = tween(100),
        label = "scale"
    )
    val backgroundColor by animateColorAsState(
        targetValue = if (conversation.isPinned) {
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f)
        } else {
            MaterialTheme.colorScheme.surface
        },
        animationSpec = tween(200),
        label = "bg"
    )
    
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                ),
                onClick = onClick
            ),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 头像区域（包含未读角标）
            Box {
                // 现代化头像
                ModernAvatarInternal(
                    name = conversation.name,
                    isGroup = conversation.type == ConversationType.GROUP || 
                             conversation.type == ConversationType.CHANNEL,
                    isOnline = conversation.isOnline,
                    avatarUrl = conversation.avatarUrl
                )
                
                // 未读角标 - 在头像右上角
                if (conversation.unreadCount > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 4.dp, y = (-4).dp)
                    ) {
                        ModernUnreadBadge(
                            count = conversation.unreadCount,
                            isMuted = conversation.isMuted
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.width(14.dp))
            
            // 内容区域
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // 第一行：名称 + 状态图标 + 时间
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = conversation.name,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = if (conversation.unreadCount > 0) 
                                    FontWeight.SemiBold else FontWeight.Medium
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        
                        // 置顶图标
                        if (conversation.isPinned) {
                            Icon(
                                imageVector = Icons.Default.PushPin,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(start = 4.dp)
                                    .size(14.dp),
                                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                            )
                        }
                        
                        // 静音图标
                        if (conversation.isMuted) {
                            Icon(
                                imageVector = Icons.Default.NotificationsOff,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(start = 4.dp)
                                    .size(14.dp),
                                tint = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                    
                    // 时间
                    Text(
                        text = conversation.timestamp,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (conversation.unreadCount > 0) 
                            MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outline
                    )
                }
                
                // 第二行：消息预览
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 发送状态图标（仅自己发送的消息）
                    if (conversation.lastMessageFromMe) {
                        MessageStatusIcon(
                            isRead = conversation.lastMessageRead,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }
                    
                    val messagePreview = conversation.draft?.let { 
                        "[草稿] $it" 
                    } ?: conversation.lastMessage
                    
                    Text(
                        text = messagePreview,
                        style = MaterialTheme.typography.bodyMedium,
                        color = when {
                            conversation.draft != null -> MaterialTheme.colorScheme.error
                            conversation.unreadCount > 0 -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            else -> MaterialTheme.colorScheme.outline
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

/**
 * 现代化会话列表项 - 参考 Telegram/WhatsApp 设计
 * @deprecated 使用 SwipeableConversationItem 代替
 */
@Composable
fun ConversationItem(
    conversation: ConversationUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ConversationItemContent(
        conversation = conversation,
        onClick = onClick,
        modifier = modifier
    )
}

/**
 * 现代化头像 - 渐变色背景
 */
@Composable
private fun ModernAvatarInternal(
    name: String,
    isGroup: Boolean,
    isOnline: Boolean,
    avatarUrl: String?,
    size: Int = 52
) {
    val gradientColors = remember(name) {
        getAvatarGradientColors(name)
    }
    
    Box {
        // 头像主体
        Box(
            modifier = Modifier
                .size(size.dp)
                .shadow(3.dp, RoundedCornerShape((size * 0.22f).dp))
                .clip(RoundedCornerShape((size * 0.22f).dp))
                .background(brush = Brush.linearGradient(gradientColors)),
            contentAlignment = Alignment.Center
        ) {
            if (avatarUrl == null) {
                if (isGroup) {
                    Icon(
                        imageVector = Icons.Filled.Group,
                        contentDescription = null,
                        modifier = Modifier.size((size * 0.5f).dp),
                        tint = Color.White
                    )
                } else {
                    Text(
                        text = name.firstOrNull()?.uppercase() ?: "?",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = (size * 0.38f).sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )
                }
            }
        }
        
        // 在线指示器
        if (isOnline && !isGroup) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 1.dp, y = 1.dp)
                    .size(15.dp)
                    .shadow(2.dp, CircleShape)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4CAF50))
            )
        }
    }
}

/**
 * 消息发送状态图标
 */
@Composable
private fun MessageStatusIcon(
    isRead: Boolean,
    modifier: Modifier = Modifier
) {
    val tint = if (isRead) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outline
    }
    
    Row(modifier = modifier) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = tint
        )
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            modifier = Modifier
                .size(14.dp)
                .offset(x = (-6).dp),
            tint = tint
        )
    }
}

/**
 * 现代化未读徽章
 */
@Composable
private fun ModernUnreadBadge(
    count: Int,
    isMuted: Boolean
) {
    val displayCount = when {
        count > 99 -> "99+"
        else -> count.toString()
    }
    
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isMuted) {
            MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        } else {
            MaterialTheme.colorScheme.error
        },
        shadowElevation = 2.dp
    ) {
        Text(
            text = displayCount,
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            ),
            color = Color.White
        )
    }
}

/**
 * 生成头像渐变色
 */
private fun getAvatarGradientColors(name: String): List<Color> {
    val hash = name.hashCode()
    val gradients = listOf(
        listOf(Color(0xFF667EEA), Color(0xFF764BA2)), // 紫蓝
        listOf(Color(0xFF11998E), Color(0xFF38EF7D)), // 青绿
        listOf(Color(0xFFFC466B), Color(0xFF3F5EFB)), // 粉紫
        listOf(Color(0xFFF093FB), Color(0xFFF5576C)), // 粉红
        listOf(Color(0xFF4FACFE), Color(0xFF00F2FE)), // 蓝青
        listOf(Color(0xFF43E97B), Color(0xFF38F9D7)), // 绿色
        listOf(Color(0xFFFA709A), Color(0xFFFEE140)), // 粉黄
        listOf(Color(0xFF30CFD0), Color(0xFF330867)), // 青紫
        listOf(Color(0xFFFF6B6B), Color(0xFFFFE66D)), // 橙红
        listOf(Color(0xFF6B73FF), Color(0xFF000DFF)), // 深蓝
    )
    return gradients[kotlin.math.abs(hash) % gradients.size]
}

// 为兼容保留旧接口
@Composable
fun AvatarWithIndicator(
    avatarUrl: String?,
    isGroup: Boolean,
    isOnline: Boolean,
    name: String,
    modifier: Modifier = Modifier
) {
    ModernAvatarInternal(
        name = name,
        isGroup = isGroup,
        isOnline = isOnline,
        avatarUrl = avatarUrl
    )
}

@Composable
fun UnreadBadge(
    count: Int,
    isMuted: Boolean,
    modifier: Modifier = Modifier
) {
    ModernUnreadBadge(count = count, isMuted = isMuted)
}

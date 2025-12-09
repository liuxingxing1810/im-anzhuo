package com.aurora.wave.design.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 现代化头像组件 - 支持渐变背景和在线状态
 */
@Composable
fun ModernAvatar(
    name: String,
    size: Dp = 52.dp,
    isOnline: Boolean = false,
    isGroup: Boolean = false,
    avatarUrl: String? = null,
    modifier: Modifier = Modifier
) {
    val gradientColors = remember(name) {
        getAvatarGradient(name)
    }
    
    Box(modifier = modifier) {
        // 头像主体 - 渐变背景
        Box(
            modifier = Modifier
                .size(size)
                .shadow(4.dp, RoundedCornerShape(size * 0.22f))
                .clip(RoundedCornerShape(size * 0.22f))
                .background(
                    brush = Brush.linearGradient(gradientColors)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (avatarUrl == null) {
                if (isGroup) {
                    Icon(
                        imageVector = Icons.Filled.Group,
                        contentDescription = null,
                        modifier = Modifier.size(size * 0.5f),
                        tint = Color.White
                    )
                } else {
                    Text(
                        text = name.firstOrNull()?.uppercase() ?: "?",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = (size.value * 0.4f).sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )
                }
            }
            // TODO: Coil image loading
        }
        
        // 在线状态指示器
        if (isOnline && !isGroup) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 2.dp, y = 2.dp)
                    .size(size * 0.28f)
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
 * 根据名称生成渐变色
 */
private fun getAvatarGradient(name: String): List<Color> {
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
    )
    return gradients[kotlin.math.abs(hash) % gradients.size]
}

/**
 * 未读消息徽章 - 现代化设计
 */
@Composable
fun ModernBadge(
    count: Int,
    isMuted: Boolean = false,
    modifier: Modifier = Modifier
) {
    val displayCount = when {
        count > 99 -> "99+"
        count > 0 -> count.toString()
        else -> return
    }
    
    val backgroundColor = if (isMuted) {
        MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)
    } else {
        MaterialTheme.colorScheme.error
    }
    
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = backgroundColor,
        shadowElevation = 2.dp
    ) {
        Text(
            text = displayCount,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            ),
            color = Color.White
        )
    }
}

/**
 * 现代化列表项卡片
 */
@Composable
fun ModernListCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = tween(100),
        label = "scale"
    )
    
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                onClick = onClick
            ),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        content()
    }
}

/**
 * 现代化悬浮按钮
 */
@Composable
fun ModernFAB(
    onClick: () -> Unit,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primary
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = tween(100),
        label = "fab_scale"
    )
    
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
            .scale(scale)
            .shadow(8.dp, CircleShape),
        containerColor = containerColor,
        contentColor = Color.White,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 6.dp,
            pressedElevation = 12.dp
        ),
        interactionSource = interactionSource
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
    }
}

/**
 * 现代化搜索栏
 */
@Composable
fun ModernSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String = "搜索",
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(40.dp),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f),
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            leadingIcon?.invoke()
            
            androidx.compose.foundation.text.BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = if (leadingIcon != null) 8.dp else 0.dp),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Box {
                        if (query.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
    }
}

/**
 * 分组标题
 */
@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.SemiBold
    )
}

/**
 * 设置项组件
 */
@Composable
fun SettingsItem(
    title: String,
    subtitle: String? = null,
    icon: ImageVector,
    iconTint: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit,
    trailing: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    ModernListCard(
        modifier = modifier,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 图标容器
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconTint.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp),
                    tint = iconTint
                )
            }
            
            Spacer(modifier = Modifier.width(14.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            
            trailing?.invoke()
        }
    }
}

/**
 * 消息发送状态指示器
 */
@Composable
fun MessageStatusIndicator(
    isSent: Boolean,
    isDelivered: Boolean,
    isRead: Boolean,
    modifier: Modifier = Modifier
) {
    val tint = when {
        isRead -> MaterialTheme.colorScheme.primary
        isDelivered -> MaterialTheme.colorScheme.outline
        isSent -> MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        else -> Color.Transparent
    }
    
    if (isSent) {
        Row(modifier = modifier) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = tint
            )
            if (isDelivered) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier
                        .size(14.dp)
                        .offset(x = (-8).dp),
                    tint = tint
                )
            }
        }
    }
}

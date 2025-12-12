package com.aurora.wave.discover

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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

/**
 * 评论数据模型
 */
data class MomentComment(
    val id: String,
    val authorId: String,
    val authorName: String,
    val content: String,
    val timestamp: String,
    val replyToName: String? = null
)

/**
 * 朋友圈互动面板 - 点赞和评论按钮
 */
@Composable
fun MomentInteractionPanel(
    isVisible: Boolean,
    isLiked: Boolean,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "panel_scale"
    )
    
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(tween(100)) + scaleIn(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            ),
            initialScale = 0.8f,
            transformOrigin = TransformOrigin(1f, 0.5f)
        ),
        exit = fadeOut(tween(100)) + scaleOut(
            animationSpec = tween(100),
            targetScale = 0.9f,
            transformOrigin = TransformOrigin(1f, 0.5f)
        ),
        modifier = modifier
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.inverseSurface,
            tonalElevation = 4.dp
        ) {
            Row(
                modifier = Modifier.padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 点赞按钮
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .clickable {
                            onLikeClick()
                            onDismiss()
                        }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "点赞",
                        modifier = Modifier.size(18.dp),
                        tint = if (isLiked) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.inverseOnSurface
                        }
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isLiked) "取消" else "赞",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.inverseOnSurface
                    )
                }
                
                // 分隔线
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(20.dp)
                        .background(MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.3f))
                )
                
                // 评论按钮
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .clickable {
                            onCommentClick()
                            onDismiss()
                        }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "评论",
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.inverseOnSurface
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "评论",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.inverseOnSurface
                    )
                }
            }
        }
    }
}

/**
 * 评论输入框
 */
@Composable
fun CommentInputBar(
    isVisible: Boolean,
    replyToName: String?,
    onSendComment: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var commentText by remember { mutableStateOf("") }
    
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically { it } + fadeIn(),
        exit = slideOutVertically { it } + fadeOut(),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
                .pointerInput(Unit) {
                    detectTapGestures { onDismiss() }
                }
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .imePadding()
                    .pointerInput(Unit) {
                        detectTapGestures { }
                    },
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 输入框
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerHighest
                    ) {
                        BasicTextField(
                            value = commentText,
                            onValueChange = { commentText = it },
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            textStyle = TextStyle(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 14.sp
                            ),
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                            decorationBox = { innerTextField ->
                                Box(
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    if (commentText.isEmpty()) {
                                        Text(
                                            text = replyToName?.let { "回复 $it" } ?: "发表评论...",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                        )
                                    }
                                    innerTextField()
                                }
                            },
                            singleLine = true
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    // 表情按钮
                    IconButton(
                        onClick = { /* TODO: 打开表情面板 */ },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEmotions,
                            contentDescription = "表情",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    // 发送按钮
                    IconButton(
                        onClick = {
                            if (commentText.isNotBlank()) {
                                onSendComment(commentText)
                                commentText = ""
                                onDismiss()
                            }
                        },
                        enabled = commentText.isNotBlank(),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "发送",
                            tint = if (commentText.isNotBlank()) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * 点赞用户列表
 */
@Composable
fun LikesList(
    likes: List<String>,
    modifier: Modifier = Modifier
) {
    if (likes.isNotEmpty()) {
        Surface(
            modifier = modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            shape = RoundedCornerShape(4.dp)
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.error
                )
                
                Spacer(modifier = Modifier.width(4.dp))
                
                Text(
                    text = likes.joinToString(", "),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

/**
 * 评论列表
 */
@Composable
fun CommentsList(
    comments: List<MomentComment>,
    onCommentClick: (MomentComment) -> Unit,
    modifier: Modifier = Modifier
) {
    if (comments.isNotEmpty()) {
        Surface(
            modifier = modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            shape = RoundedCornerShape(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                comments.forEachIndexed { index, comment ->
                    CommentItem(
                        comment = comment,
                        onClick = { onCommentClick(comment) }
                    )
                    
                    if (index < comments.lastIndex) {
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}

/**
 * 评论项
 */
@Composable
private fun CommentItem(
    comment: MomentComment,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Text(
            text = buildString {
                append(comment.authorName)
                if (comment.replyToName != null) {
                    append(" 回复 ")
                    append(comment.replyToName)
                }
                append("：")
            },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium
        )
        
        Text(
            text = comment.content,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f, fill = false)
        )
    }
}

/**
 * 朋友圈详情底部栏
 */
@Composable
fun MomentDetailBottomBar(
    isLiked: Boolean,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hapticFeedback = LocalHapticFeedback.current
    
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 点赞按钮
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    onLikeClick()
                }
            ) {
                val likeScale by animateFloatAsState(
                    targetValue = if (isLiked) 1.2f else 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    label = "like_scale"
                )
                
                Icon(
                    imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "点赞",
                    modifier = Modifier
                        .size(24.dp)
                        .scale(likeScale),
                    tint = if (isLiked) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
                
                Text(
                    text = if (isLiked) "已赞" else "赞",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isLiked) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
            
            // 评论按钮
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable(onClick = onCommentClick)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "评论",
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Text(
                    text = "评论",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // 更多按钮
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable(onClick = onShareClick)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreHoriz,
                    contentDescription = "更多",
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Text(
                    text = "更多",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

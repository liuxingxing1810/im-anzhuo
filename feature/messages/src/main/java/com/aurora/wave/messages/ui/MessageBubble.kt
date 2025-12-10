package com.aurora.wave.messages.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aurora.wave.data.model.DeliveryStatus
import com.aurora.wave.data.model.MessageContent
import com.aurora.wave.data.model.MessageType
import com.aurora.wave.messages.model.MessageUiModel

@Composable
fun MessageBubble(
    message: MessageUiModel,
    onAvatarClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val isOutgoing = message.isOutgoing
    
    // 计算最大气泡宽度：屏幕宽度 - 头像(40dp) - 间距(8dp) - 两侧边距(24dp) ≈ 70%屏幕宽度
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val maxBubbleWidth = screenWidth * 0.7f
    
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = if (isOutgoing) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        // 左侧头像 (对方消息)
        if (!isOutgoing) {
            MessageAvatar(
                initial = message.senderName?.firstOrNull()?.toString() ?: "?",
                isOutgoing = false,
                onClick = { message.senderId?.let { onAvatarClick(it) } }
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        
        Column(
            horizontalAlignment = if (isOutgoing) Alignment.End else Alignment.Start,
            modifier = Modifier.widthIn(max = maxBubbleWidth)
        ) {
            // Sender name for group chats (incoming messages only)
            if (!isOutgoing && message.isFirstInGroup && message.senderName != null) {
                Text(
                    text = message.senderName,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)
                )
            }
            
            Surface(
                shape = bubbleShape(isOutgoing, message.isFirstInGroup, message.isLastInGroup),
                color = if (isOutgoing) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                },
                tonalElevation = if (isOutgoing) 0.dp else 1.dp
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    // Message content based on type
                    when (val content = message.content) {
                        is MessageContent.Text -> TextMessageContent(
                            text = content.text,
                            isOutgoing = isOutgoing
                        )
                        is MessageContent.Image -> ImageMessageContent(
                            content = content,
                            isOutgoing = isOutgoing
                        )
                        is MessageContent.Voice -> VoiceMessageContent(
                            content = content,
                            isOutgoing = isOutgoing
                        )
                        is MessageContent.File -> FileMessageContent(
                            content = content,
                            isOutgoing = isOutgoing
                        )
                        is MessageContent.Location -> LocationMessageContent(
                            content = content,
                            isOutgoing = isOutgoing
                        )
                        else -> TextMessageContent(
                            text = "[Unsupported message]",
                            isOutgoing = isOutgoing
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Timestamp and status
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = message.timestamp,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isOutgoing) {
                                MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            }
                        )
                        
                        if (isOutgoing) {
                            Spacer(modifier = Modifier.width(4.dp))
                            DeliveryStatusIcon(
                                status = message.status,
                                tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        }
        
        // 右侧头像 (自己的消息)
        if (isOutgoing) {
            Spacer(modifier = Modifier.width(8.dp))
            MessageAvatar(
                initial = "我",
                isOutgoing = true,
                onClick = { /* 自己的头像暂不处理 */ }
            )
        }
    }
}

/**
 * 消息头像组件
 */
@Composable
private fun MessageAvatar(
    initial: String,
    isOutgoing: Boolean,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(4.dp))
            .clickable(onClick = onClick)
            .background(
                if (isOutgoing) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.tertiaryContainer
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initial,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = if (isOutgoing) {
                MaterialTheme.colorScheme.onPrimaryContainer
            } else {
                MaterialTheme.colorScheme.onTertiaryContainer
            }
        )
    }
}

@Composable
private fun TextMessageContent(
    text: String,
    isOutgoing: Boolean
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        color = if (isOutgoing) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }
    )
}

@Composable
private fun ImageMessageContent(
    content: MessageContent.Image,
    isOutgoing: Boolean
) {
    Column {
        // Image placeholder
        Box(
            modifier = Modifier
                .size(200.dp, 150.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    if (isOutgoing) {
                        MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f)
                    } else {
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Image,
                contentDescription = "Image",
                modifier = Modifier.size(48.dp),
                tint = if (isOutgoing) {
                    MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
                } else {
                    MaterialTheme.colorScheme.outline
                }
            )
        }
        
        content.caption?.let { caption ->
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = caption,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isOutgoing) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}

@Composable
private fun VoiceMessageContent(
    content: MessageContent.Voice,
    isOutgoing: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.width(180.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Mic,
            contentDescription = "Voice message",
            modifier = Modifier.size(24.dp),
            tint = if (isOutgoing) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.primary
            }
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        // Waveform placeholder
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            content.waveform?.take(12)?.forEach { amplitude ->
                Box(
                    modifier = Modifier
                        .width(3.dp)
                        .height((amplitude.coerceIn(2, 20)).dp)
                        .clip(RoundedCornerShape(1.dp))
                        .background(
                            if (isOutgoing) {
                                MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
                            } else {
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                            }
                        )
                )
            }
        }
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Text(
            text = formatDuration(content.durationSeconds),
            style = MaterialTheme.typography.labelMedium,
            color = if (isOutgoing) {
                MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
    }
}

@Composable
private fun FileMessageContent(
    content: MessageContent.File,
    isOutgoing: Boolean
) {
    // File message placeholder
    Text(
        text = "📎 ${content.fileName}",
        style = MaterialTheme.typography.bodyMedium,
        color = if (isOutgoing) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }
    )
}

@Composable
private fun LocationMessageContent(
    content: MessageContent.Location,
    isOutgoing: Boolean
) {
    Column {
        // Map placeholder
        Box(
            modifier = Modifier
                .size(180.dp, 100.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF90CAF9)),
            contentAlignment = Alignment.Center
        ) {
            Text("📍", style = MaterialTheme.typography.headlineLarge)
        }
        
        content.address?.let { address ->
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = address,
                style = MaterialTheme.typography.bodySmall,
                color = if (isOutgoing) {
                    MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}

@Composable
private fun DeliveryStatusIcon(
    status: DeliveryStatus,
    tint: Color
) {
    val (icon, contentDescription) = when (status) {
        DeliveryStatus.SENDING -> Icons.Default.Schedule to "Sending"
        DeliveryStatus.SENT -> Icons.Default.Check to "Sent"
        DeliveryStatus.DELIVERED -> Icons.Default.DoneAll to "Delivered"
        DeliveryStatus.READ -> Icons.Default.DoneAll to "Read"
        DeliveryStatus.FAILED -> Icons.Default.Error to "Failed"
    }
    
    Icon(
        imageVector = icon,
        contentDescription = contentDescription,
        modifier = Modifier.size(14.dp),
        tint = if (status == DeliveryStatus.READ) {
            Color(0xFF4FC3F7) // Light blue for read
        } else if (status == DeliveryStatus.FAILED) {
            MaterialTheme.colorScheme.error
        } else {
            tint
        }
    )
}

private fun bubbleShape(
    isOutgoing: Boolean,
    isFirstInGroup: Boolean,
    isLastInGroup: Boolean
): RoundedCornerShape {
    val largeRadius = 18.dp
    val smallRadius = 4.dp
    
    return if (isOutgoing) {
        RoundedCornerShape(
            topStart = largeRadius,
            topEnd = if (isFirstInGroup) largeRadius else smallRadius,
            bottomStart = largeRadius,
            bottomEnd = if (isLastInGroup) largeRadius else smallRadius
        )
    } else {
        RoundedCornerShape(
            topStart = if (isFirstInGroup) largeRadius else smallRadius,
            topEnd = largeRadius,
            bottomStart = if (isLastInGroup) largeRadius else smallRadius,
            bottomEnd = largeRadius
        )
    }
}

private fun formatDuration(seconds: Int): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return if (mins > 0) "$mins:${secs.toString().padStart(2, '0')}" else "0:${secs.toString().padStart(2, '0')}"
}

package com.aurora.wave.discover

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.aurora.wave.design.WhiteStatusBar

/**
 * 可见性选项
 */
enum class MomentVisibility {
    PUBLIC,     // 公开
    FRIENDS,    // 好友可见
    PARTIAL,    // 部分可见
    PRIVATE     // 仅自己可见
}

/**
 * 发布朋友圈页面
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PostMomentScreen(
    onBackClick: () -> Unit,
    onPublish: (String, List<String>, String?, MomentVisibility) -> Unit = { _, _, _, _ -> }
) {
    var content by remember { mutableStateOf("") }
    val selectedImages = remember { mutableStateListOf<String>() }
    var location by remember { mutableStateOf<String?>(null) }
    var visibility by remember { mutableStateOf(MomentVisibility.PUBLIC) }
    var showVisibilityPicker by remember { mutableStateOf(false) }
    
    val context = LocalContext.current
    val canPublish = content.isNotBlank() || selectedImages.isNotEmpty()
    
    WhiteStatusBar()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            if (canPublish) {
                                onPublish(content, selectedImages.toList(), location, visibility)
                                Toast.makeText(context, "发布成功", Toast.LENGTH_SHORT).show()
                                onBackClick()
                            }
                        },
                        enabled = canPublish
                    ) {
                        Text(
                            text = "发表",
                            fontWeight = FontWeight.Bold,
                            color = if (canPublish) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // 输入内容区域
            BasicTextField(
                value = content,
                onValueChange = { content = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 150.dp)
                    .padding(16.dp),
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                decorationBox = { innerTextField ->
                    Box {
                        if (content.isEmpty()) {
                            Text(
                                text = "这一刻的想法...",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                        }
                        innerTextField()
                    }
                }
            )
            
            // 已选图片预览
            if (selectedImages.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(selectedImages) { image ->
                        ImagePreviewItem(
                            onRemove = { selectedImages.remove(image) }
                        )
                    }
                    
                    // 添加更多图片
                    if (selectedImages.size < 9) {
                        item {
                            AddImageButton(
                                onClick = {
                                    // 模拟添加图片
                                    selectedImages.add("image_${selectedImages.size + 1}")
                                }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            
            // 功能选项列表
            Column {
                // 添加图片
                if (selectedImages.isEmpty()) {
                    OptionItem(
                        icon = Icons.Default.Image,
                        title = "图片",
                        subtitle = "添加图片或视频",
                        onClick = {
                            selectedImages.add("image_1")
                        }
                    )
                }
                
                // 位置
                OptionItem(
                    icon = Icons.Default.LocationOn,
                    title = "所在位置",
                    subtitle = location ?: "不显示位置",
                    onClick = {
                        // 模拟选择位置
                        location = if (location == null) "北京市朝阳区" else null
                    }
                )
                
                // 提醒谁看
                OptionItem(
                    icon = Icons.Default.Tag,
                    title = "提醒谁看",
                    subtitle = "选择朋友",
                    onClick = { /* TODO */ }
                )
                
                // 谁可以看
                OptionItem(
                    icon = when (visibility) {
                        MomentVisibility.PUBLIC -> Icons.Default.Public
                        MomentVisibility.FRIENDS -> Icons.Default.People
                        MomentVisibility.PARTIAL -> Icons.Default.Visibility
                        MomentVisibility.PRIVATE -> Icons.Default.VisibilityOff
                    },
                    title = "谁可以看",
                    subtitle = when (visibility) {
                        MomentVisibility.PUBLIC -> "公开"
                        MomentVisibility.FRIENDS -> "好友可见"
                        MomentVisibility.PARTIAL -> "部分可见"
                        MomentVisibility.PRIVATE -> "仅自己可见"
                    },
                    onClick = { showVisibilityPicker = true }
                )
            }
        }
    }
    
    // 可见性选择器
    VisibilityPickerPopup(
        isVisible = showVisibilityPicker,
        currentVisibility = visibility,
        onSelect = {
            visibility = it
            showVisibilityPicker = false
        },
        onDismiss = { showVisibilityPicker = false }
    )
}

/**
 * 图片预览项
 */
@Composable
private fun ImagePreviewItem(
    onRemove: () -> Unit
) {
    Box(
        modifier = Modifier.size(100.dp)
    ) {
        // 图片占位
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Image,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
        }
        
        // 删除按钮
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
                .size(24.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.6f))
                .clickable(onClick = onRemove),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "删除",
                modifier = Modifier.size(14.dp),
                tint = Color.White
            )
        }
    }
}

/**
 * 添加图片按钮
 */
@Composable
private fun AddImageButton(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "添加图片",
            modifier = Modifier.size(32.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        )
    }
}

/**
 * 功能选项项
 */
@Composable
private fun OptionItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * 可见性选择弹窗
 */
@Composable
private fun VisibilityPickerPopup(
    isVisible: Boolean,
    currentVisibility: MomentVisibility,
    onSelect: (MomentVisibility) -> Unit,
    onDismiss: () -> Unit
) {
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
                    Text(
                        text = "谁可以看",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    VisibilityOption(
                        icon = Icons.Default.Public,
                        title = "公开",
                        subtitle = "所有人可见",
                        isSelected = currentVisibility == MomentVisibility.PUBLIC,
                        onClick = { onSelect(MomentVisibility.PUBLIC) }
                    )
                    
                    VisibilityOption(
                        icon = Icons.Default.People,
                        title = "好友可见",
                        subtitle = "仅好友可见",
                        isSelected = currentVisibility == MomentVisibility.FRIENDS,
                        onClick = { onSelect(MomentVisibility.FRIENDS) }
                    )
                    
                    VisibilityOption(
                        icon = Icons.Default.Visibility,
                        title = "部分可见",
                        subtitle = "选择可见的好友",
                        isSelected = currentVisibility == MomentVisibility.PARTIAL,
                        onClick = { onSelect(MomentVisibility.PARTIAL) }
                    )
                    
                    VisibilityOption(
                        icon = Icons.Default.VisibilityOff,
                        title = "仅自己可见",
                        subtitle = "私密，仅自己可见",
                        isSelected = currentVisibility == MomentVisibility.PRIVATE,
                        onClick = { onSelect(MomentVisibility.PRIVATE) }
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

/**
 * 可见性选项
 */
@Composable
private fun VisibilityOption(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.02f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "option_scale"
    )
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) {
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        } else {
            Color.Transparent
        }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = if (isSelected) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

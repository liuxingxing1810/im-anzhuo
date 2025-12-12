package com.aurora.wave.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aurora.wave.design.WhiteStatusBar

/**
 * 个人资料编辑页面 - 微信风格
 * 
 * 特性：
 * - 头像编辑
 * - 昵称、微信号等信息编辑
 * - 二维码名片
 * - 更多信息（性别、地区、个性签名）
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBackClick: () -> Unit = {},
    onQrCodeClick: () -> Unit = {}
) {
    WhiteStatusBar()
    
    // Mock user data
    var nickname by remember { mutableStateOf("Aurora用户") }
    var waveId by remember { mutableStateOf("aurora_12345") }
    var gender by remember { mutableStateOf("男") }
    var region by remember { mutableStateOf("北京 海淀") }
    var signature by remember { mutableStateOf("这是我的个性签名") }
    
    var editingField by remember { mutableStateOf<String?>(null) }
    var tempValue by remember { mutableStateOf("") }
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "个人信息",
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            // 头像区域
            item {
                ProfileSection {
                    ProfileItem(
                        title = "头像",
                        onClick = { /* TODO: Change avatar */ },
                        trailing = {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF07C160).copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(36.dp),
                                    tint = Color(0xFF07C160)
                                )
                                // 相机图标
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surface),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.CameraAlt,
                                        contentDescription = "更换头像",
                                        modifier = Modifier.size(12.dp),
                                        tint = MaterialTheme.colorScheme.outline
                                    )
                                }
                            }
                        }
                    )
                }
            }
            
            item { Spacer(modifier = Modifier.height(8.dp)) }
            
            // 基本信息
            item {
                ProfileSection {
                    EditableProfileItem(
                        title = "昵称",
                        value = nickname,
                        isEditing = editingField == "nickname",
                        editValue = if (editingField == "nickname") tempValue else "",
                        onEditClick = {
                            editingField = "nickname"
                            tempValue = nickname
                        },
                        onValueChange = { tempValue = it },
                        onSave = {
                            nickname = tempValue
                            editingField = null
                        },
                        onCancel = { editingField = null }
                    )
                    
                    ProfileDivider()
                    
                    ProfileItem(
                        title = "Wave号",
                        value = waveId,
                        onClick = { /* Wave号不可修改 */ },
                        showArrow = false
                    )
                    
                    ProfileDivider()
                    
                    ProfileItem(
                        title = "二维码名片",
                        onClick = onQrCodeClick,
                        trailing = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.QrCode,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    tint = MaterialTheme.colorScheme.outline
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    )
                }
            }
            
            item { Spacer(modifier = Modifier.height(8.dp)) }
            
            // 更多信息
            item {
                ProfileSection {
                    ProfileItem(
                        title = "性别",
                        value = gender,
                        onClick = {
                            gender = if (gender == "男") "女" else "男"
                        }
                    )
                    
                    ProfileDivider()
                    
                    ProfileItem(
                        title = "地区",
                        value = region,
                        onClick = { /* TODO: Select region */ }
                    )
                    
                    ProfileDivider()
                    
                    EditableProfileItem(
                        title = "个性签名",
                        value = signature.ifBlank { "未填写" },
                        isEditing = editingField == "signature",
                        editValue = if (editingField == "signature") tempValue else "",
                        onEditClick = {
                            editingField = "signature"
                            tempValue = signature
                        },
                        onValueChange = { tempValue = it },
                        onSave = {
                            signature = tempValue
                            editingField = null
                        },
                        onCancel = { editingField = null }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileSection(
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column {
            content()
        }
    }
}

@Composable
private fun ProfileItem(
    title: String,
    value: String? = null,
    onClick: () -> Unit = {},
    showArrow: Boolean = true,
    trailing: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        if (trailing != null) {
            trailing()
        } else {
            if (value != null) {
                Text(
                    text = value,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.outline
                )
                
                if (showArrow) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
            
            if (showArrow) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun EditableProfileItem(
    title: String,
    value: String,
    isEditing: Boolean,
    editValue: String,
    onEditClick: () -> Unit,
    onValueChange: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = !isEditing, onClick = onEditClick)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            if (!isEditing) {
                Text(
                    text = value,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.outline
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        
        // 编辑模式
        AnimatedVisibility(
            visible = isEditing,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 12.dp)
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ) {
                    BasicTextField(
                        value = editValue,
                        onValueChange = onValueChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 15.sp
                        ),
                        cursorBrush = SolidColor(Color(0xFF07C160)),
                        decorationBox = { innerTextField ->
                            Box {
                                if (editValue.isEmpty()) {
                                    Text(
                                        text = "请输入$title",
                                        color = MaterialTheme.colorScheme.outline,
                                        fontSize = 15.sp
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onCancel) {
                        Text("取消", color = MaterialTheme.colorScheme.outline)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = onSave) {
                        Text("保存", color = Color(0xFF07C160))
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 16.dp),
        thickness = 0.5.dp,
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    )
}

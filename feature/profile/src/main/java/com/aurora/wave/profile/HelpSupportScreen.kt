package com.aurora.wave.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aurora.wave.design.WhiteStatusBar

/**
 * 帮助与反馈页面 - 微信风格
 * 
 * 特性：
 * - 常见问题 FAQ（可展开）
 * - 问题反馈入口
 * - 客服联系方式
 * - 搜索帮助内容
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpSupportScreen(
    onBackClick: () -> Unit = {}
) {
    WhiteStatusBar()
    
    var searchQuery by remember { mutableStateOf("") }
    var showFeedbackForm by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "帮助与反馈",
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
            // 搜索栏
            item {
                SearchHelpBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it }
                )
            }
            
            item { Spacer(modifier = Modifier.height(8.dp)) }
            
            // 快捷入口
            item {
                QuickActionsRow(
                    onFeedbackClick = { showFeedbackForm = true },
                    onBugReportClick = { /* TODO */ },
                    onContactClick = { /* TODO */ }
                )
            }
            
            item { Spacer(modifier = Modifier.height(8.dp)) }
            
            // 常见问题
            item {
                Text(
                    text = "常见问题",
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            
            items(generateFAQList()) { faq ->
                FAQItem(faq = faq)
            }
            
            // 反馈表单
            if (showFeedbackForm) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    FeedbackForm(
                        onSubmit = { showFeedbackForm = false },
                        onCancel = { showFeedbackForm = false }
                    )
                }
            }
            
            item { Spacer(modifier = Modifier.height(16.dp)) }
            
            // 联系我们
            item {
                ContactSection()
            }
        }
    }
}

/**
 * 搜索帮助栏
 */
@Composable
private fun SearchHelpBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(20.dp)
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.weight(1f),
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 15.sp
                ),
                singleLine = true,
                cursorBrush = SolidColor(Color(0xFF07C160)),
                decorationBox = { innerTextField ->
                    Box {
                        if (query.isEmpty()) {
                            Text(
                                text = "搜索帮助内容",
                                color = MaterialTheme.colorScheme.outline,
                                fontSize = 15.sp
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
 * 快捷入口行
 */
@Composable
private fun QuickActionsRow(
    onFeedbackClick: () -> Unit,
    onBugReportClick: () -> Unit,
    onContactClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            QuickActionItem(
                icon = Icons.Default.Feedback,
                label = "意见反馈",
                color = Color(0xFF07C160),
                onClick = onFeedbackClick
            )
            
            QuickActionItem(
                icon = Icons.Default.BugReport,
                label = "问题上报",
                color = Color(0xFFE53935),
                onClick = onBugReportClick
            )
            
            QuickActionItem(
                icon = Icons.Default.Headset,
                label = "在线客服",
                color = Color(0xFF1DA1F2),
                onClick = onContactClick
            )
        }
    }
}

/**
 * 快捷入口项
 */
@Composable
private fun QuickActionItem(
    icon: ImageVector,
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = label,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

/**
 * FAQ 项
 */
@Composable
private fun FAQItem(faq: FAQData) {
    var expanded by remember { mutableStateOf(false) }
    val rotationAngle by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "expand_rotation"
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.HelpOutline,
                    contentDescription = null,
                    tint = Color(0xFF07C160),
                    modifier = Modifier.size(20.dp)
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Text(
                    text = faq.question,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    modifier = Modifier.weight(1f)
                )
                
                Icon(
                    Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "收起" else "展开",
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                        .size(20.dp)
                        .rotate(rotationAngle)
                )
            }
            
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 48.dp, end = 16.dp, bottom = 16.dp)
                ) {
                    HorizontalDivider(
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text(
                        text = faq.answer,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    )
                }
            }
        }
    }
}

/**
 * 反馈表单
 */
@Composable
private fun FeedbackForm(
    onSubmit: () -> Unit,
    onCancel: () -> Unit
) {
    var feedback by remember { mutableStateOf("") }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "意见反馈",
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            OutlinedTextField(
                value = feedback,
                onValueChange = { feedback = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                placeholder = { Text("请描述您遇到的问题或建议...") },
                textStyle = TextStyle(fontSize = 15.sp)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onCancel,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text("取消")
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Button(
                    onClick = onSubmit,
                    enabled = feedback.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF07C160)
                    )
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Send,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("提交")
                }
            }
        }
    }
}

/**
 * 联系我们区域
 */
@Composable
private fun ContactSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = null,
                    tint = Color(0xFF1DA1F2),
                    modifier = Modifier.size(20.dp)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = "联系我们",
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            ContactRow(label = "客服热线", value = "400-123-4567")
            ContactRow(label = "工作时间", value = "周一至周五 9:00-18:00")
            ContactRow(label = "邮箱", value = "support@aurora-wave.com")
        }
    }
}

@Composable
private fun ContactRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.outline,
            fontSize = 14.sp,
            modifier = Modifier.width(80.dp)
        )
        Text(
            text = value,
            fontSize = 14.sp
        )
    }
}

// Data class
data class FAQData(
    val question: String,
    val answer: String
)

// Mock FAQ data
private fun generateFAQList(): List<FAQData> = listOf(
    FAQData(
        question = "如何修改我的个人信息？",
        answer = "进入「我」→ 点击头像区域 → 即可修改昵称、头像、个性签名等个人信息。"
    ),
    FAQData(
        question = "如何添加新朋友？",
        answer = "在「通讯录」页面点击右上角「+」按钮，可以通过搜索Wave号、扫描二维码、手机联系人等方式添加好友。"
    ),
    FAQData(
        question = "消息发送失败怎么办？",
        answer = "1. 检查网络连接是否正常\n2. 尝试切换网络（WiFi/移动数据）\n3. 重启应用后重试\n4. 如果问题持续，请联系客服"
    ),
    FAQData(
        question = "如何设置消息免打扰？",
        answer = "进入聊天 → 点击右上角「···」→ 打开「消息免打扰」开关即可。设置后该聊天的新消息将不会有声音和震动提醒。"
    ),
    FAQData(
        question = "如何清理缓存数据？",
        answer = "进入「我」→「设置」→「存储空间」→ 点击「清理缓存」按钮。这不会删除您的聊天记录和重要数据。"
    ),
    FAQData(
        question = "如何修改密码？",
        answer = "进入「我」→「设置」→「账号与安全」→「修改密码」，按提示操作即可完成密码修改。"
    )
)

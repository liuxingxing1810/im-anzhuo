package com.aurora.wave.connections

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
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aurora.wave.design.WhiteStatusBar

/**
 * 标签管理页面 - 微信风格
 * 
 * 特性：
 * - 可展开的标签项显示成员
 * - 创建/编辑标签
 * - 成员头像预览
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagsScreen(
    onBackClick: () -> Unit = {},
    onContactClick: (String) -> Unit = {}
) {
    WhiteStatusBar()
    
    // Mock data
    val tags = remember { mutableStateListOf(*generateMockTags().toTypedArray()) }
    var showCreateDialog by remember { mutableStateOf(false) }
    var editingTag by remember { mutableStateOf<TagItem?>(null) }
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "标签",
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = Color(0xFF07C160),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "新建标签")
            }
        }
    ) { padding ->
        if (tags.isEmpty()) {
            // 空状态
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Label,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "暂无标签",
                        color = MaterialTheme.colorScheme.outline,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "点击右下角按钮创建新标签",
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(tags, key = { it.id }) { tag ->
                    TagItemCard(
                        tag = tag,
                        onExpand = { expanded ->
                            val index = tags.indexOf(tag)
                            if (index >= 0) {
                                tags[index] = tag.copy(isExpanded = expanded)
                            }
                        },
                        onEdit = { editingTag = tag },
                        onContactClick = onContactClick
                    )
                }
            }
        }
    }
    
    // 创建标签对话框
    if (showCreateDialog) {
        CreateTagDialog(
            onDismiss = { showCreateDialog = false },
            onCreate = { name ->
                tags.add(
                    TagItem(
                        id = "tag_${System.currentTimeMillis()}",
                        name = name,
                        members = emptyList(),
                        isExpanded = false
                    )
                )
                showCreateDialog = false
            }
        )
    }
    
    // 编辑标签对话框
    editingTag?.let { tag ->
        EditTagDialog(
            tag = tag,
            onDismiss = { editingTag = null },
            onSave = { newName ->
                val index = tags.indexOf(tag)
                if (index >= 0) {
                    tags[index] = tag.copy(name = newName)
                }
                editingTag = null
            },
            onDelete = {
                tags.remove(tag)
                editingTag = null
            }
        )
    }
}

/**
 * 单个标签卡片
 */
@Composable
private fun TagItemCard(
    tag: TagItem,
    onExpand: (Boolean) -> Unit,
    onEdit: () -> Unit,
    onContactClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val rotationAngle by animateFloatAsState(
        targetValue = if (tag.isExpanded) 180f else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "expand_rotation"
    )
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            // 标签标题行
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onExpand(!tag.isExpanded) }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 标签图标
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF4B8BE5).copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Label,
                        contentDescription = null,
                        tint = Color(0xFF4B8BE5),
                        modifier = Modifier.size(20.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = tag.name,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "${tag.members.size} 位成员",
                        color = MaterialTheme.colorScheme.outline,
                        fontSize = 13.sp
                    )
                }
                
                IconButton(onClick = onEdit) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "编辑",
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(20.dp)
                    )
                }
                
                Icon(
                    Icons.Default.ExpandMore,
                    contentDescription = if (tag.isExpanded) "收起" else "展开",
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.rotate(rotationAngle)
                )
            }
            
            // 展开的成员列表
            AnimatedVisibility(
                visible = tag.isExpanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                MembersList(
                    members = tag.members,
                    onContactClick = onContactClick
                )
            }
        }
    }
}

/**
 * 成员列表
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MembersList(
    members: List<TagMember>,
    onContactClick: (String) -> Unit
) {
    if (members.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "暂无成员",
                color = MaterialTheme.colorScheme.outline,
                fontSize = 14.sp
            )
        }
    } else {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            members.forEach { member ->
                MemberChip(
                    member = member,
                    onClick = { onContactClick(member.id) }
                )
            }
        }
    }
}

/**
 * 成员头像+名称
 */
@Composable
private fun MemberChip(
    member: TagMember,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(56.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    Color(
                        (0xFF000000 + (member.name.hashCode() and 0xFFFFFF))
                            .toLong().and(0xFFFFFFFF).toInt()
                    ).copy(alpha = 0.3f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = member.name.first().toString(),
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = member.name,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * 创建标签对话框
 */
@Composable
private fun CreateTagDialog(
    onDismiss: () -> Unit,
    onCreate: (String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("新建标签") },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("标签名称") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                onClick = { if (name.isNotBlank()) onCreate(name.trim()) },
                enabled = name.isNotBlank()
            ) {
                Text("创建", color = Color(0xFF07C160))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}

/**
 * 编辑标签对话框
 */
@Composable
private fun EditTagDialog(
    tag: TagItem,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
    onDelete: () -> Unit
) {
    var name by remember { mutableStateOf(tag.name) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    
    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("删除标签") },
            text = { Text("确定要删除标签「${tag.name}」吗？") },
            confirmButton = {
                TextButton(onClick = onDelete) {
                    Text("删除", color = Color(0xFFE53935))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("取消")
                }
            }
        )
    } else {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("编辑标签") },
            text = {
                Column {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("标签名称") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextButton(
                        onClick = { showDeleteConfirm = true },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("删除标签", color = Color(0xFFE53935))
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { if (name.isNotBlank()) onSave(name.trim()) },
                    enabled = name.isNotBlank()
                ) {
                    Text("保存", color = Color(0xFF07C160))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("取消")
                }
            }
        )
    }
}

// Data classes
data class TagItem(
    val id: String,
    val name: String,
    val members: List<TagMember>,
    val isExpanded: Boolean = false
)

data class TagMember(
    val id: String,
    val name: String
)

// Mock data generator
private fun generateMockTags(): List<TagItem> = listOf(
    TagItem(
        id = "tag_1",
        name = "家人",
        members = listOf(
            TagMember("c1", "爸爸"),
            TagMember("c2", "妈妈"),
            TagMember("c3", "姐姐"),
            TagMember("c4", "弟弟")
        )
    ),
    TagItem(
        id = "tag_2",
        name = "同事",
        members = listOf(
            TagMember("c5", "张经理"),
            TagMember("c6", "李主管"),
            TagMember("c7", "王工程师"),
            TagMember("c8", "赵设计"),
            TagMember("c9", "钱前端"),
            TagMember("c10", "孙后端")
        )
    ),
    TagItem(
        id = "tag_3",
        name = "大学同学",
        members = listOf(
            TagMember("c11", "小明"),
            TagMember("c12", "小红"),
            TagMember("c13", "小刚")
        )
    ),
    TagItem(
        id = "tag_4",
        name = "游泳群友",
        members = listOf(
            TagMember("c14", "教练老王"),
            TagMember("c15", "蛙泳达人")
        )
    ),
    TagItem(
        id = "tag_5",
        name = "客户",
        members = emptyList()
    )
)

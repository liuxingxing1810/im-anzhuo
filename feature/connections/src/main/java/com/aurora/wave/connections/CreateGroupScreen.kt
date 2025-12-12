package com.aurora.wave.connections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aurora.wave.connections.data.FakeContactsProvider
import com.aurora.wave.connections.model.ContactUiModel
import com.aurora.wave.design.WhiteStatusBar

/**
 * 发起群聊页面
 * 选择联系人创建新群聊
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGroupScreen(
    onBackClick: () -> Unit,
    onCreateGroup: (List<String>) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var contacts by remember { mutableStateOf<List<ContactUiModel>>(emptyList()) }
    val selectedContacts = remember { mutableStateListOf<ContactUiModel>() }
    
    // 加载联系人
    LaunchedEffect(Unit) {
        contacts = FakeContactsProvider.generateContacts()
    }
    
    // 过滤后的联系人
    val filteredContacts = remember(searchQuery, contacts) {
        if (searchQuery.isBlank()) {
            contacts
        } else {
            contacts.filter { 
                it.name.contains(searchQuery, ignoreCase = true) 
            }
        }
    }
    
    // 分组后的联系人
    val groupedContacts = remember(filteredContacts) {
        FakeContactsProvider.groupContactsByLetter(filteredContacts)
    }
    
    WhiteStatusBar()
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "发起群聊",
                        fontWeight = FontWeight.Medium,
                        fontSize = 17.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                actions = {
                    val enabled = selectedContacts.size >= 2
                    Button(
                        onClick = { 
                            if (enabled) {
                                onCreateGroup(selectedContacts.map { it.id })
                            }
                        },
                        enabled = enabled,
                        modifier = Modifier.padding(end = 8.dp),
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = if (selectedContacts.isEmpty()) "完成" else "完成(${selectedContacts.size})",
                            fontSize = 14.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // 搜索框
            SearchContactBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            
            // 已选择的联系人
            AnimatedVisibility(
                visible = selectedContacts.isNotEmpty(),
                enter = fadeIn() + scaleIn(initialScale = 0.9f),
                exit = fadeOut() + scaleOut(targetScale = 0.9f)
            ) {
                SelectedContactsRow(
                    contacts = selectedContacts.toList(),
                    onRemove = { contact -> selectedContacts.remove(contact) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            
            // 提示文字
            if (selectedContacts.size < 2) {
                Text(
                    text = "至少选择2位好友创建群聊",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            
            // 联系人列表
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                groupedContacts.forEach { (letter, contactsInGroup) ->
                    item(key = "header_$letter") {
                        SectionHeader(letter = letter)
                    }
                    
                    items(
                        items = contactsInGroup,
                        key = { it.id }
                    ) { contact ->
                        val isSelected = selectedContacts.contains(contact)
                        SelectableContactItem(
                            contact = contact,
                            isSelected = isSelected,
                            onSelect = {
                                if (isSelected) {
                                    selectedContacts.remove(contact)
                                } else {
                                    selectedContacts.add(contact)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * 搜索框
 */
@Composable
private fun SearchContactBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.weight(1f),
                textStyle = TextStyle(
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                decorationBox = { innerTextField ->
                    Box {
                        if (query.isEmpty()) {
                            Text(
                                text = "搜索联系人",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 15.sp
                            )
                        }
                        innerTextField()
                    }
                }
            )
            
            if (query.isNotEmpty()) {
                IconButton(
                    onClick = { onQueryChange("") },
                    modifier = Modifier.size(18.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "清除",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

/**
 * 已选联系人横向列表
 */
@Composable
private fun SelectedContactsRow(
    contacts: List<ContactUiModel>,
    onRemove: (ContactUiModel) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(contacts, key = { it.id }) { contact ->
            SelectedContactChip(
                contact = contact,
                onRemove = { onRemove(contact) }
            )
        }
    }
}

/**
 * 已选联系人头像
 */
@Composable
private fun SelectedContactChip(
    contact: ContactUiModel,
    onRemove: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .animateContentSize()
            .clickable(onClick = onRemove)
    ) {
        Box {
            // 头像
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = contact.name.firstOrNull()?.toString() ?: "?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            
            // 删除按钮
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.TopEnd)
                    .clip(CircleShape)
                    .background(Color(0xFFFA5151))
                    .clickable(onClick = onRemove),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "移除",
                    tint = Color.White,
                    modifier = Modifier.size(10.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = contact.name.take(4),
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * 分组标题
 */
@Composable
private fun SectionHeader(letter: Char) {
    Text(
        text = letter.toString(),
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 6.dp),
        fontSize = 13.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

/**
 * 可选择的联系人项
 */
@Composable
private fun SelectableContactItem(
    contact: ContactUiModel,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        } else {
            MaterialTheme.colorScheme.surface
        },
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "background"
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 复选框
            Checkbox(
                checked = isSelected,
                onCheckedChange = { onSelect() },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.outline
                )
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // 头像
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = contact.name.firstOrNull()?.toString() ?: "?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // 名称
            Text(
                text = contact.name,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

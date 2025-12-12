package com.aurora.wave.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material3.Badge
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aurora.wave.design.WhiteStatusBar

/**
 * 服务页面 - 微信风格
 * 
 * 特性：
 * - 支付服务入口
 * - 生活服务分类
 * - 可展开的服务组
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesScreen(
    onBackClick: () -> Unit = {},
    onServiceClick: (String) -> Unit = {}
) {
    WhiteStatusBar()
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "服务",
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
            // 支付服务
            item {
                PaymentSection(onServiceClick = onServiceClick)
            }
            
            // 以下分类暂时隐藏，保留核心支付功能
            // TODO: 后续根据业务需要开放
            
            /*
            item { Spacer(modifier = Modifier.height(8.dp)) }
            
            // 金融理财
            item {
                ServiceGroup(
                    title = "金融理财",
                    services = listOf(
                        ServiceItem("credit_card", "信用卡还款", Icons.Default.CreditCard, Color(0xFF1DA1F2)),
                        ServiceItem("loan", "微粒贷借钱", Icons.Default.AccountBalance, Color(0xFF4CAF50)),
                        ServiceItem("wealth", "理财通", Icons.Default.Star, Color(0xFFFF9800), badge = "热门")
                    ),
                    onServiceClick = onServiceClick
                )
            }
            
            item { Spacer(modifier = Modifier.height(8.dp)) }
            
            // 生活服务
            item {
                ServiceGroup(
                    title = "生活服务",
                    services = listOf(
                        ServiceItem("phone_bill", "手机充值", Icons.Default.Payment, Color(0xFF9C27B0)),
                        ServiceItem("utility", "生活缴费", Icons.Default.Receipt, Color(0xFF607D8B)),
                        ServiceItem("city_services", "城市服务", Icons.Default.LocalOffer, Color(0xFF00BCD4))
                    ),
                    onServiceClick = onServiceClick
                )
            }
            
            item { Spacer(modifier = Modifier.height(8.dp)) }
            
            // 购物消费
            item {
                ServiceGroup(
                    title = "购物消费",
                    services = listOf(
                        ServiceItem("shopping", "京东购物", Icons.Default.ShoppingBag, Color(0xFFE53935)),
                        ServiceItem("coupons", "优惠券", Icons.Default.LocalOffer, Color(0xFFFFB300), badge = "3"),
                        ServiceItem("gift", "礼品卡", Icons.Default.CardGiftcard, Color(0xFFE91E63))
                    ),
                    onServiceClick = onServiceClick,
                    defaultExpanded = false
                )
            }
            
            item { Spacer(modifier = Modifier.height(8.dp)) }
            
            // 第三方服务
            item {
                ServiceGroup(
                    title = "第三方服务",
                    services = listOf(
                        ServiceItem("meituan", "美团外卖", Icons.Default.ShoppingBag, Color(0xFFFFB300)),
                        ServiceItem("didi", "滴滴出行", Icons.Default.LocalOffer, Color(0xFFFF6D00)),
                        ServiceItem("movie", "电影演出", Icons.Default.Subscriptions, Color(0xFF8E24AA))
                    ),
                    onServiceClick = onServiceClick,
                    defaultExpanded = false
                )
            }
            */
        }
    }
}

/**
 * 支付服务区域
 */
@Composable
private fun PaymentSection(
    onServiceClick: (String) -> Unit
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                PaymentQuickAction(
                    icon = Icons.Default.Payment,
                    label = "收付款",
                    color = Color(0xFF07C160),
                    onClick = { onServiceClick("payment") }
                )
                
                PaymentQuickAction(
                    icon = Icons.Default.QrCode,
                    label = "扫一扫",
                    color = Color(0xFF1DA1F2),
                    onClick = { onServiceClick("scan") }
                )
                
                PaymentQuickAction(
                    icon = Icons.Default.CardGiftcard,
                    label = "红包",
                    color = Color(0xFFE53935),
                    onClick = { onServiceClick("red_packet") }
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            HorizontalDivider(
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // 钱包入口
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onServiceClick("wallet") }
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.AccountBalance,
                    contentDescription = null,
                    tint = Color(0xFF07C160),
                    modifier = Modifier.size(24.dp)
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "钱包",
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "零钱·银行卡",
                        color = MaterialTheme.colorScheme.outline,
                        fontSize = 12.sp
                    )
                }
                
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

/**
 * 支付快捷操作按钮
 */
@Composable
private fun PaymentQuickAction(
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
 * 可展开的服务组
 */
@Composable
private fun ServiceGroup(
    title: String,
    services: List<ServiceItem>,
    onServiceClick: (String) -> Unit,
    defaultExpanded: Boolean = true
) {
    var expanded by remember { mutableStateOf(defaultExpanded) }
    val rotationAngle by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "expand_rotation"
    )
    
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
        Column {
            // 标题行
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
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
            
            // 服务列表
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )
                    
                    services.forEach { service ->
                        ServiceRow(
                            service = service,
                            onClick = { onServiceClick(service.id) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * 单个服务行
 */
@Composable
private fun ServiceRow(
    service: ServiceItem,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(service.color.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                service.icon,
                contentDescription = null,
                tint = service.color,
                modifier = Modifier.size(20.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Text(
            text = service.name,
            fontSize = 15.sp,
            modifier = Modifier.weight(1f)
        )
        
        if (service.badge != null) {
            Badge(
                containerColor = Color(0xFFE53935)
            ) {
                Text(
                    text = service.badge,
                    fontSize = 10.sp
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
        }
        
        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outline,
            modifier = Modifier.size(20.dp)
        )
    }
}

// Data class
data class ServiceItem(
    val id: String,
    val name: String,
    val icon: ImageVector,
    val color: Color,
    val badge: String? = null
)

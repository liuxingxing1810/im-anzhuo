package com.aurora.wave.messages.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import timber.log.Timber
import java.util.Locale

/**
 * 位置选择结果
 * Location selection result
 */
data class LocationResult(
    val latitude: Double,
    val longitude: Double,
    val address: String?,
    val name: String?
)

/**
 * 附近地点
 * Nearby place
 */
data class NearbyPlace(
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val distance: String
)

/**
 * 位置选择页面
 * Location picker screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationPickerScreen(
    onBackClick: () -> Unit,
    onLocationSelected: (LocationResult) -> Unit
) {
    val context = LocalContext.current
    var hasPermission by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var currentLocation by remember { mutableStateOf<Location?>(null) }
    var currentAddress by remember { mutableStateOf("获取位置中...") }
    var searchQuery by remember { mutableStateOf("") }
    var nearbyPlaces by remember { mutableStateOf<List<NearbyPlace>>(emptyList()) }
    
    // 权限请求
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasPermission = permissions.values.all { it }
        if (hasPermission) {
            // 获取位置
            getCurrentLocation(context) { location ->
                currentLocation = location
                location?.let {
                    currentAddress = getAddressFromLocation(context, it.latitude, it.longitude)
                    nearbyPlaces = generateNearbyPlaces(it.latitude, it.longitude)
                }
                isLoading = false
            }
        } else {
            isLoading = false
            currentAddress = "位置权限被拒绝"
        }
    }
    
    // 检查权限
    LaunchedEffect(Unit) {
        val fineLocation = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        )
        val coarseLocation = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        )
        
        if (fineLocation == PackageManager.PERMISSION_GRANTED ||
            coarseLocation == PackageManager.PERMISSION_GRANTED) {
            hasPermission = true
            getCurrentLocation(context) { location ->
                currentLocation = location
                location?.let {
                    currentAddress = getAddressFromLocation(context, it.latitude, it.longitude)
                    nearbyPlaces = generateNearbyPlaces(it.latitude, it.longitude)
                }
                isLoading = false
            }
        } else {
            permissionLauncher.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ))
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("位置") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "返回")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // 搜索框
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("搜索地点") },
                leadingIcon = {
                    Icon(Icons.Default.Search, "搜索")
                },
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                singleLine = true
            )
            
            // 当前位置卡片
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable {
                        currentLocation?.let { location ->
                            onLocationSelected(
                                LocationResult(
                                    latitude = location.latitude,
                                    longitude = location.longitude,
                                    address = currentAddress,
                                    name = "我的位置"
                                )
                            )
                        }
                    },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.MyLocation,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "发送当前位置",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        if (isLoading) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(14.dp),
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "定位中...",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                            Text(
                                text = currentAddress,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 附近地点标题
            Text(
                text = "附近地点",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 附近地点列表
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn {
                    items(nearbyPlaces) { place ->
                        NearbyPlaceItem(
                            place = place,
                            onClick = {
                                onLocationSelected(
                                    LocationResult(
                                        latitude = place.latitude,
                                        longitude = place.longitude,
                                        address = place.address,
                                        name = place.name
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NearbyPlaceItem(
    place: NearbyPlace,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.LocationOn,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = place.name,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = place.address,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Text(
            text = place.distance,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * 获取当前位置
 */
@SuppressLint("MissingPermission")
private fun getCurrentLocation(context: Context, callback: (Location?) -> Unit) {
    try {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        
        // 尝试获取最后已知位置
        val providers = listOf(
            LocationManager.GPS_PROVIDER,
            LocationManager.NETWORK_PROVIDER
        )
        
        for (provider in providers) {
            try {
                val location = locationManager.getLastKnownLocation(provider)
                if (location != null) {
                    Timber.d("Got location from $provider: ${location.latitude}, ${location.longitude}")
                    callback(location)
                    return
                }
            } catch (e: Exception) {
                Timber.e(e, "Failed to get location from $provider")
            }
        }
        
        // 没有获取到位置，返回默认位置（北京）
        Timber.w("No location available, using default location")
        val defaultLocation = Location("default").apply {
            latitude = 39.9042
            longitude = 116.4074
        }
        callback(defaultLocation)
    } catch (e: Exception) {
        Timber.e(e, "Failed to get current location")
        callback(null)
    }
}

/**
 * 根据经纬度获取地址
 */
private fun getAddressFromLocation(context: Context, latitude: Double, longitude: Double): String {
    return try {
        val geocoder = Geocoder(context, Locale.getDefault())
        @Suppress("DEPRECATION")
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        if (!addresses.isNullOrEmpty()) {
            val address = addresses[0]
            buildString {
                address.thoroughfare?.let { append(it) }
                address.subThoroughfare?.let { append(it) }
                if (isEmpty()) {
                    address.featureName?.let { append(it) }
                }
                if (isEmpty()) {
                    address.locality?.let { append(it) }
                    address.subLocality?.let { append(it) }
                }
            }.ifEmpty { "位置 (${String.format("%.4f", latitude)}, ${String.format("%.4f", longitude)})" }
        } else {
            "位置 (${String.format("%.4f", latitude)}, ${String.format("%.4f", longitude)})"
        }
    } catch (e: Exception) {
        Timber.e(e, "Failed to get address from location")
        "位置 (${String.format("%.4f", latitude)}, ${String.format("%.4f", longitude)})"
    }
}

/**
 * 生成模拟的附近地点
 */
private fun generateNearbyPlaces(latitude: Double, longitude: Double): List<NearbyPlace> {
    // 模拟附近地点数据
    return listOf(
        NearbyPlace(
            name = "星巴克咖啡",
            address = "商业街1号",
            latitude = latitude + 0.001,
            longitude = longitude + 0.001,
            distance = "100m"
        ),
        NearbyPlace(
            name = "肯德基",
            address = "美食广场B1",
            latitude = latitude + 0.002,
            longitude = longitude - 0.001,
            distance = "200m"
        ),
        NearbyPlace(
            name = "万达广场",
            address = "城市中心路88号",
            latitude = latitude - 0.001,
            longitude = longitude + 0.002,
            distance = "350m"
        ),
        NearbyPlace(
            name = "中心公园",
            address = "公园路1号",
            latitude = latitude - 0.003,
            longitude = longitude - 0.002,
            distance = "500m"
        ),
        NearbyPlace(
            name = "地铁站",
            address = "交通枢纽站",
            latitude = latitude + 0.004,
            longitude = longitude + 0.001,
            distance = "600m"
        )
    )
}

package com.aurora.wave.messages.ui

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 媒体选择器状态
 * Media picker state
 */
data class MediaPickerState(
    val showPhotoPicker: Boolean = false,
    val showCamera: Boolean = false,
    val tempPhotoUri: Uri? = null
)

/**
 * 创建用于存储拍照图片的临时 Uri
 * Create a temporary Uri for camera capture
 */
@Composable
fun rememberCameraUri(): Uri? {
    val context = LocalContext.current
    return remember {
        try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val imageFileName = "WAVE_${timeStamp}.jpg"
            val storageDir = context.cacheDir
            val imageFile = File(storageDir, imageFileName)
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                imageFile
            )
        } catch (e: Exception) {
            Timber.e(e, "Failed to create camera Uri")
            null
        }
    }
}

/**
 * 图片选择器启动器
 * Photo picker launcher
 */
@Composable
fun rememberPhotoPickerLauncher(
    onPhotoSelected: (Uri) -> Unit
) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
) { uri ->
    uri?.let {
        Timber.d("Photo selected: $uri")
        onPhotoSelected(it)
    }
}

/**
 * 多图选择器启动器
 * Multiple photo picker launcher
 */
@Composable
fun rememberMultiplePhotoPickerLauncher(
    onPhotosSelected: (List<Uri>) -> Unit
) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetMultipleContents()
) { uris ->
    if (uris.isNotEmpty()) {
        Timber.d("${uris.size} photos selected")
        onPhotosSelected(uris)
    }
}

/**
 * 相机拍照启动器
 * Camera capture launcher
 */
@Composable
fun rememberCameraLauncher(
    onPhotoTaken: (Uri) -> Unit,
    photoUri: Uri?
) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.TakePicture()
) { success ->
    if (success && photoUri != null) {
        Timber.d("Photo captured: $photoUri")
        onPhotoTaken(photoUri)
    } else {
        Timber.d("Photo capture cancelled or failed")
    }
}

/**
 * 视频拍摄启动器
 * Video capture launcher
 */
@Composable
fun rememberVideoCaptureUri(): Uri? {
    val context = LocalContext.current
    return remember {
        try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val videoFileName = "WAVE_${timeStamp}.mp4"
            val storageDir = context.cacheDir
            val videoFile = File(storageDir, videoFileName)
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                videoFile
            )
        } catch (e: Exception) {
            Timber.e(e, "Failed to create video Uri")
            null
        }
    }
}

@Composable
fun rememberVideoCaptureLauncher(
    onVideoTaken: (Uri) -> Unit,
    videoUri: Uri?
) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.CaptureVideo()
) { success ->
    if (success && videoUri != null) {
        Timber.d("Video captured: $videoUri")
        onVideoTaken(videoUri)
    } else {
        Timber.d("Video capture cancelled or failed")
    }
}

/**
 * 相机权限请求启动器
 * Camera permission request launcher
 */
@Composable
fun rememberCameraPermissionLauncher(
    onPermissionResult: (Boolean) -> Unit
) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
) { granted ->
    Timber.d("Camera permission result: $granted")
    onPermissionResult(granted)
}

/**
 * 存储权限请求启动器（Android 10 以下）
 * Storage permission request launcher (for Android 10 and below)
 */
@Composable
fun rememberStoragePermissionLauncher(
    onPermissionResult: (Boolean) -> Unit
) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
) { granted ->
    Timber.d("Storage permission result: $granted")
    onPermissionResult(granted)
}

/**
 * 检查相机权限
 * Check camera permission
 */
fun checkCameraPermission(context: android.content.Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}

/**
 * 检查存储权限（Android 10 以下需要）
 * Check storage permission (needed for Android 10 and below)
 */
fun checkStoragePermission(context: android.content.Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        // Android 10+ 使用 Scoped Storage，不需要额外权限
        true
    } else {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }
}

/**
 * 媒体选择器组合函数
 * Composable media picker hooks
 */
@Composable
fun rememberMediaPickerHandlers(
    onImageSelected: (Uri) -> Unit,
    onVideoSelected: (Uri) -> Unit
): MediaPickerHandlers {
    val context = LocalContext.current
    
    // 临时 URI
    val photoUri = rememberCameraUri()
    val videoUri = rememberVideoCaptureUri()
    
    // 权限状态
    var hasCameraPermission by remember { mutableStateOf(checkCameraPermission(context)) }
    var hasStoragePermission by remember { mutableStateOf(checkStoragePermission(context)) }
    
    // 等待权限后执行的操作
    var pendingAction by remember { mutableStateOf<PendingMediaAction?>(null) }
    
    // 图片选择器
    val photoPickerLauncher = rememberPhotoPickerLauncher { uri ->
        onImageSelected(uri)
    }
    
    // 相机拍照
    val cameraLauncher = rememberCameraLauncher(
        onPhotoTaken = { uri -> onImageSelected(uri) },
        photoUri = photoUri
    )
    
    // 视频拍摄
    val videoLauncher = rememberVideoCaptureLauncher(
        onVideoTaken = { uri -> onVideoSelected(uri) },
        videoUri = videoUri
    )
    
    // 相机权限请求
    val cameraPermissionLauncher = rememberCameraPermissionLauncher { granted ->
        hasCameraPermission = granted
        if (granted) {
            when (pendingAction) {
                PendingMediaAction.TAKE_PHOTO -> photoUri?.let { cameraLauncher.launch(it) }
                PendingMediaAction.TAKE_VIDEO -> videoUri?.let { videoLauncher.launch(it) }
                else -> {}
            }
        }
        pendingAction = null
    }
    
    // 存储权限请求
    val storagePermissionLauncher = rememberStoragePermissionLauncher { granted ->
        hasStoragePermission = granted
        if (granted && pendingAction == PendingMediaAction.PICK_PHOTO) {
            photoPickerLauncher.launch("image/*")
        }
        pendingAction = null
    }
    
    return MediaPickerHandlers(
        onPickPhoto = {
            if (hasStoragePermission) {
                photoPickerLauncher.launch("image/*")
            } else {
                pendingAction = PendingMediaAction.PICK_PHOTO
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    storagePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                } else {
                    photoPickerLauncher.launch("image/*")
                }
            }
        },
        onTakePhoto = {
            if (hasCameraPermission) {
                photoUri?.let { cameraLauncher.launch(it) }
            } else {
                pendingAction = PendingMediaAction.TAKE_PHOTO
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        },
        onTakeVideo = {
            if (hasCameraPermission) {
                videoUri?.let { videoLauncher.launch(it) }
            } else {
                pendingAction = PendingMediaAction.TAKE_VIDEO
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    )
}

/**
 * 媒体选择器处理函数集合
 * Media picker handler functions
 */
data class MediaPickerHandlers(
    val onPickPhoto: () -> Unit,
    val onTakePhoto: () -> Unit,
    val onTakeVideo: () -> Unit
)

/**
 * 等待执行的媒体操作
 * Pending media action
 */
private enum class PendingMediaAction {
    PICK_PHOTO,
    TAKE_PHOTO,
    TAKE_VIDEO
}

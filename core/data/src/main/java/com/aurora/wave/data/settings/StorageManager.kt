package com.aurora.wave.data.settings

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.text.DecimalFormat

/**
 * 存储管理器 - 处理缓存和存储空间
 * Storage Manager - handles cache and storage
 */
class StorageManager(private val context: Context) {
    
    /**
     * 获取各类缓存大小
     */
    suspend fun getStorageInfo(): StorageInfo = withContext(Dispatchers.IO) {
        val cacheSize = calculateDirSize(context.cacheDir)
        val externalCacheSize = context.externalCacheDir?.let { calculateDirSize(it) } ?: 0L
        val filesSize = calculateDirSize(context.filesDir)
        val externalFilesSize = context.getExternalFilesDir(null)?.let { calculateDirSize(it) } ?: 0L
        
        // 分类计算
        val imageCacheSize = getMediaCacheSize("images")
        val videoCacheSize = getMediaCacheSize("videos")
        val audioCacheSize = getMediaCacheSize("audio")
        val documentSize = getMediaCacheSize("documents")
        
        StorageInfo(
            totalCache = cacheSize + externalCacheSize,
            imageCache = imageCacheSize,
            videoCache = videoCacheSize,
            audioCache = audioCacheSize,
            documentCache = documentSize,
            otherFiles = filesSize + externalFilesSize - imageCacheSize - videoCacheSize - audioCacheSize - documentSize
        )
    }
    
    /**
     * 清除所有缓存
     */
    suspend fun clearAllCache(): Boolean = withContext(Dispatchers.IO) {
        try {
            deleteDir(context.cacheDir)
            context.externalCacheDir?.let { deleteDir(it) }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    /**
     * 清除图片缓存
     */
    suspend fun clearImageCache(): Boolean = withContext(Dispatchers.IO) {
        clearMediaCache("images")
    }
    
    /**
     * 清除视频缓存
     */
    suspend fun clearVideoCache(): Boolean = withContext(Dispatchers.IO) {
        clearMediaCache("videos")
    }
    
    /**
     * 清除音频缓存
     */
    suspend fun clearAudioCache(): Boolean = withContext(Dispatchers.IO) {
        clearMediaCache("audio")
    }
    
    /**
     * 清除文档缓存
     */
    suspend fun clearDocumentCache(): Boolean = withContext(Dispatchers.IO) {
        clearMediaCache("documents")
    }
    
    private fun getMediaCacheSize(type: String): Long {
        val cacheDir = File(context.cacheDir, type)
        val externalCacheDir = context.externalCacheDir?.let { File(it, type) }
        return calculateDirSize(cacheDir) + (externalCacheDir?.let { calculateDirSize(it) } ?: 0L)
    }
    
    private fun clearMediaCache(type: String): Boolean {
        return try {
            val cacheDir = File(context.cacheDir, type)
            val externalCacheDir = context.externalCacheDir?.let { File(it, type) }
            deleteDir(cacheDir)
            externalCacheDir?.let { deleteDir(it) }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    private fun calculateDirSize(dir: File?): Long {
        if (dir == null || !dir.exists()) return 0L
        
        var size = 0L
        val files = dir.listFiles()
        if (files != null) {
            for (file in files) {
                size += if (file.isDirectory) {
                    calculateDirSize(file)
                } else {
                    file.length()
                }
            }
        }
        return size
    }
    
    private fun deleteDir(dir: File): Boolean {
        if (dir.isDirectory) {
            val children = dir.listFiles()
            if (children != null) {
                for (child in children) {
                    deleteDir(child)
                }
            }
        }
        return dir.delete()
    }
    
    companion object {
        /**
         * 格式化文件大小显示
         */
        fun formatSize(size: Long): String {
            if (size <= 0) return "0 B"
            
            val units = arrayOf("B", "KB", "MB", "GB", "TB")
            val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
            val format = DecimalFormat("#,##0.#")
            
            return "${format.format(size / Math.pow(1024.0, digitGroups.toDouble()))} ${units[digitGroups]}"
        }
    }
}

/**
 * 存储信息
 */
data class StorageInfo(
    val totalCache: Long,
    val imageCache: Long,
    val videoCache: Long,
    val audioCache: Long,
    val documentCache: Long,
    val otherFiles: Long
) {
    val total: Long get() = totalCache + otherFiles
    
    fun formatTotal(): String = StorageManager.formatSize(total)
    fun formatTotalCache(): String = StorageManager.formatSize(totalCache)
    fun formatImageCache(): String = StorageManager.formatSize(imageCache)
    fun formatVideoCache(): String = StorageManager.formatSize(videoCache)
    fun formatAudioCache(): String = StorageManager.formatSize(audioCache)
    fun formatDocumentCache(): String = StorageManager.formatSize(documentCache)
    fun formatOtherFiles(): String = StorageManager.formatSize(otherFiles)
}

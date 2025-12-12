package com.aurora.wave.data.converter

import androidx.room.TypeConverter
import com.aurora.wave.data.model.ConversationType
import com.aurora.wave.data.model.DeliveryStatus
import com.aurora.wave.data.model.DownloadStatus
import com.aurora.wave.data.model.MediaType
import com.aurora.wave.data.model.MessageType
import com.aurora.wave.data.model.SyncStatus
import com.aurora.wave.data.model.UploadStatus
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Room type converters for enum types and complex types
 */
class Converters {
    
    private val json = Json { ignoreUnknownKeys = true }
    
    // ==================== MessageType ====================
    @TypeConverter
    fun fromMessageType(value: MessageType): String = value.name
    
    @TypeConverter
    fun toMessageType(value: String): MessageType = MessageType.valueOf(value)
    
    // ==================== DeliveryStatus ====================
    @TypeConverter
    fun fromDeliveryStatus(value: DeliveryStatus): String = value.name
    
    @TypeConverter
    fun toDeliveryStatus(value: String): DeliveryStatus = DeliveryStatus.valueOf(value)
    
    // ==================== ConversationType ====================
    @TypeConverter
    fun fromConversationType(value: ConversationType): String = value.name
    
    @TypeConverter
    fun toConversationType(value: String): ConversationType = ConversationType.valueOf(value)
    
    // ==================== MediaType ====================
    @TypeConverter
    fun fromMediaType(value: MediaType): String = value.name
    
    @TypeConverter
    fun toMediaType(value: String): MediaType = MediaType.valueOf(value)
    
    // ==================== DownloadStatus ====================
    @TypeConverter
    fun fromDownloadStatus(value: DownloadStatus): String = value.name
    
    @TypeConverter
    fun toDownloadStatus(value: String): DownloadStatus = DownloadStatus.valueOf(value)
    
    // ==================== UploadStatus ====================
    @TypeConverter
    fun fromUploadStatus(value: UploadStatus): String = value.name
    
    @TypeConverter
    fun toUploadStatus(value: String): UploadStatus = UploadStatus.valueOf(value)
    
    // ==================== SyncStatus ====================
    @TypeConverter
    fun fromSyncStatus(value: SyncStatus): String = value.name
    
    @TypeConverter
    fun toSyncStatus(value: String): SyncStatus = SyncStatus.valueOf(value)
    
    // ==================== List<String> ====================
    @TypeConverter
    fun fromStringList(value: List<String>): String = json.encodeToString(value)
    
    @TypeConverter
    fun toStringList(value: String): List<String> = try {
        json.decodeFromString(value)
    } catch (e: Exception) {
        emptyList()
    }
}

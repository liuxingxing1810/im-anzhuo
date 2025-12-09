package com.aurora.wave.data.converter

import androidx.room.TypeConverter
import com.aurora.wave.data.model.ConversationType
import com.aurora.wave.data.model.DeliveryStatus
import com.aurora.wave.data.model.MessageType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Room type converters for enum types and complex types
 */
class Converters {
    
    private val json = Json { ignoreUnknownKeys = true }
    
    @TypeConverter
    fun fromMessageType(value: MessageType): String = value.name
    
    @TypeConverter
    fun toMessageType(value: String): MessageType = MessageType.valueOf(value)
    
    @TypeConverter
    fun fromDeliveryStatus(value: DeliveryStatus): String = value.name
    
    @TypeConverter
    fun toDeliveryStatus(value: String): DeliveryStatus = DeliveryStatus.valueOf(value)
    
    @TypeConverter
    fun fromConversationType(value: ConversationType): String = value.name
    
    @TypeConverter
    fun toConversationType(value: String): ConversationType = ConversationType.valueOf(value)
    
    @TypeConverter
    fun fromStringList(value: List<String>): String = json.encodeToString(value)
    
    @TypeConverter
    fun toStringList(value: String): List<String> = try {
        json.decodeFromString(value)
    } catch (e: Exception) {
        emptyList()
    }
}

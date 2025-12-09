package com.aurora.wave.data.model

import kotlinx.serialization.Serializable

/**
 * Sealed interface for different message content types.
 * Each type carries its specific payload.
 */
@Serializable
sealed interface MessageContent {
    
    @Serializable
    data class Text(
        val text: String
    ) : MessageContent
    
    @Serializable
    data class Image(
        val url: String,
        val thumbnailUrl: String? = null,
        val width: Int = 0,
        val height: Int = 0,
        val caption: String? = null
    ) : MessageContent
    
    @Serializable
    data class Voice(
        val url: String,
        val durationSeconds: Int,
        val waveform: List<Int>? = null  // Audio waveform for UI visualization
    ) : MessageContent
    
    @Serializable
    data class File(
        val url: String,
        val fileName: String,
        val fileSize: Long,
        val mimeType: String? = null
    ) : MessageContent
    
    @Serializable
    data class Location(
        val latitude: Double,
        val longitude: Double,
        val address: String? = null,
        val name: String? = null
    ) : MessageContent
    
    @Serializable
    data class Video(
        val url: String,
        val thumbnailUrl: String? = null,
        val durationSeconds: Int,
        val width: Int = 0,
        val height: Int = 0
    ) : MessageContent
    
    @Serializable
    data class Sticker(
        val stickerId: String,
        val packId: String,
        val url: String
    ) : MessageContent
    
    @Serializable
    data class System(
        val text: String,
        val action: String? = null  // e.g., "member_joined", "group_renamed"
    ) : MessageContent
}

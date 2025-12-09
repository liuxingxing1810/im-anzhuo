package com.aurora.wave.data.model

/**
 * Types of messages supported in the IM system
 */
enum class MessageType {
    TEXT,
    IMAGE,
    VOICE,
    FILE,
    LOCATION,
    VIDEO,
    STICKER,
    SYSTEM  // System notifications like "X joined the group"
}

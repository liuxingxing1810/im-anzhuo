package com.aurora.wave.data.model

/**
 * Message delivery status
 */
enum class DeliveryStatus {
    SENDING,    // Message is being sent
    SENT,       // Server received the message
    DELIVERED,  // Recipient device received it
    READ,       // Recipient read it
    FAILED      // Failed to send
}

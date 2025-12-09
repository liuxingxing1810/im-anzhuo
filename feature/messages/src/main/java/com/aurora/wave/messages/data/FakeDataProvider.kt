package com.aurora.wave.messages.data

import com.aurora.wave.data.model.ConversationType
import com.aurora.wave.data.model.DeliveryStatus
import com.aurora.wave.data.model.MessageContent
import com.aurora.wave.data.model.MessageType
import com.aurora.wave.messages.model.ConversationUiModel
import com.aurora.wave.messages.model.MessageUiModel

/**
 * Fake data provider for development and preview.
 * Replace with real repository in production.
 */
object FakeDataProvider {
    
    val conversations = listOf(
        ConversationUiModel(
            id = "conv_1",
            name = "Design Sync",
            avatarUrl = null,
            lastMessage = "Let's finalize the color palette tomorrow",
            timestamp = "2m",
            unreadCount = 3,
            isPinned = true,
            isMuted = false,
            type = ConversationType.GROUP,
            isOnline = false
        ),
        ConversationUiModel(
            id = "conv_2",
            name = "Aria Chen",
            avatarUrl = null,
            lastMessage = "Sounds great! See you then 🎉",
            timestamp = "15m",
            unreadCount = 0,
            isPinned = true,
            isMuted = false,
            type = ConversationType.DIRECT,
            isOnline = true
        ),
        ConversationUiModel(
            id = "conv_3",
            name = "Family Group",
            avatarUrl = null,
            lastMessage = "Mom: Don't forget dinner on Sunday!",
            timestamp = "1h",
            unreadCount = 12,
            isPinned = false,
            isMuted = true,
            type = ConversationType.GROUP
        ),
        ConversationUiModel(
            id = "conv_4",
            name = "Blake Rivera",
            avatarUrl = null,
            lastMessage = "Can you send me the file?",
            timestamp = "3h",
            unreadCount = 1,
            isPinned = false,
            isMuted = false,
            type = ConversationType.DIRECT,
            draft = "Sure, here it is"
        ),
        ConversationUiModel(
            id = "conv_5",
            name = "Project Alpha",
            avatarUrl = null,
            lastMessage = "Cairo: Deployment successful ✅",
            timestamp = "5h",
            unreadCount = 0,
            isPinned = false,
            isMuted = false,
            type = ConversationType.GROUP
        ),
        ConversationUiModel(
            id = "conv_6",
            name = "Luna Park",
            avatarUrl = null,
            lastMessage = "Thanks for the help!",
            timestamp = "Yesterday",
            unreadCount = 0,
            isPinned = false,
            isMuted = false,
            type = ConversationType.DIRECT,
            isOnline = true
        ),
        ConversationUiModel(
            id = "conv_7",
            name = "Tech News",
            avatarUrl = null,
            lastMessage = "New article: AI advances in 2025",
            timestamp = "Yesterday",
            unreadCount = 5,
            isPinned = false,
            isMuted = true,
            type = ConversationType.CHANNEL
        ),
        ConversationUiModel(
            id = "conv_8",
            name = "Dune Aoki",
            avatarUrl = null,
            lastMessage = "[Voice message]",
            timestamp = "2d",
            unreadCount = 0,
            isPinned = false,
            isMuted = false,
            type = ConversationType.DIRECT
        )
    )
    
    fun getMessagesForConversation(conversationId: String): List<MessageUiModel> {
        return listOf(
            MessageUiModel(
                id = "msg_1",
                senderId = "user_other",
                senderName = "Aria Chen",
                senderAvatar = null,
                type = MessageType.TEXT,
                content = MessageContent.Text("Hey! How's the project going?"),
                timestamp = "10:30 AM",
                fullTimestamp = System.currentTimeMillis() - 3600000,
                status = DeliveryStatus.READ,
                isOutgoing = false,
                isFirstInGroup = true,
                isLastInGroup = false
            ),
            MessageUiModel(
                id = "msg_2",
                senderId = "user_other",
                senderName = "Aria Chen",
                senderAvatar = null,
                type = MessageType.TEXT,
                content = MessageContent.Text("Did you finish the UI designs?"),
                timestamp = "10:30 AM",
                fullTimestamp = System.currentTimeMillis() - 3580000,
                status = DeliveryStatus.READ,
                isOutgoing = false,
                isFirstInGroup = false,
                isLastInGroup = true
            ),
            MessageUiModel(
                id = "msg_3",
                senderId = "user_me",
                senderName = "Me",
                senderAvatar = null,
                type = MessageType.TEXT,
                content = MessageContent.Text("Yes! Just finished the main screens. Let me share them with you."),
                timestamp = "10:35 AM",
                fullTimestamp = System.currentTimeMillis() - 3300000,
                status = DeliveryStatus.READ,
                isOutgoing = true,
                isFirstInGroup = true,
                isLastInGroup = true
            ),
            MessageUiModel(
                id = "msg_4",
                senderId = "user_me",
                senderName = "Me",
                senderAvatar = null,
                type = MessageType.IMAGE,
                content = MessageContent.Image(
                    url = "https://example.com/design.png",
                    thumbnailUrl = null,
                    width = 1200,
                    height = 800,
                    caption = "Main dashboard design"
                ),
                timestamp = "10:36 AM",
                fullTimestamp = System.currentTimeMillis() - 3240000,
                status = DeliveryStatus.DELIVERED,
                isOutgoing = true,
                isFirstInGroup = true,
                isLastInGroup = true
            ),
            MessageUiModel(
                id = "msg_5",
                senderId = "user_other",
                senderName = "Aria Chen",
                senderAvatar = null,
                type = MessageType.TEXT,
                content = MessageContent.Text("Wow, this looks amazing! 🎨"),
                timestamp = "10:40 AM",
                fullTimestamp = System.currentTimeMillis() - 3000000,
                status = DeliveryStatus.READ,
                isOutgoing = false,
                isFirstInGroup = true,
                isLastInGroup = true
            ),
            MessageUiModel(
                id = "msg_6",
                senderId = "user_other",
                senderName = "Aria Chen",
                senderAvatar = null,
                type = MessageType.VOICE,
                content = MessageContent.Voice(
                    url = "https://example.com/voice.m4a",
                    durationSeconds = 15,
                    waveform = listOf(2, 5, 8, 12, 15, 18, 14, 10, 8, 12, 16, 12, 8, 5, 3)
                ),
                timestamp = "10:42 AM",
                fullTimestamp = System.currentTimeMillis() - 2880000,
                status = DeliveryStatus.READ,
                isOutgoing = false,
                isFirstInGroup = true,
                isLastInGroup = true
            ),
            MessageUiModel(
                id = "msg_7",
                senderId = "user_me",
                senderName = "Me",
                senderAvatar = null,
                type = MessageType.TEXT,
                content = MessageContent.Text("Thanks! Let me know if you need any changes."),
                timestamp = "10:45 AM",
                fullTimestamp = System.currentTimeMillis() - 2700000,
                status = DeliveryStatus.SENT,
                isOutgoing = true,
                isFirstInGroup = true,
                isLastInGroup = true
            ),
            MessageUiModel(
                id = "msg_8",
                senderId = "user_other",
                senderName = "Aria Chen",
                senderAvatar = null,
                type = MessageType.TEXT,
                content = MessageContent.Text("Sounds great! See you then 🎉"),
                timestamp = "10:50 AM",
                fullTimestamp = System.currentTimeMillis() - 2400000,
                status = DeliveryStatus.READ,
                isOutgoing = false,
                isFirstInGroup = true,
                isLastInGroup = true
            )
        )
    }
}

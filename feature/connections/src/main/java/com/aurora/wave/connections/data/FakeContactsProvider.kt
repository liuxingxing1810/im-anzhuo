package com.aurora.wave.connections.data

import com.aurora.wave.connections.model.ContactUiModel
import com.aurora.wave.connections.model.FriendRequestStatus
import com.aurora.wave.connections.model.FriendRequestUiModel

/**
 * Fake data provider for testing UI
 */
object FakeContactsProvider {
    
    private val firstNames = listOf(
        "Alice", "Bob", "Charlie", "David", "Emma", "Frank", "Grace", "Henry",
        "Ivy", "Jack", "Kate", "Leo", "Mia", "Noah", "Olivia", "Peter",
        "Quinn", "Rachel", "Sam", "Tom", "Uma", "Victor", "Wendy", "Xavier",
        "Yuki", "Zoe"
    )
    
    private val lastNames = listOf(
        "Anderson", "Brown", "Chen", "Davis", "Evans", "Foster", "Garcia", "Harris",
        "Ishikawa", "Johnson", "Kim", "Lee", "Miller", "Nguyen", "O'Brien", "Park",
        "Quinn", "Robinson", "Smith", "Taylor", "Ueda", "Vance", "Wilson", "Xu",
        "Yang", "Zhang"
    )
    
    private val bios = listOf(
        "Life is beautiful 🌸",
        "Coffee addict ☕",
        "Travel enthusiast ✈️",
        "Music lover 🎵",
        "Foodie 🍕",
        "Tech geek 💻",
        "Photographer 📷",
        "Book worm 📚",
        "Fitness freak 💪",
        "Art lover 🎨",
        null
    )
    
    fun generateContacts(): List<ContactUiModel> {
        return (0 until 50).map { index ->
            val firstName = firstNames[index % firstNames.size]
            val lastName = lastNames[(index + 7) % lastNames.size]
            val fullName = "$firstName $lastName"
            
            ContactUiModel(
                id = "contact_$index",
                name = fullName,
                avatarUrl = null,
                initial = fullName.first().uppercaseChar(),
                bio = bios[index % bios.size],
                isOnline = index % 5 == 0,
                isFriend = true,
                lastSeenTime = if (index % 5 != 0) {
                    when (index % 4) {
                        0 -> "5 min ago"
                        1 -> "1 hour ago"
                        2 -> "Yesterday"
                        else -> "2 days ago"
                    }
                } else null
            )
        }.sortedBy { it.name.lowercase() }
    }
    
    fun generateFriendRequests(): List<FriendRequestUiModel> {
        val colors = listOf("blue", "green", "orange", "purple")
        return listOf(
            FriendRequestUiModel(
                id = "req_1",
                userId = "user_101",
                userName = "Michael Scott",
                name = "Michael Scott",
                userAvatar = null,
                avatarColor = colors[0],
                message = "Hi! 我们上周在会议上见过面。",
                timestamp = "2小时前",
                status = FriendRequestStatus.PENDING,
                isRecent = true
            ),
            FriendRequestUiModel(
                id = "req_2",
                userId = "user_102",
                userName = "Jim Halpert",
                name = "Jim Halpert",
                userAvatar = null,
                avatarColor = colors[1],
                message = "嗨，想加个好友吗？",
                timestamp = "昨天",
                status = FriendRequestStatus.PENDING,
                isRecent = true
            ),
            FriendRequestUiModel(
                id = "req_3",
                userId = "user_103",
                userName = "Pam Beesly",
                name = "Pam Beesly",
                userAvatar = null,
                avatarColor = colors[2],
                message = "我是Pam，希望能和你成为朋友",
                timestamp = "前天",
                status = FriendRequestStatus.PENDING,
                isRecent = true
            ),
            FriendRequestUiModel(
                id = "req_4",
                userId = "user_104",
                userName = "Dwight Schrute",
                name = "Dwight Schrute",
                userAvatar = null,
                avatarColor = colors[3],
                message = "你好，我是Dwight",
                timestamp = "一周前",
                status = FriendRequestStatus.ACCEPTED,
                isRecent = false
            ),
            FriendRequestUiModel(
                id = "req_5",
                userId = "user_105",
                userName = "Angela Martin",
                name = "Angela Martin",
                userAvatar = null,
                avatarColor = colors[0],
                message = "请通过我的好友请求",
                timestamp = "两周前",
                status = FriendRequestStatus.REJECTED,
                isRecent = false
            )
        )
    }
    
    fun groupContactsByLetter(contacts: List<ContactUiModel>): Map<Char, List<ContactUiModel>> {
        return contacts.groupBy { contact ->
            val firstChar = contact.name.firstOrNull()?.uppercaseChar() ?: '#'
            if (firstChar.isLetter()) firstChar else '#'
        }.toSortedMap()
    }
}

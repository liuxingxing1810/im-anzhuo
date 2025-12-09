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
        return listOf(
            FriendRequestUiModel(
                id = "req_1",
                userId = "user_101",
                userName = "Michael Scott",
                userAvatar = null,
                message = "Hi! We met at the conference last week.",
                timestamp = "2 hours ago",
                status = FriendRequestStatus.PENDING
            ),
            FriendRequestUiModel(
                id = "req_2",
                userId = "user_102",
                userName = "Jim Halpert",
                userAvatar = null,
                message = "Hey, want to connect?",
                timestamp = "Yesterday",
                status = FriendRequestStatus.PENDING
            ),
            FriendRequestUiModel(
                id = "req_3",
                userId = "user_103",
                userName = "Pam Beesly",
                userAvatar = null,
                message = null,
                timestamp = "3 days ago",
                status = FriendRequestStatus.PENDING
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

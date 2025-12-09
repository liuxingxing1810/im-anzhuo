package com.aurora.wave.data

import kotlinx.serialization.Serializable

@Serializable
data class StubProfile(
    val id: String = "stub",
    val displayName: String = "Placeholder",
    val avatarUrl: String? = null
)

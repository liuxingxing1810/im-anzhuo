package com.aurora.wave.network

import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.MediaType.Companion.toMediaType

object ApiConfig {
    private val json = Json { ignoreUnknownKeys = true }

    val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder().build()
    }

    @OptIn(ExperimentalSerializationApi::class)
    fun retrofit(baseUrl: String): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
}

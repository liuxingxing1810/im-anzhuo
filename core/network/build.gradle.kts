plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.aurora.wave.network"
    compileSdk = rootProject.extra["compileSdk"] as Int

    defaultConfig { minSdk = rootProject.extra["minSdk"] as Int }

    buildFeatures { buildConfig = true }
}

dependencies {
    val composeBom = rootProject.extra["composeBom"] as String
    val retrofitVersion = rootProject.extra["retrofitVersion"] as String
    val okHttpVersion = rootProject.extra["okHttpVersion"] as String
    val kotlinSerialization = rootProject.extra["kotlinSerialization"] as String
    
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinSerialization")
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-kotlinx-serialization:$retrofitVersion")
    implementation("com.squareup.okhttp3:okhttp:$okHttpVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okHttpVersion")

    implementation(platform("androidx.compose:compose-bom:$composeBom"))
    implementation("androidx.compose.runtime:runtime")
}

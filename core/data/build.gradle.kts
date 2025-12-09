plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.aurora.wave.data"
    compileSdk = rootProject.extra["compileSdk"] as Int

    defaultConfig { minSdk = rootProject.extra["minSdk"] as Int }

    buildFeatures { buildConfig = true }
}

dependencies {
    val composeBom = rootProject.extra["composeBom"] as String
    val roomVersion = rootProject.extra["roomVersion"] as String
    
    implementation(platform("androidx.compose:compose-bom:$composeBom"))
    implementation("androidx.compose.runtime:runtime")
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
}

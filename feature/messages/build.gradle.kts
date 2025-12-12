plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.aurora.wave.messages"
    compileSdk = rootProject.extra["compileSdk"] as Int

    defaultConfig { minSdk = rootProject.extra["minSdk"] as Int }

    buildFeatures { compose = true }
    composeOptions { kotlinCompilerExtensionVersion = rootProject.extra["composeCompiler"] as String }
}

dependencies {
    val composeBom = rootProject.extra["composeBom"] as String
    val hiltVersion = rootProject.extra["hiltVersion"] as String
    
    implementation(platform("androidx.compose:compose-bom:$composeBom"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.3")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.3")
    implementation("io.coil-kt:coil-compose:2.6.0")
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    ksp("com.google.dagger:hilt-compiler:$hiltVersion")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation(project(":core:design"))
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    
    // CameraX dependencies for video call
    val cameraVersion = "1.3.4"
    implementation("androidx.camera:camera-core:$cameraVersion")
    implementation("androidx.camera:camera-camera2:$cameraVersion")
    implementation("androidx.camera:camera-lifecycle:$cameraVersion")
    implementation("androidx.camera:camera-view:$cameraVersion")

    debugImplementation("androidx.compose.ui:ui-tooling")
}

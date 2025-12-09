plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.aurora.wave.profile"
    compileSdk = rootProject.extra["compileSdk"] as Int

    defaultConfig { minSdk = rootProject.extra["minSdk"] as Int }

    buildFeatures { compose = true }
    composeOptions { kotlinCompilerExtensionVersion = rootProject.extra["composeCompiler"] as String }
}

dependencies {
    val composeBom = rootProject.extra["composeBom"] as String
    implementation(platform("androidx.compose:compose-bom:$composeBom"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material:material-icons-extended")
    implementation(project(":core:design"))
    implementation(project(":core:common"))
    implementation(project(":core:data"))

    // ViewModel support
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.3")
    // Image loading
    implementation("io.coil-kt:coil-compose:2.6.0")

    debugImplementation("androidx.compose.ui:ui-tooling")
}

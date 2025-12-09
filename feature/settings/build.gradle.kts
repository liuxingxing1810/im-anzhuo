plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.aurora.wave.settings"
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
    implementation(project(":core:design"))
    implementation(project(":core:common"))

    debugImplementation("androidx.compose.ui:ui-tooling")
}

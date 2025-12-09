plugins {
    id("com.android.application") version "8.5.2" apply false
    id("com.android.library") version "8.5.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.24" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.24" apply false
    id("com.google.dagger.hilt.android") version "2.52" apply false
    id("com.google.devtools.ksp") version "1.9.24-1.0.20" apply false
}

val compileSdk by extra(34)
val minSdk by extra(24)
val targetSdk by extra(34)

val composeBom by extra("2024.06.00")
val composeCompiler by extra("1.5.14")
val kotlinSerialization by extra("1.6.3")
val roomVersion by extra("2.6.1")
val retrofitVersion by extra("2.11.0")
val okHttpVersion by extra("4.12.0")
val hiltVersion by extra("2.52")

subprojects {
    configurations.all {
        resolutionStrategy.eachDependency {
            if (requested.group == "org.jetbrains.kotlin" && requested.name.contains("kotlin-stdlib")) {
                useVersion("1.9.24")
            }
        }
    }
    
    afterEvaluate {
        extensions.findByType<com.android.build.gradle.BaseExtension>()?.apply {
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }
        }
        
        tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }
}

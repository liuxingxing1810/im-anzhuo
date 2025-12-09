# Placeholder for ProGuard rules

# ========== Kotlin ==========
-dontwarn kotlin.**
-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

# Kotlin Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# ========== Kotlin Serialization ==========
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-keep,includedescriptorclasses class com.aurora.wave.**$$serializer { *; }
-keepclassmembers class com.aurora.wave.** {
    *** Companion;
}
-keepclasseswithmembers class com.aurora.wave.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# ========== Hilt / Dagger ==========
-dontwarn dagger.hilt.**
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }

# Keep Hilt generated classes
-keep class **_HiltModules* { *; }
-keep class **_Factory { *; }
-keep class **_MembersInjector { *; }

# ========== Compose ==========
-dontwarn androidx.compose.**
-keep class androidx.compose.** { *; }

# Keep Composable functions
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}

# ========== Timber ==========
-dontwarn org.jetbrains.annotations.**

# ========== General ==========
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keepattributes Exceptions

# Keep model classes
-keep class com.aurora.wave.**.model.** { *; }
-keep class com.aurora.wave.data.model.** { *; }

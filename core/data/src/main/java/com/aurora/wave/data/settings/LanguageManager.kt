package com.aurora.wave.data.settings

import android.app.Activity
import android.app.LocaleManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

/**
 * 语言管理器 - 处理应用语言切换
 * Language Manager - handles app language switching
 */
object LanguageManager {
    
    // 保存当前选择的语言（用于 Application 级别的 Context）
    private var currentAppLanguage: AppLanguage = AppLanguage.SYSTEM
    
    /**
     * 获取当前语言设置
     */
    fun getCurrentLanguage(context: Context): AppLanguage {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val localeManager = context.getSystemService(LocaleManager::class.java)
            val locales = localeManager.applicationLocales
            if (locales.isEmpty) {
                AppLanguage.SYSTEM
            } else {
                getLanguageFromLocale(locales.get(0))
            }
        } else {
            // 对于低版本 Android，从内存中获取
            currentAppLanguage
        }
    }
    
    /**
     * 设置应用语言
     * @param context Context 或 Activity
     * @param language 要设置的语言
     */
    fun setLanguage(context: Context, language: AppLanguage) {
        currentAppLanguage = language
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ 使用 LocaleManager，系统会自动处理 Activity 重建
            val localeManager = context.getSystemService(LocaleManager::class.java)
            if (language == AppLanguage.SYSTEM) {
                localeManager.applicationLocales = LocaleList.getEmptyLocaleList()
            } else {
                localeManager.applicationLocales = LocaleList.forLanguageTags(language.code)
            }
        } else {
            // Android 12 及以下：设置 Locale 并重启 Activity
            val locale = getLocaleFromLanguage(language)
            Locale.setDefault(locale)
            
            val localeList = if (language == AppLanguage.SYSTEM) {
                LocaleListCompat.getEmptyLocaleList()
            } else {
                LocaleListCompat.forLanguageTags(language.code)
            }
            AppCompatDelegate.setApplicationLocales(localeList)
            
            // 使用 Intent 重新启动 Activity，确保完全重建
            if (context is Activity) {
                restartActivity(context)
            }
        }
    }
    
    /**
     * 重新启动 Activity
     */
    private fun restartActivity(activity: Activity) {
        val intent = activity.intent
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.finish()
        activity.startActivity(intent)
        // 禁用过渡动画
        activity.overridePendingTransition(0, 0)
    }
    
    /**
     * 从 AppLanguage 获取 Locale
     */
    private fun getLocaleFromLanguage(language: AppLanguage): Locale {
        return if (language == AppLanguage.SYSTEM) {
            Locale.getDefault()
        } else {
            val parts = language.code.split("-")
            if (parts.size > 1) {
                Locale(parts[0], parts[1])
            } else {
                Locale(language.code)
            }
        }
    }
    
    /**
     * 在 Application 或 Activity 的 attachBaseContext 中调用
     * 用于包装 Context 以应用语言设置
     */
    fun wrapContext(context: Context): Context {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ 由系统处理
            return context
        }
        
        val language = currentAppLanguage
        if (language == AppLanguage.SYSTEM) {
            return context
        }
        
        val locale = getLocaleFromLanguage(language)
        Locale.setDefault(locale)
        
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocales(LocaleList(locale))
        }
        
        return context.createConfigurationContext(config)
    }
    
    /**
     * 初始化语言设置（从 DataStore 恢复）
     */
    fun initLanguage(language: AppLanguage) {
        currentAppLanguage = language
    }
    
    /**
     * 根据 Locale 获取对应的 AppLanguage
     */
    private fun getLanguageFromLocale(locale: Locale?): AppLanguage {
        if (locale == null) return AppLanguage.SYSTEM
        
        val language = locale.language
        val country = locale.country
        
        return when {
            language == "zh" && (country == "CN" || country == "Hans") -> AppLanguage.SIMPLIFIED_CHINESE
            language == "zh" && (country == "TW" || country == "HK" || country == "Hant") -> AppLanguage.TRADITIONAL_CHINESE
            language == "en" -> AppLanguage.ENGLISH
            else -> AppLanguage.SYSTEM
        }
    }
    
    /**
     * 获取所有支持的语言列表
     */
    fun getSupportedLanguages(): List<AppLanguage> = AppLanguage.entries
    
    /**
     * 获取当前系统语言的显示名称
     */
    fun getSystemLanguageDisplayName(context: Context): String {
        val systemLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0]
        } else {
            @Suppress("DEPRECATION")
            context.resources.configuration.locale
        }
        return systemLocale.displayName
    }
}

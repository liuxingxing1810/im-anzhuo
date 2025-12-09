package com.aurora.wave.data.settings

import android.app.LocaleManager
import android.content.Context
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
            val locales = AppCompatDelegate.getApplicationLocales()
            if (locales.isEmpty) {
                AppLanguage.SYSTEM
            } else {
                getLanguageFromLocale(locales.get(0))
            }
        }
    }
    
    /**
     * 设置应用语言
     */
    fun setLanguage(context: Context, language: AppLanguage) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val localeManager = context.getSystemService(LocaleManager::class.java)
            if (language == AppLanguage.SYSTEM) {
                localeManager.applicationLocales = LocaleList.getEmptyLocaleList()
            } else {
                localeManager.applicationLocales = LocaleList.forLanguageTags(language.code)
            }
        } else {
            val localeList = if (language == AppLanguage.SYSTEM) {
                LocaleListCompat.getEmptyLocaleList()
            } else {
                LocaleListCompat.forLanguageTags(language.code)
            }
            AppCompatDelegate.setApplicationLocales(localeList)
        }
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

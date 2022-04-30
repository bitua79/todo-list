package com.example.todolist.core.settings

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import android.view.ContextThemeWrapper
import android.view.View
import androidx.core.os.ConfigurationCompat
import androidx.core.text.layoutDirection
import java.util.*

private const val PREF_LANGUAGE = "signalLanguage"
val DEFAULT_LANGUAGE = Language.Fa

object LocaleHelper {


    @JvmStatic
    var currentLocale: Locale = Locale(DEFAULT_LANGUAGE.value)
        private set

    /* START: Save and Read From SharedPreferences*/
    /**
     * Read Language from SharedPreferences
     * @return language[Language] saved in sharedPref, if not found then [DEFAULT_LANGUAGE] is used
     * */
    @JvmStatic
    fun readLanguagePref(appPreferences: AppPreferences): Language {
        return appPreferences.language
    }

    /**
     * Save the language in SharedPreferences and also calls setDefault method on [Locale]
     * */
    @JvmStatic
    fun saveLanguagePref(appPreferences: AppPreferences, language: Language) {
        Locale.setDefault(language.getLocale())
        currentLocale = language.getLocale()
        appPreferences.language = language
    }

    /**
     * Read language from [SharedPreferences] and call setDefault on [Locale]
     * */
    @JvmStatic
    fun loadLocalePref(appPreferences: AppPreferences) {
        val language = readLanguagePref(appPreferences)
        val locale = Locale(language.value)
        Locale.setDefault(locale)
        currentLocale = locale
    }
    /* END: Save and Read From SharedPreferences*/

    /* START: changing the locale of */
    @JvmStatic
    fun localizeContext(context: Context, wrapper: ContextThemeWrapper) {
        val config = Configuration(context.resources.configuration)
        config.setLocale(currentLocale)
        wrapper.applyOverrideConfiguration(config)
    }

    @JvmStatic
    @JvmOverloads
    fun localizeContext(app: Application, config: Configuration, configChange: Boolean = false) {
        if (configChange) {
            val configLocale = ConfigurationCompat.getLocales(config)[0]
            if (currentLocale.language == configLocale.language) {
                return
            }
        }
        val newConfig = updateConfigLocale(config, currentLocale)
        val resources = app.resources
        resources.updateConfiguration(newConfig, resources.displayMetrics)

        Locale.setDefault(currentLocale)
    }

    /**
     * Returns a new configuration with the given locale applied.
     */
    private fun updateConfigLocale(config: Configuration, locale: Locale): Configuration {
        val newConfig = Configuration(config)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            @Suppress("DEPRECATION")
            newConfig.locale = locale
        } else {
            newConfig.setLocale(locale)
        }
        return newConfig
    }
    /* END: changing the locale of */

    /**
     * check if layoutDirection is RTL
     * */
    @JvmStatic
    fun isRtl(): Boolean {
        return currentLocale.layoutDirection == View.LAYOUT_DIRECTION_RTL
    }

    /**
     * @return current text direction
     * */
    @JvmStatic
    fun getTextDirection(): Int {
        return if (isRtl()) View.TEXT_DIRECTION_RTL else View.TEXT_DIRECTION_LTR
    }

    /**
     * check if current language is Persian(Farsi)
     * */
    @JvmStatic
    fun isFarsi(): Boolean {
        return currentLocale.language == Language.Fa.value
    }
}


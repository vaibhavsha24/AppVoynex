package com.voynex.app.preferences

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

lateinit var appContext: Context

actual fun createSettings(): Settings {
    return SharedPreferencesSettings(appContext.getSharedPreferences("yatra_ai_prefs", Context.MODE_PRIVATE))
}

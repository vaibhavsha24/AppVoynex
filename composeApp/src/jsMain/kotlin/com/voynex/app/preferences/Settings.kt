package com.voynex.app.preferences

import com.russhwolf.settings.JsSettings
import com.russhwolf.settings.Settings

actual fun createSettings(): Settings {
    return JsSettings()
}

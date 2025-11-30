package com.voynex.app

import androidx.compose.ui.window.ComposeUIViewController
import com.voynex.app.navigation.App
import com.voynex.app.preferences.createSettings
import com.voynex.app.ui.common.ViewModelFactory


fun MainViewController() = ComposeUIViewController {
    val settings = createSettings()
    val viewModelFactory = ViewModelFactory(settings)

    App(viewModelFactory)
}

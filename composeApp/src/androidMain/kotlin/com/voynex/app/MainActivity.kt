package com.voynex.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.voynex.app.navigation.App
import com.voynex.app.preferences.SharedPref
import com.voynex.app.ui.common.ViewModelFactory
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.takeFrom
import io.kamel.image.config.Default
import moe.tlaster.precompose.PreComposeApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val sharedPref = SharedPref(this)

        val viewModelFactory = ViewModelFactory(sharedPref)
        setContent {
            PreComposeApp {
                KamelConfig {
                    takeFrom(KamelConfig.Default)
                }
                App(viewModelFactory)
            }
        }
    }
}
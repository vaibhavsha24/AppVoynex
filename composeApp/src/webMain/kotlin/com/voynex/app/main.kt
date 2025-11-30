package com.voynex.app

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.voynex.app.ui.AppRoot

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport {
        AppRoot(){}
    }
}
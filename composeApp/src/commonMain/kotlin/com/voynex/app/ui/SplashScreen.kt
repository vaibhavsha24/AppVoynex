package com.voynex.app.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import moe.tlaster.precompose.navigation.Navigator
import org.jetbrains.compose.resources.painterResource
import voynex.composeapp.generated.resources.Res
import voynex.composeapp.generated.resources.minus
import voynex.composeapp.generated.resources.voynex

@Composable
fun SplashScreen(onFinished: () -> Unit) {

    var startAnim by remember { mutableStateOf(false) }

    // Fade animation
    val alpha = animateFloatAsState(
        targetValue = if (startAnim) 1f else 0f,
        animationSpec = tween(durationMillis = 1200)
    )

    LaunchedEffect(Unit) {
        startAnim = true
        kotlinx.coroutines.delay(1800)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),   // your theme background
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.voynex), // your PNG asset
            contentDescription = null,
            modifier = Modifier
                .size(180.dp)
                .alpha(alpha.value)
        )
    }
}
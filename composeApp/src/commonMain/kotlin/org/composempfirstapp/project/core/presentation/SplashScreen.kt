package org.composempfirstapp.project.core.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit,
    appTheme: String?
) {
    val isDarkMode = when (appTheme) {
        "LIGHT_MODE" -> false
        "DARK_MODE"  -> true
        else         -> isSystemInDarkTheme()
    }

    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim by animateFloatAsState(
        targetValue   = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label         = "Alpha Animation"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2000)
        onSplashFinished()
    }

    // Choose the right DrawableResource
    val logoRes = if (isDarkMode)
        AppResources.Drawables.LogoDark
    else
        AppResources.Drawables.LogoLight

    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter            = painterResource(logoRes),
            contentDescription = "App Logo",
            modifier           = Modifier
                .size(250.dp)
                .alpha(alphaAnim),
            colorFilter        = null
        )
    }
}

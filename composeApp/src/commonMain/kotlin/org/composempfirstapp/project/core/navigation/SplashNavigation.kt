//package org.composempfirstapp.project.core.navigation
//
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import org.composempfirstapp.project.App
//import org.composempfirstapp.project.core.presentation.SplashScreen
//import org.composempfirstapp.project.profile.presentation.settings.SettingsViewModel
//
//@Composable
//fun SplashNavigation(settingsViewModel: SettingsViewModel) {
//    var showSplash by remember { mutableStateOf(true) }
//    val currentTheme by settingsViewModel.currentTheme.collectAsState()
//
//    if (showSplash) {
//        SplashScreen(
//            onSplashFinished = { showSplash = false },
//            appTheme = currentTheme
//        )
//    } else {
//        App()
//    }
//}

package org.composempfirstapp.project.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.composempfirstapp.project.App
import org.composempfirstapp.project.profile.presentation.settings.SettingsViewModel

@Composable
fun SplashNavigation(settingsViewModel: SettingsViewModel) {
    val currentTheme by settingsViewModel.currentTheme.collectAsState()

    // We can directly go to the main app since the splash screen
    // is now handled by the Android splash screen API
    App()
}
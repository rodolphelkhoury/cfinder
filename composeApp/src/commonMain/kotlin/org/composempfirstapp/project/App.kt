package org.composempfirstapp.project

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.composempfirstapp.project.core.navigation.graphs.RootNavGraph
import org.composempfirstapp.project.profile.presentation.settings.SettingViewModel
import org.composempfirstapp.project.core.theme.CFinderTheme
import org.composempfirstapp.project.core.AppPreferences

@Composable
@Preview
fun App() {
    val appPreferences = remember {
        AppPreferences(
            dataStorePreference()
        )
    }

    // Add test token to AppPreferences on app initialization
    LaunchedEffect(Unit) {
        // Set a test token for development/testing
        appPreferences.saveToken("15|ba8ySDvwzWJnGKp9K8ViUtIeSTxD4G6kf3smsMWl6f5a0a23")
    }

    val settingViewModel = viewModel {
        SettingViewModel(appPreferences)
    }

    val currentTheme by settingViewModel.currentTheme.collectAsState()

    CFinderTheme(
        appTheme = currentTheme
    ) {
        RootNavGraph(settingViewModel, appPreferences)
    }
}
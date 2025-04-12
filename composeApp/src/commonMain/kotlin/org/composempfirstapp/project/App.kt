package org.composempfirstapp.project

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.composempfirstapp.project.navigation.graphs.RootNavGraph
import org.composempfirstapp.project.profile.presentation.settings.SettingViewModel
import org.composempfirstapp.project.theme.CFinderTheme
import org.composempfirstapp.project.utils.AppPreferences
import org.composempfirstapp.project.utils.Theme

// Create a custom factory function for SettingViewModel
@Composable
fun createSettingViewModel(appPreferences: AppPreferences): SettingViewModel {
    return remember(appPreferences) {
        SettingViewModel(appPreferences)
    }
}

@Composable
@Preview
fun App() {
    val appPreferences = remember {
        AppPreferences(
            dataStorePreference()
        )
    }

    // Use our custom factory function to create the ViewModel
    val settingViewModel = createSettingViewModel(appPreferences)

    // Collect the current theme as a state
    val currentTheme by settingViewModel.currentTheme.collectAsState()

    CFinderTheme(
        appTheme = currentTheme
    ) {
        RootNavGraph(settingViewModel)
    }
}
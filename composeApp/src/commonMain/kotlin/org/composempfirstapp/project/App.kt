package org.composempfirstapp.project

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.composempfirstapp.project.navigation.graphs.RootNavGraph
import org.composempfirstapp.project.profile.presentation.settings.SettingViewModel
import org.composempfirstapp.project.theme.CFinderTheme
import org.composempfirstapp.project.utils.AppPreferences
import org.composempfirstapp.project.utils.Theme

@Composable
@Preview
fun App() {

    val appPreferences = remember {
        AppPreferences(
            dataStorePreference()
        )
    }

    val settingViewModel = viewModel {
        SettingViewModel(appPreferences)
    }

    val currentTheme by settingViewModel.currentTheme.collectAsState()

    CFinderTheme(
        appTheme = currentTheme
    ) {
        RootNavGraph(settingViewModel)
    }
}
package org.composempfirstapp.project


import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.composempfirstapp.project.core.navigation.graphs.RootNavGraph
import org.composempfirstapp.project.profile.presentation.settings.SettingViewModel
import org.composempfirstapp.project.core.theme.CFinderTheme
import org.composempfirstapp.project.core.AppPreferences
import org.composempfirstapp.project.profile.presentation.myfavorites.FavoritesViewModel

@Composable
@Preview
fun App() {

    val appPreferences = remember {
        AppPreferences(
            dataStorePreference()
        )
    }

    LaunchedEffect(Unit) {
    }

    val settingViewModel = viewModel {
        SettingViewModel(appPreferences)
    }

    // Create a shared FavoritesViewModel
    val favoritesViewModel = viewModel {
        FavoritesViewModel()
    }

    val currentTheme by settingViewModel.currentTheme.collectAsState()

    CFinderTheme(
        appTheme = currentTheme
    ) {
        RootNavGraph(settingViewModel, appPreferences, favoritesViewModel)
    }
}
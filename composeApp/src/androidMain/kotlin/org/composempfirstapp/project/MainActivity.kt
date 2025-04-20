package org.composempfirstapp.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import org.composempfirstapp.project.core.AppPreferences
import org.composempfirstapp.project.core.navigation.SplashNavigation
import org.composempfirstapp.project.profile.presentation.settings.SettingsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setActivityProvider {
            this
        }

        setContent {
            val appPreferences = AppPreferences(dataStorePreference())
            val settingsViewModel = viewModel { SettingsViewModel(appPreferences) }

            SplashNavigation(settingsViewModel)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    val appPreferences = AppPreferences(dataStorePreference())
    val settingsViewModel = viewModel { SettingsViewModel(appPreferences) }

    SplashNavigation(settingsViewModel)
}



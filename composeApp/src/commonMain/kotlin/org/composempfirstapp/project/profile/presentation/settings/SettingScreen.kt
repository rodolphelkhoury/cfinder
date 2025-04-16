package org.composempfirstapp.project.profile.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import cfinder.composeapp.generated.resources.Res
import cfinder.composeapp.generated.resources.ic_delete
import cfinder.composeapp.generated.resources.ic_light_mode
import cfinder.composeapp.generated.resources.logout
import cfinder.composeapp.generated.resources.settings
import cfinder.composeapp.generated.resources.theme
import org.composempfirstapp.project.profile.presentation.settings.SettingViewModel
import org.composempfirstapp.project.profile.presentation.settings.components.LogoutDialog
import org.composempfirstapp.project.profile.presentation.settings.components.SettingItem
import org.composempfirstapp.project.profile.presentation.settings.components.ThemeSelectionDialog
import org.composempfirstapp.project.core.Theme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    rootNavController: NavHostController,
    settingViewModel: SettingViewModel,
    modifier: Modifier = Modifier
) {

    val currentTheme by settingViewModel.currentTheme.collectAsState()


    var showSelectThemeDialog by remember {
        mutableStateOf(false)
    }

    var showLogoutDialog by remember {
        mutableStateOf(false)
    }

    when {
        showLogoutDialog -> {
            LogoutDialog(
                onDismissRequest = {
                    showLogoutDialog = false
                },
                onLogout = {
                    // TODO logout
                    showLogoutDialog = false

                }
            )
        }
        showSelectThemeDialog -> {
            ThemeSelectionDialog(
                currentTheme = currentTheme ?: Theme.DARK_MODE.name,
                onDismissRequest = {
                    showSelectThemeDialog = false
                },
                onThemeChange = {
                    settingViewModel.changeTheme(it)
                    showSelectThemeDialog = false
                }
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(
                            Res.string.settings
                        )
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            rootNavController.navigateUp()
                        }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.settings)
                        )
                    }
                }

            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            item {
                SettingItem(
                    onClick = {
                        showSelectThemeDialog = true
                    },
                    painter = painterResource(Res.drawable.ic_light_mode),
                    itemName = stringResource(Res.string.theme)
                )
            }
            item {
                SettingItem(
                    onClick = {
                        showLogoutDialog = true
                    },
                    // TODO logout
                    painter = painterResource(Res.drawable.ic_delete),
                    itemName = stringResource(Res.string.logout),
                    itemColor = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}









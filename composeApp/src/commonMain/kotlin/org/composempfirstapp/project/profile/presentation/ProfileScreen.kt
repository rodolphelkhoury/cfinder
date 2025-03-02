package org.composempfirstapp.project.profile.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import org.composempfirstapp.project.navigation.SettingRouteScreen

@Composable
fun ProfileScreen(
    rootNavController: NavHostController,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier,
    ) {
        IconButton(
            onClick = {
                rootNavController.navigate(SettingRouteScreen.Setting.route)
            }
        ) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = "App settings"
            )
        }
    }
}
package org.composempfirstapp.project.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.composempfirstapp.project.court.presentation.CourtDetailScreen
import org.composempfirstapp.project.court.presentation.MainScreen
import org.composempfirstapp.project.navigation.CourtRouteScreen
import org.composempfirstapp.project.navigation.Graph
import org.composempfirstapp.project.navigation.ProfileRouteScreen
import org.composempfirstapp.project.profile.presentation.mycourts.MyCourtScreen
import org.composempfirstapp.project.profile.presentation.myprofile.MyProfileScreen
import org.composempfirstapp.project.profile.presentation.settings.SettingsScreen
import org.composempfirstapp.project.profile.presentation.settings.SettingViewModel
import org.composempfirstapp.project.profile.presentation.aboutus.AboutUsScreen
import org.composempfirstapp.project.utils.courts


@Composable
fun RootNavGraph(
    settingViewModel: SettingViewModel
) {
    val rootNavController = rememberNavController()
    NavHost(
        navController = rootNavController,
        route = Graph.RootScreenGraph,
        startDestination = Graph.MainScreenGraph
    ) {
        composable(
            route = Graph.MainScreenGraph
        ) {
            MainScreen(rootNavController)
        }

        composable(
            route = ProfileRouteScreen.Settings.route
        ) {
            SettingsScreen(rootNavController, settingViewModel)
        }

        composable(
            route = CourtRouteScreen.CourtDetail.route
        ) {
            // Use the first court from the courts list as a default
            // This assumes courts is not empty and is accessible
            rootNavController.previousBackStackEntry?.savedStateHandle?.get<String>("court")?.let { courtJson ->
                // If we have court data in the savedStateHandle, use it
                // For now, we'll use the first court from the courts list
                CourtDetailScreen(rootNavController, courts.first())
            } ?: run {
                // Fallback to the first court if no data is available
                CourtDetailScreen(rootNavController, courts.first())
            }
        }

        // Add new routes for profile section
        composable(
            route = ProfileRouteScreen.MyProfile.route
        ) {
            MyProfileScreen(rootNavController)
        }

        composable(
            route = ProfileRouteScreen.MyCourts.route
        ) {
            MyCourtScreen(rootNavController)
        }

        composable(
            route = ProfileRouteScreen.AboutUs.route
        ) {
            AboutUsScreen(rootNavController)
        }
    }
}
package org.composempfirstapp.project.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.json.Json
import org.composempfirstapp.project.court.presentation.CourtDetailScreen
import org.composempfirstapp.project.court.presentation.MainScreen
import org.composempfirstapp.project.navigation.CourtRouteScreen
import org.composempfirstapp.project.navigation.Graph
import org.composempfirstapp.project.navigation.SettingRouteScreen
import org.composempfirstapp.project.profile.presentation.SettingScreen
import org.composempfirstapp.project.profile.presentation.settings.SettingViewModel

@Composable
fun RootNavGraph(
    settingViewModel: SettingViewModel,
    isUserLoggedIn: Boolean = false // Parameter to determine starting destination
) {
    val rootNavController = rememberNavController()
    NavHost(
        navController = rootNavController,
        route = Graph.RootScreenGraph,
        startDestination = if (isUserLoggedIn) Graph.MainScreenGraph else Graph.AuthScreenGraph
    ) {
        // Authentication graph
        composable(
            route = Graph.AuthScreenGraph
        ) {
            AuthNavGraph(rootNavController)
        }

        // Main application graph
        composable(
            route = Graph.MainScreenGraph
        ) {
            MainScreen(rootNavController)
        }

        // Settings screen
        composable(
            route = SettingRouteScreen.Setting.route
        ) {
            SettingScreen(rootNavController, settingViewModel)
        }

        // Court detail screen
        composable(
            route = CourtRouteScreen.CourtDetail.route
        ) {
            rootNavController.previousBackStackEntry?.savedStateHandle?.get<String>("court")?.let {
                CourtDetailScreen(
                    rootNavController,
                    Json.decodeFromString(it)
                )
            }
        }
    }
}
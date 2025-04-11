package org.composempfirstapp.project.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
            route = SettingRouteScreen.Setting.route
        ) {
            SettingScreen(rootNavController,settingViewModel)
        }

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
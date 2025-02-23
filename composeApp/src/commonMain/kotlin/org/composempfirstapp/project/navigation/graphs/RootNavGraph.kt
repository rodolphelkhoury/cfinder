package org.composempfirstapp.project.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.composempfirstapp.project.court.presentation.MainScreen
import org.composempfirstapp.project.navigation.Graph

@Composable
fun RootNavGraph(modifier: Modifier = Modifier) {
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
    }
}
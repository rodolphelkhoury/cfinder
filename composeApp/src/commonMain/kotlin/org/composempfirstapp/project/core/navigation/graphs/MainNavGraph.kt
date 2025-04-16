package org.composempfirstapp.project.core.navigation.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.composempfirstapp.project.core.AppPreferences
import org.composempfirstapp.project.court.presentation.CourtHomeScreen
import org.composempfirstapp.project.core.navigation.Graph
import org.composempfirstapp.project.core.navigation.MainRouteScreen
import org.composempfirstapp.project.profile.presentation.ProfileScreen
import org.composempfirstapp.project.reservation.presentation.ReservationScreen

@Composable
fun MainNavGraph(
    rootNavController: NavHostController,
    homeNavHostController: NavHostController,
    paddingValues: PaddingValues,
    appPreferences: AppPreferences,
    modifier: Modifier = Modifier
) {
    NavHost(
        modifier = Modifier.fillMaxSize().padding(paddingValues),
        navController = homeNavHostController,
        route = Graph.MainScreenGraph,
        startDestination = MainRouteScreen.Home.route
    ) {
        composable(
            route = MainRouteScreen.Home.route
        ) {
            CourtHomeScreen(rootNavController, appPreferences)
        }
        composable(
            route = MainRouteScreen.Booking.route
        ) {
            ReservationScreen(rootNavController)
        }
        composable(
            route = MainRouteScreen.Profile.route
        ) {
            ProfileScreen(rootNavController)
        }
    }
}
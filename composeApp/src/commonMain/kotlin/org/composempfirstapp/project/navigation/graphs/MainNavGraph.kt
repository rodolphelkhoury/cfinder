package org.composempfirstapp.project.navigation.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.composempfirstapp.project.court.presentation.BookingHomeScreen
import org.composempfirstapp.project.navigation.Graph
import org.composempfirstapp.project.navigation.MainRouteScreen
import org.composempfirstapp.project.offers.presentation.OffersScreen
import org.composempfirstapp.project.profile.presentation.ProfileScreen
import org.composempfirstapp.project.reservation.presentation.ReservationScreen

@Composable
fun MainNavGraph(
    rootNavController: NavHostController,
    homeNavHostController: NavHostController,
    paddingValues: PaddingValues,
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
            BookingHomeScreen()
        }
        composable(
            route = MainRouteScreen.Offers.route
        ) {
            OffersScreen()
        }
        composable(
            route = MainRouteScreen.Booking.route
        ) {
            ReservationScreen()
        }
        composable(
            route = MainRouteScreen.Profile.route
        ) {
            ProfileScreen(rootNavController)
        }
    }
}
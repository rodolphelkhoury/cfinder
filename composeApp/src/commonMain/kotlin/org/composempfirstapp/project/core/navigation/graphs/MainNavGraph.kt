package org.composempfirstapp.project.core.navigation.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.composempfirstapp.project.core.AppPreferences
import org.composempfirstapp.project.court.presentation.CourtHomeScreen
import org.composempfirstapp.project.core.navigation.Graph
import org.composempfirstapp.project.core.navigation.MainRouteScreen
import org.composempfirstapp.project.profile.data.ProfileRepository
import org.composempfirstapp.project.profile.presentation.ProfileScreen
import org.composempfirstapp.project.profile.presentation.ProfileViewModel
import org.composempfirstapp.project.reservation.presentation.ReservationScreen

@Composable
fun MainNavGraph(
    rootNavController: NavHostController,
    homeNavHostController: NavHostController,
    paddingValues: PaddingValues,
    appPreferences: AppPreferences,
    modifier: Modifier = Modifier
) {
    // Create ProfileRepository and ProfileViewModel instances
    val profileRepository = remember { ProfileRepository(appPreferences) }
    val profileViewModel: ProfileViewModel = viewModel { ProfileViewModel(profileRepository) }

    NavHost(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
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
            route = MainRouteScreen.Reservation.route
        ) {
            ReservationScreen(rootNavController, appPreferences)
        }
        composable(
            route = MainRouteScreen.Profile.route
        ) {
            // Pass the profileViewModel parameter
            ProfileScreen(rootNavController, profileViewModel)
        }
    }
}
package org.composempfirstapp.project.navigation.graphs

import LoginScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.composempfirstapp.project.authentication.presentation.AuthViewModel
import org.composempfirstapp.project.authentication.presentation.RegisterScreen
import org.composempfirstapp.project.authentication.presentation.VerifyPhoneNumberScreen
import org.composempfirstapp.project.navigation.AuthRouteScreen
import org.composempfirstapp.project.navigation.Graph

@Composable
fun AuthNavGraph(
    rootNavController: NavHostController
) {
    val authNavController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    // Check login and verification status
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    val isPhoneVerified by authViewModel.isPhoneVerified.collectAsState()

    // React to login state changes
    if (isLoggedIn) {
        if (isPhoneVerified) {
            // If user is logged in and phone is verified, navigate to main app
            rootNavController.navigate(Graph.MainScreenGraph) {
                popUpTo(Graph.AuthScreenGraph) {
                    inclusive = true
                }
            }
        } else {
            // If user is logged in but phone is not verified,
            // we should navigate to verification screen
            val customer = authViewModel.customer.collectAsState().value
            if (customer != null) {
                authNavController.navigate(AuthRouteScreen.VerifyPhoneNumber.route) {
                    popUpTo(AuthRouteScreen.Login.route) {
                        inclusive = true
                    }
                }
            }
        }
    }

    NavHost(
        navController = authNavController,
        route = Graph.AuthScreenGraph,
        startDestination = AuthRouteScreen.Login.route
    ) {
        composable(
            route = AuthRouteScreen.Login.route
        ) {
            LoginScreen(
                onNavigateToRegister = {
                    authNavController.navigate(AuthRouteScreen.Register.route)
                },
                onNavigateToVerification = { phoneNumber ->
                    authNavController.currentBackStackEntry?.savedStateHandle?.set("phoneNumber", phoneNumber)
                    authNavController.navigate(AuthRouteScreen.VerifyPhoneNumber.route)
                },
                onLoginSuccess = {
                    // Navigation to main or verification is now handled by the observer above
                }
            )
        }

        composable(
            route = AuthRouteScreen.Register.route
        ) {
            RegisterScreen(
                onNavigateToLogin = {
                    authNavController.navigate(AuthRouteScreen.Login.route) {
                        popUpTo(AuthRouteScreen.Register.route) {
                            inclusive = true
                        }
                    }
                },
                onRegistrationSuccess = { phoneNumber ->
                    // Navigation to verification is now handled by the observer above
                    // but we'll still pass the phone number for verification
                    authNavController.currentBackStackEntry?.savedStateHandle?.set("phoneNumber", phoneNumber)
                },
                viewModel = authViewModel  // Add this line to pass the viewModel
            )
        }

        composable(
            route = AuthRouteScreen.VerifyPhoneNumber.route
        ) {
            val phoneNumber = authNavController.previousBackStackEntry?.savedStateHandle?.get<String>("phoneNumber")
                ?: authViewModel.customer.collectAsState().value?.phoneNumber
                ?: ""

            VerifyPhoneNumberScreen(
                phoneNumber = phoneNumber,
                onVerificationSuccess = {
                    // Navigation to main is now handled by the observer above
                },
                onNavigateBack = {
                    authNavController.popBackStack()
                }
            )
        }
    }
}
package org.composempfirstapp.project.navigation.graphs

import LoginScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import org.composempfirstapp.project.authentication.presentation.RegisterScreen
import org.composempfirstapp.project.authentication.presentation.VerifyPhoneNumberScreen
import org.composempfirstapp.project.navigation.AuthRouteScreen
import org.composempfirstapp.project.navigation.Graph



@Composable
fun AuthNavGraph(
    rootNavController: NavHostController
) {
    val authNavController = rememberNavController()

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
                    // Navigate to main app after successful login
                    rootNavController.navigate(Graph.MainScreenGraph) {
                        popUpTo(Graph.AuthScreenGraph) {
                            inclusive = true
                        }
                    }
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
                    // After registration, usually we verify phone number
                    authNavController.currentBackStackEntry?.savedStateHandle?.set("phoneNumber", phoneNumber)
                    authNavController.navigate(AuthRouteScreen.VerifyPhoneNumber.route)
                }
            )
        }

        composable(
            route = AuthRouteScreen.VerifyPhoneNumber.route
        ) {
            val phoneNumber = authNavController.previousBackStackEntry?.savedStateHandle?.get<String>("phoneNumber") ?: ""

            VerifyPhoneNumberScreen(
                phoneNumber = phoneNumber,
                onVerificationSuccess = {
                    // Navigate to main app after successful verification
                    rootNavController.navigate(Graph.MainScreenGraph) {
                        popUpTo(Graph.AuthScreenGraph) {
                            inclusive = true
                        }
                    }
                },
                onNavigateBack = {
                    authNavController.popBackStack()
                }
            )
        }
    }
}
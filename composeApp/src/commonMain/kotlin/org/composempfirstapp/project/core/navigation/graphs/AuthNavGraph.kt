package org.composempfirstapp.project.core.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.composempfirstapp.project.core.authentication.presentation.AuthViewModel
import org.composempfirstapp.project.core.authentication.presentation.LoginScreen
import org.composempfirstapp.project.core.authentication.presentation.RegisterScreen
import org.composempfirstapp.project.core.authentication.presentation.VerifyOtpScreen
import org.composempfirstapp.project.core.navigation.AuthRoutes
import org.composempfirstapp.project.core.navigation.Graph

@Composable
fun AuthNavGraph(
    navController: NavHostController,
    rootNavController: NavHostController,
    authViewModel: AuthViewModel
) {
    NavHost(
        navController = navController,
        route = AuthRoutes.AUTH_GRAPH,
        startDestination = AuthRoutes.LOGIN
    ) {
        composable(route = AuthRoutes.LOGIN) {
            LoginScreen(
                navController = navController,
                authViewModel = authViewModel,
                onLoginSuccess = {
                    rootNavController.navigate(Graph.MainScreenGraph) {
                        popUpTo(AuthRoutes.AUTH_GRAPH) { inclusive = true }
                    }
                }
            )
        }

        composable(route = AuthRoutes.REGISTER) {
            RegisterScreen(
                navController = navController,
                authViewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate(AuthRoutes.VERIFY_OTP)
                }
            )
        }

        composable(route = AuthRoutes.VERIFY_OTP) {
            VerifyOtpScreen(
                navController = navController,
                authViewModel = authViewModel,
                onVerificationSuccess = {
                    rootNavController.navigate(Graph.MainScreenGraph) {
                        popUpTo(AuthRoutes.AUTH_GRAPH) { inclusive = true }
                    }
                }
            )
        }
    }
}
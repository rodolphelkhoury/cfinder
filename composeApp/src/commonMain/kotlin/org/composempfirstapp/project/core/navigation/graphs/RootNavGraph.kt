package org.composempfirstapp.project.core.navigation.graphs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import kotlinx.serialization.json.Json
import org.composempfirstapp.project.core.AppPreferences
import org.composempfirstapp.project.core.authentication.data.AuthRepository
import org.composempfirstapp.project.core.authentication.presentation.AuthViewModel
import org.composempfirstapp.project.core.authentication.presentation.LoginScreen
import org.composempfirstapp.project.core.authentication.presentation.RegisterScreen
import org.composempfirstapp.project.core.authentication.presentation.VerifyOtpScreen
import org.composempfirstapp.project.core.navigation.AuthRoutes
import org.composempfirstapp.project.court.presentation.CourtDetailScreen
import org.composempfirstapp.project.court.presentation.MainScreen
import org.composempfirstapp.project.core.navigation.CourtRouteScreen
import org.composempfirstapp.project.core.navigation.Graph
import org.composempfirstapp.project.core.navigation.SettingRouteScreen
import org.composempfirstapp.project.profile.presentation.SettingScreen
import org.composempfirstapp.project.profile.presentation.settings.SettingViewModel


@Composable
fun RootNavGraph(
    settingViewModel: SettingViewModel,
    appPreferences: AppPreferences
) {
    val rootNavController = rememberNavController()

    val authRepository = remember { AuthRepository(appPreferences) }
    val authViewModel = viewModel { AuthViewModel(authRepository) }

    var startDestination by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }

    // Check if user is logged in and phone is verified
    LaunchedEffect(Unit) {
        val isLoggedIn = authRepository.isLoggedIn()
        val isPhoneVerified = authRepository.isPhoneVerified()

        startDestination = when {
            !isLoggedIn -> AuthRoutes.AUTH_GRAPH
            !isPhoneVerified -> AuthRoutes.VERIFY_OTP
            else -> Graph.MainScreenGraph
        }

        isLoading = false
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        NavHost(
            navController = rootNavController,
            route = Graph.RootScreenGraph,
            startDestination = startDestination
        ) {
            // Authentication graph
            navigation(
                route = AuthRoutes.AUTH_GRAPH,
                startDestination = AuthRoutes.LOGIN
            ) {
                composable(route = AuthRoutes.LOGIN) {
                    LoginScreen(
                        navController = rootNavController,
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
                        navController = rootNavController,
                        authViewModel = authViewModel,
                        onRegisterSuccess = {
                            rootNavController.navigate(AuthRoutes.VERIFY_OTP)
                        }
                    )
                }

                composable(route = AuthRoutes.VERIFY_OTP) {
                    VerifyOtpScreen(
                        navController = rootNavController,
                        authViewModel = authViewModel,
                        onVerificationSuccess = {
                            rootNavController.navigate(Graph.MainScreenGraph) {
                                popUpTo(AuthRoutes.AUTH_GRAPH) { inclusive = true }
                            }
                        }
                    )
                }
            }

            // Main graph - existing code
            composable(route = Graph.MainScreenGraph) {
                MainScreen(rootNavController, appPreferences)
            }

            composable(route = SettingRouteScreen.Setting.route) {
                SettingScreen(rootNavController, settingViewModel)
            }

            composable(route = CourtRouteScreen.CourtDetail.route) {
                rootNavController.previousBackStackEntry?.savedStateHandle?.get<String>("court")?.let {
                    CourtDetailScreen(
                        rootNavController,
                        Json.decodeFromString(it)
                    )
                }
            }
        }
    }
}
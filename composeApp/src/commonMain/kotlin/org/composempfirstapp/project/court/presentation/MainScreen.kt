package org.composempfirstapp.project.court.presentation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.composempfirstapp.project.core.AppPreferences
import org.composempfirstapp.project.core.components.TopBar
import org.composempfirstapp.project.core.navigation.BottomNavigationBar
import org.composempfirstapp.project.core.navigation.graphs.MainNavGraph
import org.composempfirstapp.project.core.bottomNavigationList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    rootNavController: NavHostController,
    appPreferences: AppPreferences,
    modifier: Modifier = Modifier,
) {
    val homeNavContrller = rememberNavController()
    val navBackStackEntry by homeNavContrller.currentBackStackEntryAsState()

    val currentRoute by rememberSaveable(navBackStackEntry) { mutableStateOf(
        navBackStackEntry?.destination?.route
    ) }

    // Here we mock the notification count, you can replace this with dynamic data later.
    val notificationCount = 3  // This is a static value for testing.

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        containerColor = MaterialTheme.colorScheme.background,  // Automatically adapts to dark/light theme
        topBar = {
            TopBar(
                notificationCount = notificationCount,
                onNotificationClick = { rootNavController.navigate("notifications") }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                 items = bottomNavigationList,
                currentRoute = currentRoute,
                onItemClick = { currentBottomNavigationItem ->
                    homeNavContrller.navigate(currentBottomNavigationItem.route) {
                        homeNavContrller.graph.startDestinationRoute?.let {
                            popUpTo(it) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) {
        MainNavGraph(
            rootNavController,
            homeNavContrller,
            it,
            appPreferences
        )
    }
}

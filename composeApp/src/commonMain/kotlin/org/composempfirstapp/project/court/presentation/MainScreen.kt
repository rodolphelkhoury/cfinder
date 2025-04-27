package org.composempfirstapp.project.court.presentation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.composempfirstapp.project.core.AppPreferences
import org.composempfirstapp.project.core.navigation.BottomNavigationBar
import org.composempfirstapp.project.core.navigation.graphs.MainNavGraph
import org.composempfirstapp.project.core.bottomNavigationList
import org.composempfirstapp.project.core.components.TopBar
import org.composempfirstapp.project.profile.presentation.ProfileViewModel
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    rootNavController: NavHostController,
    appPreferences: AppPreferences,
    profileViewModel: ProfileViewModel,
    modifier: Modifier = Modifier,
) {
    val homeNavContrller = rememberNavController()
    val navBackStackEntry by homeNavContrller.currentBackStackEntryAsState()

    // for route icon clicked
    val currentRoute by rememberSaveable(navBackStackEntry) { mutableStateOf(
        navBackStackEntry?.destination?.route
    ) }


    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        containerColor = MaterialTheme.colorScheme.background,  // Automatically adapts to dark/light theme
        topBar = {
            TopBar(

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
            appPreferences,
            profileViewModel
        )
    }
}
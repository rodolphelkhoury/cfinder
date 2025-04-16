package org.composempfirstapp.project.court.presentation

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
import org.composempfirstapp.project.core.navigation.BottomNavigationBar
import org.composempfirstapp.project.core.navigation.graphs.MainNavGraph
import org.composempfirstapp.project.core.bottomNavigationList
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    rootNavController: NavHostController,
    modifier: Modifier = Modifier,
) {

    val homeNavContrller = rememberNavController()
    val navBackStackEntry by homeNavContrller.currentBackStackEntryAsState()

    // for route icon clicked
    val currentRoute by rememberSaveable(navBackStackEntry) { mutableStateOf(
        navBackStackEntry?.destination?.route
    ) }

    val topBarTitle by remember(currentRoute) {
        derivedStateOf {
            if (currentRoute != null) {
                bottomNavigationList[bottomNavigationList.indexOfFirst {
                    it.route == currentRoute
                }].title
            } else {
                bottomNavigationList[0].title
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(topBarTitle),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
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
            it
        )
    }
}
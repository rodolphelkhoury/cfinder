package org.composempfirstapp.project.navigation.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.NavigationBar
import org.composempfirstapp.project.navigation.domain.NavItem
import org.composempfirstapp.project.navigation.domain.NavTitle
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier


@Composable
fun MainScreen(
    modifier: Modifier = Modifier
) {
    val navItemList = listOf(
        NavItem(NavTitle.HOME, Icons.Default.Home),
        NavItem(NavTitle.OFFERS, Icons.Default.Menu),
        NavItem(NavTitle.BOOKING, Icons.Default.Favorite),
        NavItem(NavTitle.PROFILE, Icons.Default.Person)
    )

    var selectedIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                navItemList.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        icon = { Icon(navItem.icon, contentDescription = navItem.label) },
                        label = { Text(text = navItem.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        ContentScreen(modifier = Modifier.padding(innerPadding), selectedIndex)
    }
}

@Composable
fun ContentScreen(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
) {
    when (selectedIndex) {
        0 -> HomeScreen()
        1 -> OffersScreen()
        2 -> BookingScreen()
        3 -> ProfileScreen()
    }
}
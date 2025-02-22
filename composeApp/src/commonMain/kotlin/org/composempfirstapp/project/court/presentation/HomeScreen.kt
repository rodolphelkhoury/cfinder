package org.composempfirstapp.project.court.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import org.composempfirstapp.project.navigation.BottomNavigationBar
import org.composempfirstapp.project.utils.bottomNavigationList

// NOT sure ha lashou
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    var currentRoute by remember { mutableStateOf(bottomNavigationList[0].route) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Home",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                actions = {
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Notifications,
                            // get from RES string
                            contentDescription = "notifications"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                items = bottomNavigationList,
                currentRoute = currentRoute,
                onItemClick = { currentBottomNavigationItem ->
                    currentRoute = currentBottomNavigationItem.route
                }
            )
        }
    ) {
        Column {
            Text(text = "Hello wwww")
        }
    }
}
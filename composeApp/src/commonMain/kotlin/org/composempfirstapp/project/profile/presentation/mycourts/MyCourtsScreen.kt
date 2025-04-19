package org.composempfirstapp.project.profile.presentation.mycourts

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.composempfirstapp.project.core.theme.mediumPadding


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCourtScreen(
    rootNavController: NavHostController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Courts") },
                navigationIcon = {
                    IconButton(onClick = { rootNavController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 24.dp, start = mediumPadding, end = mediumPadding, bottom = mediumPadding), // Increased top padding
            contentAlignment = Alignment.Center // Center the content
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Centered text
                Text(
                    text = "My Courts Page",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

            }
        }
    }
}
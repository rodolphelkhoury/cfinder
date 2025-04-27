package org.composempfirstapp.project.core.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    TopAppBar(
        modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
        title = {
            Text(
                text = "CourtConnect",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium
                    .copy(fontWeight = FontWeight.ExtraBold)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

package org.composempfirstapp.project.core.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.painterResource
import cfinder.composeapp.generated.resources.Res
import cfinder.composeapp.generated.resources.ic_notification

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    notificationCount: Int,
    onNotificationClick: () -> Unit
) {
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
        actions = {
            NotificationIconWithBadge(notificationCount) {
                onNotificationClick()        // ← invoke it here
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

package org.composempfirstapp.project.core.components


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import cfinder.composeapp.generated.resources.Res
import cfinder.composeapp.generated.resources.ic_notification
import org.jetbrains.compose.resources.painterResource


@Composable
fun NotificationIconWithBadge(notificationCount: Int, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clickable { onClick() }
            .padding(4.dp) // Add some padding around the icon
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_notification),
            contentDescription = "Notifications",
            modifier = Modifier.fillMaxSize(),
            tint = MaterialTheme.colorScheme.primary
        )

        // Badge Circle
        if (notificationCount > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(15.dp) // Size of the badge circle
                    .background(
                        color = Color.Red,
                        shape = CircleShape
                    )

                    .padding(0.dp) // Padding inside the badge to position the text better
            ) {
                Text(
                    text = notificationCount.toString(),
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 8.sp // Adjust the font size to make the text smaller
                    ),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

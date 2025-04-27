package org.composempfirstapp.project.reservation.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cfinder.composeapp.generated.resources.Res
import cfinder.composeapp.generated.resources.ic_delete
import coil3.compose.AsyncImage
import org.composempfirstapp.project.core.theme.imageSize
import org.composempfirstapp.project.reservation.domain.Reservation
import org.jetbrains.compose.resources.painterResource

@Composable
fun ReservationComponent(
    modifier: Modifier = Modifier,
    reservation: Reservation,
    onClick: () -> Unit,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 0.dp, horizontal = 12.dp)
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 4.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(imageSize)
                    .clip(MaterialTheme.shapes.large)
                    .background(Color.Gray),
                model = reservation.court?.imageUrl,
                error = painterResource(Res.drawable.ic_delete),
                contentDescription = null
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Court name
                Text(
                    text = reservation.court?.name ?: "Unknown Court",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2
                )

                // Date
                Text(
                    text = reservation.reservationDate,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )

                // Time
                Text(
                    text = "${reservation.startTime} - ${reservation.endTime}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )

                // Price
                Text(
                    text = "$${reservation.totalPrice}$/ hour",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            }

            if (reservation.isCanceled) {
                Icon(
                    painter = painterResource(Res.drawable.ic_delete),
                    contentDescription = "Canceled",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
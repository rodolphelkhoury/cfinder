package org.composempfirstapp.project.reservation.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cfinder.composeapp.generated.resources.Res
import cfinder.composeapp.generated.resources.ic_delete
import coil3.compose.AsyncImage
import org.composempfirstapp.project.core.theme.mediumPadding
import org.composempfirstapp.project.reservation.domain.Reservation
import org.jetbrains.compose.resources.painterResource

@Composable
fun ReservationComponent(
    modifier: Modifier = Modifier,
    reservation: Reservation,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(mediumPadding),
            horizontalArrangement = Arrangement.spacedBy(mediumPadding)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Court name
                Text(
                    text = reservation.court?.name ?: "Unknown Court",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                // Date
                Text(
                    text = reservation.reservationDate,
                    style = MaterialTheme.typography.bodyMedium
                )

                // Time
                Text(
                    text = "${reservation.startTime} - ${reservation.endTime}",
                    style = MaterialTheme.typography.bodyMedium
                )

                // Price
                Text(
                    text = "$${reservation.totalPrice}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            // Status indicators
            if (reservation.isCanceled) {
                Icon(
                    // TODO
                    painter = painterResource(Res.drawable.ic_delete),
                    contentDescription = "Canceled",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
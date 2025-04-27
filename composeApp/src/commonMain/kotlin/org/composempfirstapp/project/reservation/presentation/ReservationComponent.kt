package org.composempfirstapp.project.reservation.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cfinder.composeapp.generated.resources.Res
import cfinder.composeapp.generated.resources.ic_delete
import kotlinx.datetime.LocalDate
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month
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

                // Date with full format
                Text(
                    text = formatFullDate(reservation.reservationDate),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )

                // Time
                Text(
                    text = "${formatTimeWithAMPM(reservation.startTime)} - ${formatTimeWithAMPM(reservation.endTime)}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )

                // Price
                Text(
                    text = "$${reservation.totalPrice}$",
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

private fun formatTimeWithAMPM(time: String): String {
    val parts = time.split(":")
    if (parts.size < 2) return time

    val hour = parts[0].toIntOrNull() ?: return time
    val minute = parts[1]

    val amPm = if (hour < 12) "AM" else "PM"
    val hour12 = when {
        hour == 0 -> 12
        hour > 12 -> hour - 12
        else -> hour
    }

    return "$hour12:$minute $amPm"
}

private fun formatFullDate(dateString: String): String {
    try {
        val dateParts = dateString.split("-")
        if (dateParts.size != 3) return dateString

        val year = dateParts[0].toIntOrNull() ?: return dateString
        val month = dateParts[1].toIntOrNull() ?: return dateString
        val day = dateParts[2].toIntOrNull() ?: return dateString

        val date = LocalDate(year, month, day)
        val dayOfWeek = getDayName(date.dayOfWeek)
        val monthName = getMonthName(date.month)

        return "$dayOfWeek, $monthName $day, $year"
    } catch (e: Exception) {
        return dateString
    }
}

private fun getDayName(dayOfWeek: DayOfWeek): String {
    return when (dayOfWeek) {
        DayOfWeek.MONDAY -> "Monday"
        DayOfWeek.TUESDAY -> "Tuesday"
        DayOfWeek.WEDNESDAY -> "Wednesday"
        DayOfWeek.THURSDAY -> "Thursday"
        DayOfWeek.FRIDAY -> "Friday"
        DayOfWeek.SATURDAY -> "Saturday"
        DayOfWeek.SUNDAY -> "Sunday"
        else -> "Unknown"
    }
}

private fun getMonthName(month: Month): String {
    return when (month) {
        Month.JANUARY -> "January"
        Month.FEBRUARY -> "February"
        Month.MARCH -> "March"
        Month.APRIL -> "April"
        Month.MAY -> "May"
        Month.JUNE -> "June"
        Month.JULY -> "July"
        Month.AUGUST -> "August"
        Month.SEPTEMBER -> "September"
        Month.OCTOBER -> "October"
        Month.NOVEMBER -> "November"
        Month.DECEMBER -> "December"
        else -> "Unknown"
    }
}
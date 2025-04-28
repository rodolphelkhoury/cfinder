package org.composempfirstapp.project.reservation.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cfinder.composeapp.generated.resources.*
import coil3.compose.AsyncImage
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import org.composempfirstapp.project.CourtLocationMap
import org.composempfirstapp.project.core.theme.detailImageSize
import org.composempfirstapp.project.core.theme.mediumPadding
import org.composempfirstapp.project.core.theme.xLargePadding
import org.composempfirstapp.project.openInExternalMaps
import org.composempfirstapp.project.reservation.domain.Reservation
import org.composempfirstapp.project.shareLink
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationDetailScreen(
    navController: NavController,
    currentReservation: Reservation,
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val currentCourt = currentReservation.court ?: return


    var snackbarMessage by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Reservation",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.court_details)
                        )
                    }
                },
                actions = {
                    currentCourt.imageUrl?.let {
                        IconButton(onClick = { shareLink(it) }) {
                            Icon(Icons.Filled.Share,
                                contentDescription = "Share",
                                tint = MaterialTheme.colorScheme.primary)
                        }

//                        IconButton(onClick = { uriHandler.openUri(it) }) {
//                            Icon(
//                                painter = painterResource(Res.drawable.ic_browse),
//                                contentDescription = "Open in browser",
//                                tint = MaterialTheme.colorScheme.primary
//                            )
//                        }
                    }

                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = mediumPadding, vertical = xLargePadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                AsyncImage(
                    model = currentCourt.imageUrl,
                    error = painterResource(Res.drawable.logo),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(detailImageSize)
                        .clip(MaterialTheme.shapes.medium)
                        .background(Color.Gray)
                )
            }

            item {
                Text(
                    text = currentCourt.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            currentCourt.description?.let {
                item {
                    Text(
                        text = "Description:",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Group time, price, and date with minimal spacing
            item {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = MaterialTheme.typography.titleMedium.toSpanStyle().copy(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append("Date: ")
                            }
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) {
                                append(formatFullDate(currentReservation.reservationDate))
                            }
                        }
                    )

                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = MaterialTheme.typography.titleMedium.toSpanStyle().copy(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append("Time: ")
                            }
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) {
                                append("${formatTimeWithAMPM(currentReservation.startTime)} - ${formatTimeWithAMPM(currentReservation.endTime)}")
                            }
                        }
                    )

                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = MaterialTheme.typography.titleMedium.toSpanStyle().copy(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append("Price: ")
                            }
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) {
                                append("$${currentReservation.totalPrice}")
                            }
                        }
                    )
                }
            }
        }
    }
}

private fun formatTimeWithAMPM(time: String): String {
    // Handle time format with seconds (e.g., "15:00:00")
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
        // Assuming dateString is in format "yyyy-MM-dd"
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
        else -> "Unknown" // Add else branch for exhaustiveness
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
        else -> "Unknown" // Add else branch for exhaustiveness
    }
}
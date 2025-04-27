package org.composempfirstapp.project.court.presentation

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.navigation.NavController
import org.composempfirstapp.project.court.data.TimeSlot
import org.composempfirstapp.project.court.domain.Court
import org.composempfirstapp.project.core.Resource
import org.composempfirstapp.project.core.navigation.MainRouteScreen
import kotlinx.datetime.LocalTime
import kotlinx.datetime.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import org.composempfirstapp.project.court.data.AvailableReservationsResponse
import org.composempfirstapp.project.reservation.data.ReservationsResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourtReservationScreen(
    navController: NavController,
    court: Court,
    viewModel: CourtViewModel
) {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val selectedDate = remember { mutableStateOf(now) }
    val selectedTimeSlot = remember { mutableStateOf<TimeSlot?>(null) }
    val timeSlotsState by viewModel.availableTimeSlotsFlow.collectAsState()
    val reservationStatus by viewModel.reservationStatus.collectAsState()

    val currentMonth = remember {
        mutableStateOf(Pair(now.year, now.monthNumber))
    }

    // Show a success message when reservation is created
    var showSuccessDialog by remember { mutableStateOf(false) }

    LaunchedEffect(selectedDate.value) {
        viewModel.getAvailableReservations(
            court.id,
            selectedDate.value.toString()
        )
    }

    // Handle reservation success
    LaunchedEffect(reservationStatus) {
        when (reservationStatus) {
            is Resource.Success -> {
                // Update available time slots with the new data from response
                val response = (reservationStatus as Resource.Success<AvailableReservationsResponse>).data

                // Update the available slots with the response data
                response.availableReservations?.let { slots ->
                    viewModel.updateAvailableTimeSlots(slots)
                }

                // Reset selected time slot since it's now reserved
                selectedTimeSlot.value = null

                // Show success dialog instead of navigating immediately
                showSuccessDialog = true
            }
            else -> {}
        }
    }

    // Success dialog
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                // Reset reservation status after showing success
                viewModel.resetReservationStatus()
            },
            title = { Text("Reservation Successful") },
            text = {
                Text("Your court has been successfully reserved. You can view your reservation in the reservations list.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        viewModel.resetReservationStatus()
                    }
                ) {
                    Text("Okay")
                }
            }
        )
    }

    // Show loading dialog when creating reservation
    if (reservationStatus is Resource.Loading) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Creating Reservation") },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CircularProgressIndicator()
                    Text(
                        "Processing your reservation...",
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            },
            confirmButton = { }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Reserve ${court.name}",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                    )
                }
                ,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            // Calendar section
            Text(
                text = "Select Date",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    val (year, month) = currentMonth.value
                    val newMonth = if (month == 1) 12 else month - 1
                    val newYear = if (month == 1) year - 1 else year
                    currentMonth.value = Pair(newYear, newMonth)
                }) {
                    Icon(Icons.Default.KeyboardArrowLeft, "Previous Month")
                }

                val (year, month) = currentMonth.value
                Text(
                    text = "${Month(month).name.lowercase().replaceFirstChar { it.uppercase() }} $year",
                    style = MaterialTheme.typography.titleMedium
                )

                IconButton(onClick = {
                    val (year, month) = currentMonth.value
                    val newMonth = if (month == 12) 1 else month + 1
                    val newYear = if (month == 12) year + 1 else year
                    currentMonth.value = Pair(newYear, newMonth)
                }) {
                    Icon(Icons.Default.KeyboardArrowRight, "Next Month")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa").forEach { day ->
                    Text(
                        text = day,
                        modifier = Modifier.padding(4.dp),
                        fontWeight = FontWeight.Bold,
//                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            val (year, month) = currentMonth.value
            val firstDay = LocalDate(year, month, 1)
            val firstDayNextMonth = if (month == 12) {
                LocalDate(year + 1, 1, 1)
            } else {
                LocalDate(year, month + 1, 1)
            }
            val lastDay = firstDayNextMonth.minus(DatePeriod(days = 1))

            val firstDayOfWeek = firstDay.dayOfWeek.isoDayNumber % 7

            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                items(firstDayOfWeek) {
                    Box(modifier = Modifier.size(32.dp))
                }

                items(lastDay.dayOfMonth) { day ->
                    val date = LocalDate(year, month, day + 1)
                    val isSelected = selectedDate.value == date
                    val isPast = date < now

                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primary
                                else Color.Transparent
                            )
                            .clickable(enabled = !isPast) {
                                selectedDate.value = date
                                selectedTimeSlot.value = null
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (day + 1).toString(),
                            color = when {
                                isSelected -> MaterialTheme.colorScheme.onPrimary
                                isPast -> Color.Gray
                                else -> MaterialTheme.colorScheme.onSurface
                            },
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Divider(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
            )

            Text(
                text = "Available Times for ${formatDate(selectedDate.value)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )


            // Available time slots section
            when (timeSlotsState) {
                is Resource.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is Resource.Error -> {
                    val errorMessage = (timeSlotsState as Resource.Error).message
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Error: $errorMessage",
                                color = MaterialTheme.colorScheme.error
                            )
                            Button(
                                onClick = {
                                    viewModel.getAvailableReservations(
                                        court.id,
                                        selectedDate.value.toString()
                                    )
                                },
                                modifier = Modifier.padding(top = 16.dp)
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }

                is Resource.Success -> {
                    val timeSlots = (timeSlotsState as Resource.Success<List<TimeSlot>>).data
                    if (timeSlots.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No available time slots for this date.")
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(timeSlots) { timeSlot ->
                                val startTime = LocalTime.parse(timeSlot.startTime)
                                val endTime = LocalTime.parse(timeSlot.endTime)

                                val isSelected = selectedTimeSlot.value == timeSlot

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { selectedTimeSlot.value = timeSlot },
                                    shape = RoundedCornerShape(8.dp),
                                    elevation = CardDefaults.cardElevation(
                                        defaultElevation = if (isSelected) 4.dp else 1.dp
                                    ),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected)
                                            MaterialTheme.colorScheme.primaryContainer
                                        else
                                            MaterialTheme.colorScheme.surface
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(12.dp)
                                            .fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = formatTime(startTime),
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Text(
                                            text = "to",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = formatTime(endTime),
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        if (isSelected) {
                                            Icon(
                                                Icons.Default.Check,
                                                contentDescription = "Selected",
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.padding(top = 4.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                else -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Select a date to see available time slots")
                    }
                }
            }

            Button(
                onClick = {
                    selectedTimeSlot.value?.let { timeSlot ->
                        viewModel.createReservation(
                            courtId = court.id,
                            date = selectedDate.value.toString(),
                            timeSlot = timeSlot
                        )
                    }
                },
                enabled = selectedTimeSlot.value != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text("Confirm Reservation")
            }
        }
    }

    // Add error handling for reservation errors
    if (reservationStatus is Resource.Error) {
        val errorMessage = (reservationStatus as Resource.Error).message
        AlertDialog(
            onDismissRequest = { viewModel.resetReservationStatus() },
            title = { Text("Reservation Failed") },
            text = { Text("Error: $errorMessage") },
            confirmButton = {
                Button(onClick = { viewModel.resetReservationStatus() }) {
                    Text("OK")
                }
            }
        )
    }
}

fun formatDate(date: LocalDate): String {
    val dayOfWeek = date.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
    val month = date.month.name.lowercase().replaceFirstChar { it.uppercase() }
    return "$dayOfWeek, $month ${date.dayOfMonth}, ${date.year}"
}

fun formatTime(time: LocalTime): String {
    val hour = if (time.hour % 12 == 0) 12 else time.hour % 12
    val amPm = if (time.hour < 12) "AM" else "PM"
    val minute = if (time.minute < 10) "0${time.minute}" else "${time.minute}"
    return "$hour:$minute $amPm"
}
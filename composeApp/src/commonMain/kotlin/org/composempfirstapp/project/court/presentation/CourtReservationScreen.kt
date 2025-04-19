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

    val currentMonth = remember {
        mutableStateOf(Pair(now.year, now.monthNumber))
    }

    LaunchedEffect(selectedDate.value) {
        viewModel.getAvailableReservations(
            court.id,
            selectedDate.value.toString()
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reserve Court") },
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
            Text(
                text = "Reserve ${court.name}",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Select Date",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
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
                        fontWeight = FontWeight.Bold
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
                modifier = Modifier.padding(bottom = 8.dp)
            )

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
                        Text("Waiting for time slot data...")
                    }
                }
            }

            Button(
                onClick = {
                    selectedTimeSlot.value?.let {
                        navController.navigate(MainRouteScreen.Reservation.route) {
                            popUpTo(MainRouteScreen.Home.route)
                        }
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

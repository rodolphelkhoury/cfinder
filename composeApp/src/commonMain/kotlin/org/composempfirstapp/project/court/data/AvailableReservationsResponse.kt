package org.composempfirstapp.project.court.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AvailableReservationsResponse(
    @SerialName("available_reservations")
    val availableReservations: List<TimeSlot>
)

@Serializable
data class TimeSlot(
    @SerialName("start_time")
    val startTime: String,

    @SerialName("end_time")
    val endTime: String
)
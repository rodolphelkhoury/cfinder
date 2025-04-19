package org.composempfirstapp.project.reservation.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReservationRequest(
    @SerialName("reservation_date")
    val reservation_date: String,
    @SerialName("start_time")
    val start_time: String,
    @SerialName("end_time")
    val end_time: String
)

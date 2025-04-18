package org.composempfirstapp.project.reservation.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.composempfirstapp.project.reservation.domain.Reservation

@Serializable
data class ReservationsResponse(
    @SerialName("upcoming")
    val upcoming: List<Reservation>,

    @SerialName("completed")
    val completed: List<Reservation>,

    @SerialName("status")
    val status: String,
)

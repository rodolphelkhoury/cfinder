package org.composempfirstapp.project.reservation.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ReservationErrorResponse(
    @SerialName("code")
    val code: String,
    @SerialName("message")
    val message: String,
    @SerialName("status")
    val status: String
)
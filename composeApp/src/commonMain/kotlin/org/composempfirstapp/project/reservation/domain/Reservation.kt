package org.composempfirstapp.project.reservation.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.composempfirstapp.project.court.domain.Court

@Serializable
data class Reservation(
    @SerialName("id")
    val id: Long,

    @SerialName("court_id")
    val courtId: Long?,

    @SerialName("section_id")
    val sectionId: Long?,

    @SerialName("customer_id")
    val customerId: Long,

    @SerialName("reservation_date")
    val reservationDate: String,

    @SerialName("start_time")
    val startTime: String,

    @SerialName("end_time")
    val endTime: String,

    @SerialName("total_price")
    val totalPrice: Double,

    @SerialName("is_canceled")
    val isCanceled: Boolean = false,

    @SerialName("is_no_show")
    val isNoShow: Boolean = false,

    @SerialName("court")
    val court: Court? = null
)

package org.composempfirstapp.project.court.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Court(
    @SerialName("id")
    val id: Long,

    @SerialName("complex_id")
    val complexId: Long,

    @SerialName("court_type_id")
    val courtTypeId: Long,

    @SerialName("surface_type_id")
    val surfaceTypeId: Long,

    @SerialName("court_type_name")
    val courtType: String,

    @SerialName("surface_type_name")
    val surfaceType: String,

    @SerialName("name")
    val name: String,

    @SerialName("description")
    val description: String?,

    @SerialName("hourly_rate")
    val hourlyRate: Double,

    @SerialName("divisible")
    val divisible: Boolean,

    @SerialName("max_divisions")
    val maxDivisions: Int?,

    @SerialName("opening_time")
    val openingTime: String,

    @SerialName("closing_time")
    val closingTime: String,

    @SerialName("image_url")
    val imageUrl: String?,

    @SerialName("longitude")
    val longitude: Double,

    @SerialName("latitude")
    val latitude: Double,

    @SerialName("address_line")
    val addressLine: String?,
)
package org.composempfirstapp.project.court.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.composempfirstapp.project.court.domain.Court

@Serializable
data class CourtsResponse(
    @SerialName("courts")
    val courts: List<Court>,
    @SerialName("status")
    val status: String,
)

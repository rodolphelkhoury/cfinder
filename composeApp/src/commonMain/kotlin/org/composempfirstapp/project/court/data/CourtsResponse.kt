package org.composempfirstapp.project.court.data

import org.composempfirstapp.project.court.domain.Court

data class CourtsResponse(
    val courts: List<Court>,
    val status: String,
    val totalResults: Int
)

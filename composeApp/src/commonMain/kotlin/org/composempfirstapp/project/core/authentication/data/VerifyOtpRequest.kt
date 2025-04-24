package org.composempfirstapp.project.core.authentication.data

import kotlinx.serialization.Serializable

@Serializable
data class VerifyOtpRequest(
    val code: String
)
package org.composempfirstapp.project.authentication.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerifyOtpRequest(
    @SerialName("phone_number")
    val phoneNumber: String,
    @SerialName("code")
    val code: String
)
package org.composempfirstapp.project.authentication.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    @SerialName("phone_number")
    val phoneNumber: String,
    @SerialName("password")
    val password: String
)

package org.composempfirstapp.project.authentication.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.composempfirstapp.project.authentication.domain.Customer

@Serializable
data class AuthResponse(
    @SerialName("token")
    val token: String,
    @SerialName("customer")
    val customer: Customer
)
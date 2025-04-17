package org.composempfirstapp.project.core.authentication.data

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val phone_number: String,
    val password: String
)

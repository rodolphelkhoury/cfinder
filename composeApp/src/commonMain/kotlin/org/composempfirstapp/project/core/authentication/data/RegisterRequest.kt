package org.composempfirstapp.project.core.authentication.data

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val name: String,
    val phone_number: String,
    val password: String
)
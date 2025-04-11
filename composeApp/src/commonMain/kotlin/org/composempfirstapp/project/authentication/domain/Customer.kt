package org.composempfirstapp.project.authentication.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Customer(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("phone_number")
    val phoneNumber: String,
    @SerialName("phone_number_verified_at")
    val phoneNumberVerifiedAt: String?,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String
)

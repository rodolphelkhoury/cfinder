package org.composempfirstapp.project.profile.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Profile(
    @SerialName("id")
    val id: Int,

    @SerialName("name")
    val name: String,

    @SerialName("phone_number")
    val phoneNumber: String,

    @SerialName("phone_number_verified_at")
    val phoneNumberVerifiedAt: String? = null,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("updated_at")
    val updatedAt: String
)
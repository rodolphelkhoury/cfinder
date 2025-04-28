package org.composempfirstapp.project.profile.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.composempfirstapp.project.profile.domain.Profile

@Serializable
data class ProfileResponse(
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
) {
    fun toProfile(): Profile {
        return Profile(
            id = id,
            name = name,
            phoneNumber = phoneNumber,
            phoneNumberVerifiedAt = phoneNumberVerifiedAt,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
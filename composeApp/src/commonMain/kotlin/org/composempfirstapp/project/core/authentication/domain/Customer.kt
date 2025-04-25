package org.composempfirstapp.project.core.authentication.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Customer(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("phone_number")
    val phone_number: String,
    @SerialName("phone_number_verified_at")
    val phoneNumberVerifiedAt: String?,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String
)

data class CountryCode(
    val name: String,
    val code: String,
    val flag: String
)

val countryCodes = listOf(
    CountryCode("United States", "+1", "🇺🇸"),
    CountryCode("United Kingdom", "+44", "🇬🇧"),
    CountryCode("India", "+91", "🇮🇳"),
    CountryCode("Australia", "+61", "🇦🇺"),
    CountryCode("Canada", "+1", "🇨🇦"),
    CountryCode("Germany", "+49", "🇩🇪"),
    CountryCode("France", "+33", "🇫🇷"),
    CountryCode("Japan", "+81", "🇯🇵"),
    CountryCode("China", "+86", "🇨🇳"),
    CountryCode("Brazil", "+55", "🇧🇷"),
    CountryCode("Russia", "+7", "🇷🇺"),
    CountryCode("South Africa", "+27", "🇿🇦"),
    CountryCode("Mexico", "+52", "🇲🇽"),
    CountryCode("Spain", "+34", "🇪🇸"),
    CountryCode("Italy", "+39", "🇮🇹"),
    CountryCode("Lebanon", "+961", "🇱🇧")
)
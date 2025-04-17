package org.composempfirstapp.project.core.authentication.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerifyOtpResponse(
    @SerialName("verified")
    val verified: Boolean
)
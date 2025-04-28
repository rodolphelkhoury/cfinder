package org.composempfirstapp.project.profile.data

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.composempfirstapp.project.core.AppPreferences
import org.composempfirstapp.project.core.BASE_URL
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class ProfileRepository(
    private val appPreferences: AppPreferences
) {
    private val httpClient = HttpClient {
        defaultRequest {
            url(BASE_URL)
            contentType(ContentType.Application.Json)
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 60_000
        }
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                    explicitNulls = false
                }
            )
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                }
            }
            level = LogLevel.ALL
        }
    }

    suspend fun getCustomer(): HttpResponse {
        val token = appPreferences.getToken() ?: throw Exception("Authentication token not found. Please login again.")

        return httpClient.get {
            url("customer")
            header(HttpHeaders.Authorization, "Bearer $token")
        }
    }

    suspend fun updateProfile(name: String): HttpResponse {
        val token = appPreferences.getToken() ?: throw Exception("Authentication token not found. Please login again.")

        return httpClient.put {
            url("customer/profile")
            header(HttpHeaders.Authorization, "Bearer $token")
            setBody(mapOf("name" to name))
        }
    }
}

@Serializable
data class ProfileUpdateResponse(
    @SerialName("message")
    val message: String,

    @SerialName("customer")
    val customer: ProfileResponse? = null
)

@Serializable
data class ErrorResponse(
    @SerialName("code")
    val code: String? = null,

    @SerialName("message")
    val message: String? = null,

    @SerialName("status")
    val status: String? = null
)

@Serializable
data class Profile(
    val id: Int,
    val name: String,
    val phoneNumber: String,
    val phoneNumberVerifiedAt: String? = null,
    val createdAt: String,
    val updatedAt: String
)
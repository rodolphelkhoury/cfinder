package org.composempfirstapp.project.core.authentication.data

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.Logger as KtorLogger
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.composempfirstapp.project.core.AppPreferences
import org.composempfirstapp.project.core.BASE_URL

@Serializable
data class ErrorResponse(
    val message: String? = null,
    val errors: Map<String, JsonArray>? = null
)

class AuthRepository(
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
            logger = object : KtorLogger {
                override fun log(message: String) {
                    Logger.d("Ktor Auth") { message }
                }
            }
            level = LogLevel.ALL
        }
    }

    suspend fun login(phoneNumber: String, password: String): Result<AuthResponse> {
        return try {
            val response = httpClient.post {
                url("login")
                setBody(LoginRequest(phoneNumber, password))
            }

            // Check status code before trying to parse the response
            if (response.status.isSuccess()) {
                val authResponse = response.body<AuthResponse>()
                appPreferences.saveToken(authResponse.token)
                Result.success(authResponse)
            } else {
                // This code shouldn't be reached in normal circumstances because Ktor throws exceptions for non-2xx responses
                // But just in case, we handle it
                val errorText = response.bodyAsText()
                Result.failure(Exception(parseErrorMessage(errorText)))
            }
        } catch (e: ClientRequestException) {
            // This handles 4xx responses
            try {
                val errorBody = e.response.bodyAsText()
                val errorMessage = parseErrorMessage(errorBody)
                Result.failure(Exception(errorMessage))
            } catch (parseException: Exception) {
                Logger.e("Auth Error") { "Error parsing response: ${parseException.message}" }
                Result.failure(e)
            }
        } catch (e: Exception) {
            // Log the exception for debugging
            Logger.e("Auth Error") { "Login error: ${e.message}" }
            Result.failure(e)
        }
    }

    private fun parseErrorMessage(errorBody: String): String {
        return try {
            val errorJson = Json.decodeFromString<ErrorResponse>(errorBody)
            errorJson.message ?: "Unknown error occurred"
        } catch (e: Exception) {
            // If we can't parse the error response as our ErrorResponse class,
            // try to extract just the message field
            try {
                val jsonElement = Json.parseToJsonElement(errorBody)
                val messageElement = jsonElement.jsonObject["message"]
                messageElement?.jsonPrimitive?.contentOrNull ?: "Unknown error occurred"
            } catch (e2: Exception) {
                "Error occurred during authentication"
            }
        }
    }

    suspend fun register(name: String, phoneNumber: String, password: String): Result<AuthResponse> {
        return try {
            val response = httpClient.post {
                url("register")
                setBody(RegisterRequest(name, phoneNumber, password))
            }

            if (response.status.isSuccess()) {
                val authResponse = response.body<AuthResponse>()
                appPreferences.saveToken(authResponse.token)
                Result.success(authResponse)
            } else {
                val errorText = response.bodyAsText()
                Result.failure(Exception(parseErrorMessage(errorText)))
            }
        } catch (e: ClientRequestException) {
            try {
                val errorBody = e.response.bodyAsText()
                val errorMessage = parseErrorMessage(errorBody)
                Result.failure(Exception(errorMessage))
            } catch (parseException: Exception) {
                Logger.e("Auth Error") { "Error parsing response: ${parseException.message}" }
                Result.failure(e)
            }
        } catch (e: Exception) {
            // Log the exception for debugging
            Logger.e("Auth Error") { "Registration error: ${e.message}" }
            Result.failure(e)
        }
    }

    suspend fun verifyOtp(code: String): Result<Boolean> {
        return try {
            val token = appPreferences.getToken()
            if (token == null) {
                return Result.failure(Exception("Authentication token not found. Please login again."))
            }

            val response = httpClient.post {
                url("verify-otp")
                header(HttpHeaders.Authorization, "Bearer $token")
                setBody(VerifyOtpRequest(code))
            }

            if (response.status.isSuccess()) {
                val verifyResponse = response.body<VerifyOtpResponse>()
                Result.success(verifyResponse.verified)
            } else {
                val errorText = response.bodyAsText()
                Result.failure(Exception(parseErrorMessage(errorText)))
            }
        } catch (e: ClientRequestException) {
            try {
                val errorBody = e.response.bodyAsText()
                val errorMessage = parseErrorMessage(errorBody)
                Result.failure(Exception(errorMessage))
            } catch (parseException: Exception) {
                Logger.e("Auth Error") { "Error parsing response: ${parseException.message}" }
                Result.failure(e)
            }
        } catch (e: Exception) {
            // Log the exception for debugging
            Logger.e("Auth Error") { "Verify OTP error: ${e.message}" }
            Result.failure(e)
        }
    }

    suspend fun isLoggedIn(): Boolean {
        return appPreferences.getToken() != null
    }

    suspend fun isPhoneVerified(): Boolean {
        // You may want to implement a more robust check based on your backend
        return true // Placeholder implementation
    }

    suspend fun logout() {
        appPreferences.clearToken()
    }
}


package org.composempfirstapp.project.core.authentication.data

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.call.body
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
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.composempfirstapp.project.core.AppPreferences
import org.composempfirstapp.project.core.BASE_URL

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

            val authResponse = response.body<AuthResponse>()
            appPreferences.saveToken(authResponse.token)
            Result.success(authResponse)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(name: String, phoneNumber: String, password: String): Result<AuthResponse> {
        return try {
            val response = httpClient.post {
                url("register")
                setBody(RegisterRequest(name, phoneNumber, password))
            }

            val authResponse = response.body<AuthResponse>()
            appPreferences.saveToken(authResponse.token)
            Result.success(authResponse)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun verifyOtp(code: String): Result<Boolean> {
        return try {
            val token = appPreferences.getToken()
            val response = httpClient.post {
                url("verify-otp")
                header(HttpHeaders.Authorization, "Bearer $token")
                setBody(VerifyOtpRequest(code))
            }

            val verifyResponse = response.body<VerifyOtpResponse>()
            Result.success(verifyResponse.verified)
        } catch (e: Exception) {
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
}
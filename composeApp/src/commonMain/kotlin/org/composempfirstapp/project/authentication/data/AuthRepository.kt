package org.composempfirstapp.project.authentication.data

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.composempfirstapp.project.authentication.domain.LoginRequest
import org.composempfirstapp.project.authentication.domain.RegisterRequest
import org.composempfirstapp.project.authentication.domain.VerifyOtpRequest
import org.composempfirstapp.project.utils.BASE_URL

class AuthRepository {
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
            logger = Logger.DEFAULT
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) {
                    co.touchlab.kermit.Logger.d("Ktor Client") {
                        message
                    }
                }
            }
        }
    }

    suspend fun login(phoneNumber: String, password: String): HttpResponse {
        return httpClient.post {
            url("login")
            setBody(LoginRequest(phoneNumber, password))
        }
    }

    suspend fun register(name: String, email: String, phoneNumber: String, password: String): HttpResponse {
        return httpClient.post {
            url("register")
            setBody(RegisterRequest(name, email, phoneNumber, password))
        }
    }

    suspend fun verifyOtp(phoneNumber: String, otp: String): HttpResponse {
        return httpClient.post {
            url("verify-otp")
//            TODO
//            header(HttpHeaders.Authorization, "Bearer $token")
            setBody(VerifyOtpRequest(phoneNumber, otp))
        }
    }
}
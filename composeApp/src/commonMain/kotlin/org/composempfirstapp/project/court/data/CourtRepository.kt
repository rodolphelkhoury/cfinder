package org.composempfirstapp.project.court.data

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.composempfirstapp.project.core.AppPreferences
import org.composempfirstapp.project.core.BASE_URL
import org.composempfirstapp.project.reservation.data.ReservationRequest

class CourtRepository(
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

    suspend fun getCourts(searchQuery: String = "", courtType: String = ""): HttpResponse {
        val token = appPreferences.getToken()

        return httpClient.get {
            url("courts")

            token?.let {
                header(HttpHeaders.Authorization, "Bearer $it")
            }

            if (searchQuery.isNotEmpty()) {
                parameter("search", searchQuery)
            }

            if (courtType.isNotEmpty()) {
                parameter("court_type", courtType)
            }
        }
    }

    suspend fun getAvailableReservations(courtId: Long, date: String): HttpResponse {
        val token = appPreferences.getToken()

        return httpClient.get {
            url("courts/$courtId/available-reservations")  // Fixed API endpoint

            token?.let {
                header(HttpHeaders.Authorization, "Bearer $it")
            }

            parameter("reservation_date", date)
        }
    }

    suspend fun createReservation(
        courtId: Long,
        reservationDate: String,
        startTime: String,
        endTime: String
    ): HttpResponse {
        val token = appPreferences.getToken()

        return httpClient.post {
            url("reservations/$courtId")

            token?.let {
                header(HttpHeaders.Authorization, "Bearer $it")
            }

            setBody(
                ReservationRequest(
                    reservation_date = reservationDate,
                    start_time = startTime,
                    end_time = endTime
                )
            )
        }
    }
}
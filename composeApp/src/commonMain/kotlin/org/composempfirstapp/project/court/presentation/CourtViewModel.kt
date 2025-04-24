package org.composempfirstapp.project.court.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.call.body
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.composempfirstapp.project.court.data.CourtRepository
import org.composempfirstapp.project.court.data.CourtsResponse
import org.composempfirstapp.project.court.data.ErrorResponse
import org.composempfirstapp.project.court.domain.Court
import org.composempfirstapp.project.core.Resource
import org.composempfirstapp.project.court.data.AvailableReservationsResponse
import org.composempfirstapp.project.court.data.TimeSlot
import org.composempfirstapp.project.reservation.data.ReservationsResponse

class CourtViewModel(
    private val courtRepository: CourtRepository
) : ViewModel() {
    private val _courtStateFlow = MutableStateFlow<Resource<List<Court>>>(Resource.Loading)
    val courtStateFlow : StateFlow<Resource<List<Court>>>
        get() = _courtStateFlow

    private val _availableTimeSlotsFlow = MutableStateFlow<Resource<List<TimeSlot>>>(Resource.Loading)
    val availableTimeSlotsFlow: StateFlow<Resource<List<TimeSlot>>>
        get() = _availableTimeSlotsFlow

    // Set proper initial state to Idle
    private val _reservationStatus = MutableStateFlow<Resource<AvailableReservationsResponse>>(Resource.Idle)
    val reservationStatus: StateFlow<Resource<AvailableReservationsResponse>> = _reservationStatus

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    init {
        getCourts()
    }

    // Add reset function using Idle state
    fun resetReservationStatus() {
        _reservationStatus.value = Resource.Idle
    }

    // Add function to update time slots directly
    fun updateAvailableTimeSlots(slots: List<TimeSlot>) {
        _availableTimeSlotsFlow.value = Resource.Success(slots)
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        getCourts(query)
    }

    fun getCourts(query: String = _searchQuery.value) {
        viewModelScope.launch(Dispatchers.IO) {
            _courtStateFlow.emit(Resource.Loading)
            try {
                val httpResponse = courtRepository.getCourts(query)
                if (httpResponse.status.value in 200 .. 299) {
                    val body = httpResponse.body<CourtsResponse>()
                    _courtStateFlow.emit(Resource.Success(body.courts))
                } else {
                    val body = httpResponse.body<ErrorResponse>()
                    _courtStateFlow.emit(Resource.Error(body.message))
                }
            } catch (e:Exception) {
                _courtStateFlow.emit(Resource.Error(e.message.toString()))
            }
        }
    }

    fun getAvailableReservations(courtId: Long, date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _availableTimeSlotsFlow.emit(Resource.Loading)
            try {
                val httpResponse = courtRepository.getAvailableReservations(courtId, date)
                if (httpResponse.status.value in 200..299) {
                    val body = httpResponse.body<AvailableReservationsResponse>()
                    _availableTimeSlotsFlow.emit(Resource.Success(body.availableReservations))
                } else {
                    val body = httpResponse.body<ErrorResponse>()
                    _availableTimeSlotsFlow.emit(Resource.Error(body.message))
                }
            } catch (e: Exception) {
                _availableTimeSlotsFlow.emit(Resource.Error(e.message.toString()))
            }
        }
    }

    fun createReservation(
        courtId: Long,
        date: String,
        timeSlot: TimeSlot
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _reservationStatus.emit(Resource.Loading)
            try {
                val httpResponse = courtRepository.createReservation(
                    courtId = courtId,
                    reservationDate = date,
                    startTime = timeSlot.startTime,
                    endTime = timeSlot.endTime
                )

                if (httpResponse.status.value in 200..299) {
                    val body = httpResponse.body<AvailableReservationsResponse>()
                    _reservationStatus.emit(Resource.Success(body))
                } else {
                    val body = httpResponse.body<ErrorResponse>()
                    _reservationStatus.emit(Resource.Error(body.message))
                }
            } catch (e: Exception) {
                _reservationStatus.emit(Resource.Error(e.message.toString()))
            }
        }
    }
}
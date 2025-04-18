package org.composempfirstapp.project.reservation.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.call.body
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.composempfirstapp.project.core.Resource
import org.composempfirstapp.project.reservation.data.ReservationErrorResponse
import org.composempfirstapp.project.reservation.data.ReservationRepository
import org.composempfirstapp.project.reservation.data.ReservationsResponse
import org.composempfirstapp.project.reservation.domain.Reservation

class ReservationViewModel(
    private val reservationRepository: ReservationRepository
) : ViewModel() {

    private val _upcomingReservationStateFlow = MutableStateFlow<Resource<List<Reservation>>>(Resource.Idle)
    val upcomingReservationStateFlow: StateFlow<Resource<List<Reservation>>>
        get() = _upcomingReservationStateFlow

    private val _completedReservationStateFlow = MutableStateFlow<Resource<List<Reservation>>>(Resource.Idle)
    val completedReservationStateFlow: StateFlow<Resource<List<Reservation>>>
        get() = _completedReservationStateFlow

    init {
        getReservations()
    }

    fun getReservations() {
        viewModelScope.launch(Dispatchers.IO) {
            _upcomingReservationStateFlow.emit(Resource.Loading)
            _completedReservationStateFlow.emit(Resource.Loading)
            try {
                val httpResponse = reservationRepository.getReservations()
                if (httpResponse.status.value in 200..299) {
                    val body = httpResponse.body<ReservationsResponse>()
                    _upcomingReservationStateFlow.emit(Resource.Success(body.upcoming))
                    _completedReservationStateFlow.emit(Resource.Success(body.completed))
                } else {
                    val body = httpResponse.body<ReservationErrorResponse>()
                    _upcomingReservationStateFlow.emit(Resource.Error(body.message))
                    _completedReservationStateFlow.emit(Resource.Error(body.message))
                }
            } catch (e: Exception) {
                _upcomingReservationStateFlow.emit(Resource.Error(e.message.toString()))
            }
        }
    }
}

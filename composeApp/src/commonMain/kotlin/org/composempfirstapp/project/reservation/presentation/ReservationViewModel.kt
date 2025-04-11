package org.composempfirstapp.project.reservation.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.composempfirstapp.project.court.domain.Court
import org.composempfirstapp.project.utils.Resource
import org.composempfirstapp.project.utils.courts

class ReservationViewModel : ViewModel() {

    //    Idle means we do not need to do anything at first
    private val _reservationStateFlow = MutableStateFlow<Resource<List<Court>>>(Resource.Idle)

    val reservationStateFlow : StateFlow<Resource<List<Court>>>
        get() = _reservationStateFlow

    init {
        getReservations()
    }

    fun getReservations() {
        viewModelScope.launch(Dispatchers.IO) {
//            _courtStateFlow.emit(Resource.Loading)
//            // TODO
//            delay(2000)
//            try {
//                val courtList = courts
//                _courtStateFlow.emit(Resource.Success(courtList))
//            } catch (e:Exception) {
//                _courtStateFlow.emit(Resource.Error(e.message.toString()))
//            }
        }
    }
}
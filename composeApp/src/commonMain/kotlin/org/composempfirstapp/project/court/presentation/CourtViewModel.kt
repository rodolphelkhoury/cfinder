package org.composempfirstapp.project.court.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.call.body
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.composempfirstapp.project.court.data.CourtRepository
import org.composempfirstapp.project.court.data.CourtsResponse
import org.composempfirstapp.project.court.data.ErrorResponse
import org.composempfirstapp.project.court.domain.Court
import org.composempfirstapp.project.utils.Resource
import org.composempfirstapp.project.utils.courts

class CourtViewModel(
    private val courtRepository: CourtRepository
) : ViewModel() {

//    Idle means we do not need to do anything at first
    private val _courtStateFlow = MutableStateFlow<Resource<List<Court>>>(Resource.Loading)

    val courtStateFlow : StateFlow<Resource<List<Court>>>
        get() = _courtStateFlow

    init {
        getCourts()
    }

    fun getCourts() {
        viewModelScope.launch(Dispatchers.IO) {
            _courtStateFlow.emit(Resource.Loading)
            try {
                val httpResponse = courtRepository.getCourts()
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
}
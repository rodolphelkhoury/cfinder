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

class CourtViewModel(
    private val courtRepository: CourtRepository
) : ViewModel() {
    private val _courtStateFlow = MutableStateFlow<Resource<List<Court>>>(Resource.Loading)
    val courtStateFlow : StateFlow<Resource<List<Court>>>
        get() = _courtStateFlow

    // Search query state
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    init {
        getCourts()
    }

    // Update search query and trigger search
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        getCourts(query)
    }

    // Get courts with optional search parameter
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
}
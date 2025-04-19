package org.composempfirstapp.project.profile.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.call.body
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.composempfirstapp.project.court.domain.Court
import org.composempfirstapp.project.core.Resource
import org.composempfirstapp.project.profile.data.ProfileErrorResponse
import org.composempfirstapp.project.profile.data.ProfileRepository
import org.composempfirstapp.project.profile.data.ProfileResponse
import org.composempfirstapp.project.profile.domain.Profile
import org.composempfirstapp.project.reservation.data.ReservationErrorResponse
import org.composempfirstapp.project.reservation.data.ReservationRepository
import org.composempfirstapp.project.reservation.data.ReservationsResponse
import org.composempfirstapp.project.reservation.domain.Reservation

class ProfileViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _profile = MutableStateFlow<Resource<Profile>>(Resource.Idle)
    val profile: StateFlow<Resource<Profile>>
        get() = _profile

    // Add updateable profile state
    private val _fullName = MutableStateFlow("")
    val fullName: StateFlow<String> = _fullName

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber

    init {
        getProfile()
    }

    fun getProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            _profile.emit(Resource.Loading)
            try {
                val httpResponse = profileRepository.getCustomer()
                if (httpResponse.status.value in 200..299) {
                    val body = httpResponse.body<ProfileResponse>()
                    val profile = body.toProfile()
                    _profile.emit(Resource.Success(profile))

                    // Update the individual fields
                    _fullName.emit(profile.name)
                    _phoneNumber.emit(profile.phoneNumber)
                } else {
                    val body = httpResponse.body<ProfileErrorResponse>()
                    _profile.emit(Resource.Error(body.message))
                }
            } catch (e: Exception) {
                _profile.emit(Resource.Error(e.message.toString()))
            }
        }
    }

    // Add update profile function that calls repository
    fun updateProfile(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = profileRepository.updateProfile(name)
                if (response.status.value in 200..299) {
                    _fullName.emit(name)
                    // Refresh profile data
                    getProfile()
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}


package org.composempfirstapp.project.profile.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.call.body
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.composempfirstapp.project.court.domain.Court
import org.composempfirstapp.project.core.Resource
import org.composempfirstapp.project.court.data.ErrorResponse
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

    private val _updateProfileState = MutableStateFlow<Resource<Unit>>(Resource.Idle)
    val updateProfileState: StateFlow<Resource<Unit>> = _updateProfileState


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
                    val errorBody = httpResponse.bodyAsText()
                    val errorMessage = parseErrorMessage(errorBody)
                    _profile.emit(Resource.Error(errorMessage))
                }
            } catch (e: Exception) {
                _profile.emit(Resource.Error(e.message ?: "Unknown error occurred"))
            }
        }
    }


    fun updateProfile(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _updateProfileState.emit(Resource.Loading)
            try {
                val httpResponse = profileRepository.updateProfile(name)
                if (httpResponse.status.value in 200..299) {
                    // Parse the response directly
                    val body = httpResponse.body<ProfileUpdateResponse>()
                    val updatedProfile = body.customer.toProfile()

                    // Update all relevant states at once
                    _fullName.emit(updatedProfile.name)
                    _phoneNumber.emit(updatedProfile.phoneNumber)

                    // Update the main profile state too
                    _profile.emit(Resource.Success(updatedProfile))
                    _updateProfileState.emit(Resource.Success(Unit))
                } else {
                    val errorBody = httpResponse.bodyAsText()
                    val errorMessage = parseErrorMessage(errorBody)
                    _updateProfileState.emit(Resource.Error(errorMessage))
                }
            } catch (e: Exception) {
                _updateProfileState.emit(Resource.Error(e.message ?: "Unknown error occurred"))
            }
        }
    }


    private fun parseErrorMessage(errorBody: String): String {
        return try {
            val errorJson = Json.decodeFromString<ErrorResponse>(errorBody)
            errorJson.message ?: "Unknown error occurred"
        } catch (e: Exception) {
            try {
                val jsonElement = Json.parseToJsonElement(errorBody)
                val messageElement = jsonElement.jsonObject["message"]
                messageElement?.jsonPrimitive?.contentOrNull ?: "Unknown error occurred"
            } catch (e2: Exception) {
                "Error occurred during profile operation"
            }
        }
    }
}

@Serializable
data class ProfileUpdateResponse(
    val message: String,
    val customer: ProfileResponse
)

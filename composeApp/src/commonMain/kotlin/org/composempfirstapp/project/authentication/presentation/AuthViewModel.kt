package org.composempfirstapp.project.authentication.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.composempfirstapp.project.authentication.data.AuthRepository
import org.composempfirstapp.project.authentication.data.AuthResponse
import org.composempfirstapp.project.authentication.data.ErrorResponse
import org.composempfirstapp.project.authentication.domain.Customer

class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    // Auth state
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    // User state
    private val _token = MutableStateFlow<String?>(null)
    private val _customer = MutableStateFlow<Customer?>(null)
    val customer: StateFlow<Customer?> = _customer.asStateFlow()

    // Verification status
    private val _isPhoneVerified = MutableStateFlow(false)
    val isPhoneVerified: StateFlow<Boolean> = _isPhoneVerified.asStateFlow()

    // Check if user is already logged in
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    init {
        // Check if user is logged in on initialization
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            val savedToken = retrieveTokenFromStorage()
            val savedCustomer = retrieveCustomerFromStorage()

            if (savedToken != null && savedCustomer != null) {
                _token.value = savedToken
                _customer.value = savedCustomer
                _isLoggedIn.value = true
                _isPhoneVerified.value = savedCustomer.phoneNumberVerifiedAt != null
            } else {
                _isLoggedIn.value = false
                _isPhoneVerified.value = false
            }
        }
    }

    fun login(phoneNumber: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            try {
                val response = authRepository.login(phoneNumber, password)
                handleAuthResponse(response)
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Login failed. Please try again.")
            }
        }
    }

    fun register(name: String, email: String, phoneNumber: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            try {
                val response = authRepository.register(name, email, phoneNumber, password)
                handleAuthResponse(response)
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Registration failed. Please try again.")
            }
        }
    }

    fun verifyOtp(phoneNumber: String, otp: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            try {
                val response = authRepository.verifyOtp(phoneNumber, otp)
                handleAuthResponse(response)
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Verification failed. Please try again.")
            }
        }
    }

    private suspend fun handleAuthResponse(response: HttpResponse) {
        try {
            if (response.status.value in 200..299) {
                val authResponse = response.body<AuthResponse>()

                _token.value = authResponse.token
                _customer.value = authResponse.customer
                _isPhoneVerified.value = authResponse.customer.phoneNumberVerifiedAt != null
                _isLoggedIn.value = true

                saveUserSession(authResponse.token, authResponse.customer)
                _authState.value = AuthState.Success
            } else {
//                TODO
//                val errorBody = Json.decodeFromString<ErrorResponse>()
//                _authState.value = AuthState.Error(errorBody.message)
            }
        } catch (e: Exception) {
            _authState.value = AuthState.Error("Error processing response: ${e.message}")
        }
    }

    fun logout() {
        viewModelScope.launch {
            clearUserSession()
            _token.value = null
            _customer.value = null
            _isLoggedIn.value = false
            _isPhoneVerified.value = false
        }
    }

    private fun saveUserSession(token: String, customer: Customer) {

    }

    private fun clearUserSession() {

    }

    private fun retrieveTokenFromStorage(): String? {
        // TODO
        return null;
    }

    private fun retrieveCustomerFromStorage(): Customer? {
        // TODO
        return null;
    }

    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }
}

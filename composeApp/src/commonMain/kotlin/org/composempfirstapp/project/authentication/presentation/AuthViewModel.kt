package org.composempfirstapp.project.authentication.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Auth states
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
    object Success : AuthState()
}

class AuthViewModel : ViewModel() {

    // Auth state
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    // Check if user is already logged in
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    init {
        // Check if user is logged in on initialization
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            // Here you would check for stored credentials or tokens
            // For example, from SharedPreferences or DataStore
            // For now, we'll just set it to false
            _isLoggedIn.value = false
        }
    }

    fun login(phoneNumber: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            try {
                // Here you would make your API call for login
                // For demonstration, we'll just simulate a delay
                kotlinx.coroutines.delay(1500)

                // Check if credentials are valid (replace with actual validation)
                if (phoneNumber.isNotBlank() && password.isNotBlank()) {
                    // Save user session information
                    saveUserSession("user_token")
                    _isLoggedIn.value = true
                    _authState.value = AuthState.Success
                } else {
                    _authState.value = AuthState.Error("Invalid credentials")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun register(name: String, email: String, phoneNumber: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            try {
                // Here you would make your API call for registration
                kotlinx.coroutines.delay(1500)

                // Validate registration data
                if (name.isNotBlank() && email.isNotBlank() &&
                    phoneNumber.isNotBlank() && password.isNotBlank()) {
                    _authState.value = AuthState.Success
                } else {
                    _authState.value = AuthState.Error("All fields are required")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun verifyOtp(phoneNumber: String, otp: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            try {
                // Here you would make your API call for OTP verification
                kotlinx.coroutines.delay(1500)

                // Check if OTP is valid
                if (otp.length == 6) {
                    // Save user session after successful verification
                    saveUserSession("user_token")
                    _isLoggedIn.value = true
                    _authState.value = AuthState.Success
                } else {
                    _authState.value = AuthState.Error("Invalid OTP")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            // Clear user session
            clearUserSession()
            _isLoggedIn.value = false
        }
    }

    private fun saveUserSession(token: String) {
        // Here you would save the user's session token
        // For example, in SharedPreferences or DataStore
    }

    private fun clearUserSession() {
        // Here you would clear the user's session token
    }

    // Reset auth state (e.g., after handling an error)
    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }
}
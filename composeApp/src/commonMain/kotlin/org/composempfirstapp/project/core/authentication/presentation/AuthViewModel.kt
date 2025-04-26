package org.composempfirstapp.project.core.authentication.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.composempfirstapp.project.core.authentication.data.AuthRepository
import org.composempfirstapp.project.core.authentication.data.AuthResponse
import org.composempfirstapp.project.core.authentication.data.UiState

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _loginState = MutableStateFlow<UiState<AuthResponse>>(UiState.Initial)
    val loginState = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow<UiState<AuthResponse>>(UiState.Initial)
    val registerState = _registerState.asStateFlow()

    private val _verifyOtpState = MutableStateFlow<UiState<Boolean>>(UiState.Initial)
    val verifyOtpState = _verifyOtpState.asStateFlow()

    fun login(phoneNumber: String, password: String) {
        _loginState.value = UiState.Loading
        viewModelScope.launch {
            val result = authRepository.login(phoneNumber, password)
            _loginState.value = result.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Login failed") }
            )
        }
    }

    fun register(name: String, phoneNumber: String, password: String) {
        _registerState.value = UiState.Loading
        viewModelScope.launch {
            val result = authRepository.register(name, phoneNumber, password)
            _registerState.value = result.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Registration failed") }
            )
        }
    }

    fun verifyOtp(code: String) {
        _verifyOtpState.value = UiState.Loading
        viewModelScope.launch {
            val result = authRepository.verifyOtp(code)
            _verifyOtpState.value = result.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Verification failed") }
            )
        }
    }

    fun resetStates() {
        _loginState.value = UiState.Initial
        _registerState.value = UiState.Initial
        _verifyOtpState.value = UiState.Initial
    }
}
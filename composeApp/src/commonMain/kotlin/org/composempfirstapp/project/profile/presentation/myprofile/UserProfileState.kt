package org.composempfirstapp.project.profile.presentation.myprofile

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// Singleton object to manage user profile data across screens
object UserProfileState {
    private val _firstName = MutableStateFlow("John")
    private val _lastName = MutableStateFlow("Doe")
    private val _phoneNumber = MutableStateFlow("+1 234 567 8901")

    val firstName: StateFlow<String> = _firstName
    val lastName: StateFlow<String> = _lastName
    val phoneNumber: StateFlow<String> = _phoneNumber

    // Full name for display
    val fullName: String
        get() = "${_firstName.value} ${_lastName.value}"

    // Update user profile data
    fun updateProfile(firstName: String, lastName: String, phoneNumber: String) {
        _firstName.value = firstName
        _lastName.value = lastName
        _phoneNumber.value = phoneNumber
    }
}
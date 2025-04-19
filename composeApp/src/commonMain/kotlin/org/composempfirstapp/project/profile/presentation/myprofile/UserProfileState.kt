package org.composempfirstapp.project.profile.presentation.myprofile

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// Singleton object to manage user profile data across screens
object UserProfileState {
    private val _fullName = MutableStateFlow("Joe Akiki")
    private val _phoneNumber = MutableStateFlow("03 456 789")

    val fullName: StateFlow<String> = _fullName
    val phoneNumber: StateFlow<String> = _phoneNumber

    fun updateProfile(fullName: String, phoneNumber: String) {
        _fullName.value = fullName
        _phoneNumber.value = phoneNumber
    }
}

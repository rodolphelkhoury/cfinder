package org.composempfirstapp.project.profile.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.composempfirstapp.project.utils.AppPreferences
import org.composempfirstapp.project.utils.Theme

class SettingViewModel(
    private val appPreferences: AppPreferences
) : ViewModel() {

    private val _currentTheme = MutableStateFlow<String?>(null)
    val currentTheme: StateFlow<String?> = _currentTheme

    init {
        viewModelScope.launch {
            // Get the initial theme
            val theme = appPreferences.getTheme()
            _currentTheme.value = theme
        }
    }

    fun updateTheme(themeName: String) {
        viewModelScope.launch {
            // Convert the theme name to Theme enum
            val theme = Theme.valueOf(themeName)
            appPreferences.changeTheme(theme)
            _currentTheme.value = themeName
        }
    }
}
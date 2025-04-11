package org.composempfirstapp.project.profile.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.composempfirstapp.project.utils.AppPreferences
import org.composempfirstapp.project.utils.Theme

class SettingViewModel(
    private val appPreferences: AppPreferences
): ViewModel() {
    private val _currentTheme : MutableStateFlow<String?> = MutableStateFlow(null)
    val currentTheme = _currentTheme.asStateFlow()

    init {
        currentThemeGet()
    }

    // The runBlocking will block the app if hay tawalit la temshe la kenit kholsit
    fun currentThemeGet() = runBlocking {
        _currentTheme.update {
            appPreferences.getTheme()
        }
    }

    fun changeTheme(value: Theme) {
        viewModelScope.launch(
            Dispatchers.IO
        ) {
            appPreferences.changeTheme(theme = value)
            _currentTheme.update {
                value.name
            }
        }

    }
}
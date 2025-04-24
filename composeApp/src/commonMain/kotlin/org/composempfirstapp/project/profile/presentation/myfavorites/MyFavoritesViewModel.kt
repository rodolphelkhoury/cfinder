//package org.composempfirstapp.project.profile.presentation.myfavorites
//
//import androidx.lifecycle.ViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.update
//import org.composempfirstapp.project.court.domain.Court
//
//class MyFavoritesViewModel : ViewModel() {
//    // Store favorite courts
//    private val _favoriteCourts = MutableStateFlow<List<Court>>(emptyList())
//    val favoriteCourts: StateFlow<List<Court>> = _favoriteCourts.asStateFlow()
//
//    // Check if a court is favorited
//    fun isCourtFavorite(court: Court): Boolean {
//        return _favoriteCourts.value.any { it.id == court.id }
//    }
//
//    // Toggle favorite status
//    fun toggleFavorite(court: Court) {
//        val currentFavorites = _favoriteCourts.value
//        if (isCourtFavorite(court)) {
//            // Remove from favorites
//            _favoriteCourts.update { favorites ->
//                favorites.filter { it.id != court.id }
//            }
//        } else {
//            // Add to favorites
//            _favoriteCourts.update { favorites ->
//                favorites + court
//            }
//        }
//    }
//
//    // Remove a court from favorites
//    fun removeFavorite(court: Court) {
//        _favoriteCourts.update { favorites ->
//            favorites.filter { it.id != court.id }
//        }
//    }
//}
//


package org.composempfirstapp.project.profile.presentation.myfavorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.composempfirstapp.project.core.AppPreferences
import org.composempfirstapp.project.court.domain.Court

class MyFavoritesViewModel(
    private val appPreferences: AppPreferences
) : ViewModel() {
    private val _favoriteCourts = MutableStateFlow<List<Court>>(emptyList())
    val favoriteCourts: StateFlow<List<Court>> = _favoriteCourts.asStateFlow()

    init {
        // load persisted favorites on start
        viewModelScope.launch {
            val saved = appPreferences.getFavoriteCourts()
            _favoriteCourts.value = saved
        }
    }

    private fun persist() {
        viewModelScope.launch {
            appPreferences.saveFavoriteCourts(_favoriteCourts.value)
        }
    }

    fun isCourtFavorite(court: Court): Boolean =
        _favoriteCourts.value.any { it.id == court.id }

    fun toggleFavorite(court: Court) {
        _favoriteCourts.update { current ->
            if (current.any { it.id == court.id })
                current.filterNot { it.id == court.id }
            else
                current + court
        }
        persist()
    }

    fun removeFavorite(court: Court) {
        _favoriteCourts.update { it.filterNot { it.id == court.id } }
        persist()
    }
}


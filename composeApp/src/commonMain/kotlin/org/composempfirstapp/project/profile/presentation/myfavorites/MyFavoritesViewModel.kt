package org.composempfirstapp.project.profile.presentation.myfavorites

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.composempfirstapp.project.court.domain.Court

class MyFavoritesViewModel : ViewModel() {
    // Store favorite courts
    private val _favoriteCourts = MutableStateFlow<List<Court>>(emptyList())
    val favoriteCourts: StateFlow<List<Court>> = _favoriteCourts.asStateFlow()

    // Check if a court is favorited
    fun isCourtFavorite(court: Court): Boolean {
        return _favoriteCourts.value.any { it.id == court.id }
    }

    // Toggle favorite status
    fun toggleFavorite(court: Court) {
        val currentFavorites = _favoriteCourts.value
        if (isCourtFavorite(court)) {
            // Remove from favorites
            _favoriteCourts.update { favorites ->
                favorites.filter { it.id != court.id }
            }
        } else {
            // Add to favorites
            _favoriteCourts.update { favorites ->
                favorites + court
            }
        }
    }

    // Remove a court from favorites
    fun removeFavorite(court: Court) {
        _favoriteCourts.update { favorites ->
            favorites.filter { it.id != court.id }
        }
    }
}


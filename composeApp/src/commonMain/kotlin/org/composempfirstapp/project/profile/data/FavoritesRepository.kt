package org.composempfirstapp.project.profile.data

import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.composempfirstapp.project.court.domain.Court

/**
 * Repository that stores favorite courts list using Multiplatform Settings.
 */
class FavoritesRepository(private val settings: Settings) {
    private val FAVORITES_KEY = "favorite_courts"

    private val favorites = MutableStateFlow<List<Court>>(loadFavorites())

    /**
     * A StateFlow of the current list of favorite Courts.
     */
    val favoritesFlow: StateFlow<List<Court>> = favorites.asStateFlow()

    private fun loadFavorites(): List<Court> {
        return try {
            val json = settings.getString(FAVORITES_KEY, "")
            if (json.isNotBlank()) Json.decodeFromString(json) else emptyList()

        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun saveFavorites(list: List<Court>) {
        settings.putString(FAVORITES_KEY, Json.encodeToString(list))
    }

    /**
     * Toggle the given court in/out of the favorites list.
     */
    suspend fun toggleFavorite(court: Court) {
        val current = favorites.value
        val updated = if (current.any { it.id == court.id }) {
            current.filter { it.id != court.id }
        } else {
            current + court
        }
        favorites.value = updated
        saveFavorites(updated)
    }

    /**
     * Remove the given court explicitly from favorites.
     */
    suspend fun removeFavorite(court: Court) {
        val updated = favorites.value.filter { it.id != court.id }
        favorites.value = updated
        saveFavorites(updated)
    }
}
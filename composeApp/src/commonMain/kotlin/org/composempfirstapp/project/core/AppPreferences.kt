package org.composempfirstapp.project.core

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.composempfirstapp.project.court.domain.Court

class AppPreferences(
    private val dataStore: DataStore<Preferences>
) {
    private val tokenKey = stringPreferencesKey("auth_token")
    private val themeKey = stringPreferencesKey("theme")


    // new key for favorites
    private val favoritesKey = stringPreferencesKey("favorite_courts")

    /** Save the entire list of courts as a JSON string */
    suspend fun saveFavoriteCourts(courts: List<Court>) {
        val json = Json.encodeToString(courts)
        dataStore.edit { it[favoritesKey] = json }
    }

    /** Load the list (empty if never saved) */
    suspend fun getFavoriteCourts(): List<Court> {
        val json = dataStore.data
            .map { prefs -> prefs[favoritesKey] }
            .first()
        return if (!json.isNullOrBlank()) {
            Json.decodeFromString(json)
        } else {
            emptyList()
        }
    }


    suspend fun saveToken(token: String) {
        dataStore.edit { it[tokenKey] = token }
    }

    suspend fun getToken(): String? {
        return dataStore.data.map { it[tokenKey] }.first()
    }

    suspend fun clearToken() {
        dataStore.edit { it.remove(tokenKey) }
    }

    suspend fun getTheme(): String {
        return dataStore.data.map { it[themeKey] ?: Theme.DARK_MODE.name }.first()
    }

    suspend fun changeTheme(theme: Theme) {
        dataStore.edit { it[themeKey] = theme.name }
    }
}

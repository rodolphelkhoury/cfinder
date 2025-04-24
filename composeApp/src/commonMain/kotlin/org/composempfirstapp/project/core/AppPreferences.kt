package org.composempfirstapp.project.core

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class AppPreferences(
    private val dataStore: DataStore<Preferences>
) {
    private val tokenKey = stringPreferencesKey("auth_token")
    private val themeKey = stringPreferencesKey("theme")

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

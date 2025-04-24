package org.composempfirstapp.project

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.SynchronizedObject
import kotlinx.coroutines.internal.synchronized
import okio.Path.Companion.toPath


interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun shareLink(url:String)

expect fun dataStorePreference() : DataStore<Preferences>

object AppSettings {
    private lateinit var dataStore: DataStore<Preferences>

    @OptIn(InternalCoroutinesApi::class)
    private var lock = SynchronizedObject()

    @OptIn(InternalCoroutinesApi::class)
    fun getDataStore(producerPath: () -> String): DataStore<Preferences> {
        return synchronized(lock) {
            if (::dataStore.isInitialized) {
                dataStore
            } else {
                PreferenceDataStoreFactory.createWithPath(
                    produceFile = {
                        producerPath().toPath()
                    }
                ).also {
                    dataStore = it
                }
            }
        }
    }
}

expect fun initializeMaps()

expect fun openInExternalMaps(context: Any, latitude: Double, longitude: Double, label: String)

@Composable
expect fun CourtLocationMap(
    latitude: Double,
    longitude: Double,
    courtName: String,
    modifier: Modifier = Modifier
)
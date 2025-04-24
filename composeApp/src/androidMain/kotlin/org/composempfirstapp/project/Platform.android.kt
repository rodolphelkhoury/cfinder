
package org.composempfirstapp.project

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.composempfirstapp.project.core.datastoreFileName
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.io.File

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

// In android whenever we want to share we need an activity which hone aam nekhda bel awal men l main activity
actual fun shareLink(url: String) {
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_TEXT, url)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, "Share Link")
    activityProvider.invoke().startActivity(shareIntent)
}

private var activityProvider: () -> Activity = {
    throw IllegalArgumentException("Error")
}

fun setActivityProvider(provider: () -> Activity) {
    activityProvider = provider
}

actual fun dataStorePreference(): DataStore<Preferences> {
    return AppSettings.getDataStore(
        producerPath = {
            activityProvider.invoke().filesDir.resolve(datastoreFileName).absolutePath
        }
    )
}



// This will be called from your App's initialization
actual fun initializeMaps() {
    // This is a no-op function at the platform level
    // The actual initialization will happen in the Composable when we have Context
}

@Composable
actual fun CourtLocationMap(latitude: Double, longitude: Double, courtName: String, modifier: Modifier) {
    val context = LocalContext.current

    // Initialize OSMDroid configuration when the composable is first created
    DisposableEffect(Unit) {
        val config = Configuration.getInstance()
        config.userAgentValue = context.packageName
        config.osmdroidTileCache = File(context.cacheDir, "osmdroid")

        onDispose { }
    }

    // Create and configure the map view
    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            MapView(ctx).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)

                controller.setZoom(15.0)
                val startPoint = GeoPoint(latitude, longitude)
                controller.setCenter(startPoint)

                // Add marker
                val marker = Marker(this)
                marker.position = startPoint
                marker.title = courtName
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                overlays.add(marker)
            }
        },
        update = { mapView ->
            // Update logic when parameters change
            val mapPoint = GeoPoint(latitude, longitude)
            mapView.controller.animateTo(mapPoint)

            // Update or create marker
            val existingMarker = mapView.overlays.filterIsInstance<Marker>().firstOrNull()
            if (existingMarker != null) {
                existingMarker.position = mapPoint
                existingMarker.title = courtName
            } else {
                val marker = Marker(mapView)
                marker.position = mapPoint
                marker.title = courtName
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                mapView.overlays.add(marker)
            }

            mapView.invalidate()
        }
    )
}

actual fun openInExternalMaps(context: Any, latitude: Double, longitude: Double, label: String) {
    val androidContext = context as Context
    val uri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude($label)")
    val intent = Intent(Intent.ACTION_VIEW, uri)

    // Check if Google Maps is installed
    val packageManager = androidContext.packageManager
    val activities = packageManager.queryIntentActivities(intent, 0)
    val isGoogleMapsInstalled = activities.any { it.activityInfo.packageName == "com.google.android.apps.maps" }

    // Set Google Maps as explicit handler if installed
    if (isGoogleMapsInstalled) {
        intent.setPackage("com.google.android.apps.maps")
    }

    // Make sure we only start the activity if there's something to handle it
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

    androidContext.startActivity(intent)
}
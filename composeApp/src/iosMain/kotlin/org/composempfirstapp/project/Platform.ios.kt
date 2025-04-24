package org.composempfirstapp.project

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIDevice
import org.composempfirstapp.project.core.datastoreFileName
import androidx.compose.ui.interop.UIKitView
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.MapKit.MKMapView
import platform.MapKit.MKPointAnnotation
import platform.MapKit.MKCoordinateRegionMakeWithDistance
import androidx.compose.foundation.clickable



class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

actual fun shareLink(url: String) {
    val currentViewController = UIApplication.sharedApplication().keyWindow?.rootViewController
    val activityViewController = UIActivityViewController(listOf(url), null)
    currentViewController?.presentViewController(
        viewControllerToPresent = activityViewController,
        animated = true,
        completion = null
    )
}

@OptIn(ExperimentalForeignApi::class)
actual fun dataStorePreference(): DataStore<Preferences> {
    return AppSettings.getDataStore(
        producerPath =  {
            val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
                directory = NSDocumentDirectory,
                inDomain = NSUserDomainMask,
                appropriateForURL = null,
                create = false,
                error = null,
            )
            requireNotNull(documentDirectory).path + "/$datastoreFileName"
        })
}



actual fun initializeMaps() {
    // No initialization needed for iOS MapKit
}


@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun CourtLocationMap(
    latitude: Double,
    longitude: Double,
    courtName: String,
    modifier: Modifier,
) {

    // Function to open in Apple Maps
    val openInMaps = {
        // Create URL for Apple Maps
        val mapUrl = "https://maps.apple.com/?q=$courtName&ll=$latitude,$longitude"
        val url = NSURL.URLWithString(mapUrl)

        // Open URL in Maps app
        if (url != null) {
            UIApplication.sharedApplication.openURL(url)
        }

    }

    UIKitView(
        modifier = modifier.clickable { openInMaps() },
        factory = {
            MKMapView().apply {

                // Set region with zoom level
                val coordinate = CLLocationCoordinate2DMake(latitude, longitude)
                setRegion(MKCoordinateRegionMakeWithDistance(
                    coordinate,
                    500.0, // meters of latitude (zoom level)
                    500.0  // meters of longitude
                ), animated = false)

                // Add annotation (marker)
                val annotation = MKPointAnnotation().apply {
                    setCoordinate(coordinate)
                    setTitle(courtName)
                    setSubtitle("Tap to open in Maps")
                }
                addAnnotation(annotation)

                // Set tap handler
            }
        },
        update = { mapView ->
            // Remove existing annotations
            val existingAnnotations = mapView.annotations?.toList() ?: listOf()
            if (existingAnnotations.isNotEmpty()) {
                mapView.removeAnnotations(existingAnnotations)
            }

            // Update map center and add new annotation
            val coordinate = CLLocationCoordinate2DMake(latitude, longitude)
            mapView.setRegion(MKCoordinateRegionMakeWithDistance(
                coordinate,
                500.0,
                500.0
            ), animated = true)

            val annotation = MKPointAnnotation().apply {
                setCoordinate(coordinate)
                setTitle(courtName)
                setSubtitle("Tap to open in Maps")
            }
            mapView.addAnnotation(annotation)

        }
    )
}


actual fun openInExternalMaps(context: Any, latitude: Double, longitude: Double, label: String) {
    // Create URL for Apple Maps
    val encodedLabel = label.replace(" ", "+")
    val mapUrl = "http://maps.apple.com/?q=$encodedLabel&ll=$latitude,$longitude"
    val url = NSURL.URLWithString(mapUrl)

    // Open URL in Maps app
    if (url != null) {
        UIApplication.sharedApplication.openURL(url)
    }
}

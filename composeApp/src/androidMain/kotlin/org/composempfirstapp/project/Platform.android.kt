package org.composempfirstapp.project

import android.app.Activity
import android.content.Intent
import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

// In android whenever we want to share we need  an activity which hone aam nekhda bel awal men l main activity
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
package org.composempfirstapp.project.navigation

object Graph {
    const val RootScreenGraph = "rootScreenGraph"
    const val MainScreenGraph = "mainScreenGraph"
}

sealed class MainRouteScreen (var route : String) {
    object Home : MainRouteScreen("home")
    object Offers : MainRouteScreen("offers")
    object Booking : MainRouteScreen("booking")
    object Profile : MainRouteScreen("profile")
}

sealed class SettingRouteScreen (var route : String) {
    object Setting : SettingRouteScreen("settings")
}

sealed class CourtRouteScreen (var route : String) {
    object CourtDetail : CourtRouteScreen("courtDetail")
}
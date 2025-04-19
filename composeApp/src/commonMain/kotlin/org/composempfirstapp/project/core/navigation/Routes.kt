package org.composempfirstapp.project.core.navigation

object Graph {
    const val RootScreenGraph = "rootScreenGraph"
    const val MainScreenGraph = "mainScreenGraph"
}

object AuthRoutes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val VERIFY_OTP = "verify_otp"
    const val AUTH_GRAPH = "auth_graph"
}

sealed class MainRouteScreen(var route: String) {
    object Home : MainRouteScreen("home")
    object Reservation : MainRouteScreen("reservation")
    object Profile : MainRouteScreen("profile")
}

sealed class ProfileRouteScreen(val route: String) {
    object MyProfile : ProfileRouteScreen("my_profile")
    object MyCourts  : ProfileRouteScreen("my_courts")
    object Settings  : ProfileRouteScreen("settings")
    object AboutUs   : ProfileRouteScreen("aboutus")
}

sealed class CourtRouteScreen(var route: String) {
    object CourtDetail : CourtRouteScreen("courtDetail")
}

sealed class ReservationRouteScreen(var route: String) {
    object ReservationDetail : ReservationRouteScreen("reservationDetail")
    object CourtReservation : ReservationRouteScreen("courtReservation")
}
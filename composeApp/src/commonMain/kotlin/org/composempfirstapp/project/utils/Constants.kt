package org.composempfirstapp.project.utils

import cfinder.composeapp.generated.resources.Res
import cfinder.composeapp.generated.resources.booking
import cfinder.composeapp.generated.resources.dark_mode
import cfinder.composeapp.generated.resources.headline
import cfinder.composeapp.generated.resources.home
import cfinder.composeapp.generated.resources.light_mode
import cfinder.composeapp.generated.resources.offers
import cfinder.composeapp.generated.resources.person
import cfinder.composeapp.generated.resources.profile
import cfinder.composeapp.generated.resources.system_default
import org.composempfirstapp.project.navigation.BottomNavigationItem
import org.composempfirstapp.project.navigation.MainRouteScreen
import org.jetbrains.compose.resources.StringResource

enum class Theme(val title: StringResource) {
    SYSTEM_DEFAULT(Res.string.system_default),
    LIGHT_MODE(Res.string.light_mode),
    DARK_MODE(Res.string.dark_mode)
}

val bottomNavigationList = listOf(
    BottomNavigationItem(
        icon = Res.drawable.home,
        title = Res.string.home,
        route = MainRouteScreen.Home.route
    ),
    BottomNavigationItem(
        icon = Res.drawable.headline,
        title = Res.string.offers,
        route = MainRouteScreen.Offers.route
    ),
    BottomNavigationItem(
        icon = Res.drawable.booking,
        title = Res.string.booking,
        route = MainRouteScreen.Booking.route
    ),
    BottomNavigationItem(
        icon = Res.drawable.person,
        title = Res.string.profile,
        route = MainRouteScreen.Profile.route
    ),
)
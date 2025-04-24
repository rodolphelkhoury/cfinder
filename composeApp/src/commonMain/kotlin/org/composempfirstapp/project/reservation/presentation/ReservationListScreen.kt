package org.composempfirstapp.project.reservation.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.composempfirstapp.project.core.navigation.CourtRouteScreen
import org.composempfirstapp.project.core.navigation.ReservationRouteScreen
import org.composempfirstapp.project.core.theme.xLargePadding
import org.composempfirstapp.project.core.theme.xSmallPadding
import org.composempfirstapp.project.court.domain.Court
import org.composempfirstapp.project.court.presentation.CourtComponent
import org.composempfirstapp.project.reservation.domain.Reservation

@Composable
fun ReservationListScreen(
    modifier: Modifier = Modifier,
    reservationList: List<Reservation>,
    navController: NavController
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = xLargePadding),
        contentAlignment = Alignment.TopCenter // Force alignment to top
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(xSmallPadding),
            contentPadding = PaddingValues(
                top = xLargePadding,
                bottom = xLargePadding
            )
        ) {
            items(
                reservationList,
                key = {
                    it.id
                }
            ) {
                ReservationComponent(
                    reservation = it,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        val reservation = Json.encodeToString(it)
                        navController.currentBackStackEntry?.savedStateHandle?.apply {
                            set("reservation", reservation)
                        }
                        navController.navigate(ReservationRouteScreen.ReservationDetail.route)
                    }
                )
            }
        }
    }
}
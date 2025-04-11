package org.composempfirstapp.project.reservation.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cfinder.composeapp.generated.resources.Res
import cfinder.composeapp.generated.resources.ic_browse
import cfinder.composeapp.generated.resources.no_courts
import org.composempfirstapp.project.court.presentation.CourtListScreen
import org.composempfirstapp.project.court.presentation.CourtViewModel
import org.composempfirstapp.project.utils.EmptyContent
import org.composempfirstapp.project.utils.ShimmerEffect
import org.jetbrains.compose.resources.stringResource

@Composable
fun ReservationScreen(
    navController: NavController
) {

    val reservationViewModel = viewModel {
        ReservationViewModel()
    }

    val uiState by reservationViewModel.reservationStateFlow.collectAsState()

    uiState.DisplayResult(
        onIdle = {

        },
        onLoading = {
            ShimmerEffect()
        },
        onSuccess = { courtList ->
            if (courtList.isEmpty()) {
                EmptyContent(
                    message = stringResource(Res.string.no_courts),
                    icon = Res.drawable.ic_browse,
                    onRetryClick = {
                        reservationViewModel.getReservations()
                    }
                )

            } else {
                // TODO
                CourtListScreen(
                    courtList = courtList,
                    navController = navController
                )
            }
        },
        onError = {
            EmptyContent(
                message = it,
                icon = Res.drawable.ic_browse,
                onRetryClick = {
                    reservationViewModel.getReservations()
                }
            )
        }
    )
}
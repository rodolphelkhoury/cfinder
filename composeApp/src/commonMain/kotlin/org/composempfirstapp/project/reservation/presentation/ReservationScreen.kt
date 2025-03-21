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
import org.composempfirstapp.project.court.presentation.CourtListScreen
import org.composempfirstapp.project.court.presentation.CourtViewModel
import org.composempfirstapp.project.utils.EmptyContent
import org.composempfirstapp.project.utils.ShimmerEffect

@Composable
fun ReservationScreen(modifier: Modifier = Modifier) {

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
                EmptyContent("No reservations")

            } else {
                // TODO
                CourtListScreen(
                    courtList = courtList
                )
            }
        },
        onError = {
            EmptyContent(it)
        }
    )
}
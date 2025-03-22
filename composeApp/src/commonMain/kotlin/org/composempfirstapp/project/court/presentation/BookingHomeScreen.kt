package org.composempfirstapp.project.court.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.composempfirstapp.project.utils.EmptyContent
import org.composempfirstapp.project.utils.ShimmerEffect
import org.composempfirstapp.project.utils.courts

@Composable
fun BookingHomeScreen(
    navController: NavController
) {
    Box(
        modifier = Modifier,
    ) {
        val courtViewModel = viewModel {
            CourtViewModel()
        }

        val uiState by courtViewModel.courtStateFlow.collectAsState()
        uiState.DisplayResult(
            onIdle = {

            },
            onLoading = {
                ShimmerEffect()
            },
            onSuccess = { courtList ->
                if (courtList.isEmpty()) {
                    EmptyContent("No courts")

                } else {
                    CourtListScreen(
                        courtList = courtList,
                        navController = navController
                    )
                }
            },
            onError = {
                EmptyContent(it)
            }
        )
    }
}
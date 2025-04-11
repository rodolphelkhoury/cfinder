package org.composempfirstapp.project.court.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cfinder.composeapp.generated.resources.Res
import cfinder.composeapp.generated.resources.ic_network_error
import cfinder.composeapp.generated.resources.ic_browse
import cfinder.composeapp.generated.resources.no_courts
import org.composempfirstapp.project.court.data.CourtRepository
import org.composempfirstapp.project.utils.EmptyContent
import org.composempfirstapp.project.utils.ShimmerEffect
import org.jetbrains.compose.resources.stringResource

@Composable
fun BookingHomeScreen(
    navController: NavController
) {
    Box(
        modifier = Modifier,
    ) {
        val courtViewModel = viewModel {
            CourtViewModel(CourtRepository())
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
                    EmptyContent(
                        message = stringResource(Res.string.no_courts),
                        icon = Res.drawable.ic_network_error,
                        onRetryClick = {
                            courtViewModel.getCourts()
                        }
                    )

                } else {
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
                        courtViewModel.getCourts()
                    }
                )
            }
        )
    }
}
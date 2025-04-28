package org.composempfirstapp.project.reservation.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cfinder.composeapp.generated.resources.Res
import cfinder.composeapp.generated.resources.ic_browse
import cfinder.composeapp.generated.resources.ic_network_error
import cfinder.composeapp.generated.resources.no_reservations
import org.composempfirstapp.project.core.AppPreferences
import org.composempfirstapp.project.core.EmptyContent
import org.composempfirstapp.project.core.ShimmerEffect
import org.composempfirstapp.project.reservation.data.ReservationRepository
import org.jetbrains.compose.resources.stringResource

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.composempfirstapp.project.core.Resource

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ReservationScreen(
    navController: NavController,
    appPreferences: AppPreferences
) {
    val reservationViewModel = viewModel {
        ReservationViewModel(ReservationRepository(appPreferences))
    }

    val upcomingState by reservationViewModel.upcomingReservationStateFlow.collectAsState()
    val completedState by reservationViewModel.completedReservationStateFlow.collectAsState()

    var selectedTabIndex by remember { mutableStateOf(0) }

    var isRefreshing by remember { mutableStateOf(false) }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            reservationViewModel.getReservations()
        }
    )

    LaunchedEffect(upcomingState, completedState) {
        if (isRefreshing &&
            upcomingState !is Resource.Loading &&
            completedState !is Resource.Loading) {
            isRefreshing = false
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Tab Row for switching between upcoming and completed
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 },
                text = {
                    Text(
                        text = "Upcoming",
                        fontSize = 18.sp, // Adjust font size here
                        fontWeight = FontWeight.Bold // Optional: Make it bold
                    )
                }
            )
            Tab(
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 },
                text = {
                    Text(
                        text = "Completed",
                        fontSize = 18.sp, // Adjust font size here
                        fontWeight = FontWeight.Bold // Optional: Make it bold
                    )
                }
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .pullRefresh(pullRefreshState),
            contentAlignment = Alignment.Center
        ) {
            when (selectedTabIndex) {
                0 -> {
                    upcomingState.DisplayResult(
                        onIdle = {},
                        onLoading = {
                            ShimmerEffect()
                        },
                        onSuccess = { reservationList ->
                            if (reservationList.isEmpty()) {
                                EmptyContent(
                                    message = stringResource(Res.string.no_reservations),
                                    icon = Res.drawable.ic_browse,
                                    onRetryClick = {
                                        reservationViewModel.getReservations()
                                    }
                                )
                            } else {
                                ReservationListScreen(
                                    reservationList = reservationList,
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
                1 -> {
                    completedState.DisplayResult(
                        onIdle = {},
                        onLoading = {
                            ShimmerEffect()
                        },
                        onSuccess = { reservationList ->
                            if (reservationList.isEmpty()) {
                                EmptyContent(
                                    message = stringResource(Res.string.no_reservations),
                                    icon = Res.drawable.ic_browse,
                                    onRetryClick = {
                                        reservationViewModel.getReservations()
                                    }
                                )
                            } else {
                                ReservationListScreen(
                                    reservationList = reservationList,
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
            }

            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                contentColor = MaterialTheme.colorScheme.primary
            )
        }
    }
}
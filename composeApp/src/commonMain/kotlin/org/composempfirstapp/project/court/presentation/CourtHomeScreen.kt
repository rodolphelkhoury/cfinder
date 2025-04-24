package org.composempfirstapp.project.court.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cfinder.composeapp.generated.resources.Res
import cfinder.composeapp.generated.resources.ic_network_error
import cfinder.composeapp.generated.resources.ic_browse
import cfinder.composeapp.generated.resources.no_courts
import org.composempfirstapp.project.core.AppPreferences
import org.composempfirstapp.project.court.data.CourtRepository
import org.composempfirstapp.project.core.EmptyContent
import org.composempfirstapp.project.core.ShimmerEffect
import org.composempfirstapp.project.core.components.SearchBar
import org.composempfirstapp.project.core.theme.mediumPadding
import org.jetbrains.compose.resources.stringResource

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.composempfirstapp.project.core.Resource

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CourtHomeScreen(
    navController: NavController,
    appPreferences: AppPreferences
) {
    val courtViewModel = viewModel {
        CourtViewModel(CourtRepository(appPreferences))
    }

    val uiState by courtViewModel.courtStateFlow.collectAsState()
    val searchQuery by courtViewModel.searchQuery.collectAsState()

    // For pull refresh, track a separate refreshing state
    var isRefreshing by remember { mutableStateOf(false) }

    // Pull-to-refresh state
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            courtViewModel.getCourts(searchQuery)
        }
    )

    // Reset isRefreshing when loading completes
    LaunchedEffect(uiState) {
        if (isRefreshing && uiState !is Resource.Loading) {
            isRefreshing = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            SearchBar(
                text = searchQuery,
                onValueChange = { courtViewModel.updateSearchQuery(it) },
                onSearch = { courtViewModel.getCourts(it) },
                modifier = Modifier.padding(top = mediumPadding)
            )

            uiState.DisplayResult(
                onIdle = {},
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

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            contentColor = MaterialTheme.colorScheme.primary
        )
    }
}
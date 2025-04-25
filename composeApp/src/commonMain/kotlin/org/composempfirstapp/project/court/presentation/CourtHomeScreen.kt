package org.composempfirstapp.project.court.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cfinder.composeapp.generated.resources.Res
import cfinder.composeapp.generated.resources.ic_browse
import cfinder.composeapp.generated.resources.ic_network_error
import cfinder.composeapp.generated.resources.no_courts
import org.composempfirstapp.project.core.AppPreferences
import org.composempfirstapp.project.core.EmptyContent
import org.composempfirstapp.project.core.ShimmerEffect
import org.composempfirstapp.project.core.components.SearchBar
import org.composempfirstapp.project.core.theme.mediumPadding
import org.composempfirstapp.project.court.data.CourtRepository
import org.composempfirstapp.project.core.Resource
import org.jetbrains.compose.resources.stringResource

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

    var isRefreshing by remember { mutableStateOf(false) }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            courtViewModel.getCourts(searchQuery)
        }
    )

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
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp)
        ) {
            // Search bar
            SearchBar(
                text = searchQuery,
                onValueChange = { courtViewModel.updateSearchQuery(it) },
                onSearch = { courtViewModel.getCourts(it) },
                modifier = Modifier.padding(horizontal = mediumPadding)
            )

            // Results
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

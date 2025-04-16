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

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SearchBar(
            text = searchQuery,
            onValueChange = { courtViewModel.updateSearchQuery(it) },
            onSearch = { courtViewModel.getCourts(it) },
            modifier = Modifier.padding(top = mediumPadding)
        )

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
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
    }
}
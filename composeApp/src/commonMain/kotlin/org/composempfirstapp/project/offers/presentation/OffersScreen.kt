package org.composempfirstapp.project.offers.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cfinder.composeapp.generated.resources.Res
import cfinder.composeapp.generated.resources.ic_browse
import cfinder.composeapp.generated.resources.no_courts
import cfinder.composeapp.generated.resources.type_to_search
import org.composempfirstapp.project.court.presentation.CourtListScreen
import org.composempfirstapp.project.theme.mediumPadding
import org.composempfirstapp.project.utils.EmptyContent
import org.composempfirstapp.project.utils.ShimmerEffect
import org.composempfirstapp.project.utils.components.SearchBar
import org.composempfirstapp.project.utils.courts
import org.jetbrains.compose.resources.stringResource

@Composable
fun OffersScreen(
    navController: NavController
) {
    var searchQuery by rememberSaveable() {
        mutableStateOf("")
    }

    val offersViewModel = viewModel {
        OffersViewModel()
    }


    val uiState by offersViewModel.courtStateFlow.collectAsState()

    Column(
        modifier = Modifier,
        verticalArrangement =  Arrangement.spacedBy(mediumPadding)
    ) {
        SearchBar(
            text = searchQuery,
            onValueChange = {
                searchQuery = it
            },
            onSearch = { query ->
                if (query.trim().isNotEmpty()) {
                    offersViewModel.searchQueryCourts(query)
                }

            }
        )

        uiState.DisplayResult(
            onIdle = {
                EmptyContent(
                    message = stringResource(Res.string.type_to_search),
                    icon = Res.drawable.ic_browse,
                    isOnRetryBtnVisible = false
                )

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
                            if (searchQuery.trim().isNotEmpty()) {
                                offersViewModel.searchQueryCourts(searchQuery)
                            }
                        }
                    )

                } else {
                    CourtListScreen(courtList = courts, navController = navController)
                }
            },
            onError = {
                EmptyContent(
                    message = it,
                    icon = Res.drawable.ic_browse,
                    onRetryClick = {
                        if (searchQuery.trim().isNotEmpty()) {
                            offersViewModel.searchQueryCourts(searchQuery)
                        }
                    }
                )
            }
        )
    }
}
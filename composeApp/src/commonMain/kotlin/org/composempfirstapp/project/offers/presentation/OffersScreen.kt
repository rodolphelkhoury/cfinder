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
import org.composempfirstapp.project.court.presentation.CourtListScreen
import org.composempfirstapp.project.court.presentation.CourtViewModel
import org.composempfirstapp.project.theme.mediumPadding
import org.composempfirstapp.project.utils.EmptyContent
import org.composempfirstapp.project.utils.ShimmerEffect
import org.composempfirstapp.project.utils.components.SearchBar
import org.composempfirstapp.project.utils.courts

@Composable
fun OffersScreen(modifier: Modifier = Modifier) {
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
                EmptyContent("Type to search")
            },
            onLoading = {
                ShimmerEffect()
            },
            onSuccess = { courtList ->
                if (courtList.isEmpty()) {
                    EmptyContent("No courts")

                } else {
                    CourtListScreen(courtList = courts)
                }
            },
            onError = {
                EmptyContent(it)
            }
        )
    }
}
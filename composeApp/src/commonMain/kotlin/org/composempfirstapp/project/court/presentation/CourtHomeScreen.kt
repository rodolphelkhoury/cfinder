package org.composempfirstapp.project.court.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import org.composempfirstapp.project.core.theme.smallPadding
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
    val courtTypeFilter by courtViewModel.courtTypeFilter.collectAsState()
    val availableCourtTypes by courtViewModel.availableCourtTypes.collectAsState()

    var isRefreshing by remember { mutableStateOf(false) }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            courtViewModel.getCourts(searchQuery, courtTypeFilter)
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
                onSearch = { courtViewModel.updateSearchQuery(it) },
                modifier = Modifier.padding(horizontal = mediumPadding)
            )

            // Court type filter - always visible
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = mediumPadding, vertical = smallPadding)
            ) {
                // Filter header with clear button
                if (courtTypeFilter.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Court Type: $courtTypeFilter",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )

                        // Clear filter button
                        IconButton(onClick = { courtViewModel.updateCourtTypeFilter("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear filter",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }

                // Court type chips - always visible
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = smallPadding),
                    horizontalArrangement = Arrangement.spacedBy(smallPadding)
                ) {
                    // "All" option
                    item {
                        FilterChip(
                            selected = courtTypeFilter.isEmpty(),
                            onClick = { courtViewModel.updateCourtTypeFilter("") },
                            label = { Text("All") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }

                    // All available court types
                    items(availableCourtTypes) { type ->
                        FilterChip(
                            selected = courtTypeFilter == type,
                            onClick = { courtViewModel.updateCourtTypeFilter(type) },
                            label = { Text(type) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                }
            }

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
                            icon = Res.drawable.ic_browse,
                            iconModifier = Modifier.padding(end = 12.dp),
                            onRetryClick = {
                                courtViewModel.getCourts(searchQuery, courtTypeFilter)
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
                        iconModifier = Modifier.padding(end = 20.dp),
                        onRetryClick = {
                            courtViewModel.getCourts(searchQuery, courtTypeFilter)
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
package org.composempfirstapp.project.reservation.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cfinder.composeapp.generated.resources.Res
import cfinder.composeapp.generated.resources.court_details
import cfinder.composeapp.generated.resources.ic_bookmark_filled
import cfinder.composeapp.generated.resources.ic_bookmark_outlined
import cfinder.composeapp.generated.resources.ic_browse
import cfinder.composeapp.generated.resources.logo
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch
import org.composempfirstapp.project.shareLink
import org.composempfirstapp.project.core.theme.detailImageSize
import org.composempfirstapp.project.core.theme.mediumPadding
import org.composempfirstapp.project.core.theme.xLargePadding
import org.composempfirstapp.project.profile.presentation.myfavorites.MyFavoritesViewModel
import org.composempfirstapp.project.reservation.domain.Reservation
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationDetailScreen(
    navController: NavController,
    currentReservation: Reservation,
    myfavoritedViewModel: MyFavoritesViewModel = viewModel()
) {
    val uriHandler = LocalUriHandler.current
    val currentCourt = currentReservation.court ?: return

    // Check if this court is favorited
    val favoriteCourts = myfavoritedViewModel.favoriteCourts.collectAsState(initial = emptyList())
    val isCourtFavorite = favoriteCourts.value.any { it.id == currentCourt.id }

    // States for Snackbar and favorites
    var snackbarMessage by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(Res.string.court_details),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.court_details)
                        )
                    }
                },
                actions = {
                    currentCourt.imageUrl?.let {
                        IconButton(onClick = { shareLink(it) }) {
                            Icon(Icons.Filled.Share, contentDescription = "Share")
                        }

                        IconButton(onClick = { uriHandler.openUri(it) }) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_browse),
                                contentDescription = "Open in browser"
                            )
                        }
                    }
                    IconButton(onClick = {
                        myfavoritedViewModel.toggleFavorite(currentCourt)
                        snackbarMessage = if (isCourtFavorite) {
                            "${currentCourt.name} was removed from My Favorites"
                        } else {
                            "${currentCourt.name} was added to My Favorites"
                        }
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(snackbarMessage)
                        }
                    }) {
                        Icon(
                            painter = painterResource(
                                if (isCourtFavorite) Res.drawable.ic_bookmark_filled
                                else Res.drawable.ic_bookmark_outlined
                            ),
                            contentDescription = "Toggle Favorite",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = mediumPadding, vertical = xLargePadding),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    // Court Image
                    AsyncImage(
                        model = currentCourt.imageUrl,
                        error = painterResource(Res.drawable.logo),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(detailImageSize)
                            .clip(MaterialTheme.shapes.medium)
                            .background(Color.Gray)
                    )
                }

                item {
                    Column {
                        Text(
                            text = currentCourt.name,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        currentCourt.description?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Hourly Rate: ${currentCourt.hourlyRate} USD",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Optional reservation details section
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Column(
                            modifier = Modifier.padding(mediumPadding)
                        ) {
                            Text(
                                text = "Details",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(currentReservation.reservationDate.toString())
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Time")
                                Text("${currentReservation.startTime} - ${currentReservation.endTime}")
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Price")
                                Text("$${currentReservation.totalPrice}")
                            }
                        }
                    }
                }
            }
        }
    )
}
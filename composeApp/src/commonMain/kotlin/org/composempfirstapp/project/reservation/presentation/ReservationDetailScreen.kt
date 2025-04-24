package org.composempfirstapp.project.reservation.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cfinder.composeapp.generated.resources.Res
import cfinder.composeapp.generated.resources.court_details
import cfinder.composeapp.generated.resources.ic_bookmark_outlined
import cfinder.composeapp.generated.resources.ic_browse
import cfinder.composeapp.generated.resources.logo
import coil3.compose.AsyncImage
import org.composempfirstapp.project.shareLink
import org.composempfirstapp.project.core.theme.detailImageSize
import org.composempfirstapp.project.core.theme.mediumPadding
import org.composempfirstapp.project.core.theme.xLargePadding
import org.composempfirstapp.project.reservation.domain.Reservation
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationDetailScreen(
    navController: NavController,
    currentReservation: Reservation
) {
    val uriHandler = LocalUriHandler.current
    val currentCourt = currentReservation.court ?: return

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(
                            Res.string.court_details
                        ),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigateUp()
                        }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.court_details)
                        )
                    }
                },
                actions = {
                    currentCourt.imageUrl?.let {
                        IconButton(
                            onClick = {
                                shareLink(it)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Share,
                                contentDescription = "Share"
                            )
                        }
                    }
                    currentCourt.imageUrl?.let {
                        IconButton(
                            onClick = {
                                uriHandler.openUri(it)
                            }
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_browse),
                                contentDescription = "Open in browser"
                            )
                        }
                    }
                    IconButton(
                        onClick = {
                            // Bookmark functionality would be implemented here
                        }
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_bookmark_outlined),
                            contentDescription = "Bookmark"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(xLargePadding),
            verticalArrangement = Arrangement.spacedBy(xLargePadding)
        ) {
            // Display reservation details
            item {
                AsyncImage(
                    modifier = Modifier
                        .height(detailImageSize)
                        .clip(MaterialTheme.shapes.large)
                        .background(Color.Gray)
                        .fillMaxWidth(),
                    model = currentCourt.imageUrl,
                    error = painterResource(Res.drawable.logo),
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
            }
            item {
                Text(
                    text = currentCourt.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            currentCourt.description?.let {
                item {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            item {
                Text(
                    text = currentReservation.totalPrice.toString() + currentCourt.hourlyRate.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            // Add reservation-specific details
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(
                        modifier = Modifier.padding(mediumPadding)
                    ) {
                        Text(
                            // TODO
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
}













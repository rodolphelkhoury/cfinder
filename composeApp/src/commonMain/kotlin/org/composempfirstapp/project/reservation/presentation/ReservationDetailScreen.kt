//
//package org.composempfirstapp.project.reservation.presentation
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.runtime.getValue
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.TopAppBar
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.ArrowBack
//import androidx.compose.material.icons.filled.LocationOn
//import androidx.compose.material.icons.filled.Share
//import androidx.compose.material3.Card
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.SnackbarHost
//import androidx.compose.material3.SnackbarHostState
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextButton
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.LocalUriHandler
//import androidx.compose.ui.text.SpanStyle
//import androidx.compose.ui.text.buildAnnotatedString
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.withStyle
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import cfinder.composeapp.generated.resources.Res
//import cfinder.composeapp.generated.resources.court_details
//import cfinder.composeapp.generated.resources.ic_bookmark_filled
//import cfinder.composeapp.generated.resources.ic_bookmark_outlined
//import cfinder.composeapp.generated.resources.ic_browse
//import cfinder.composeapp.generated.resources.logo
//import coil3.compose.AsyncImage
//import kotlinx.coroutines.launch
//import org.composempfirstapp.project.CourtLocationMap
//import org.composempfirstapp.project.openInExternalMaps
//import org.composempfirstapp.project.shareLink
//import org.composempfirstapp.project.core.theme.detailImageSize
//import org.composempfirstapp.project.core.theme.mediumPadding
//import org.composempfirstapp.project.core.theme.xLargePadding
//import org.composempfirstapp.project.profile.presentation.myfavorites.MyFavoritesViewModel
//import org.composempfirstapp.project.reservation.domain.Reservation
//import org.jetbrains.compose.resources.painterResource
//import org.jetbrains.compose.resources.stringResource
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ReservationDetailScreen(
//    navController: NavController,
//    currentReservation: Reservation,
//    myfavoritedViewModel: MyFavoritesViewModel = viewModel()
//) {
//    val context = LocalContext.current
//    val uriHandler = LocalUriHandler.current
//    val currentCourt = currentReservation.court ?: return
//
//    val favoriteCourts = myfavoritedViewModel.favoriteCourts.collectAsState(initial = emptyList())
//    val isCourtFavorite = favoriteCourts.value.any { it.id == currentCourt.id }
//
//    var snackbarMessage by remember { mutableStateOf("") }
//    val snackbarHostState = remember { SnackbarHostState() }
//    val coroutineScope = rememberCoroutineScope()
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        stringResource(Res.string.court_details),
//                        style = MaterialTheme.typography.headlineSmall,
//                        fontWeight = FontWeight.Bold,
//                        color = MaterialTheme.colorScheme.primary
//                    )
//                },
//                navigationIcon = {
//                    IconButton(onClick = { navController.navigateUp() }) {
//                        Icon(
//                            Icons.AutoMirrored.Filled.ArrowBack,
//                            contentDescription = stringResource(Res.string.court_details)
//                        )
//                    }
//                },
//                actions = {
//                    currentCourt.imageUrl?.let {
//                        IconButton(onClick = { shareLink(it) }) {
//                            Icon(Icons.Filled.Share,
//                                contentDescription = "Share",
//                                tint = MaterialTheme.colorScheme.primary)
//                        }
//
//                        IconButton(onClick = { uriHandler.openUri(it) }) {
//                            Icon(
//                                painter = painterResource(Res.drawable.ic_browse),
//                                contentDescription = "Open in browser",
//                                tint = MaterialTheme.colorScheme.primary
//                            )
//                        }
//                    }
//                    IconButton(onClick = {
//                        myfavoritedViewModel.toggleFavorite(currentCourt)
//                        snackbarMessage = if (isCourtFavorite) {
//                            "${currentCourt.name} was removed from My Favorites"
//                        } else {
//                            "${currentCourt.name} was added to My Favorites"
//                        }
//                        coroutineScope.launch {
//                            snackbarHostState.showSnackbar(snackbarMessage)
//                        }
//                    }) {
//                        Icon(
//                            painter = painterResource(
//                                if (isCourtFavorite) Res.drawable.ic_bookmark_filled
//                                else Res.drawable.ic_bookmark_outlined
//                            ),
//                            contentDescription = "Toggle Favorite",
//                            tint = MaterialTheme.colorScheme.primary
//                        )
//                    }
//                }
//            )
//        },
//        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
//    ) { padding ->
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding)
//                .padding(horizontal = mediumPadding, vertical = xLargePadding),
//            verticalArrangement = Arrangement.spacedBy(20.dp)
//        ) {
//            item {
//                AsyncImage(
//                    model = currentCourt.imageUrl,
//                    error = painterResource(Res.drawable.logo),
//                    contentDescription = null,
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(detailImageSize)
//                        .clip(MaterialTheme.shapes.medium)
//                        .background(Color.Gray)
//                )
//            }
//
//            item {
//                Text(
//                    text = currentCourt.name,
//                    style = MaterialTheme.typography.headlineSmall,
//                    fontWeight = FontWeight.Bold,
//                    color = MaterialTheme.colorScheme.primary
//                )
//            }
//
//            currentCourt.description?.let {
//                item {
//                    Text(
//                        text = "Description:",
//                        fontWeight = FontWeight.Bold,
//                        style = MaterialTheme.typography.titleMedium,
//                        color = MaterialTheme.colorScheme.primary
//                    )
//
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    Text(
//                        text = it,
//                        style = MaterialTheme.typography.bodyMedium,
//                        color = MaterialTheme.colorScheme.onSurfaceVariant
//                    )
//                }
//            }
//
//            item {
//                Text(
//                    text = buildAnnotatedString {
//                        withStyle(
//                            style = MaterialTheme.typography.titleMedium.toSpanStyle().copy(
//                                color = MaterialTheme.colorScheme.primary,
//                                fontWeight = FontWeight.Bold
//                            )
//                        ) {
//                            append("Time: ")
//                        }
//                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) {
//                            append("${currentReservation.startTime} - ${currentReservation.endTime}")
//                        }
//                    }
//                )
//            }
//
//            item {
//                Text(
//                    text = buildAnnotatedString {
//                        withStyle(
//                            style = MaterialTheme.typography.titleMedium.toSpanStyle().copy(
//                                color = MaterialTheme.colorScheme.primary,
//                                fontWeight = FontWeight.Bold
//                            )
//                        ) {
//                            append("Price: ")
//                        }
//                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) {
//                            append("${currentCourt.hourlyRate} $/ hour")
//                        }
//                    },
//                    modifier = Modifier.padding(bottom = 0.dp)
//                )
//            }
//
//            item {
//                Text(
//                    text = buildAnnotatedString {
//                        withStyle(
//                            style = MaterialTheme.typography.titleMedium.toSpanStyle().copy(
//                                color = MaterialTheme.colorScheme.primary,
//                                fontWeight = FontWeight.Bold
//                            )
//                        ) {
//                            append("Date: ")
//                        }
//                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) {
//                            append(currentReservation.reservationDate.toString())
//                        }
//                    }
//                )
//            }
//
//            item {
//                Column(modifier = Modifier.padding(top = 0.dp)) {
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        Text(
//                            text = "Location:",
//                            fontWeight = FontWeight.Bold,
//                            style = MaterialTheme.typography.titleMedium,
//                            color = MaterialTheme.colorScheme.primary
//                        )
//                        TextButton(
//                            onClick = {
//                                openInExternalMaps(context, currentCourt.latitude, currentCourt.longitude, currentCourt.name)
//                            }
//                        ) {
//                            Icon(
//                                Icons.Default.LocationOn,
//                                contentDescription = "Open in Maps",
//                                modifier = Modifier.padding(end = 4.dp)
//                            )
//                            Text("Open in Maps")
//                        }
//                    }
//                    CourtLocationMap(
//                        latitude = currentCourt.latitude,
//                        longitude = currentCourt.longitude,
//                        courtName = currentCourt.name,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(200.dp)
//                            .clip(RoundedCornerShape(8.dp))
//                    )
//                }
//            }
//        }
//    }
//}

package org.composempfirstapp.project.reservation.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cfinder.composeapp.generated.resources.*
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch
import org.composempfirstapp.project.CourtLocationMap
import org.composempfirstapp.project.core.theme.detailImageSize
import org.composempfirstapp.project.core.theme.mediumPadding
import org.composempfirstapp.project.core.theme.xLargePadding
import org.composempfirstapp.project.openInExternalMaps
import org.composempfirstapp.project.profile.presentation.myfavorites.MyFavoritesViewModel
import org.composempfirstapp.project.reservation.domain.Reservation
import org.composempfirstapp.project.shareLink
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationDetailScreen(
    navController: NavController,
    currentReservation: Reservation,
    myfavoritedViewModel: MyFavoritesViewModel = viewModel()
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val currentCourt = currentReservation.court ?: return

    val favoriteCourts = myfavoritedViewModel.favoriteCourts.collectAsState(initial = emptyList())
    val isCourtFavorite = favoriteCourts.value.any { it.id == currentCourt.id }

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
                        color = MaterialTheme.colorScheme.primary
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
                            Icon(Icons.Filled.Share,
                                contentDescription = "Share",
                                tint = MaterialTheme.colorScheme.primary)
                        }

                        IconButton(onClick = { uriHandler.openUri(it) }) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_browse),
                                contentDescription = "Open in browser",
                                tint = MaterialTheme.colorScheme.primary
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
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = mediumPadding, vertical = xLargePadding),
            verticalArrangement = Arrangement.spacedBy(16.dp) // Less spacing than 20.dp
        ) {
            item {
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
                Text(
                    text = currentCourt.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            currentCourt.description?.let {
                item {
                    Text(
                        text = "Description:",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Group time, price, and date with minimal spacing
            item {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = MaterialTheme.typography.titleMedium.toSpanStyle().copy(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append("Time: ")
                            }
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) {
                                append("${currentReservation.startTime} - ${currentReservation.endTime}")
                            }
                        }
                    )

                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = MaterialTheme.typography.titleMedium.toSpanStyle().copy(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append("Price: ")
                            }
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) {
                                append("${currentCourt.hourlyRate} $/ hour")
                            }
                        }
                    )

                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = MaterialTheme.typography.titleMedium.toSpanStyle().copy(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append("Date: ")
                            }
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) {
                                append(currentReservation.reservationDate.toString())
                            }
                        }
                    )
                }
            }

            item {
                Column(modifier = Modifier.padding(top = 0.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Location:",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        TextButton(
                            onClick = {
                                openInExternalMaps(context, currentCourt.latitude, currentCourt.longitude, currentCourt.name)
                            }
                        ) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = "Open in Maps",
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Text("Open in Maps")
                        }
                    }
                    CourtLocationMap(
                        latitude = currentCourt.latitude,
                        longitude = currentCourt.longitude,
                        courtName = currentCourt.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
            }
        }
    }
}

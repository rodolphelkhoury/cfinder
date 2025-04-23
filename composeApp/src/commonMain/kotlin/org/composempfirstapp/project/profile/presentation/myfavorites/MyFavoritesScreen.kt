package org.composempfirstapp.project.profile.presentation.myfavorites

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.composempfirstapp.project.core.navigation.CourtRouteScreen
import org.composempfirstapp.project.core.theme.mediumPadding
import org.composempfirstapp.project.court.domain.Court
import cfinder.composeapp.generated.resources.Res
import cfinder.composeapp.generated.resources.logo
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyFavoritesScreen(
    rootNavController: NavHostController,
    favoritesViewModel: MyFavoritesViewModel = viewModel()
) {
    val favoriteCourts by favoritesViewModel.favoriteCourts.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    // Snackbar state
    val snackbarHostState = remember { SnackbarHostState() }

    // State for delete confirmation dialog
    var showDeleteDialog by remember { mutableStateOf(false) }
    var courtToDelete by remember { mutableStateOf<Court?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Favorites") },
                navigationIcon = {
                    IconButton(onClick = { rootNavController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 16.dp, start = mediumPadding, end = mediumPadding, bottom = mediumPadding),
            contentAlignment = if (favoriteCourts.isEmpty()) Alignment.Center else Alignment.TopCenter
        ) {
            if (favoriteCourts.isEmpty()) {
                // Show empty state
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "No favorite courts yet",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Mark courts as favorites to see them here",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                // Show list of favorite courts
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(favoriteCourts) { court ->
                        FavoriteCourtItem(
                            court = court,
                            onRemove = {
                                courtToDelete = court
                                showDeleteDialog = true
                            },
                            onClick = {
                                // Navigate to court detail screen like we do from home
                                val encodedCourt = Json.encodeToString(court)
                                rootNavController.currentBackStackEntry?.savedStateHandle?.apply {
                                    set("court", encodedCourt)
                                }
                                rootNavController.navigate(CourtRouteScreen.CourtDetail.route)
                            }
                        )
                    }
                }
            }
        }
    }

    // Delete confirmation dialog
    if (showDeleteDialog && courtToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                courtToDelete = null
            },
            title = { Text("Remove from MyFavorites") },
            text = { Text("Are you sure you want to delete the ${courtToDelete?.name} from your favorites?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        courtToDelete?.let { court ->
                            favoritesViewModel.removeFavorite(court)
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(" ${court.name} was deleted")
                            }
                        }
                        showDeleteDialog = false
                        courtToDelete = null
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        courtToDelete = null
                    }
                ) {
                    Text("No")
                }
            }
        )
    }
}

@Composable
fun FavoriteCourtItem(
    court: Court,
    onRemove: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Court image
            AsyncImage(
                modifier = Modifier
                    .size(80.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                model = court.imageUrl,
                error = painterResource(Res.drawable.logo),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Court details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = court.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                court.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2
                    )
                }

                Text(
                    text = "${court.hourlyRate}/hour",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Remove button
            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove from favorites",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
package org.composempfirstapp.project.court.presentation

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cfinder.composeapp.generated.resources.Res
import cfinder.composeapp.generated.resources.logo
import coil3.compose.AsyncImage
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.composempfirstapp.project.CourtLocationMap
import org.composempfirstapp.project.core.navigation.ReservationRouteScreen
import org.composempfirstapp.project.court.domain.Court
import org.composempfirstapp.project.openInExternalMaps
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourtDetailScreen(
    navController: NavController,
    court: Court,
    viewModel: CourtViewModel
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = court.name,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )

        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Court Image
            item {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Gray),
                    model = court.imageUrl,
                    error = painterResource(Res.drawable.logo),
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
            }



            // Court Description
            court.description?.let {
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

            // Court Price
            item {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = MaterialTheme.typography.titleMedium.toSpanStyle().copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )) {
                            append("Price: ")
                        }
                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) {
                            append("${court.hourlyRate} $/ hour")
                        }
                    },
                    modifier = Modifier.padding(bottom = 0.dp) // Minimal bottom padding
                )
            }


            // Location Section
            item {
                Column(modifier = Modifier.padding(top = 0.dp)) { // Reduced top padding
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
                                openInExternalMaps(context, court.latitude, court.longitude, court.name)
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
                        latitude = court.latitude,
                        longitude = court.longitude,
                        courtName = court.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    // Add padding between map and reserve button
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }


            // Reserve Button
            item {
                Button(
                    onClick = {
                        val encodedCourt = Json.encodeToString(court)
                        navController.currentBackStackEntry?.savedStateHandle?.apply {
                            set("court", encodedCourt)
                        }
                        navController.navigate(ReservationRouteScreen.CourtReservation.route)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Reserve This Court")
                }
            }
        }
    }
}
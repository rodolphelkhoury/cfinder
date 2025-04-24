package org.composempfirstapp.project.court.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
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
import cfinder.composeapp.generated.resources.settings
import coil3.compose.AsyncImage
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.composempfirstapp.project.core.navigation.ReservationRouteScreen
import org.composempfirstapp.project.court.domain.Court
import org.composempfirstapp.project.shareLink
import org.composempfirstapp.project.core.theme.detailImageSize
import org.composempfirstapp.project.core.theme.imageSize
import org.composempfirstapp.project.core.theme.xLargePadding
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourtDetailScreen(
    navController: NavController,
    court: Court,
    viewModel: CourtViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(court.name) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Court Image
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

            Spacer(modifier = Modifier.height(16.dp))

            // Court Name
            Text(
                text = court.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Court Rate
            Text(
                text = "Rate: ${court.hourlyRate} per hour",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Court Description
            court.description?.let {
                Text(
                    text = "About this court",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Add other court details as needed

            Spacer(modifier = Modifier.weight(1f))

            // Reserve Button
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












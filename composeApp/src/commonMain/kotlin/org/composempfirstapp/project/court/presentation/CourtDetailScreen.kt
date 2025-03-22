package org.composempfirstapp.project.court.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import cfinder.composeapp.generated.resources.Res
import cfinder.composeapp.generated.resources.court_details
import cfinder.composeapp.generated.resources.ic_bookmark_outlined
import cfinder.composeapp.generated.resources.ic_browse
import cfinder.composeapp.generated.resources.logo
import cfinder.composeapp.generated.resources.settings
import coil3.compose.AsyncImage
import org.composempfirstapp.project.court.domain.Court
import org.composempfirstapp.project.theme.detailImageSize
import org.composempfirstapp.project.theme.imageSize
import org.composempfirstapp.project.theme.xLargePadding
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourtDetailScreen(
    navController: NavController,
    currentCourt: Court
) {
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
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = null
                        )
                    }
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_browse),
                            contentDescription = null
                        )
                    }
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_bookmark_outlined),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(xLargePadding),
            verticalArrangement = Arrangement.spacedBy(xLargePadding)
        ) {
            item {
                AsyncImage(
                    modifier = Modifier.height(detailImageSize).clip(MaterialTheme.shapes.large).background(Color.Gray).fillMaxWidth(),
                    model = currentCourt.imageUrl,
                    error = painterResource(Res.drawable.logo),
                    contentScale = ContentScale.Crop,
                    contentDescription =  null
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
                        text = currentCourt.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            item {
                Text(
                    text = currentCourt.hourlyRate.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}














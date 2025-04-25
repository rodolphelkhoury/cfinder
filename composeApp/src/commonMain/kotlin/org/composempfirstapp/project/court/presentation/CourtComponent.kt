package org.composempfirstapp.project.court.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cfinder.composeapp.generated.resources.Res
import cfinder.composeapp.generated.resources.logo
import coil3.compose.AsyncImage
import org.composempfirstapp.project.court.domain.Court
import org.composempfirstapp.project.core.theme.imageSize
import org.jetbrains.compose.resources.painterResource

@Composable
fun CourtComponent(
    modifier: Modifier = Modifier,
    court: Court,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 0.dp, horizontal = 12.dp)
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 4.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp), // Increased inner box size
            horizontalArrangement = Arrangement.spacedBy(15.dp) // More space between image and text
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(imageSize)
                    .clip(MaterialTheme.shapes.large)
                    .background(Color.Gray),
                model = court.imageUrl,
                error = painterResource(Res.drawable.logo),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = court.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2
                )

                court.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2
                    )
                }

                Text(
                    text = "${court.hourlyRate}$/ hour",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}











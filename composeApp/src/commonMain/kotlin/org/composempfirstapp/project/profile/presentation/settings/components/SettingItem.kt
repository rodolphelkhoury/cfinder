package org.composempfirstapp.project.profile.presentation.settings.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.composempfirstapp.project.core.theme.mediumPadding
import org.composempfirstapp.project.core.theme.smallPadding
import org.composempfirstapp.project.core.theme.xLargePadding


@Composable
fun SettingItem(
    onClick: () -> Unit,
    painter: Painter? = null,
    imageVector: ImageVector? = null,
    itemName: String,
    itemColor: Color = MaterialTheme.colorScheme.primary, // Set default color to primary
    trailingContent: @Composable (() -> Unit)? = null
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 1.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(mediumPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Display icon if present (imageVector or painter)
                if (imageVector != null) {
                    Icon(
                        imageVector = imageVector,
                        contentDescription = null,
                        tint = itemColor // Use primary color for the icon
                    )
                } else if (painter != null) {
                    Icon(
                        painter = painter,
                        contentDescription = null,
                        tint = itemColor // Use primary color for the icon
                    )
                }

                Spacer(modifier = Modifier.width(smallPadding))

                Text(
                    text = itemName,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), // Make text bold
                    color = itemColor // Use primary color for the text
                )
            }
            if (trailingContent != null) {
                trailingContent()
            }
        }
    }
}

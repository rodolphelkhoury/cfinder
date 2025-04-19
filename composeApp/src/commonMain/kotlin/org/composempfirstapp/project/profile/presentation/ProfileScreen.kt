package org.composempfirstapp.project.profile.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.composempfirstapp.project.core.navigation.ProfileRouteScreen
import org.composempfirstapp.project.core.theme.mediumPadding
import org.composempfirstapp.project.core.theme.smallPadding
import org.composempfirstapp.project.profile.presentation.myprofile.UserProfileState


@Composable
fun ProfileScreen(
    rootNavController: NavHostController
) {
    // Collect the current profile data
    val fullName by UserProfileState.fullName.collectAsState()
    val phoneNumber by UserProfileState.phoneNumber.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 24.dp, // Reduced from 40.dp to 24.dp
                start = mediumPadding,
                end = mediumPadding,
                bottom = mediumPadding
            )
            .verticalScroll(rememberScrollState())
    ) {
        // Profile header with full name
        ProfileHeader(
            name = fullName,
            phoneNumber = phoneNumber
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Navigation items
        ProfileNavigationItem(
            title = "My Profile",
            onClick = { rootNavController.navigate(ProfileRouteScreen.MyProfile.route) }
        )

        Spacer(modifier = Modifier.height(12.dp))


        ProfileNavigationItem(
            title = "My Courts",
            onClick = { rootNavController.navigate(ProfileRouteScreen.MyCourts.route) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        ProfileNavigationItem(
            title = "Settings",
            onClick = { rootNavController.navigate(ProfileRouteScreen.Settings.route) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        ProfileNavigationItem(
            title = "About us",
            onClick = { rootNavController.navigate(ProfileRouteScreen.AboutUs.route) }
        )

    }
}

@Composable
fun ProfileHeader(
    name: String,
    phoneNumber: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp), // Reduced from smallPadding to 8.dp
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Profile",
            modifier = Modifier.size(60.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(12.dp)) // Reduced from mediumPadding to 12.dp

        Column {
            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = phoneNumber,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ProfileNavigationItem(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
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
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Navigate to $title",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

package org.composempfirstapp.project.profile.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import org.composempfirstapp.project.core.theme.mediumPadding
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.composempfirstapp.project.core.Resource
import org.composempfirstapp.project.core.navigation.ProfileRouteScreen
import org.composempfirstapp.project.profile.domain.Profile

@Composable
fun ProfileScreen(
    rootNavController: NavHostController,
    // Get viewModel as parameter instead of reading from global state
    profileViewModel: ProfileViewModel
) {
    // Use state flow from viewModel
    val profileState by profileViewModel.profile.collectAsState()

    // Extract profile data when available
    val profile = (profileState as? Resource.Success)?.data
    val fullName = profile?.name ?: ""
    val phoneNumber = profile?.phoneNumber ?: ""

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 24.dp,
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

        // Navigation items - pass profile data to destinations
        ProfileNavigationItem(
            title = "My Profile",
            onClick = {
                // Pass profile data as navigation arguments
                val profileJson = Json.encodeToString(profile)
                rootNavController.currentBackStackEntry?.savedStateHandle?.set("profile", profileJson)
                rootNavController.navigate(ProfileRouteScreen.MyProfile.route)
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        ProfileNavigationItem(
            title = "My Favorites",
            onClick = { rootNavController.navigate(ProfileRouteScreen.MyFavorites.route) }
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
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Profile",
            modifier = Modifier.size(60.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(12.dp))

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
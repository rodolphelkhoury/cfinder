package org.composempfirstapp.project.profile.presentation.myprofilescreen


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.composempfirstapp.project.core.theme.mediumPadding
import org.composempfirstapp.project.core.theme.smallPadding
import org.composempfirstapp.project.profile.domain.Profile
import org.composempfirstapp.project.profile.presentation.ProfileHeader
import org.composempfirstapp.project.profile.presentation.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfileScreen(rootNavController: NavHostController) {
    // Get profile from navigation arguments
    val savedStateHandle = rootNavController.currentBackStackEntry?.savedStateHandle
    val profileJson = savedStateHandle?.get<String>("profile")
    val profile = remember {
        profileJson?.let {
            Json.decodeFromString<Profile>(it)
        }
    }

    // Create viewModel with proper factory if needed
    val viewModel: ProfileViewModel = viewModel()

    var fullName by remember { mutableStateOf(profile?.name ?: "") }
    val phoneNumber = profile?.phoneNumber ?: ""

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Profile") },
                navigationIcon = {
                    IconButton(onClick = { rootNavController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 24.dp, start = mediumPadding, end = mediumPadding, bottom = mediumPadding)
        ) {
            ProfileHeader(
                name = fullName,
                phoneNumber = phoneNumber
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    autoCorrect = false
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )

            Spacer(modifier = Modifier.height(smallPadding))

            // Better handling of phone number field by properly disabling it
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { /* No-op - field is read-only */ },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = false, // Properly disable the field instead of resetting it
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    autoCorrect = false
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )

            Spacer(modifier = Modifier.height(mediumPadding))

            Button(
                onClick = {
                    // Call the viewModel method to update the profile
                    viewModel.updateProfile(fullName)

                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Profile updated successfully",
                            duration = SnackbarDuration.Short
                        )
                        // Delay and navigate back
                        delay(1500)
                        rootNavController.popBackStack()
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Save Changes")
            }
        }
    }
}
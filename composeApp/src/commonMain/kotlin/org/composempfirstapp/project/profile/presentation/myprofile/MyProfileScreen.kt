package org.composempfirstapp.project.profile.presentation.myprofile

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
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.composempfirstapp.project.core.theme.mediumPadding
import org.composempfirstapp.project.core.theme.smallPadding
import org.composempfirstapp.project.profile.presentation.ProfileHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfileScreen(rootNavController: NavHostController) {
    val currentFullName by UserProfileState.fullName.collectAsState()
    val currentPhoneNumber by UserProfileState.phoneNumber.collectAsState()

    var fullName by remember { mutableStateOf(currentFullName) }
    var phone by remember { mutableStateOf(currentPhoneNumber) }
    var originalPhone by remember { mutableStateOf(currentPhoneNumber) }

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
                name = currentFullName,
                phoneNumber = currentPhoneNumber
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

            OutlinedTextField(
                value = phone,
                onValueChange = {
                    // Show toast message when trying to edit phone number
                    if (phone != it) {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "You cannot edit the phone number",
                                duration = SnackbarDuration.Long
                            )
                        }
                    }
                    // Keep the original phone number
                    phone = originalPhone
                },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    autoCorrect = false
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )

            Spacer(modifier = Modifier.height(mediumPadding))

            Button(
                onClick = {
                    // Only update the full name, not the phone number
                    UserProfileState.updateProfile(fullName, originalPhone)
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Profile updated successfully",
                            duration = SnackbarDuration.Long // Increased duration
                        )
                    }
                    // Don't pop back stack immediately to allow the user to see the message
                    scope.launch {
                        kotlinx.coroutines.delay(1500) // Wait 2 seconds before navigating back
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

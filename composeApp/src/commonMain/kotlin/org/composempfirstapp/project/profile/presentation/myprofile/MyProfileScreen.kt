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
import org.composempfirstapp.project.profile.presentation.ProfileHeader
import org.composempfirstapp.project.theme.mediumPadding
import org.composempfirstapp.project.theme.smallPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfileScreen(
    rootNavController: NavHostController
) {
    // Collect current profile data
    val currentFirstName by UserProfileState.firstName.collectAsState()
    val currentLastName by UserProfileState.lastName.collectAsState()
    val currentPhoneNumber by UserProfileState.phoneNumber.collectAsState()

    // Local state for editing
    var firstName by remember { mutableStateOf(currentFirstName) }
    var lastName by remember { mutableStateOf(currentLastName) }
    var phone by remember { mutableStateOf(currentPhoneNumber) }

    // Show a snackbar when changes are saved
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Profile") },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 24.dp, start = mediumPadding, end = mediumPadding, bottom = mediumPadding)
        ) {
            // Display the profile header from shared state
            ProfileHeader(
                name = "$currentFirstName $currentLastName",
                phoneNumber = currentPhoneNumber
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Editable field: First Name (suggestions disabled)
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") },
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

            // Editable field: Last Name (suggestions disabled)
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
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

            // Editable field: Phone Number (suggestions disabled)
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
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

            // Save button that updates the shared profile state and shows a snackbar
            Button(
                onClick = {
                    UserProfileState.updateProfile(firstName, lastName, phone)
                    scope.launch {
                        snackbarHostState.showSnackbar("Profile updated successfully")
                    }
                    rootNavController.popBackStack()
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Save Changes")
            }
        }
    }
}

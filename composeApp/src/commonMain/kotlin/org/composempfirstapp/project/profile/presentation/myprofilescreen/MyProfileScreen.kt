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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.composempfirstapp.project.core.Resource
import org.composempfirstapp.project.core.theme.mediumPadding
import org.composempfirstapp.project.core.theme.smallPadding
import org.composempfirstapp.project.profile.domain.Profile
import org.composempfirstapp.project.profile.presentation.ProfileHeader
import org.composempfirstapp.project.profile.presentation.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfileScreen(
    rootNavController: NavHostController,
    profileViewModel: ProfileViewModel
) {
    // Observe the profile state from the ViewModel
    val profileState by profileViewModel.profile.collectAsState()
    val fullNameState by profileViewModel.fullName.collectAsState()
    val phoneNumberState by profileViewModel.phoneNumber.collectAsState()
    val updateProfileState by profileViewModel.updateProfileState.collectAsState()

    var fullName by remember { mutableStateOf(fullNameState) }
    var isFullNameError by remember { mutableStateOf(false) }
    var fullNameErrorText by remember { mutableStateOf("") }

    LaunchedEffect(fullNameState) {
        fullName = fullNameState
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Show snackbar when update is successful
    LaunchedEffect(updateProfileState) {
        if (updateProfileState is Resource.Success) {
            snackbarHostState.showSnackbar(
                message = "Profile updated successfully",
                duration = SnackbarDuration.Short
            )
        } else if (updateProfileState is Resource.Error) {
            snackbarHostState.showSnackbar(
                message = "Update failed: ${(updateProfileState as Resource.Error).message}",
                duration = SnackbarDuration.Short
            )
        }
    }

    // Validate full name input
    fun validateFullName(input: String): Boolean {
        return when {
            input.isBlank() -> {
                fullNameErrorText = "Full name cannot be empty"
                false
            }
            input.trim().isEmpty() -> {
                fullNameErrorText = "Full name must contain at least one non-space character"
                false
            }
            input.length > 50 -> {
                fullNameErrorText = "Full name cannot exceed 50 characters"
                false
            }
            else -> {
                fullNameErrorText = ""
                true
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Profile", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { rootNavController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        when (profileState) {
            is Resource.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(top = 24.dp, start = mediumPadding, end = mediumPadding, bottom = mediumPadding)
                ) {
                    ProfileHeader(
                        name = fullName,
                        phoneNumber = phoneNumberState
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    OutlinedTextField(
                        value = fullName,
                        onValueChange = {
                            if (it.length <= 50) {
                                fullName = it
                                isFullNameError = false
                                fullNameErrorText = ""
                            } else {
                                isFullNameError = true
                                fullNameErrorText = "Full name cannot exceed 50 characters"
                            }
                        },
                        label = { Text("Full Name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = isFullNameError,
                        supportingText = {
                            if (isFullNameError) {
                                Text(text = fullNameErrorText)
                            } else {
                                Text("${fullName.length}/50")
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            autoCorrect = false
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            errorContainerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    )

                    Spacer(modifier = Modifier.height(smallPadding))

                    OutlinedTextField(
                        value = phoneNumberState,
                        onValueChange = { /* No-op - field is read-only */ },
                        label = { Text("Phone Number") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = false, // Properly disable the field
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

                    // Show loading indicator in the button when updating
                    Button(
                        onClick = {
                            val isValid = validateFullName(fullName)
                            isFullNameError = !isValid

                            if (isValid) {
                                // Call the viewModel method to update the profile
                                profileViewModel.updateProfile(fullName.trim())
                            }
                        },
                        modifier = Modifier.align(Alignment.End),
                        enabled = updateProfileState !is Resource.Loading && !isFullNameError
                    ) {
                        if (updateProfileState is Resource.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text("Save Changes")
                    }
                }
            }
            is Resource.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is Resource.Error -> {
                val errorMessage = (profileState as Resource.Error).message
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Error: $errorMessage")
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { profileViewModel.getProfile() }) {
                            Text("Retry")
                        }
                    }
                }
            }
            else -> { /* Handle Idle state if needed */ }
        }
    }
}
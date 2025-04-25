package org.composempfirstapp.project.core.authentication.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.composempfirstapp.project.core.authentication.data.UiState
import org.composempfirstapp.project.core.authentication.domain.countryCodes
import org.composempfirstapp.project.core.navigation.AuthRoutes

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit
) {
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isPhoneNumberError by remember { mutableStateOf(false) }
    var isPasswordError by remember { mutableStateOf(false) }
    var passwordErrorMessage by remember { mutableStateOf("") }
    var showCountryDropdown by remember { mutableStateOf(false) }
    var selectedCountry by remember { mutableStateOf(countryCodes[0]) }

    val phoneNumberFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val loginState by authViewModel.loginState.collectAsState()

    // Password validation
    val isPasswordValid by remember(password) {
        derivedStateOf {
            password.length >= 8
        }
    }

    LaunchedEffect(loginState) {
        if (loginState is UiState.Success) {
            onLoginSuccess()
        }
    }

    // Reset errors when inputs change
    LaunchedEffect(phoneNumber) { isPhoneNumberError = false }
    LaunchedEffect(password) {
        isPasswordError = false
        if (password.isNotEmpty() && password.length < 8) {
            passwordErrorMessage = "Password must be at least 8 characters"
            isPasswordError = true
        }
    }

    Scaffold { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Welcome Back",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Sign in to continue",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(32.dp))

                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp) // Reduced from 16.dp to 12.dp
                    ) {
                        // Phone number with country code
                        Column {
                            Text(
                                text = "Phone Number",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Country code dropdown
                                Box {
                                    Row(
                                        modifier = Modifier
                                            .border(
                                                width = 1.dp,
                                                color = MaterialTheme.colorScheme.outline,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .clickable { showCountryDropdown = true }
                                            .padding(horizontal = 12.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${selectedCountry.flag} ${selectedCountry.code}",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Icon(
                                            imageVector = Icons.Default.ArrowDropDown,
                                            contentDescription = "Select country"
                                        )
                                    }

                                    DropdownMenu(
                                        expanded = showCountryDropdown,
                                        onDismissRequest = { showCountryDropdown = false }
                                    ) {
                                        countryCodes.forEach { country ->
                                            DropdownMenuItem(
                                                text = {
                                                    Text("${country.flag} ${country.name} (${country.code})")
                                                },
                                                onClick = {
                                                    selectedCountry = country
                                                    showCountryDropdown = false
                                                    phoneNumberFocusRequester.requestFocus()
                                                }
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                OutlinedTextField(
                                    value = phoneNumber,
                                    onValueChange = { phoneNumber = it },
                                    placeholder = { Text("Phone number") },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Phone,
                                        imeAction = ImeAction.Next
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                                    ),
                                    singleLine = true,
                                    isError = isPhoneNumberError,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .focusRequester(phoneNumberFocusRequester)
                                )
                            }

                            if (isPhoneNumberError) {
                                Text(
                                    text = "Please enter a valid phone number",
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            }
                        }

                        // Password field
                        Column {
                            Text(
                                text = "Password",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            OutlinedTextField(
                                value = password,
                                onValueChange = { password = it },
                                placeholder = { Text("Enter your password") },
                                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Password,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        keyboardController?.hide()
                                        if (phoneNumber.isNotBlank() && isPasswordValid) {
                                            authViewModel.login("${selectedCountry.code}$phoneNumber", password)
                                        } else {
                                            if (phoneNumber.isBlank()) isPhoneNumberError = true
                                            if (!isPasswordValid) {
                                                isPasswordError = true
                                                passwordErrorMessage = "Password must be at least 8 characters"
                                            }
                                        }
                                    }
                                ),
                                singleLine = true,
                                isError = isPasswordError,
                                trailingIcon = {
                                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                        Icon(
                                            imageVector = if (passwordVisible) Icons.Default.Lock else Icons.Default.Lock,
                                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp) // Made password input smaller (reduced from default)
                                    .focusRequester(passwordFocusRequester)
                            )

                            if (isPasswordError) {
                                Text(
                                    text = passwordErrorMessage,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            }
                        }

                        // Password requirements indicator when typing
                        AnimatedVisibility(visible = password.isNotEmpty()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 2.dp) // Reduced from 4.dp
                            ) {
                                Icon(
                                    imageVector = if (isPasswordValid) Icons.Default.Check else Icons.Default.Close,
                                    contentDescription = "Password requirement",
                                    tint = if (isPasswordValid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(16.dp)
                                )

                                Spacer(modifier = Modifier.width(4.dp))

                                Text(
                                    text = "At least 8 characters",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (isPasswordValid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        if (loginState is UiState.Error) {
                            Text(
                                text = (loginState as UiState.Error).message,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 2.dp) // Reduced from 4.dp
                            )
                        }

                        // Reduced spacing by removing this spacer
                        // Spacer(modifier = Modifier.height(1.dp))

                        TextButton(
                            onClick = { /* Handle forgot password */ },
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(top = 0.dp, bottom = 0.dp) // Reduced padding
                        ) {
                            Text("Forgot Password?")
                        }

                        Spacer(modifier = Modifier.height(0.dp))

                        Button(
                            onClick = {
                                if (phoneNumber.isBlank()) {
                                    isPhoneNumberError = true
                                }
                                if (password.isBlank()) {
                                    isPasswordError = true
                                    passwordErrorMessage = "Password is required"
                                } else if (!isPasswordValid) {
                                    isPasswordError = true
                                    passwordErrorMessage = "Password must be at least 8 characters"
                                }

                                if (phoneNumber.isNotBlank() && isPasswordValid) {
                                    authViewModel.login("${selectedCountry.code}$phoneNumber", password)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(45.dp),
                            enabled = loginState !is UiState.Loading,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            if (loginState is UiState.Loading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(22.dp),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text("Sign In", fontSize = MaterialTheme.typography.bodyLarge.fontSize)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Don't have an account?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    TextButton(
                        onClick = {
                            authViewModel.resetStates()
                            navController.navigate(AuthRoutes.REGISTER)
                        }
                    ) {
                        Text("Register")
                    }
                }
            }
        }
    }
}
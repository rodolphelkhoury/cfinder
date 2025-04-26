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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.composempfirstapp.project.core.authentication.data.UiState

@Composable
fun VerifyOtpScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    onVerificationSuccess: () -> Unit,
) {
    var otpCode by remember { mutableStateOf("") }
    var isOtpError by remember { mutableStateOf(false) }
    val maxOtpLength = 6

    var remainingTime by remember { mutableStateOf(60) }
    var canResend by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    val verifyOtpState by authViewModel.verifyOtpState.collectAsState()

    // Add clipboard functionality
    var showPasteCodeButton by remember { mutableStateOf(true) }
    val clipboardManager = LocalClipboardManager.current

    LaunchedEffect(verifyOtpState) {
        if (verifyOtpState is UiState.Success && (verifyOtpState as UiState.Success<Boolean>).data) {
            onVerificationSuccess()
        }
    }

    // Reset error when input changes
    LaunchedEffect(otpCode) { isOtpError = false }

    // Timer for resending code
    LaunchedEffect(Unit) {
        while (remainingTime > 0) {
            delay(1000)
            remainingTime--
        }
        canResend = true
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
                    text = "Verification Code",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Enter the 6-digit code sent to your phone",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
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
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // OTP Input field with styled container
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            OutlinedTextField(
                                value = otpCode,
                                onValueChange = {
                                    if (it.length <= maxOtpLength && it.all { char -> char.isDigit() }) {
                                        otpCode = it
                                    }
                                },
                                label = { Text("Enter Code") },
                                placeholder = { Text("6-digit code") },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.NumberPassword,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        keyboardController?.hide()
                                        if (otpCode.length == maxOtpLength) {
                                            authViewModel.verifyOtp(otpCode)
                                        } else {
                                            isOtpError = true
                                        }
                                    }
                                ),
                                singleLine = true,
                                isError = isOtpError,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            )

                            if (isOtpError) {
                                Text(
                                    text = "Please enter a valid 6-digit code",
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }

                            // OTP code visualization
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                for (i in 0 until maxOtpLength) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .border(
                                                width = 1.dp,
                                                color = if (i < otpCode.length) MaterialTheme.colorScheme.primary
                                                else MaterialTheme.colorScheme.outline,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .background(
                                                color = if (i < otpCode.length) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                                else Color.Transparent,
                                                shape = RoundedCornerShape(8.dp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (i < otpCode.length) {
                                            Text(
                                                text = otpCode[i].toString(),
                                                style = MaterialTheme.typography.titleLarge,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Paste code button - accesses clipboard to paste code
                        AnimatedVisibility(visible = showPasteCodeButton) {
                            Button(
                                onClick = {
                                    // Get text from clipboard
                                    val clipboardText = clipboardManager.getText()?.text ?: ""
                                    // Extract only digits and limit to maxOtpLength
                                    val numbers = clipboardText.filter { it.isDigit() }
                                    if (numbers.isNotEmpty()) {
                                        otpCode = numbers.take(maxOtpLength)
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                                ),
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .padding(vertical = 8.dp)
                            ) {
                                Text("Paste Copied Code")
                            }
                        }

                        if (verifyOtpState is UiState.Error) {
                            Text(
                                text = (verifyOtpState as UiState.Error).message,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (!canResend) {
                                Text(
                                    text = "Resend code in ${remainingTime}s",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            } else {
                                TextButton(
                                    onClick = {
                                        // Implement logic to resend OTP
                                        remainingTime = 60
                                        canResend = false
                                        scope.launch {
                                            while (remainingTime > 0) {
                                                delay(1000)
                                                remainingTime--
                                            }
                                            canResend = true
                                        }
                                    }
                                ) {
                                    Text("Resend Code")
                                }
                            }
                        }

                        Button(
                            onClick = {
                                if (otpCode.length != maxOtpLength) {
                                    isOtpError = true
                                } else {
                                    authViewModel.verifyOtp(otpCode)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            enabled = otpCode.length == maxOtpLength && verifyOtpState !is UiState.Loading,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            if (verifyOtpState is UiState.Loading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text("Verify", fontSize = MaterialTheme.typography.bodyLarge.fontSize)
                            }
                        }
                    }
                }
            }
        }
    }
}
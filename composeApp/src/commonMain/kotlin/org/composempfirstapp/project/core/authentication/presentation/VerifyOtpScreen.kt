package org.composempfirstapp.project.core.authentication.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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

    val verifyOtpState by authViewModel.verifyOtpState.collectAsState()

    LaunchedEffect(verifyOtpState) {
        if (verifyOtpState is UiState.Success && (verifyOtpState as UiState.Success<Boolean>).data) {
            onVerificationSuccess()
        }
    }

    // Reset error when input changes
    LaunchedEffect(otpCode) { isOtpError = false }

    // Timer for resending code
    val timerStarted = remember { mutableStateOf(false) }
    if (!timerStarted.value) {
        timerStarted.value = true
        scope.launch {
            while (remainingTime > 0) {
                delay(1000)
                remainingTime--
            }
            canResend = true
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Verify Phone Number",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "We sent a verification code",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = otpCode,
                onValueChange = {
                    if (it.length <= maxOtpLength && it.all { char -> char.isDigit() }) {
                        otpCode = it
                    }
                },
                label = { Text("OTP Code") },
                placeholder = { Text("Enter 6-digit code") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword
                ),
                singleLine = true,
                isError = isOtpError,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (verifyOtpState is UiState.Error) {
                Text(
                    text = (verifyOtpState as UiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (!canResend) {
                    Text(
                        text = "Resend code in ${remainingTime}s",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Text(
                        text = "Resend Code",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable {
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
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

            }

            Spacer(modifier = Modifier.height(32.dp))

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
                enabled = otpCode.length == maxOtpLength && verifyOtpState !is UiState.Loading
            ) {
                if (verifyOtpState is UiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Verify")
                }
            }
        }
    }
}
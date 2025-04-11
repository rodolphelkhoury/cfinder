package org.composempfirstapp.project.authentication.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun VerifyPhoneNumberScreen(
    phoneNumber: String,
    onVerificationSuccess: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var otpValue by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var remainingTime by remember { mutableStateOf(60) }
    var canResend by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

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
                text = "We sent a verification code to $phoneNumber",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = otpValue,
                onValueChange = {
                    if (it.length <= 6 && it.all { char -> char.isDigit() }) {
                        otpValue = it
                    }
                },
                label = { Text("OTP Code") },
                placeholder = { Text("Enter 6-digit code") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

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

                Text(
                    text = "Change Number",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onNavigateBack() },
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    isLoading = true
                    scope.launch {
                        delay(1500)
                        isLoading = false
                        if (otpValue.length == 6) {
                            onVerificationSuccess()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = otpValue.length == 6 && !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Verify")
                }
            }
        }
    }
}

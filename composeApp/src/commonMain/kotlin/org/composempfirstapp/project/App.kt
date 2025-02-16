package org.composempfirstapp.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import cfinder.composeapp.generated.resources.Res
import cfinder.composeapp.generated.resources.compose_multiplatform
import org.composempfirstapp.project.navigation.presentation.MainScreen

@Composable
@Preview
fun App() {
    MainScreen()
}
package org.composempfirstapp.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import org.composempfirstapp.project.court.presentation.HomeScreen
import org.composempfirstapp.project.theme.CFinderTheme
import org.composempfirstapp.project.utils.Theme

@Composable
@Preview
fun App() {
    CFinderTheme(
        appTheme = Theme.DARK_MODE.name,
        darkTheme = true
    ) {
        HomeScreen()
    }
}
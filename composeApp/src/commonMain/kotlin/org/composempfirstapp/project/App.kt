package org.composempfirstapp.project

import androidx.compose.runtime.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.composempfirstapp.project.navigation.graphs.RootNavGraph
import org.composempfirstapp.project.theme.CFinderTheme
import org.composempfirstapp.project.utils.Theme

@Composable
@Preview
fun App() {
    CFinderTheme(
        appTheme = Theme.DARK_MODE.name,
        darkTheme = true
    ) {
        RootNavGraph()
    }
}
package org.composempfirstapp.project.court.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.composempfirstapp.project.court.domain.Court
import org.composempfirstapp.project.core.navigation.CourtRouteScreen
import org.composempfirstapp.project.core.theme.xLargePadding

@Composable
fun CourtListScreen(
    modifier: Modifier = Modifier,
    courtList: List<Court>,
    navController: NavController
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        verticalArrangement = Arrangement.spacedBy(xLargePadding),
        horizontalArrangement = Arrangement.spacedBy(xLargePadding),
        contentPadding = PaddingValues(
            top = xLargePadding,
            start = xLargePadding,
            end = xLargePadding,
            bottom = xLargePadding
        ),
        // Add this modifier to ensure content starts from the top
        modifier = modifier.fillMaxWidth()
    ) {
        items(
            courtList,
            key = {
                it.id
            }
        ) {
            CourtComponent(
                court = it,
                onClick = {
                    val court = Json.encodeToString(it)
                    navController.currentBackStackEntry?.savedStateHandle?.apply {
                        set("court", court)
                    }
                    navController.navigate(CourtRouteScreen.CourtDetail.route)
                }
            )
        }
    }
}
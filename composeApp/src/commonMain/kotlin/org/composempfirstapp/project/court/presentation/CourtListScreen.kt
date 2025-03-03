package org.composempfirstapp.project.court.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.composempfirstapp.project.court.domain.Court
import org.composempfirstapp.project.theme.xLargePadding

@Composable
fun CourtListScreen(
    modifier: Modifier = Modifier,
    courtList: List<Court>
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        verticalArrangement = Arrangement.spacedBy(xLargePadding),
        horizontalArrangement = Arrangement.spacedBy(xLargePadding),
        contentPadding = PaddingValues(xLargePadding),

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

                }
            )
        }
    }
}
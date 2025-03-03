package org.composempfirstapp.project.court.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.composempfirstapp.project.utils.courts

@Composable
fun BookingHomeScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier,
    ) {
        CourtListScreen(
            courtList = courts
        )
    }
}
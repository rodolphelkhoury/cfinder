package org.composempfirstapp.project.offers.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.composempfirstapp.project.court.presentation.CourtListScreen
import org.composempfirstapp.project.theme.mediumPadding
import org.composempfirstapp.project.utils.components.SearchBar
import org.composempfirstapp.project.utils.courts

@Composable
fun OffersScreen(modifier: Modifier = Modifier) {
    var searchQuery by rememberSaveable() {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier,
        verticalArrangement =  Arrangement.spacedBy(mediumPadding)
    ) {
        SearchBar(
            text = searchQuery,
            onValueChange = {
                searchQuery = it
            },
            onSearch = { query ->
                if (query.trim().isNotEmpty()) {
                    println(query)
                }

            }
        )
        CourtListScreen(courtList = courts)
    }
}
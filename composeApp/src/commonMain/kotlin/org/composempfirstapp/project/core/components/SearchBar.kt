package org.composempfirstapp.project.core.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import cfinder.composeapp.generated.resources.Res
import cfinder.composeapp.generated.resources.ic_search
import cfinder.composeapp.generated.resources.search
import org.composempfirstapp.project.core.theme.mediumPadding
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults


@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    text: String,
    onValueChange: (String) -> Unit,
    onSearch: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current

    TextField(
        value = text,
        onValueChange = {
            onValueChange(it)
        },
        modifier = Modifier.fillMaxWidth().padding(horizontal = mediumPadding),
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            fontWeight = FontWeight.SemiBold
        ),
        leadingIcon = {
            Icon(
                painter =  painterResource(Res.drawable.ic_search),
                contentDescription = null
            )
        },
        placeholder = {
            Text(
                text = stringResource(Res.string.search),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
        },
        shape = MaterialTheme.shapes.medium,
        colors = TextFieldDefaults.colors(
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                focusManager.clearFocus()
                onSearch(text)
            }
        )

    )
}









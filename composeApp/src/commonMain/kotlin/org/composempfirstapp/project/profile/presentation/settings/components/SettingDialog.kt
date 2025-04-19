package org.composempfirstapp.project.profile.presentation.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import cfinder.composeapp.generated.resources.Res
import cfinder.composeapp.generated.resources.apply
import cfinder.composeapp.generated.resources.cancel
import cfinder.composeapp.generated.resources.choose_a_theme
import cfinder.composeapp.generated.resources.logout
import cfinder.composeapp.generated.resources.logout_description
import org.composempfirstapp.project.core.theme.mediumPadding
import org.composempfirstapp.project.core.theme.xLargePadding
import org.composempfirstapp.project.core.theme.xSmallPadding
import org.composempfirstapp.project.core.Theme
import org.jetbrains.compose.resources.stringResource

@Composable
fun LogoutDialog(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(stringResource(Res.string.logout))
        },
        text = {
            Text(stringResource(Res.string.logout_description))
        },
        icon = {
            Icon(
                Icons.Outlined.Delete,
                contentDescription = stringResource(Res.string.logout),
                tint = MaterialTheme.colorScheme.error
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onLogout()
                }
            ) {
                Text(stringResource(Res.string.logout))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(stringResource(Res.string.cancel))
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSelectionDialog(
    modifier: Modifier = Modifier,
    // TODO pass the theme fard mara
    currentTheme: String,
    onThemeChange: (Theme) -> Unit,
    onDismissRequest: () -> Unit
) {
    var currentSelectedTheme by remember {
        mutableStateOf(Theme.valueOf(currentTheme))
    }

    BasicAlertDialog(
        modifier = Modifier.clip(RoundedCornerShape(28.dp)),
        onDismissRequest = {
            onDismissRequest()
        },
        content = {
            Surface(
                modifier = Modifier.wrapContentSize(),
                shape = androidx.compose.material.MaterialTheme.shapes.large,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(
                    modifier = Modifier.padding(mediumPadding)
                ) {
                    Text(
                        text = stringResource(Res.string.choose_a_theme),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(xSmallPadding )
                    )
                    Theme.entries.forEach { theme ->
                        Row(
                            modifier= Modifier.fillMaxWidth().clickable {
                                currentSelectedTheme = theme
                            },
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = currentSelectedTheme == theme,
                                onClick = {
                                    currentSelectedTheme = theme
                                },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = MaterialTheme.colorScheme.primary
                                )
                            )
                            Text(
                                text = stringResource(theme.title)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(xLargePadding))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = {
                                onDismissRequest()
                            }
                        ) {
                            Text(text = stringResource(Res.string.cancel))
                        }

                        Spacer(modifier = Modifier.width(mediumPadding))

                        TextButton(
                            onClick = {
                                onThemeChange(currentSelectedTheme)
                            }
                        ) {
                            Text(text = stringResource(Res.string.apply))

                        }
                    }
                }
            }
        }

    )
}


















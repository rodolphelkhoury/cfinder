package org.composempfirstapp.project.utils

import cfinder.composeapp.generated.resources.Res
import cfinder.composeapp.generated.resources.dark_mode
import cfinder.composeapp.generated.resources.light_mode
import cfinder.composeapp.generated.resources.system_default
import org.jetbrains.compose.resources.StringResource

enum class Theme(val title: StringResource) {
    SYSTEM_DEFAULT(Res.string.system_default),
    LIGHT_MODE(Res.string.light_mode),
    DARK_MODE(Res.string.dark_mode)
}
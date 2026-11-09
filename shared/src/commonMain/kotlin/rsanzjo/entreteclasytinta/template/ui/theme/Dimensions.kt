package rsanzjo.entreteclasytinta.template.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimensions(
    val smallPadding: Dp = 4.dp,
    val mediumPadding: Dp = 8.dp,
    val largePadding: Dp = 16.dp,
    val cardCornerRadius: Dp = 12.dp,
    val avatarSize: Dp = 48.dp,
    val bookCoverWidth: Dp = 90.dp,
    val bookCoverHeight: Dp = 150.dp,
    val spacing: Dp = 12.dp,
    val progressBookCardHeight: Dp = 170.dp,
    val bottomBarHeight: Dp = 64.dp,
)

val Dimens = staticCompositionLocalOf { Dimensions() }


package rsanzjo.entreteclasytinta.template.common.extensions

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import rsanzjo.entreteclasytinta.template.ui.theme.LocalDefaultColors
import kotlin.time.TimeMark
import kotlin.time.TimeSource

private const val SHIMMER_DURATION_MILLIS = 1000

@Composable
fun Modifier.debouncedClickable(
    debounceInterval: Long = 350,
    enabled: Boolean = true,
    indication: Indication? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit
): Modifier = composed {
    var lastClick by remember { mutableStateOf<TimeMark?>(null) }

    if (enabled) {
        this.clickable(
            interactionSource = interactionSource,
            indication = indication,
            onClick = {
                val elapsed = lastClick?.elapsedNow()?.inWholeMilliseconds ?: Long.MAX_VALUE
                if (elapsed >= debounceInterval) {
                    lastClick = TimeSource.Monotonic.markNow()
                    onClick()
                }
            }
        )
    } else {
        this
    }
}

fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember { androidx.compose.runtime.mutableStateOf(IntSize.Zero) }
    val shimmerColors = LocalDefaultColors.current
    val transition = rememberInfiniteTransition(label = "ShimmerTransition")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(SHIMMER_DURATION_MILLIS)
        ),
        label = "ShimmerOffset"
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                shimmerColors.shimmerBase,
                shimmerColors.shimmerHighlight,
                shimmerColors.shimmerBase,
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    )
        .onGloballyPositioned {
            size = it.size
        }
}

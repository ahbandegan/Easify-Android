package ir.amirhesambandegan.easify_ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.animation.core.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer

/**
 * A modifier that adds a "bounce" effect when clicked, similar to iOS buttons.
 *
 * @param minScale The scale factor to shrink to when pressed. Default is 0.95f.
 * @param onClick The callback to be invoked when the element is clicked.
 * @return A [Modifier] with the bounce click effect applied.
 */
fun Modifier.bounceClick(
    minScale: Float = 0.95f,
    onClick: () -> Unit
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) minScale else 1f,
        label = "bounceScale"
    )

    this
        .scale(scale)
        .clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick
        )
}

/**
 * A modifier that hides the software keyboard when the user taps outside a focused element.
 * 
 * @return A [Modifier] that intercepts tap gestures to clear focus and hide the keyboard.
 */
fun Modifier.hideKeyboardOnTapOutside(): Modifier = composed {
    val focusManager = LocalFocusManager.current
    this.pointerInput(Unit) {
        detectTapGestures(onTap = {
            focusManager.clearFocus()
        })
    }
}

/**
 * Adds an animated Shimmer effect to the background of a Composable.
 * 
 * @param showShimmer Whether to show the shimmer animation. Default is true.
 * @param targetColor The highlight color of the shimmer. Default is a semi-transparent white.
 * @return A [Modifier] with the background shimmer animation applied.
 */
fun Modifier.shimmer(
    showShimmer: Boolean = true,
    targetColor: Color = Color.White.copy(alpha = 0.3f)
): Modifier = composed {
    if (!showShimmer) return@composed this

    val transition = rememberInfiniteTransition(label = "shimmerTransition")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerTranslation"
    )

    val brush = Brush.linearGradient(
        colors = listOf(
            Color.LightGray.copy(alpha = 0.6f),
            targetColor,
            Color.LightGray.copy(alpha = 0.6f),
        ),
        start = Offset.Zero,
        end = Offset(x = translateAnim, y = translateAnim)
    )

    this.background(brush)
}


/**
 * Adds a horizontal shaking animation to the composable, typically used to indicate an error state.
 *
 * @param isError A boolean flag indicating whether the element should shake. The animation triggers when this becomes true.
 * @return A [Modifier] with the shaking animation applied.
 */
fun Modifier.shake(isError: Boolean): Modifier = composed {
    var shakeState by remember { mutableStateOf(false) }

    LaunchedEffect(isError) {
        if (isError) {
            shakeState = true
        }
    }

    val offset by animateFloatAsState(
        targetValue = if (shakeState) 0f else 0f,
        animationSpec = keyframes {
            durationMillis = 400
            0f at 0
            (-15f) at 50
            15f at 100
            (-10f) at 150
            10f at 200
            (-5f) at 250
            5f at 300
            0f at 400
        },
        finishedListener = { shakeState = false },
        label = "ShakeAnimation"
    )

    this.then(
        Modifier.graphicsLayer {
            translationX = offset
        }
    )
}
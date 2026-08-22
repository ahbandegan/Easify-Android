package ir.amirhesambandegan.easify_haptic

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext

/**
 * Replaces the standard clickable. Provides a light impact on down, and medium on up.
 */
fun Modifier.hapticClickable(
    interactionSource: MutableInteractionSource? = null,
    onClick: () -> Unit
): Modifier = composed {
    val context = LocalContext.current
    val actualInteractionSource = interactionSource ?: remember { MutableInteractionSource() }
    
    this
        .pointerInput(Unit) {
            awaitEachGesture {
                awaitFirstDown(requireUnconsumed = false)
                context.hapticImpactLight() // Press down
                
                val up = waitForUpOrCancellation()
                if (up != null) {
                    context.hapticImpactMedium() // Release
                }
            }
        }
        .clickable(
            interactionSource = actualInteractionSource,
            indication = rememberRipple(),
            onClick = onClick
        )
}

/**
 * Just adds tactile press indication without overriding the click behavior.
 */
fun Modifier.hapticPressIndicator(): Modifier = composed {
    val context = LocalContext.current
    this.pointerInput(Unit) {
        awaitEachGesture {
            awaitFirstDown(requireUnconsumed = false)
            context.hapticImpactLight()
        }
    }
}

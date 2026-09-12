package ir.amirhesambandegan.easify_haptic

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ripple
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext

/**
 * Replaces the standard clickable. Provides a light impact on down, and medium on up.
 *
 * @param interactionSource The [MutableInteractionSource] representing the stream of interactions for this modifier.
 * @param onClick The callback to be invoked when the click action is completed.
 * @return A [Modifier] with the applied haptic clickable behavior.
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
            indication = ripple(),
            onClick = onClick
        )
}

/**
 * Just adds tactile press indication without overriding the click behavior.
 *
 * @return A [Modifier] that triggers a light haptic impact on press.
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

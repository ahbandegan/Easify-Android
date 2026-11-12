package ir.amirhesambandegan.easify_haptic

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ripple
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.SuspendingPointerInputModifierNode
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.DelegatingNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.PointerInputModifierNode
import androidx.compose.ui.node.currentValueOf
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize

/**
 * Clickable modifier with automatic tactile feedback (light impact on press down, medium on release),
 * built using modern [Modifier.Node] architecture without `Modifier.composed`.
 */
fun Modifier.hapticClickable(
    interactionSource: MutableInteractionSource? = null,
    enabled: Boolean = true,
    onClick: () -> Unit
): Modifier = this
    .then(HapticPressElement(triggerOnRelease = true, enabled = enabled))
    .clickable(
        interactionSource = interactionSource,
        indication = ripple(),
        enabled = enabled,
        onClick = onClick
    )

/**
 * Adds a light tactile press cue on touch down without overriding existing click handlers.
 * Built with [Modifier.Node] for zero per-composition overhead.
 */
fun Modifier.hapticPressIndicator(enabled: Boolean = true): Modifier =
    this then HapticPressElement(triggerOnRelease = false, enabled = enabled)

private data class HapticPressElement(
    val triggerOnRelease: Boolean,
    val enabled: Boolean
) : ModifierNodeElement<HapticPressNode>() {
    override fun create(): HapticPressNode = HapticPressNode(triggerOnRelease, enabled)
    override fun update(node: HapticPressNode) {
        node.triggerOnRelease = triggerOnRelease
        node.enabled = enabled
    }

    override fun InspectorInfo.inspectableProperties() {
        name = if (triggerOnRelease) "hapticClickable" else "hapticPressIndicator"
        properties["triggerOnRelease"] = triggerOnRelease
        properties["enabled"] = enabled
    }
}

private class HapticPressNode(
    var triggerOnRelease: Boolean,
    var enabled: Boolean
) : DelegatingNode(), PointerInputModifierNode, CompositionLocalConsumerModifierNode {

    private val pointerInputNode = delegate(
        SuspendingPointerInputModifierNode {
            awaitEachGesture {
                awaitFirstDown(requireUnconsumed = false)
                if (!enabled) return@awaitEachGesture
                val context = currentValueOf(LocalContext)
                context.hapticImpactLight()
                if (triggerOnRelease) {
                    val up = waitForUpOrCancellation()
                    if (up != null) {
                        context.hapticImpactMedium()
                    }
                }
            }
        }
    )

    override fun onPointerEvent(
        pointerEvent: PointerEvent,
        pass: PointerEventPass,
        bounds: IntSize
    ) = pointerInputNode.onPointerEvent(pointerEvent, pass, bounds)

    override fun onCancelPointerInput() = pointerInputNode.onCancelPointerInput()
}

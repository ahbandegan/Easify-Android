package ir.amirhesambandegan.easify_ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.SuspendingPointerInputModifierNode
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DelegatingNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.PointerInputModifierNode
import androidx.compose.ui.node.SemanticsModifierNode
import androidx.compose.ui.node.currentValueOf
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.sin
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeMark
import kotlin.time.TimeSource

/**
 * Adds a smooth spring "bounce" press feedback effect when clicked, using Foundation [IndicationNodeFactory].
 *
 * @param minScale The scale factor to shrink to when pressed. Default is 0.95f.
 * @param enabled Controls the enabled state of the clickable element. Default is `true`.
 * @param onClick The callback to be invoked when the element is clicked.
 * @return A [Modifier] with the bounce click effect applied.
 */
fun Modifier.bounceClick(
    minScale: Float = 0.95f,
    enabled: Boolean = true,
    onClick: () -> Unit
): Modifier = this.clickable(
    enabled = enabled,
    interactionSource = null,
    indication = BounceIndicationNodeFactory(minScale.coerceIn(0.1f, 2f)),
    onClick = onClick
)

/**
 * Overload preserved for 2-argument binary callers (`bounceClick(minScale, onClick)`).
 */
fun Modifier.bounceClick(
    minScale: Float = 0.95f,
    onClick: () -> Unit
): Modifier = bounceClick(minScale = minScale, enabled = true, onClick = onClick)

private class BounceIndicationNodeFactory(
    private val minScale: Float
) : IndicationNodeFactory {
    override fun create(interactionSource: InteractionSource): DelegatableNode {
        return BounceIndicationNode(interactionSource, minScale)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BounceIndicationNodeFactory) return false
        return minScale == other.minScale
    }

    override fun hashCode(): Int = minScale.hashCode()
}

private class BounceIndicationNode(
    private val interactionSource: InteractionSource,
    private val minScale: Float
) : Modifier.Node(), DrawModifierNode {
    private val scaleAnim = Animatable(1f)
    private var animationJob: Job? = null

    override fun onAttach() {
        coroutineScope.launch {
            interactionSource.interactions.collect { interaction ->
                when (interaction) {
                    is PressInteraction.Press -> animateScale(minScale)
                    is PressInteraction.Release,
                    is PressInteraction.Cancel -> animateScale(1f)
                }
            }
        }
    }

    private fun animateScale(target: Float) {
        animationJob?.cancel()
        animationJob = coroutineScope.launch {
            scaleAnim.animateTo(targetValue = target, animationSpec = spring())
        }
    }

    override fun ContentDrawScope.draw() {
        val currentScale = scaleAnim.value
        if (currentScale == 1f) {
            drawContent()
        } else {
            scale(scaleX = currentScale, scaleY = currentScale, pivot = center) {
                this@draw.drawContent()
            }
        }
    }
}

/**
 * A clickable modifier that automatically ignores rapid accidental double-taps within [debounceMillis].
 *
 * Uses a stateful [Modifier.Node] with platform ripple indication and accessibility semantics.
 *
 * @param debounceMillis Minimum interval in milliseconds allowed between consecutive clicks. Default is 500ms.
 * @param enabled Whether clicks are enabled. Default is `true`.
 * @param onClick The callback invoked on a debounced click.
 * @return A [Modifier] that dispatches debounced click events.
 */
fun Modifier.debouncedClickable(
    debounceMillis: Long = 500L,
    enabled: Boolean = true,
    onClick: () -> Unit
): Modifier = this.then(
    DebouncedClickableElement(
        debounceMillis = debounceMillis.coerceAtLeast(0L),
        enabled = enabled,
        onClick = onClick
    )
)

private data class DebouncedClickableElement(
    val debounceMillis: Long,
    val enabled: Boolean,
    val onClick: () -> Unit
) : ModifierNodeElement<DebouncedClickableNode>() {
    override fun create() = DebouncedClickableNode(debounceMillis, enabled, onClick)

    override fun update(node: DebouncedClickableNode) {
        node.debounceMillis = debounceMillis
        node.enabled = enabled
        node.onClick = onClick
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "debouncedClickable"
        properties["debounceMillis"] = debounceMillis
        properties["enabled"] = enabled
    }
}

private class DebouncedClickableNode(
    var debounceMillis: Long,
    var enabled: Boolean,
    var onClick: () -> Unit
) : DelegatingNode(),
    CompositionLocalConsumerModifierNode,
    PointerInputModifierNode,
    SemanticsModifierNode {

    private val interactionSource = MutableInteractionSource()
    private var indicationNode: DelegatableNode? = null
    private var lastClickMark: TimeMark? = null

    private val pointerInputNode = delegate(
        SuspendingPointerInputModifierNode {
            awaitEachGesture {
                val down = awaitFirstDown(requireUnconsumed = false)
                if (!enabled) return@awaitEachGesture
                val press = PressInteraction.Press(down.position)
                interactionSource.tryEmit(press)
                val up = waitForUpOrCancellation()
                if (up != null) {
                    up.consume()
                    interactionSource.tryEmit(PressInteraction.Release(press))
                    triggerDebouncedClick()
                } else {
                    interactionSource.tryEmit(PressInteraction.Cancel(press))
                }
            }
        }
    )

    override fun onAttach() {
        val factory = currentValueOf(LocalIndication) as? IndicationNodeFactory
        if (factory != null) {
            indicationNode = delegate(factory.create(interactionSource))
        }
    }

    override fun onDetach() {
        indicationNode?.let { undelegate(it) }
        indicationNode = null
    }

    override fun onPointerEvent(
        pointerEvent: PointerEvent,
        pass: PointerEventPass,
        bounds: IntSize
    ) {
        pointerInputNode.onPointerEvent(pointerEvent, pass, bounds)
    }

    override fun onCancelPointerInput() {
        pointerInputNode.onCancelPointerInput()
    }

    override fun SemanticsPropertyReceiver.applySemantics() {
        role = Role.Button
        if (!enabled) {
            disabled()
        }
        onClick {
            if (enabled) {
                triggerDebouncedClick()
                true
            } else {
                false
            }
        }
    }

    private fun triggerDebouncedClick() {
        val mark = lastClickMark
        if (mark == null || mark.elapsedNow() >= debounceMillis.milliseconds) {
            lastClickMark = TimeSource.Monotonic.markNow()
            onClick()
        }
    }
}

/**
 * Hides the software keyboard and clears focus when the user taps outside a focused element.
 *
 * Uses a [Modifier.Node] instead of `Modifier.composed`.
 *
 * @return A [Modifier] that intercepts tap gestures to clear focus.
 */
fun Modifier.hideKeyboardOnTapOutside(): Modifier = this.then(HideKeyboardOnTapOutsideElement)

private object HideKeyboardOnTapOutsideElement : ModifierNodeElement<HideKeyboardOnTapOutsideNode>() {
    override fun create() = HideKeyboardOnTapOutsideNode()

    override fun update(node: HideKeyboardOnTapOutsideNode) = Unit

    override fun equals(other: Any?): Boolean = other === this

    override fun hashCode(): Int = 0x484B544F

    override fun InspectorInfo.inspectableProperties() {
        name = "hideKeyboardOnTapOutside"
    }
}

private class HideKeyboardOnTapOutsideNode :
    DelegatingNode(),
    CompositionLocalConsumerModifierNode,
    PointerInputModifierNode {

    private val pointerInputNode = delegate(
        SuspendingPointerInputModifierNode {
            detectTapGestures(
                onTap = {
                    currentValueOf(LocalFocusManager).clearFocus()
                }
            )
        }
    )

    override fun onPointerEvent(
        pointerEvent: PointerEvent,
        pass: PointerEventPass,
        bounds: IntSize
    ) {
        pointerInputNode.onPointerEvent(pointerEvent, pass, bounds)
    }

    override fun onCancelPointerInput() {
        pointerInputNode.onCancelPointerInput()
    }
}

/**
 * Adds an animated Shimmer gradient background effect to a Composable using a [Modifier.Node].
 *
 * @param showShimmer Whether to show the shimmer animation. Default is `true`.
 * @param targetColor The highlight color of the shimmer sweep. Default is semi-transparent white.
 * @param baseColor The surrounding base shimmer color. Default is semi-transparent light gray.
 * @return A [Modifier] with the background shimmer animation applied.
 */
fun Modifier.shimmer(
    showShimmer: Boolean = true,
    targetColor: Color = Color.White.copy(alpha = 0.3f),
    baseColor: Color = Color.LightGray.copy(alpha = 0.6f)
): Modifier = this.then(
    ShimmerElement(
        showShimmer = showShimmer,
        targetColor = targetColor,
        baseColor = baseColor
    )
)

/**
 * Overload preserved for 2-argument binary callers (`shimmer(showShimmer, targetColor)`).
 */
fun Modifier.shimmer(
    showShimmer: Boolean = true,
    targetColor: Color = Color.White.copy(alpha = 0.3f)
): Modifier = shimmer(
    showShimmer = showShimmer,
    targetColor = targetColor,
    baseColor = Color.LightGray.copy(alpha = 0.6f)
)

private data class ShimmerElement(
    val showShimmer: Boolean,
    val targetColor: Color,
    val baseColor: Color
) : ModifierNodeElement<ShimmerNode>() {
    override fun create() = ShimmerNode(showShimmer, targetColor, baseColor)

    override fun update(node: ShimmerNode) {
        node.updateParams(showShimmer, targetColor, baseColor)
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "shimmer"
        properties["showShimmer"] = showShimmer
        properties["targetColor"] = targetColor
        properties["baseColor"] = baseColor
    }
}

private class ShimmerNode(
    private var showShimmer: Boolean,
    private var targetColor: Color,
    private var baseColor: Color
) : Modifier.Node(), DrawModifierNode {
    private val progress = Animatable(0f)
    private var animationJob: Job? = null
    private var colors = listOf(baseColor, targetColor, baseColor)

    override fun onAttach() {
        if (showShimmer) startShimmer()
    }

    override fun onDetach() {
        stopShimmer()
    }

    fun updateParams(showShimmer: Boolean, targetColor: Color, baseColor: Color) {
        val colorChanged = this.targetColor != targetColor || this.baseColor != baseColor
        this.targetColor = targetColor
        this.baseColor = baseColor
        if (colorChanged) {
            colors = listOf(baseColor, targetColor, baseColor)
        }
        if (this.showShimmer != showShimmer) {
            this.showShimmer = showShimmer
            if (isAttached) {
                if (showShimmer) startShimmer() else stopShimmer()
            }
        }
    }

    private fun startShimmer() {
        if (animationJob?.isActive == true) return
        animationJob = coroutineScope.launch {
            progress.snapTo(0f)
            progress.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Restart
                )
            )
        }
    }

    private fun stopShimmer() {
        animationJob?.cancel()
        animationJob = null
    }

    override fun ContentDrawScope.draw() {
        if (showShimmer && size.width > 0f && size.height > 0f) {
            val p = progress.value
            val sweepSpan = (size.width + size.height).coerceAtLeast(1000f)
            val end = sweepSpan * p
            val start = end - sweepSpan * 0.4f
            drawRect(
                brush = Brush.linearGradient(
                    colors = colors,
                    start = Offset(start, start * 0.5f),
                    end = Offset(end, end * 0.5f)
                )
            )
        }
        drawContent()
    }
}

/**
 * Adds a damped horizontal shake animation triggered whenever [isError] transitions to `true`.
 *
 * Uses a [Modifier.Node] with normalized progress animation so every rising error transition triggers reliably.
 *
 * @param isError Indicates an error state. The shake animation runs when this becomes `true`.
 * @return A [Modifier] with the shaking translation applied.
 */
fun Modifier.shake(isError: Boolean): Modifier = this.then(ShakeElement(isError))

private data class ShakeElement(
    val isError: Boolean
) : ModifierNodeElement<ShakeNode>() {
    override fun create() = ShakeNode(isError)

    override fun update(node: ShakeNode) {
        node.updateError(isError)
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "shake"
        properties["isError"] = isError
    }
}

private class ShakeNode(
    private var isError: Boolean
) : Modifier.Node(), LayoutModifierNode {
    private val progress = Animatable(0f)
    private var shakeJob: Job? = null

    override fun onAttach() {
        if (isError) {
            triggerShake()
        }
    }

    override fun onDetach() {
        shakeJob?.cancel()
        shakeJob = null
    }

    fun updateError(newError: Boolean) {
        val risingEdge = newError && !isError
        isError = newError
        if (isAttached && risingEdge) {
            triggerShake()
        }
    }

    private fun triggerShake() {
        shakeJob?.cancel()
        shakeJob = coroutineScope.launch {
            progress.snapTo(0f)
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 400, easing = LinearEasing)
            )
            progress.snapTo(0f)
        }
    }

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        val placeable = measurable.measure(constraints)
        val amplitudePx = 10.dp.toPx()
        return layout(placeable.width, placeable.height) {
            placeable.placeWithLayer(0, 0) {
                val t = progress.value
                translationX = if (t <= 0f || t >= 1f) {
                    0f
                } else {
                    (sin(t * 6f * PI) * amplitudePx * (1f - t)).toFloat()
                }
            }
        }
    }
}

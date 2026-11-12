package ir.amirhesambandegan.easify_ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Adds a dashed border around the composable.
 *
 * Uses a [Modifier.Node] implementation so dash [PathEffect] instances are cached across frames.
 *
 * @param color The color of the dashed border. Default is [Color.Gray].
 * @param strokeWidth The width of the dashed border stroke. Default is 2.dp.
 * @param dashLength The length of each dash. Default is 8.dp.
 * @param gapLength The length of the gap between dashes. Default is 4.dp.
 * @param cornerRadius The radius for the rounded corners of the border. Default is 0.dp (sharp corners).
 * @return A [Modifier] with the dashed border applied.
 */
fun Modifier.dashedBorder(
    color: Color = Color.Gray,
    strokeWidth: Dp = 2.dp,
    dashLength: Dp = 8.dp,
    gapLength: Dp = 4.dp,
    cornerRadius: Dp = 0.dp
): Modifier = this.then(
    DashedBorderElement(
        color = color,
        strokeWidth = strokeWidth,
        dashLength = dashLength,
        gapLength = gapLength,
        cornerRadius = cornerRadius
    )
)

private data class DashedBorderElement(
    val color: Color,
    val strokeWidth: Dp,
    val dashLength: Dp,
    val gapLength: Dp,
    val cornerRadius: Dp
) : ModifierNodeElement<DashedBorderNode>() {
    override fun create() = DashedBorderNode(color, strokeWidth, dashLength, gapLength, cornerRadius)

    override fun update(node: DashedBorderNode) {
        node.color = color
        node.strokeWidth = strokeWidth
        node.dashLength = dashLength
        node.gapLength = gapLength
        node.cornerRadius = cornerRadius
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "dashedBorder"
        properties["color"] = color
        properties["strokeWidth"] = strokeWidth
        properties["dashLength"] = dashLength
        properties["gapLength"] = gapLength
        properties["cornerRadius"] = cornerRadius
    }
}

private class DashedBorderNode(
    var color: Color,
    var strokeWidth: Dp,
    var dashLength: Dp,
    var gapLength: Dp,
    var cornerRadius: Dp
) : Modifier.Node(), DrawModifierNode {
    private var cachedDensity = Float.NaN
    private var cachedDashLength = Dp.Unspecified
    private var cachedGapLength = Dp.Unspecified
    private var cachedStrokeWidth = Dp.Unspecified
    private var cachedStroke: Stroke? = null

    override fun ContentDrawScope.draw() {
        drawContent()
        if (strokeWidth <= 0.dp || dashLength <= 0.dp) return

        val stroke = if (
            cachedStroke != null &&
            cachedDensity == density &&
            cachedDashLength == dashLength &&
            cachedGapLength == gapLength &&
            cachedStrokeWidth == strokeWidth
        ) {
            cachedStroke!!
        } else {
            val effect = PathEffect.dashPathEffect(
                floatArrayOf(dashLength.toPx(), gapLength.toPx().coerceAtLeast(0f)),
                0f
            )
            Stroke(width = strokeWidth.toPx(), pathEffect = effect).also {
                cachedDensity = density
                cachedDashLength = dashLength
                cachedGapLength = gapLength
                cachedStrokeWidth = strokeWidth
                cachedStroke = it
            }
        }

        val radiusPx = cornerRadius.toPx().coerceAtLeast(0f)
        drawRoundRect(
            color = color,
            style = stroke,
            cornerRadius = CornerRadius(radiusPx, radiusPx)
        )
    }
}

/**
 * Adds a pulsing/breathing scale and alpha animation using a stateful [Modifier.Node].
 *
 * @param minScale The minimum scale factor during the pulse animation. Default is 0.95f.
 * @param maxScale The maximum scale factor during the pulse animation. Default is 1.05f.
 * @param minAlpha The minimum alpha transparency during the pulse animation. Default is 0.5f.
 * @param durationMillis The duration of one pulse cycle in milliseconds. Default is 1000.
 * @return A [Modifier] with the pulsing animation applied.
 */
fun Modifier.pulse(
    minScale: Float = 0.95f,
    maxScale: Float = 1.05f,
    minAlpha: Float = 0.5f,
    durationMillis: Int = 1000
): Modifier = this.then(
    PulseElement(
        minScale = minScale,
        maxScale = maxScale,
        minAlpha = minAlpha.coerceIn(0f, 1f),
        durationMillis = durationMillis.coerceAtLeast(1)
    )
)

private data class PulseElement(
    val minScale: Float,
    val maxScale: Float,
    val minAlpha: Float,
    val durationMillis: Int
) : ModifierNodeElement<PulseNode>() {
    override fun create() = PulseNode(minScale, maxScale, minAlpha, durationMillis)

    override fun update(node: PulseNode) {
        node.updateParams(minScale, maxScale, minAlpha, durationMillis)
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "pulse"
        properties["minScale"] = minScale
        properties["maxScale"] = maxScale
        properties["minAlpha"] = minAlpha
        properties["durationMillis"] = durationMillis
    }
}

private class PulseNode(
    private var minScale: Float,
    private var maxScale: Float,
    private var minAlpha: Float,
    private var durationMillis: Int
) : Modifier.Node(), LayoutModifierNode {
    private val progress = Animatable(0f)
    private var animationJob: Job? = null

    override fun onAttach() {
        startAnimation()
    }

    override fun onDetach() {
        animationJob?.cancel()
        animationJob = null
    }

    fun updateParams(minScale: Float, maxScale: Float, minAlpha: Float, durationMillis: Int) {
        this.minScale = minScale
        this.maxScale = maxScale
        this.minAlpha = minAlpha
        val durationChanged = this.durationMillis != durationMillis
        this.durationMillis = durationMillis
        if (isAttached && durationChanged) {
            startAnimation()
        }
    }

    private fun startAnimation() {
        animationJob?.cancel()
        animationJob = coroutineScope.launch {
            progress.snapTo(0f)
            progress.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = durationMillis, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )
        }
    }

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        val placeable = measurable.measure(constraints)
        return layout(placeable.width, placeable.height) {
            placeable.placeWithLayer(0, 0) {
                val fraction = progress.value
                val scale = minScale + (maxScale - minScale) * fraction
                scaleX = scale
                scaleY = scale
                alpha = minAlpha + (1f - minAlpha) * fraction
            }
        }
    }
}

/**
 * Applies a frosted glassmorphism surface treatment to the composable.
 *
 * By default, child content stays sharp (`blurContent = false`) while rendering a translucent
 * tint and subtle specular border highlight. Set [blurContent] to `true` if you also want Compose
 * [Modifier.blur] applied to the element's foreground (supported on Android 12+ and Skiko desktop/iOS/Web targets).
 *
 * @param blurRadius The radius of the foreground blur effect when [blurContent] is `true`. Default is 16.dp.
 * @param backgroundColor The translucent surface background color. Default is white with 20% opacity.
 * @param shape The shape used to clip and style the surface. Default is a rounded rectangle with 16.dp radius.
 * @param borderColor Specular highlight border tint around the glass card. Default is white with 30% opacity.
 * @param borderWidth Width of the glass border highlight. Default is 1.dp.
 * @param blurContent Whether to apply [Modifier.blur] to the composable content itself. Default is `false` so inner text remains crisp.
 * @return A [Modifier] with the glassmorphism styling applied.
 */
fun Modifier.glass(
    blurRadius: Dp = 16.dp,
    backgroundColor: Color = Color.White.copy(alpha = 0.2f),
    shape: Shape = RoundedCornerShape(16.dp),
    borderColor: Color = Color.White.copy(alpha = 0.3f),
    borderWidth: Dp = 1.dp,
    blurContent: Boolean = false
): Modifier {
    val borderBrush = Brush.linearGradient(
        colors = listOf(
            borderColor,
            borderColor.copy(alpha = borderColor.alpha * 0.25f)
        )
    )
    return this
        .clip(shape)
        .let { if (blurContent && blurRadius > 0.dp) it.blur(blurRadius) else it }
        .background(backgroundColor, shape)
        .let { if (borderWidth > 0.dp) it.border(borderWidth, borderBrush, shape) else it }
}

/**
 * Adds smooth alpha fading edges to the top and bottom of a composable or scrollable container.
 *
 * Renders into an offscreen compositing layer so [BlendMode.DstIn] cleanly masks only the content
 * of this modifier rather than bleeding through to underlying window pixels.
 *
 * @param topEdgeHeight The height of the top fading edge. Default is 16.dp.
 * @param bottomEdgeHeight The height of the bottom fading edge. Default is 16.dp.
 * @return A [Modifier] with the top and bottom fading edges applied.
 */
fun Modifier.fadingEdges(
    topEdgeHeight: Dp = 16.dp,
    bottomEdgeHeight: Dp = 16.dp
): Modifier = this.then(FadingEdgesElement(topEdgeHeight, bottomEdgeHeight))

private data class FadingEdgesElement(
    val topEdgeHeight: Dp,
    val bottomEdgeHeight: Dp
) : ModifierNodeElement<FadingEdgesNode>() {
    override fun create() = FadingEdgesNode(topEdgeHeight, bottomEdgeHeight)

    override fun update(node: FadingEdgesNode) {
        node.topEdgeHeight = topEdgeHeight
        node.bottomEdgeHeight = bottomEdgeHeight
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "fadingEdges"
        properties["topEdgeHeight"] = topEdgeHeight
        properties["bottomEdgeHeight"] = bottomEdgeHeight
    }
}

private class FadingEdgesNode(
    var topEdgeHeight: Dp,
    var bottomEdgeHeight: Dp
) : Modifier.Node(), LayoutModifierNode, DrawModifierNode {

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        val placeable = measurable.measure(constraints)
        return layout(placeable.width, placeable.height) {
            placeable.placeWithLayer(0, 0) {
                compositingStrategy = CompositingStrategy.Offscreen
            }
        }
    }

    override fun ContentDrawScope.draw() {
        drawContent()
        if (size.height <= 0f || size.width <= 0f) return

        val topPx = topEdgeHeight.toPx().coerceIn(0f, size.height)
        if (topPx > 0f) {
            drawRect(
                brush = Brush.verticalGradient(
                    colorStops = TOP_FADE_STOPS,
                    startY = 0f,
                    endY = topPx
                ),
                blendMode = BlendMode.DstIn
            )
        }

        val bottomPx = bottomEdgeHeight.toPx().coerceIn(0f, size.height)
        if (bottomPx > 0f) {
            drawRect(
                brush = Brush.verticalGradient(
                    colorStops = BOTTOM_FADE_STOPS,
                    startY = size.height - bottomPx,
                    endY = size.height
                ),
                blendMode = BlendMode.DstIn
            )
        }
    }

    companion object {
        private val TOP_FADE_STOPS = arrayOf(
            0.0f to Color.Transparent,
            1.0f to Color.Black
        )
        private val BOTTOM_FADE_STOPS = arrayOf(
            0.0f to Color.Black,
            1.0f to Color.Transparent
        )
    }
}

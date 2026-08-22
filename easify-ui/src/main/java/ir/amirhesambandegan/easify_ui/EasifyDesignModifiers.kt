package ir.amirhesambandegan.easify_ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Adds a dashed border around the composable.
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
): Modifier = this.drawWithContent {
    drawContent()
    val pathEffect = PathEffect.dashPathEffect(
        floatArrayOf(dashLength.toPx(), gapLength.toPx()), 0f
    )
    drawRoundRect(
        color = color,
        style = Stroke(width = strokeWidth.toPx(), pathEffect = pathEffect),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius.toPx(), cornerRadius.toPx())
    )
}

/**
 * Adds a pulsing/breathing scale and alpha animation.
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
): Modifier = composed {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_transition")
    
    val scale by infiniteTransition.animateFloat(
        initialValue = minScale,
        targetValue = maxScale,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )
    
    val alpha by infiniteTransition.animateFloat(
        initialValue = minAlpha,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )
    
    graphicsLayer {
        scaleX = scale
        scaleY = scale
        this.alpha = alpha
    }
}

/**
 * Applies a glassmorphism (frosted glass) effect to the composable.
 * Note: `blur` modifier requires Android 12+ (API 31+) to work natively.
 *
 * @param blurRadius The radius of the blur effect. Default is 16.dp.
 * @param backgroundColor The background color, typically semi-transparent. Default is white with 20% opacity.
 * @param shape The shape to which the effect is clipped. Default is a rounded corner shape with 16.dp radius.
 * @return A [Modifier] with the glassmorphism effect applied.
 */
fun Modifier.glass(
    blurRadius: Dp = 16.dp,
    backgroundColor: Color = Color.White.copy(alpha = 0.2f),
    shape: Shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
): Modifier = this
    .clip(shape)
    .blur(blurRadius)
    .background(backgroundColor, shape)

/**
 * Adds fading edges to the top and bottom of a scrollable area.
 *
 * @param topEdgeHeight The height of the top fading edge. Default is 16.dp.
 * @param bottomEdgeHeight The height of the bottom fading edge. Default is 16.dp.
 * @return A [Modifier] with the top and bottom fading edges applied.
 */
fun Modifier.fadingEdges(
    topEdgeHeight: Dp = 16.dp,
    bottomEdgeHeight: Dp = 16.dp
): Modifier = this.drawWithContent {
    drawContent()
    
    val topColorStops = arrayOf(
        0.0f to Color.Transparent,
        1.0f to Color.Black
    )
    
    val bottomColorStops = arrayOf(
        0.0f to Color.Black,
        1.0f to Color.Transparent
    )

    if (topEdgeHeight > 0.dp) {
        drawRect(
            brush = Brush.verticalGradient(
                colorStops = topColorStops,
                startY = 0f,
                endY = topEdgeHeight.toPx()
            ),
            blendMode = BlendMode.DstIn
        )
    }
    
    if (bottomEdgeHeight > 0.dp) {
        drawRect(
            brush = Brush.verticalGradient(
                colorStops = bottomColorStops,
                startY = size.height - bottomEdgeHeight.toPx(),
                endY = size.height
            ),
            blendMode = BlendMode.DstIn
        )
    }
}

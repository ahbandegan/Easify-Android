package ir.amirhesambandegan.easify_ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.hypot

/**
 * Types of reveal transitions for theme switching.
 */
enum class ThemeRevealType {
    /** Circular ripple expanding outwards from an anchor point or center. */
    CIRCULAR,
    /** Horizontal wipe from start (left) to end (right). */
    HORIZONTAL_START_TO_END,
    /** Horizontal wipe from end (right) to start (left). */
    HORIZONTAL_END_TO_START,
    /** Vertical wipe from top to bottom. */
    VERTICAL_TOP_TO_BOTTOM,
    /** Vertical wipe from bottom to top. */
    VERTICAL_BOTTOM_TO_TOP,
    /** Expanding diamond / rhombus shape from center or anchor point. */
    DIAMOND,
    /** Expanding rectangle from center or anchor point. */
    RECTANGLE
}

/**
 * Common anchor presets for where the reveal transition originates.
 */
sealed interface ThemeRevealAnchor {
    data object Center : ThemeRevealAnchor
    data object TopStart : ThemeRevealAnchor
    data object TopEnd : ThemeRevealAnchor
    data object BottomStart : ThemeRevealAnchor
    data object BottomEnd : ThemeRevealAnchor
    data class Custom(val offset: Offset) : ThemeRevealAnchor
}

private class CircularRevealShape(
    private val center: Offset,
    private val radius: Float
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline = Outline.Generic(
        Path().apply {
            addOval(Rect(center = center, radius = radius))
        }
    )
}

private class WipeRevealShape(
    private val progress: Float,
    private val revealType: ThemeRevealType
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()
        when (revealType) {
            ThemeRevealType.HORIZONTAL_START_TO_END -> {
                path.addRect(Rect(0f, 0f, size.width * progress, size.height))
            }
            ThemeRevealType.HORIZONTAL_END_TO_START -> {
                path.addRect(Rect(size.width * (1f - progress), 0f, size.width, size.height))
            }
            ThemeRevealType.VERTICAL_TOP_TO_BOTTOM -> {
                path.addRect(Rect(0f, 0f, size.width, size.height * progress))
            }
            ThemeRevealType.VERTICAL_BOTTOM_TO_TOP -> {
                path.addRect(Rect(0f, size.height * (1f - progress), size.width, size.height))
            }
            else -> {
                path.addRect(Rect(0f, 0f, size.width, size.height))
            }
        }
        return Outline.Generic(path)
    }
}

private class DiamondRevealShape(
    private val center: Offset,
    private val radius: Float
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline = Outline.Generic(
        Path().apply {
            moveTo(center.x, center.y - radius)
            lineTo(center.x + radius, center.y)
            lineTo(center.x, center.y + radius)
            lineTo(center.x - radius, center.y)
            close()
        }
    )
}

private class RectangularRevealShape(
    private val center: Offset,
    private val halfWidth: Float,
    private val halfHeight: Float
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline = Outline.Generic(
        Path().apply {
            addRect(
                Rect(
                    left = center.x - halfWidth,
                    top = center.y - halfHeight,
                    right = center.x + halfWidth,
                    bottom = center.y + halfHeight
                )
            )
        }
    )
}

/**
 * A versatile theme transition container supporting multiple reveal transition styles
 * (Circular ripple, Horizontal wipe, Vertical wipe, Diamond, and Rectangle) with smooth animations.
 *
 * @param isDark Whether dark theme is currently active.
 * @param modifier Modifier for the root container.
 * @param revealType The transition animation style.
 * @param anchor The origin preset for the reveal animation.
 * @param revealCenter Custom origin coordinate. If provided, overrides [anchor].
 * @param animationDuration Duration of the transition animation in milliseconds.
 * @param easing Easing curve for the transition animation.
 * @param lightColors [ColorScheme] used for light mode.
 * @param darkColors [ColorScheme] used for dark mode.
 * @param content Composable content wrapped inside the theme container.
 */
@Composable
fun EasifyThemeContainer(
    isDark: Boolean,
    modifier: Modifier = Modifier,
    revealType: ThemeRevealType = ThemeRevealType.CIRCULAR,
    anchor: ThemeRevealAnchor = ThemeRevealAnchor.TopEnd,
    revealCenter: Offset? = null,
    animationDuration: Int = 600,
    easing: Easing = FastOutSlowInEasing,
    lightColors: ColorScheme = lightColorScheme(),
    darkColors: ColorScheme = darkColorScheme(),
    content: @Composable () -> Unit
) {
    var previousDark by remember { mutableStateOf(isDark) }
    val progress = remember { Animatable(0f) }
    var containerSize by remember { mutableStateOf(IntSize.Zero) }
    val safeDuration = animationDuration.coerceAtLeast(1)

    LaunchedEffect(isDark) {
        if (isDark != previousDark) {
            try {
                progress.snapTo(0f)
                progress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = safeDuration, easing = easing)
                )
            } finally {
                previousDark = isDark
                progress.snapTo(0f)
            }
        }
    }

    val width = containerSize.width.toFloat()
    val height = containerSize.height.toFloat()

    val calculatedCenter = remember(revealCenter, anchor, width, height) {
        revealCenter ?: when (anchor) {
            ThemeRevealAnchor.Center -> Offset(width / 2f, height / 2f)
            ThemeRevealAnchor.TopStart -> Offset(0f, 0f)
            ThemeRevealAnchor.TopEnd -> Offset(width, 0f)
            ThemeRevealAnchor.BottomStart -> Offset(0f, height)
            ThemeRevealAnchor.BottomEnd -> Offset(width, height)
            is ThemeRevealAnchor.Custom -> anchor.offset
        }
    }

    val maxRadius = remember(calculatedCenter, width, height) {
        val cx = calculatedCenter.x
        val cy = calculatedCenter.y
        val d1 = hypot(cx, cy)
        val d2 = hypot(width - cx, cy)
        val d3 = hypot(cx, height - cy)
        val d4 = hypot(width - cx, height - cy)
        maxOf(d1, d2, d3, d4).coerceAtLeast(1000f)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged { containerSize = it }
    ) {
        MaterialTheme(colorScheme = if (previousDark) darkColors else lightColors) {
            content()
        }

        if (progress.value > 0f) {
            val shape: Shape = when (revealType) {
                ThemeRevealType.CIRCULAR -> CircularRevealShape(
                    center = calculatedCenter,
                    radius = progress.value * maxRadius
                )
                ThemeRevealType.HORIZONTAL_START_TO_END,
                ThemeRevealType.HORIZONTAL_END_TO_START,
                ThemeRevealType.VERTICAL_TOP_TO_BOTTOM,
                ThemeRevealType.VERTICAL_BOTTOM_TO_TOP -> WipeRevealShape(
                    progress = progress.value,
                    revealType = revealType
                )
                ThemeRevealType.DIAMOND -> DiamondRevealShape(
                    center = calculatedCenter,
                    radius = progress.value * maxRadius * 1.4f
                )
                ThemeRevealType.RECTANGLE -> RectangularRevealShape(
                    center = calculatedCenter,
                    halfWidth = progress.value * maxOf(calculatedCenter.x, width - calculatedCenter.x),
                    halfHeight = progress.value * maxOf(calculatedCenter.y, height - calculatedCenter.y)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        clip = true
                        this.shape = shape
                    }
            ) {
                MaterialTheme(colorScheme = if (isDark) darkColors else lightColors) {
                    content()
                }
            }
        }
    }
}

/**
 * Standard Circular Theme Transition Container.
 *
 * @param isDark Whether dark theme is enabled.
 * @param revealCenter Center point of the circular ripple effect. Defaults to `(0, 0)`.
 * @param animationDuration Duration of the reveal animation in milliseconds. Default is 600ms.
 * @param modifier Modifier for the root container.
 * @param lightColors [ColorScheme] used for light mode.
 * @param darkColors [ColorScheme] used for dark mode.
 * @param content Child composable hierarchy to render inside the theme.
 */
@Composable
fun CircularThemeContainer(
    isDark: Boolean,
    revealCenter: Offset = Offset.Zero,
    animationDuration: Int = 600,
    modifier: Modifier = Modifier,
    lightColors: ColorScheme = lightColorScheme(),
    darkColors: ColorScheme = darkColorScheme(),
    content: @Composable () -> Unit
) {
    EasifyThemeContainer(
        isDark = isDark,
        modifier = modifier,
        revealCenter = revealCenter,
        revealType = ThemeRevealType.CIRCULAR,
        animationDuration = animationDuration,
        lightColors = lightColors,
        darkColors = darkColors,
        content = content
    )
}

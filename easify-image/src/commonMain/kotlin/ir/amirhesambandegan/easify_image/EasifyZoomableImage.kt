package ir.amirhesambandegan.easify_image

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale

/**
 * A multiplatform composable displaying an [ImageBitmap] that supports pinch-to-zoom,
 * bounded panning, and double-tap zoom toggle.
 */
@Composable
fun EasifyZoomableImage(
    imageBitmap: ImageBitmap,
    modifier: Modifier = Modifier,
    contentDescription: String? = "Zoomable Image",
    minScale: Float = 1f,
    maxScale: Float = 5f,
    doubleTapScale: Float = 2.5f
) {
    val safeMinScale = minScale.coerceAtLeast(0.5f)
    val safeMaxScale = maxScale.coerceAtLeast(safeMinScale)
    val safeDoubleTapScale = doubleTapScale.coerceIn(safeMinScale, safeMaxScale)

    var scale by remember(imageBitmap, safeMinScale, safeMaxScale) { mutableFloatStateOf(safeMinScale) }
    var offsetX by remember(imageBitmap) { mutableFloatStateOf(0f) }
    var offsetY by remember(imageBitmap) { mutableFloatStateOf(0f) }

    Image(
        bitmap = imageBitmap,
        contentDescription = contentDescription,
        contentScale = ContentScale.Fit,
        modifier = modifier
            .fillMaxSize()
            .pointerInput(imageBitmap, safeMinScale, safeMaxScale, safeDoubleTapScale) {
                detectTapGestures(
                    onDoubleTap = { tapOffset ->
                        if (scale > safeMinScale + 0.05f) {
                            scale = safeMinScale
                            offsetX = 0f
                            offsetY = 0f
                        } else {
                            scale = safeDoubleTapScale
                            val centerX = size.width / 2f
                            val centerY = size.height / 2f
                            val maxX = (size.width * (scale - 1f)) / 2f
                            val maxY = (size.height * (scale - 1f)) / 2f
                            offsetX = ((centerX - tapOffset.x) * (scale - 1f)).coerceIn(-maxX, maxX)
                            offsetY = ((centerY - tapOffset.y) * (scale - 1f)).coerceIn(-maxY, maxY)
                        }
                    }
                )
            }
            .pointerInput(imageBitmap, safeMinScale, safeMaxScale) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale = (scale * zoom).coerceIn(safeMinScale, safeMaxScale)

                    if (scale > 1f) {
                        val maxX = (size.width * (scale - 1f)) / 2f
                        val maxY = (size.height * (scale - 1f)) / 2f
                        offsetX = (offsetX + pan.x).coerceIn(-maxX, maxX)
                        offsetY = (offsetY + pan.y).coerceIn(-maxY, maxY)
                    } else {
                        offsetX = 0f
                        offsetY = 0f
                    }
                }
            }
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationX = offsetX
                translationY = offsetY
            }
    )
}

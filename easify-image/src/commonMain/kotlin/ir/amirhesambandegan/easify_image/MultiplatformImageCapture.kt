package ir.amirhesambandegan.easify_image

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer

/**
 * Multiplatform controller for capturing composable content into an [ImageBitmap].
 */
class ImageCaptureController internal constructor(
    internal val graphicsLayer: GraphicsLayer
) {
    /**
     * Renders the captured composable content into an [ImageBitmap].
     */
    suspend fun capture(): ImageBitmap = graphicsLayer.toImageBitmap()
}

/**
 * Creates and remembers a multiplatform [ImageCaptureController].
 */
@Composable
fun rememberImageCaptureController(): ImageCaptureController {
    val graphicsLayer = rememberGraphicsLayer()
    return remember(graphicsLayer) { ImageCaptureController(graphicsLayer) }
}

/**
 * Multiplatform wrapper that records child composables into [controller] so they can be
 * exported as an [ImageBitmap] on Android, iOS, JVM Desktop, JS, and Wasm.
 */
@Composable
fun ImageCapturable(
    controller: ImageCaptureController,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier.drawWithContent {
            val layer = controller.graphicsLayer
            layer.record {
                this@drawWithContent.drawContent()
            }
            drawLayer(layer)
        }
    ) {
        content()
    }
}

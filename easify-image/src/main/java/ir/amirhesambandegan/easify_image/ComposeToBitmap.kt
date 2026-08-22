package ir.amirhesambandegan.easify_image

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.drawToBitmap

class CaptureController {
    internal var captureAction: (() -> Bitmap?)? = null
    
    fun capture(): Bitmap? {
        return captureAction?.invoke()
    }
}

@Composable
fun rememberCaptureController(): CaptureController {
    return remember { CaptureController() }
}

/**
 * Wraps a Compose UI and allows capturing it to a Bitmap.
 */
@Composable
fun Capturable(
    controller: CaptureController,
    content: @Composable () -> Unit
) {
    AndroidView(
        factory = { context ->
            val composeView = ComposeView(context).apply {
                setContent(content)
            }
            controller.captureAction = {
                try {
                    composeView.drawToBitmap()
                } catch (e: Exception) {
                    null
                }
            }
            composeView
        }
    )
}

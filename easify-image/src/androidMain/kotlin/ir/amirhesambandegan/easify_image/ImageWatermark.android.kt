package ir.amirhesambandegan.easify_image

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

/**
 * Adds a text watermark to the bottom-right corner of the [Bitmap].
 */
fun Bitmap.addWatermark(
    text: String,
    alpha: Int = 128,
    textSize: Float = 50f
): Bitmap {
    if (width <= 0 || height <= 0) return this
    val safeConfig = this.config ?: Bitmap.Config.ARGB_8888
    val result = this.copy(safeConfig, true)
    val canvas = Canvas(result)
    val paint = Paint().apply {
        color = Color.WHITE
        this.alpha = alpha.coerceIn(0, 255)
        this.textSize = textSize.coerceAtLeast(1f)
        isAntiAlias = true
        setShadowLayer(5f, 2f, 2f, Color.BLACK)
    }

    val textWidth = paint.measureText(text)
    val x = (result.width - textWidth - 20f).coerceAtLeast(8f)
    val y = (result.height - 20f).coerceAtLeast(paint.textSize)

    canvas.drawText(text, x, y, paint)
    return result
}

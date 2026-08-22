package ir.amirhesambandegan.easify_image

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

fun Bitmap.addWatermark(text: String, alpha: Int = 128, textSize: Float = 50f): Bitmap {
    val safeConfig = this.config ?: Bitmap.Config.ARGB_8888

    val result = this.copy(safeConfig, true)
    val canvas = Canvas(result)
    val paint = Paint().apply {
        this.color = Color.WHITE
        this.alpha = alpha
        this.textSize = textSize
        this.isAntiAlias = true
        this.setShadowLayer(5f, 2f, 2f, Color.BLACK)
    }

    val textWidth = paint.measureText(text)
    val x = result.width - textWidth - 20f
    val y = result.height - 20f

    canvas.drawText(text, x, y, paint)
    return result
}
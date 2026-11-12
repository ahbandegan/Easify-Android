package ir.amirhesambandegan.easify_image

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import kotlin.math.min
import kotlin.math.roundToInt

fun Bitmap.toGrayscale(): Bitmap {
    if (width <= 0 || height <= 0) return this
    val result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(result)
    val paint = Paint().apply {
        colorFilter = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f) })
    }
    canvas.drawBitmap(this, 0f, 0f, paint)
    return result
}

fun Bitmap.cropCircle(): Bitmap {
    val size = min(width, height)
    if (size <= 0) return this

    val result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(result)
    val paint = Paint().apply { isAntiAlias = true }

    val xOffset = (width - size) / 2
    val yOffset = (height - size) / 2
    val srcRect = Rect(xOffset, yOffset, xOffset + size, yOffset + size)
    val destRect = Rect(0, 0, size, size)

    val radius = size / 2f
    canvas.drawCircle(radius, radius, radius, paint)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(this, srcRect, destRect, paint)
    return result
}

fun Bitmap.fastBlur(scaleFactor: Float = 0.1f): Bitmap {
    if (width <= 0 || height <= 0) return this
    val safeScale = scaleFactor.coerceIn(0.02f, 1f)
    val scaledW = (width * safeScale).roundToInt().coerceAtLeast(1)
    val scaledH = (height * safeScale).roundToInt().coerceAtLeast(1)
    val scaled = Bitmap.createScaledBitmap(this, scaledW, scaledH, true)
    val result = Bitmap.createScaledBitmap(scaled, width, height, true)
    if (scaled !== this && scaled !== result && !scaled.isRecycled) {
        scaled.recycle()
    }
    return result
}

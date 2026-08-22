package ir.amirhesambandegan.easify_image

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect

fun Bitmap.toGrayscale(): Bitmap {
    val result = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(result)
    val paint = Paint()
    val colorMatrix = ColorMatrix()
    colorMatrix.setSaturation(0f)
    paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
    canvas.drawBitmap(this, 0f, 0f, paint)
    return result
}

fun Bitmap.cropCircle(): Bitmap {
    val size = Math.min(this.width, this.height)
    val result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(result)
    val paint = Paint().apply { isAntiAlias = true }
    
    val xOffset = (this.width - size) / 2
    val yOffset = (this.height - size) / 2
    val srcRect = Rect(xOffset, yOffset, xOffset + size, yOffset + size)
    val destRect = Rect(0, 0, size, size)
    
    val radius = size / 2f
    canvas.drawCircle(radius, radius, radius, paint)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(this, srcRect, destRect, paint)
    return result
}

fun Bitmap.fastBlur(): Bitmap {
    // A simple, fast downscale blur for cross-compatibility
    val scaleFactor = 0.1f
    val width = Math.round(this.width * scaleFactor)
    val height = Math.round(this.height * scaleFactor)
    val scaled = Bitmap.createScaledBitmap(this, width, height, true)
    return Bitmap.createScaledBitmap(scaled, this.width, this.height, true)
}

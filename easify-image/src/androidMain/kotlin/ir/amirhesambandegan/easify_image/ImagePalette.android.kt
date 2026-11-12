package ir.amirhesambandegan.easify_image

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color

fun Bitmap.extractDominantColor(): Color {
    if (width <= 0 || height <= 0) return Color.Transparent
    val scaledBitmap = Bitmap.createScaledBitmap(this, 1, 1, true)
    val pixel = scaledBitmap.getPixel(0, 0)
    if (scaledBitmap !== this && !scaledBitmap.isRecycled) {
        scaledBitmap.recycle()
    }
    return Color(pixel)
}

fun Bitmap.extractAverageColor(): Color {
    if (width <= 0 || height <= 0) return Color.Transparent
    val scaledBitmap = Bitmap.createScaledBitmap(this, 5, 5, true)
    var r = 0L
    var g = 0L
    var b = 0L
    val pixels = IntArray(25)
    scaledBitmap.getPixels(pixels, 0, 5, 0, 0, 5, 5)

    for (pixel in pixels) {
        r += android.graphics.Color.red(pixel)
        g += android.graphics.Color.green(pixel)
        b += android.graphics.Color.blue(pixel)
    }
    if (scaledBitmap !== this && !scaledBitmap.isRecycled) {
        scaledBitmap.recycle()
    }
    return Color(
        red = (r / 25).toInt(),
        green = (g / 25).toInt(),
        blue = (b / 25).toInt(),
        alpha = 255
    )
}

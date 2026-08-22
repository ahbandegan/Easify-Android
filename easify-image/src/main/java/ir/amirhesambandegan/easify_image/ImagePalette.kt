package ir.amirhesambandegan.easify_image

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color

/**
 * A lightweight dominant color extractor without external dependencies.
 * Scales down the [Bitmap] to a single pixel to find the most dominant color.
 *
 * @return The dominant [Color] of the image.
 */
fun Bitmap.extractDominantColor(): Color {
    // Scale down to a very small size for performance
    val scaledBitmap = Bitmap.createScaledBitmap(this, 1, 1, true)
    val pixel = scaledBitmap.getPixel(0, 0)
    scaledBitmap.recycle()
    return Color(pixel)
}

/**
 * Extracts the average color of the [Bitmap] by scaling it down to a 5x5 grid
 * and calculating the average of the RGB values of all 25 pixels.
 *
 * @return The average [Color] of the image.
 */
fun Bitmap.extractAverageColor(): Color {
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
    scaledBitmap.recycle()
    return Color(
        red = (r / 25).toInt(),
        green = (g / 25).toInt(),
        blue = (b / 25).toInt(),
        alpha = 255
    )
}

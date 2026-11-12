package ir.amirhesambandegan.easify_image

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap

/**
 * Calculates an approximate average [Color] of this [ImageBitmap] purely in common multiplatform code.
 */
fun ImageBitmap.extractAverageColor(sampleStep: Int = 4): Color {
    if (width <= 0 || height <= 0) return Color.Transparent
    val buffer = IntArray(width * height)
    readPixels(buffer)
    return extractAverageColorFromPixels(
        pixels = buffer,
        width = width,
        height = height,
        sampleStep = sampleStep
    )
}

/**
 * Pure Kotlin helper that calculates an approximate average [Color] from an ARGB [IntArray] pixel buffer.
 */
fun extractAverageColorFromPixels(
    pixels: IntArray,
    width: Int,
    height: Int,
    sampleStep: Int = 4
): Color {
    if (width <= 0 || height <= 0 || pixels.size < width * height) return Color.Transparent
    val step = sampleStep.coerceAtLeast(1)

    var totalA = 0L
    var totalR = 0L
    var totalG = 0L
    var totalB = 0L
    var count = 0L

    var y = 0
    while (y < height) {
        val rowOffset = y * width
        var x = 0
        while (x < width) {
            val argb = pixels[rowOffset + x]
            totalA += (argb ushr 24) and 0xFF
            totalR += (argb ushr 16) and 0xFF
            totalG += (argb ushr 8) and 0xFF
            totalB += argb and 0xFF
            count++
            x += step
        }
        y += step
    }

    if (count == 0L) return Color.Transparent
    return Color(
        red = (totalR / count).toInt(),
        green = (totalG / count).toInt(),
        blue = (totalB / count).toInt(),
        alpha = (totalA / count).toInt()
    )
}

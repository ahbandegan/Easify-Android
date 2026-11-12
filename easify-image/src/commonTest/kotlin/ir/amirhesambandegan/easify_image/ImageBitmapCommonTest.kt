package ir.amirhesambandegan.easify_image

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.graphics.colorspace.ColorSpace
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.graphics.toArgb
import kotlin.test.Test
import kotlin.test.assertEquals

class ImageBitmapCommonTest {

    private class FakeImageBitmap(
        override val width: Int,
        override val height: Int,
        private val pixels: IntArray
    ) : ImageBitmap {
        override val colorSpace: ColorSpace = ColorSpaces.Srgb
        override val config: ImageBitmapConfig = ImageBitmapConfig.Argb8888
        override val hasAlpha: Boolean = true

        override fun prepareToDraw() = Unit

        override fun readPixels(
            buffer: IntArray,
            startX: Int,
            startY: Int,
            width: Int,
            height: Int,
            bufferOffset: Int,
            stride: Int
        ) {
            for (row in 0 until height) {
                val srcStart = (startY + row) * this.width + startX
                val dstStart = bufferOffset + row * stride
                pixels.copyInto(buffer, dstStart, srcStart, srcStart + width)
            }
        }
    }

    @Test
    fun extractAverageColorComputesExpectedColorOnSolidBitmap() {
        val argbRed = Color.Red.toArgb()
        val bitmap = FakeImageBitmap(
            width = 10,
            height = 10,
            pixels = IntArray(100) { argbRed }
        )

        val avg = bitmap.extractAverageColor(sampleStep = 2)
        assertEquals(Color.Red, avg)
    }

    @Test
    fun extractAverageColorHandlesEmptyAndMixedPixels() {
        assertEquals(Color.Transparent, extractAverageColorFromPixels(IntArray(0), 0, 0))
        val mixed = intArrayOf(
            Color(red = 200, green = 100, blue = 50, alpha = 255).toArgb(),
            Color(red = 100, green = 200, blue = 150, alpha = 255).toArgb()
        )
        val avg = extractAverageColorFromPixels(mixed, width = 2, height = 1, sampleStep = 1)
        assertEquals(Color(red = 150, green = 150, blue = 100, alpha = 255), avg)
    }
}

package ir.amirhesambandegan.easify_form

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/**
 * A generic multiplatform visual transformation that masks input based on a pattern
 * such as `"+98 (###) ###-####"`, using monotonic bounds-safe cursor mapping.
 */
class MaskVisualTransformation(
    private val mask: String,
    private val maskChar: Char = '#'
) : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val orig = text.text
        val origToTrans = IntArray(orig.length + 1)
        val out = StringBuilder()

        var maskIndex = 0
        var textIndex = 0

        while (maskIndex < mask.length && textIndex < orig.length) {
            if (mask[maskIndex] == maskChar) {
                origToTrans[textIndex] = out.length
                out.append(orig[textIndex])
                textIndex++
            } else {
                out.append(mask[maskIndex])
            }
            maskIndex++
        }

        val transformed = out.toString()
        for (i in textIndex..orig.length) {
            origToTrans[i] = transformed.length
        }

        val transToOrig = IntArray(transformed.length + 1)
        var origCount = 0
        for (t in transformed.indices) {
            transToOrig[t] = origCount.coerceAtMost(orig.length)
            if (t < mask.length && mask[t] == maskChar && origCount < textIndex) {
                origCount++
            }
        }
        transToOrig[transformed.length] = origCount.coerceAtMost(orig.length)

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val safe = offset.coerceIn(0, orig.length)
                return origToTrans[safe]
            }

            override fun transformedToOriginal(offset: Int): Int {
                val safe = offset.coerceIn(0, transformed.length)
                return transToOrig[safe]
            }
        }

        return TransformedText(AnnotatedString(transformed), offsetMapping)
    }
}

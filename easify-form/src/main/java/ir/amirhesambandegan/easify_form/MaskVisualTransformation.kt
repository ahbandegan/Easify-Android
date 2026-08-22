package ir.amirhesambandegan.easify_form

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/**
 * A generic visual transformation that masks input based on a pattern.
 * Example pattern: "+98 (###) ###-####" where '#' represents a character typed by the user.
 */
class MaskVisualTransformation(private val mask: String, private val maskChar: Char = '#') : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        var out = ""
        var maskIndex = 0
        var textIndex = 0
        
        while (maskIndex < mask.length && textIndex < text.text.length) {
            if (mask[maskIndex] == maskChar) {
                out += text.text[textIndex]
                textIndex++
            } else {
                out += mask[maskIndex]
            }
            maskIndex++
        }
        
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                var o = 0
                var t = 0
                while (o < offset && t < mask.length) {
                    if (mask[t] == maskChar) {
                        o++
                    }
                    t++
                }
                return t
            }
            override fun transformedToOriginal(offset: Int): Int {
                var o = 0
                var t = 0
                while (t < offset && t < mask.length) {
                    if (mask[t] == maskChar) {
                        o++
                    }
                    t++
                }
                return o
            }
        }
        return TransformedText(AnnotatedString(out), offsetMapping)
    }
}

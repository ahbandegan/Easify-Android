package ir.amirhesambandegan.easify_form

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/**
 * A generic visual transformation that masks input based on a given pattern.
 *
 * Example pattern: "+98 (###) ###-####" where '#' represents a character typed by the user.
 * It maps the characters entered by the user into the mask placeholders, ensuring
 * proper cursor placement when typing or deleting characters.
 *
 * @property mask The template mask string to enforce.
 * @property maskChar The character within the mask that acts as a placeholder for user input. Defaults to '#'.
 */
class MaskVisualTransformation(private val mask: String, private val maskChar: Char = '#') : VisualTransformation {
    
    /**
     * Transforms the original text by applying the mask pattern and creates an appropriate mapping
     * between the original and transformed character offsets.
     *
     * @param text The original unformatted text.
     * @return The [TransformedText] containing the masked output and offset mapping.
     */
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

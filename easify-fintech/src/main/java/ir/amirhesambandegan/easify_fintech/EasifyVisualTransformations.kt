package ir.amirhesambandegan.easify_fintech

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/**
 * A [VisualTransformation] that formats user input into a standard 16-digit bank card format
 * by inserting a hyphen (-) after every 4 digits.
 */
class CardNumberVisualTransformation : VisualTransformation {
    /**
     * Applies the formatting to the given [AnnotatedString].
     *
     * @param text The input string representing the card number digits.
     * @return A [TransformedText] containing the formatted card number and the corresponding offset mapping.
     */
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 16) text.text.substring(0..15) else text.text
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i % 4 == 3 && i != 15) out += "-"
        }
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 3) return offset
                if (offset <= 7) return offset + 1
                if (offset <= 11) return offset + 2
                if (offset <= 16) return offset + 3
                return 19
            }
            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 4) return offset
                if (offset <= 9) return offset - 1
                if (offset <= 14) return offset - 2
                if (offset <= 19) return offset - 3
                return 16
            }
        }
        return TransformedText(AnnotatedString(out), offsetMapping)
    }
}

/**
 * A [VisualTransformation] that formats numerical price input by adding commas (,)
 * every three digits to separate thousands.
 */
class PriceVisualTransformation : VisualTransformation {
    /**
     * Applies the formatting to the given [AnnotatedString].
     *
     * @param text The input string containing the digits of the price.
     * @return A [TransformedText] containing the comma-separated formatted price and the corresponding offset mapping.
     */
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text.filter { it.isDigit() }
        val formattedText = if (originalText.isNotEmpty()) {
            originalText.reversed().chunked(3).joinToString(",").reversed()
        } else {
            ""
        }
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (originalText.isEmpty()) return 0
                val commasBefore = (originalText.length - 1 - (originalText.length - offset)) / 3
                val totalCommas = (originalText.length - 1) / 3
                val commasToLeft = totalCommas - commasBefore
                return offset + commasToLeft
            }
            override fun transformedToOriginal(offset: Int): Int {
                val commas = formattedText.substring(0, offset).count { it == ',' }
                return offset - commas
            }
        }
        return TransformedText(AnnotatedString(formattedText), offsetMapping)
    }
}

/**
 * A [VisualTransformation] that formats a 4-digit input into a standard expiration date format
 * (MM/YY) by inserting a slash (/) after the first two digits.
 */
class ExpirationDateVisualTransformation : VisualTransformation {
    /**
     * Applies the formatting to the given [AnnotatedString].
     *
     * @param text The input string representing the expiration date digits.
     * @return A [TransformedText] containing the formatted expiration date and the corresponding offset mapping.
     */
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 4) text.text.substring(0..3) else text.text
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i == 1 && trimmed.length > 2) out += "/"
        }
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 1) return offset
                if (offset <= 4) return offset + 1
                return 5
            }
            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset <= 5) return offset - 1
                return 4
            }
        }
        return TransformedText(AnnotatedString(out), offsetMapping)
    }
}

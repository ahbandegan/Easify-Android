package ir.amirhesambandegan.easify_fintech

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

private class ArrayOffsetMapping(
    private val origToTrans: IntArray,
    private val transToOrig: IntArray
) : OffsetMapping {
    override fun originalToTransformed(offset: Int): Int =
        origToTrans[offset.coerceIn(0, origToTrans.lastIndex)]

    override fun transformedToOriginal(offset: Int): Int =
        transToOrig[offset.coerceIn(0, transToOrig.lastIndex)]
}

private fun Char.toAsciiDigitOrNull(): Char? = when (this) {
    in '0'..'9' -> this
    in '\u06F0'..'\u06F9' -> ('0'.code + (this.code - 0x06F0)).toChar()
    in '\u0660'..'\u0669' -> ('0'.code + (this.code - 0x0660)).toChar()
    else -> null
}

/**
 * Safely transforms a raw string into grouped segments using monotonic precomputed [OffsetMapping] tables.
 */
private fun buildGroupedDigitTransformation(
    raw: String,
    maxDigits: Int,
    separator: String,
    shouldInsertSeparatorAfterIndex: (digitIndex: Int, totalKept: Int) -> Boolean,
    prefix: String = "",
    suffix: String = ""
): TransformedText {
    val keptPositions = mutableListOf<Int>()
    val keptDigits = StringBuilder()
    for (i in raw.indices) {
        val digit = raw[i].toAsciiDigitOrNull() ?: continue
        if (keptPositions.size < maxDigits) {
            keptPositions.add(i)
            keptDigits.append(digit)
        }
    }

    if (keptPositions.isEmpty()) {
        val origToTrans = IntArray(raw.length + 1) { 0 }
        val transToOrig = intArrayOf(0)
        return TransformedText(AnnotatedString(""), ArrayOffsetMapping(origToTrans, transToOrig))
    }

    val out = StringBuilder()
    val transToOrigList = mutableListOf(0)

    if (prefix.isNotEmpty()) {
        val firstOrigin = keptPositions.first()
        for (ch in prefix) {
            out.append(ch)
            transToOrigList.add(firstOrigin)
        }
    }

    val outPosAfterOrigin = mutableMapOf<Int, Int>()
    for (k in keptPositions.indices) {
        if (k > 0 && shouldInsertSeparatorAfterIndex(k - 1, keptPositions.size)) {
            val boundaryOrig = keptPositions[k]
            for (ch in separator) {
                out.append(ch)
                transToOrigList.add(boundaryOrig)
            }
        }
        out.append(keptDigits[k])
        val nextOrig = keptPositions[k] + 1
        transToOrigList.add(nextOrig)
        outPosAfterOrigin[keptPositions[k]] = out.length
    }

    val endNumberOutLen = out.length
    if (suffix.isNotEmpty()) {
        for (ch in suffix) {
            out.append(ch)
            transToOrigList.add(raw.length)
        }
    }

    val origToTrans = IntArray(raw.length + 1)
    var currentMapped = prefix.length
    origToTrans[0] = currentMapped
    for (i in raw.indices) {
        outPosAfterOrigin[i]?.let { updated ->
            currentMapped = updated
        }
        origToTrans[i + 1] = currentMapped
    }
    if (suffix.isNotEmpty()) {
        // Keep cursor before suffix at end of digits
        origToTrans[raw.length] = origToTrans[raw.length].coerceAtMost(endNumberOutLen)
    }

    return TransformedText(
        AnnotatedString(out.toString()),
        ArrayOffsetMapping(origToTrans, transToOrigList.toIntArray())
    )
}

/**
 * Formats user input into a standard 16-digit bank card format (`XXXX-XXXX-XXXX-XXXX`).
 * Crash-safe for arbitrary cursor offsets, Persian/Arabic digits, and pasted separators.
 */
class CardNumberVisualTransformation(
    private val separator: String = "-"
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText =
        buildGroupedDigitTransformation(
            raw = text.text,
            maxDigits = 16,
            separator = separator,
            shouldInsertSeparatorAfterIndex = { idx, _ -> idx % 4 == 3 }
        )
}

/**
 * Formats numerical price input by inserting thousands separators (`,`) every three digits from the right.
 * Crash-safe even when mixed non-digit characters exist or when the input length fluctuates.
 */
class PriceVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText =
        buildGroupedDigitTransformation(
            raw = text.text,
            maxDigits = Int.MAX_VALUE,
            separator = ",",
            shouldInsertSeparatorAfterIndex = { idx, total ->
                val remainingToRight = total - (idx + 1)
                remainingToRight > 0 && remainingToRight % 3 == 0
            }
        )
}

/**
 * Formats numerical monetary input with thousands separators and a non-editable trailing currency suffix
 * (e.g., `"150000"` -> `"150,000 تومان"`), keeping the cursor cleanly positioned before the suffix.
 */
class CurrencyVisualTransformation(
    private val suffix: String = " تومان"
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText =
        buildGroupedDigitTransformation(
            raw = text.text,
            maxDigits = Int.MAX_VALUE,
            separator = ",",
            shouldInsertSeparatorAfterIndex = { idx, total ->
                val remainingToRight = total - (idx + 1)
                remainingToRight > 0 && remainingToRight % 3 == 0
            },
            suffix = suffix
        )
}

/**
 * Formats a 4-digit expiration date input as `MM/YY`.
 */
class ExpirationDateVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText =
        buildGroupedDigitTransformation(
            raw = text.text,
            maxDigits = 4,
            separator = "/",
            shouldInsertSeparatorAfterIndex = { idx, _ -> idx == 1 }
        )
}

/**
 * Formats the 24 numeric digits of an Iranian Sheba (IBAN) into `IR## #### #### #### #### #### ##`.
 */
class ShebaVisualTransformation(
    private val prefix: String = "IR"
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText =
        buildGroupedDigitTransformation(
            raw = text.text,
            maxDigits = 24,
            separator = " ",
            shouldInsertSeparatorAfterIndex = { idx, _ ->
                // Break after check digits (index 1 -> position 2), then every 4 digits (5, 9, 13, 17, 21)
                idx == 1 || (idx > 1 && (idx - 1) % 4 == 0)
            },
            prefix = prefix
        )
}

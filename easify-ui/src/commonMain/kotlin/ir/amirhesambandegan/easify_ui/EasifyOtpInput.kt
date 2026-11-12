package ir.amirhesambandegan.easify_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

/**
 * Normalizes OTP text by converting Persian/Arabic digits to ASCII digits and optionally
 * stripping non-digit characters, capped at [maxLength].
 */
internal fun normalizeOtpInput(
    raw: String,
    maxLength: Int,
    digitsOnly: Boolean
): String {
    val safeLimit = maxLength.coerceAtLeast(1)
    val sb = StringBuilder(raw.length)
    for (ch in raw) {
        val mapped = when (ch) {
            in '\u06F0'..'\u06F9' -> ('0'.code + (ch.code - '\u06F0'.code)).toChar()
            in '\u0660'..'\u0669' -> ('0'.code + (ch.code - '\u0660'.code)).toChar()
            else -> ch
        }
        if (!digitsOnly || mapped.isDigit()) {
            sb.append(mapped)
        }
        if (sb.length >= safeLimit) break
    }
    return sb.toString()
}

/**
 * A customizable OTP (One Time Password) input component consisting of multiple distinct character boxes.
 *
 * Enforces Left-to-Right digit order inside RTL layouts, automatically converts Persian/Arabic digits,
 * and shakes horizontally when [isError] transitions to `true`.
 *
 * @param value The current text value of the OTP input.
 * @param onValueChange The callback invoked when the normalized value changes.
 * @param modifier The [Modifier] to be applied to the entire input component.
 * @param length The total number of digits/characters in the OTP. Default is 4.
 * @param isError Indicates whether the input is in an error state, triggering shake animations and error colors. Default is `false`.
 * @param boxSize The size (width and height) of each individual character box. Default is 56.dp.
 * @param boxSpacing The horizontal spacing between the individual character boxes. Default is 8.dp.
 * @param boxShape The shape of each character box. Default is `RoundedCornerShape(12.dp)`.
 * @param digitsOnly Whether non-digit characters should be filtered out. Default is `true`.
 * @param activeColor The border color of the box currently focused or active. Default is `MaterialTheme.colorScheme.primary`.
 * @param inactiveColor The border color of boxes that are inactive/unfocused. Default is [Color.Gray].
 * @param errorColor The border color used when [isError] is `true`. Default is `MaterialTheme.colorScheme.error`.
 * @param onComplete An optional callback invoked when the full OTP length is reached.
 */
@Composable
fun EasifyOtpInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    length: Int = 4,
    isError: Boolean = false,
    boxSize: Dp = 56.dp,
    boxSpacing: Dp = 8.dp,
    boxShape: Shape = RoundedCornerShape(12.dp),
    digitsOnly: Boolean = true,
    activeColor: Color = MaterialTheme.colorScheme.primary,
    inactiveColor: Color = Color.Gray,
    errorColor: Color = MaterialTheme.colorScheme.error,
    onComplete: ((String) -> Unit)? = null
) {
    val safeLength = length.coerceAtLeast(1)
    val normalizedValue = normalizeOtpInput(value, safeLength, digitsOnly)

    BasicTextField(
        value = normalizedValue,
        onValueChange = { rawInput ->
            val sanitized = normalizeOtpInput(rawInput, safeLength, digitsOnly)
            if (sanitized != normalizedValue) {
                onValueChange(sanitized)
                if (sanitized.length == safeLength) {
                    onComplete?.invoke(sanitized)
                }
            }
        },
        modifier = modifier.shake(isError),
        keyboardOptions = KeyboardOptions(
            keyboardType = if (digitsOnly) KeyboardType.NumberPassword else KeyboardType.Text
        ),
        decorationBox = {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(safeLength) { index ->
                        val isFocused = normalizedValue.length == index
                        val char = if (index < normalizedValue.length) normalizedValue[index].toString() else ""

                        val borderColor = when {
                            isError -> errorColor
                            isFocused -> activeColor
                            else -> inactiveColor
                        }

                        Box(
                            modifier = Modifier
                                .padding(horizontal = boxSpacing / 2)
                                .size(boxSize)
                                .border(
                                    width = if (isFocused || isError) 2.dp else 1.dp,
                                    color = borderColor,
                                    shape = boxShape
                                )
                                .background(
                                    color = MaterialTheme.colorScheme.surface,
                                    shape = boxShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = char,
                                style = MaterialTheme.typography.titleLarge,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    )
}

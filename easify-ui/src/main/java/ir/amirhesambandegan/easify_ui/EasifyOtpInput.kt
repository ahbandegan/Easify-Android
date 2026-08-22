package ir.amirhesambandegan.easify_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A customizable OTP (One Time Password) input component consisting of multiple distinct character boxes.
 *
 * @param value The current text value of the OTP input.
 * @param onValueChange The callback invoked when the value changes.
 * @param modifier The [Modifier] to be applied to the entire input component.
 * @param length The total number of digits/characters in the OTP. Default is 4.
 * @param isError Indicates whether the input is in an error state, triggering shake animations and error colors. Default is false.
 * @param boxSize The size (width and height) of each individual character box. Default is 56.dp.
 * @param boxSpacing The horizontal spacing between the individual character boxes. Default is 8.dp.
 * @param activeColor The border color of the box currently focused or active. Default is the primary color from [MaterialTheme.colorScheme].
 * @param inactiveColor The border color of boxes that are inactive/unfocused. Default is [Color.Gray].
 * @param errorColor The border color used when [isError] is true. Default is the error color from [MaterialTheme.colorScheme].
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
    activeColor: Color = MaterialTheme.colorScheme.primary,
    inactiveColor: Color = Color.Gray,
    errorColor: Color = MaterialTheme.colorScheme.error,
    onComplete: ((String) -> Unit)? = null
) {
    BasicTextField(
        value = value,
        onValueChange = {
            if (it.length <= length) {
                onValueChange(it)
                if (it.length == length) {
                    onComplete?.invoke(it)
                }
            }
        },
        modifier = modifier.shake(isError),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        decorationBox = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(length) { index ->
                    val isFocused = value.length == index
                    val char = when {
                        index < value.length -> value[index].toString()
                        else -> ""
                    }
                    
                    val borderColor = when {
                        isError -> errorColor
                        isFocused -> activeColor
                        else -> inactiveColor
                    }

                    Box(
                        modifier = Modifier
                            .size(boxSize)
                            .padding(horizontal = boxSpacing / 2)
                            .border(
                                width = if (isFocused || isError) 2.dp else 1.dp,
                                color = borderColor,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .background(
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(12.dp)
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
    )
}

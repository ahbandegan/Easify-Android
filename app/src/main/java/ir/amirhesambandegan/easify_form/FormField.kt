package ir.amirhesambandegan.easify_form

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Represents a single field within a form.
 *
 * @property key A unique identifier for the field.
 * @property initialValue The starting value of the field.
 * @property validator A function that returns true if the value is valid.
 */
class FormField(
    val key: String,
    initialValue: String = "",
    val validator: (String) -> Boolean = { true }
) {
    var value by mutableStateOf(initialValue)
    var error by mutableStateOf<String?>(null)
    var isTouched by mutableStateOf(false)

    /**
     * Validates the current value and updates the [error] state.
     * @param errorMessage The message to show if validation fails.
     * @return True if valid, false otherwise.
     */
    fun validate(errorMessage: String = "Invalid input"): Boolean {
        val isValid = validator(value)
        error = if (isValid) null else errorMessage
        return isValid
    }
}

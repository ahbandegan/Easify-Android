package ir.amirhesambandegan.easify_form

import androidx.compose.runtime.*
import androidx.compose.ui.focus.FocusRequester
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Represents a single field within a form, holding its state and validation logic.
 *
 * @property key The unique identifier for this field in the form.
 * @property initialValue The initial string value of the field. Defaults to an empty string.
 * @property rules A list of synchronous [ValidationRule]s applied to this field.
 * @property asyncValidator An optional suspend function for asynchronous validation. Returns an error message or null if valid.
 * @property inputFilter An optional function to filter or format the input value dynamically as the user types.
 */
class FormField(
    val key: String,
    val initialValue: String = "",
    val rules: List<ValidationRule> = emptyList(),
    val asyncValidator: (suspend (String) -> String?)? = null,
    val inputFilter: ((String) -> String)? = null
) {
    /**
     * The current value of the field. Setting this value triggers the [inputFilter],
     * updates the [isDirty] flag, and clears any existing validation error.
     */
    private val _value = mutableStateOf(initialValue)
    var value: String
        get() = _value.value
        set(newValue) {
            val filtered = inputFilter?.invoke(newValue) ?: newValue
            _value.value = filtered
            isDirty = filtered != initialValue
            error = null // Clear error on typing
        }
        
    /**
     * The current validation error message for this field, if any.
     */
    var error by mutableStateOf<String?>(null)
    
    /**
     * Indicates whether the field has been interacted with by the user.
     */
    var isTouched by mutableStateOf(false)
    
    /**
     * Indicates whether the field's value has changed from its [initialValue].
     */
    var isDirty by mutableStateOf(false)
    
    /**
     * Indicates whether asynchronous validation is currently in progress.
     */
    var isValidating by mutableStateOf(false)
    
    /**
     * A [FocusRequester] associated with this field, allowing for programmatical focus requests.
     */
    val focusRequester = FocusRequester()

    /**
     * Synchronously validates the field against its [rules].
     *
     * @return `true` if all synchronous rules pass, `false` otherwise. Sets [error] accordingly.
     */
    fun validate(): Boolean {
        for (rule in rules) {
            if (!rule.check(value)) {
                error = rule.errorMessage
                return false
            }
        }
        error = null
        return true
    }

    /**
     * Validates synchronous rules first, and then executes the [asyncValidator] if provided.
     *
     * @return `true` if both synchronous and asynchronous validation pass, `false` otherwise.
     */
    suspend fun validateAsync(): Boolean {
        if (!validate()) return false
        
        if (asyncValidator != null) {
            isValidating = true
            val asyncError = withContext(Dispatchers.IO) { asyncValidator.invoke(value) }
            isValidating = false
            if (asyncError != null) {
                error = asyncError
                return false
            }
        }
        return true
    }
}

package ir.amirhesambandegan.easify_form

import androidx.compose.runtime.*
import androidx.compose.ui.focus.FocusRequester
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Represents a single field within a form.
 */
class FormField(
    val key: String,
    val initialValue: String = "",
    val rules: List<ValidationRule> = emptyList(),
    val asyncValidator: (suspend (String) -> String?)? = null,
    val inputFilter: ((String) -> String)? = null
) {
    var value by mutableStateOf(initialValue)
        set(newValue) {
            val filtered = inputFilter?.invoke(newValue) ?: newValue
            field = filtered
            isDirty = filtered != initialValue
            error = null // Clear error on typing
        }
        
    var error by mutableStateOf<String?>(null)
    var isTouched by mutableStateOf(false)
    var isDirty by mutableStateOf(false)
    var isValidating by mutableStateOf(false)
    
    val focusRequester = FocusRequester()

    /**
     * Validates the field against its rules.
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
     * Validates sync rules and then runs the async validator if provided.
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

package ir.amirhesambandegan.easify_form

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Represents a single reactive field within a Compose form, holding value, error, and validation logic.
 */
class FormField(
    val key: String,
    val initialValue: String = "",
    val rules: List<ValidationRule> = emptyList(),
    val asyncValidator: (suspend (String) -> String?)? = null,
    val inputFilter: ((String) -> String)? = null,
    private val asyncDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    internal var onValueChanged: (() -> Unit)? = null

    private val _value = mutableStateOf(initialValue)
    var value: String
        get() = _value.value
        set(newValue) {
            val filtered = inputFilter?.invoke(newValue) ?: newValue
            _value.value = filtered
            isDirty = filtered != initialValue
            error = null
            onValueChanged?.invoke()
        }

    var error by mutableStateOf<String?>(null)
    var isTouched by mutableStateOf(false)
    var isDirty by mutableStateOf(false)
    var isValidating by mutableStateOf(false)

    val focusRequester = FocusRequester()

    /**
     * Synchronously validates the field against [rules].
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
     * Validates synchronous rules first, then executes [asyncValidator] if configured.
     */
    suspend fun validateAsync(): Boolean {
        if (!validate()) return false

        val validator = asyncValidator ?: return true
        isValidating = true
        return try {
            val asyncError = withContext(asyncDispatcher) { validator.invoke(value) }
            if (asyncError != null) {
                error = asyncError
                false
            } else {
                true
            }
        } catch (e: CancellationException) {
            throw e
        } finally {
            isValidating = false
        }
    }

    /**
     * Resets value, touched, dirty, and error flags back to initial defaults.
     */
    fun reset() {
        _value.value = initialValue
        error = null
        isTouched = false
        isDirty = false
        isValidating = false
        onValueChanged?.invoke()
    }
}

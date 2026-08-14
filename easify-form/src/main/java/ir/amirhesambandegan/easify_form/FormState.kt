package ir.amirhesambandegan.easify_form

import androidx.compose.runtime.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Manages the state and validation of a collection of [FormField]s.
 */
class FormState(val fields: List<FormField>) {
    
    private val _isValid = MutableStateFlow(false)
    /**
     * A [StateFlow] that emits true when all fields in the form are valid.
     */
    val isValid: StateFlow<Boolean> = _isValid.asStateFlow()

    init {
        updateFormValidity()
    }

    /**
     * Returns a [FormField] by its [key].
     */
    fun getField(key: String): FormField? = fields.find { it.key == key }

    /**
     * Validates all fields and updates the overall form validity.
     * @return True if all fields are valid.
     */
    fun validateAll(): Boolean {
        val allValid = fields.all { it.validate() }
        updateFormValidity()
        return allValid
    }

    /**
     * Updates the [isValid] state based on individual field validators.
     */
    fun updateFormValidity() {
        _isValid.value = fields.all { it.validator(it.value) }
    }
}


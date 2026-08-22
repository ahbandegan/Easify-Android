package ir.amirhesambandegan.easify_form

import android.content.Context
import androidx.compose.runtime.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONObject

/**
 * Manages the state, data, and validation of a collection of [FormField]s.
 *
 * @property fields A list of all the fields registered in this form.
 */
class FormState(val fields: List<FormField>) {
    
    private val _isValid = MutableStateFlow(false)
    
    /** 
     * A state flow representing the overall validity of the form. 
     * True if all fields satisfy their rules, false otherwise.
     */
    val isValid: StateFlow<Boolean> = _isValid.asStateFlow()

    /** 
     * True if any field in the form has been modified from its initial value. 
     */
    val isDirty: Boolean
        get() = fields.any { it.isDirty }

    init {
        updateFormValidity()
    }

    /**
     * Retrieves a [FormField] by its unique key.
     *
     * @param key The key of the field to retrieve.
     * @return The [FormField] if found, or null if not.
     */
    fun getField(key: String): FormField? = fields.find { it.key == key }

    /**
     * Validates all fields synchronously and auto-focuses the first field that fails validation.
     *
     * @return `true` if all fields are valid, `false` otherwise.
     */
    fun validateAll(): Boolean {
        var firstErrorField: FormField? = null
        val allValid = fields.all { field ->
            val valid = field.validate()
            if (!valid && firstErrorField == null) firstErrorField = field
            valid
        }
        
        if (firstErrorField != null) {
            try {
                firstErrorField?.focusRequester?.requestFocus()
            } catch (e: Exception) { }
        }
        
        updateFormValidity()
        return allValid
    }
    
    /**
     * Runs both synchronous and asynchronous validation for all fields, 
     * auto-focusing on the first field that encounters an error.
     *
     * @return `true` if all fields pass all validations, `false` otherwise.
     */
    suspend fun validateAllAsync(): Boolean {
        var firstErrorField: FormField? = null
        var allValid = true
        for (field in fields) {
            val valid = field.validateAsync()
            if (!valid) {
                if (firstErrorField == null) firstErrorField = field
                allValid = false
            }
        }
        
        if (firstErrorField != null) {
            try {
                firstErrorField?.focusRequester?.requestFocus()
            } catch (e: Exception) { }
        }
        
        updateFormValidity()
        return allValid
    }

    /**
     * Updates the [_isValid] state based on the current validation status of all fields.
     */
    fun updateFormValidity() {
        _isValid.value = fields.all { field -> field.rules.all { it.check(field.value) } }
    }

    /**
     * Extracts all form data into a Map, linking field keys to their current string values.
     *
     * @return A map of field keys to values.
     */
    fun toMap(): Map<String, String> {
        return fields.associate { it.key to it.value }
    }

    /**
     * Populates form fields from a provided Map of data.
     *
     * @param data A map containing field keys and their corresponding values to set.
     */
    fun populate(data: Map<String, String>) {
        fields.forEach { field ->
            data[field.key]?.let { field.value = it }
        }
    }
    
    /**
     * Auto-saves the current form state to SharedPreferences as a JSON draft.
     *
     * @param context The Android context.
     * @param formId A unique identifier for this specific form's draft.
     */
    fun saveDraft(context: Context, formId: String) {
        val prefs = context.getSharedPreferences("easify_form_drafts", Context.MODE_PRIVATE)
        val json = JSONObject(toMap()).toString()
        prefs.edit().putString(formId, json).apply()
    }
    
    /**
     * Restores the form state from a previously saved draft in SharedPreferences.
     *
     * @param context The Android context.
     * @param formId The unique identifier of the saved draft.
     */
    fun restoreDraft(context: Context, formId: String) {
        val prefs = context.getSharedPreferences("easify_form_drafts", Context.MODE_PRIVATE)
        val jsonStr = prefs.getString(formId, null) ?: return
        try {
            val json = JSONObject(jsonStr)
            val map = mutableMapOf<String, String>()
            json.keys().forEach { key ->
                map[key] = json.getString(key)
            }
            populate(map)
        } catch (e: Exception) {}
    }
}

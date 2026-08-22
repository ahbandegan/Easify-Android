package ir.amirhesambandegan.easify_form

import android.content.Context
import androidx.compose.runtime.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONObject

/**
 * Manages the state and validation of a collection of [FormField]s.
 */
class FormState(val fields: List<FormField>) {
    
    private val _isValid = MutableStateFlow(false)
    val isValid: StateFlow<Boolean> = _isValid.asStateFlow()

    /** True if any field in the form has been modified from its initial value. */
    val isDirty: Boolean
        get() = fields.any { it.isDirty }

    init {
        updateFormValidity()
    }

    fun getField(key: String): FormField? = fields.find { it.key == key }

    /** Validates all fields and auto-focuses the first field with an error. */
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
    
    /** Asynchronous validation for all fields, auto-focusing on errors. */
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

    fun updateFormValidity() {
        _isValid.value = fields.all { field -> field.rules.all { it.check(field.value) } }
    }

    /** Extracts all form data into a Map. */
    fun toMap(): Map<String, String> {
        return fields.associate { it.key to it.value }
    }

    /** Populates form fields from a Map. */
    fun populate(data: Map<String, String>) {
        fields.forEach { field ->
            data[field.key]?.let { field.value = it }
        }
    }
    
    /** Auto-saves the current form state to SharedPreferences. */
    fun saveDraft(context: Context, formId: String) {
        val prefs = context.getSharedPreferences("easify_form_drafts", Context.MODE_PRIVATE)
        val json = JSONObject(toMap()).toString()
        prefs.edit().putString(formId, json).apply()
    }
    
    /** Restores the form state from a previously saved draft. */
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

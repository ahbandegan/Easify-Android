package ir.amirhesambandegan.easify_form

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Manages the state, data, and validation of a collection of [FormField]s.
 */
class FormState(val fields: List<FormField>) : FormFieldLookup {

    private val _isValid = MutableStateFlow(false)

    /**
     * Live validity state updated automatically whenever field values change or validate.
     */
    val isValid: StateFlow<Boolean> = _isValid.asStateFlow()

    /**
     * True if any field in the form has changed from its initial value.
     */
    val isDirty: Boolean
        get() = fields.any { it.isDirty }

    init {
        fields.forEach { field ->
            field.onValueChanged = { updateFormValidity() }
        }
        updateFormValidity()
    }

    fun getField(key: String): FormField? = fields.find { it.key == key }

    override fun getFieldValue(key: String): String? = getField(key)?.value

    /**
     * Validates every field synchronously without short-circuiting and requests focus on the first invalid field.
     */
    fun validateAll(): Boolean {
        var firstErrorField: FormField? = null
        var allValid = true
        for (field in fields) {
            val valid = field.validate()
            if (!valid) {
                allValid = false
                if (firstErrorField == null) firstErrorField = field
            }
        }

        if (firstErrorField != null) {
            runCatching { firstErrorField.focusRequester.requestFocus() }
        }

        updateFormValidity()
        return allValid
    }

    /**
     * Runs both synchronous and asynchronous validation for every field without short-circuiting.
     */
    suspend fun validateAllAsync(): Boolean {
        var firstErrorField: FormField? = null
        var allValid = true
        for (field in fields) {
            val valid = field.validateAsync()
            if (!valid) {
                allValid = false
                if (firstErrorField == null) firstErrorField = field
            }
        }

        if (firstErrorField != null) {
            runCatching { firstErrorField.focusRequester.requestFocus() }
        }

        updateFormValidity()
        return allValid
    }

    fun updateFormValidity() {
        _isValid.value = fields.all { field -> field.rules.all { it.check(field.value) } }
    }

    fun clearErrors() {
        fields.forEach { it.error = null }
    }

    fun resetAll() {
        fields.forEach { it.reset() }
        updateFormValidity()
    }

    fun toMap(): Map<String, String> = fields.associate { it.key to it.value }

    fun populate(data: Map<String, String>) {
        fields.forEach { field ->
            data[field.key]?.let { field.value = it }
        }
        updateFormValidity()
    }

    /**
     * Serializes the form values as a multiplatform JSON draft string.
     */
    fun exportDraftJson(): String = draftJson.encodeToString(toMap())

    /**
     * Restores form values from a JSON string created by [exportDraftJson].
     * @return `true` if parsing succeeded, `false` otherwise.
     */
    fun importDraftJson(json: String): Boolean {
        return runCatching {
            val map = draftJson.decodeFromString<Map<String, String>>(json)
            populate(map)
        }.isSuccess
    }

    private companion object {
        val draftJson = Json { ignoreUnknownKeys = true }
    }
}

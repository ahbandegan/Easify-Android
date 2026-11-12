package ir.amirhesambandegan.easify_form

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

/**
 * Pure multiplatform form field suitable for Kotlin Console, CLI, backend validation, or shared KMP logic.
 */
class CoreFormField(
    val key: String,
    val initialValue: String = "",
    val rules: List<ValidationRule> = emptyList(),
    val asyncValidator: (suspend (String) -> String?)? = null,
    val inputFilter: ((String) -> String)? = null
) {
    internal var onFieldChanged: (() -> Unit)? = null

    private var _value: String = inputFilter?.invoke(initialValue) ?: initialValue
    var value: String
        get() = _value
        set(newValue) {
            val filtered = inputFilter?.invoke(newValue) ?: newValue
            _value = filtered
            isDirty = filtered != initialValue
            error = null
            onFieldChanged?.invoke()
        }

    var error: String? = null
    var isTouched: Boolean = false
    var isDirty: Boolean = false
    var isValidating: Boolean = false

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

    suspend fun validateAsync(): Boolean {
        if (!validate()) return false
        val validator = asyncValidator ?: return true
        isValidating = true
        return try {
            val asyncError = withContext(Dispatchers.Default) { validator.invoke(value) }
            if (asyncError != null) {
                error = asyncError
                false
            } else {
                true
            }
        } finally {
            isValidating = false
        }
    }

    fun clearError() {
        error = null
    }

    fun reset() {
        _value = inputFilter?.invoke(initialValue) ?: initialValue
        error = null
        isTouched = false
        isDirty = false
        onFieldChanged?.invoke()
    }
}

/**
 * Pure multiplatform form state manager implementing [FormFieldLookup].
 */
class CoreFormState(val fields: List<CoreFormField>) : FormFieldLookup {

    private val _isValid = MutableStateFlow(false)
    val isValid: StateFlow<Boolean> = _isValid.asStateFlow()

    val isDirty: Boolean
        get() = fields.any { it.isDirty }

    init {
        fields.forEach { it.onFieldChanged = { updateFormValidity() } }
        updateFormValidity()
    }

    fun getField(key: String): CoreFormField? = fields.find { it.key == key }

    override fun getFieldValue(key: String): String? = getField(key)?.value

    fun validateAll(): Boolean {
        var allValid = true
        for (field in fields) {
            if (!field.validate()) {
                allValid = false
            }
        }
        updateFormValidity()
        return allValid
    }

    suspend fun validateAllAsync(): Boolean {
        var allValid = true
        for (field in fields) {
            if (!field.validateAsync()) {
                allValid = false
            }
        }
        updateFormValidity()
        return allValid
    }

    fun updateFormValidity() {
        _isValid.value = fields.all { field -> field.rules.all { it.check(field.value) } }
    }

    fun resetAll() {
        fields.forEach { it.reset() }
        updateFormValidity()
    }

    fun clearErrors() {
        fields.forEach { it.clearError() }
    }

    fun touchAll() {
        fields.forEach { it.isTouched = true }
    }

    fun toMap(): Map<String, String> = fields.associate { it.key to it.value }

    fun populate(data: Map<String, String>) {
        fields.forEach { field ->
            data[field.key]?.let { field.value = it }
        }
    }
}

/**
 * Kotlin DSL builder for constructing [CoreFormState] in pure multiplatform / Kotlin Console apps.
 */
class CoreFormBuilder {
    private val fields = mutableListOf<CoreFormField>()

    fun field(
        key: String,
        initialValue: String = "",
        rules: List<ValidationRule> = emptyList(),
        asyncValidator: (suspend (String) -> String?)? = null,
        inputFilter: ((String) -> String)? = null
    ) {
        fields.add(CoreFormField(key, initialValue, rules, asyncValidator, inputFilter))
    }

    fun build(): CoreFormState = CoreFormState(fields)
}

fun buildForm(builder: CoreFormBuilder.() -> Unit): CoreFormState =
    CoreFormBuilder().apply(builder).build()

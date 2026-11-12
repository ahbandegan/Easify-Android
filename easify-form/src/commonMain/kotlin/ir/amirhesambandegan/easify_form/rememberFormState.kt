package ir.amirhesambandegan.easify_form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/**
 * Builder DSL used to declare fields and build a [FormState].
 */
class FormBuilder {
    private val fields = mutableListOf<FormField>()

    fun field(
        key: String,
        initialValue: String = "",
        rules: List<ValidationRule> = emptyList(),
        asyncValidator: (suspend (String) -> String?)? = null,
        inputFilter: ((String) -> String)? = null
    ) {
        fields.add(FormField(key, initialValue, rules, asyncValidator, inputFilter))
    }

    fun build(): FormState = FormState(fields)
}

/**
 * Builds and remembers a [FormState] across recompositions.
 */
@Composable
fun rememberForm(
    vararg keys: Any?,
    builder: FormBuilder.() -> Unit
): FormState {
    return remember(*keys) {
        FormBuilder().apply(builder).build()
    }
}

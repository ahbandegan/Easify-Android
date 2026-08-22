package ir.amirhesambandegan.easify_form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

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
 * A beautiful Kotlin DSL to build forms in Compose.
 */
@Composable
fun rememberForm(builder: FormBuilder.() -> Unit): FormState {
    return remember {
        val formBuilder = FormBuilder()
        formBuilder.builder()
        formBuilder.build()
    }
}

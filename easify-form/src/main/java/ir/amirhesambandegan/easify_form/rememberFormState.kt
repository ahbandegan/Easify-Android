package ir.amirhesambandegan.easify_form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/**
 * A builder class used to construct a [FormState] instance declaratively.
 */
class FormBuilder {
    private val fields = mutableListOf<FormField>()

    /**
     * Registers a new field in the form.
     *
     * @param key A unique identifier for the field.
     * @param initialValue The starting value of the field. Defaults to an empty string.
     * @param rules A list of synchronous [ValidationRule]s.
     * @param asyncValidator An optional suspend function for backend or long-running validation.
     * @param inputFilter An optional function to sanitize or format user input as it is typed.
     */
    fun field(
        key: String,
        initialValue: String = "",
        rules: List<ValidationRule> = emptyList(),
        asyncValidator: (suspend (String) -> String?)? = null,
        inputFilter: ((String) -> String)? = null
    ) {
        fields.add(FormField(key, initialValue, rules, asyncValidator, inputFilter))
    }

    /**
     * Builds and returns the final [FormState] containing all registered fields.
     *
     * @return The constructed [FormState].
     */
    fun build(): FormState = FormState(fields)
}

/**
 * A beautiful Kotlin DSL to build and remember form state in Compose.
 * Uses a builder pattern to define the fields and rules for the form.
 *
 * @param builder A lambda with [FormBuilder] receiver to declare the form configuration.
 * @return A remembered instance of [FormState] that survives recomposition.
 */
@Composable
fun rememberForm(builder: FormBuilder.() -> Unit): FormState {
    return remember {
        val formBuilder = FormBuilder()
        formBuilder.builder()
        formBuilder.build()
    }
}

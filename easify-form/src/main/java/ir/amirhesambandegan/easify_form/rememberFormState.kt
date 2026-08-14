package ir.amirhesambandegan.easify_form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/**
 * Creates and remembers a [FormState] instance.
 */
@Composable
fun rememberFormState(fields: List<FormField>): FormState {
    return remember { FormState(fields) }
}

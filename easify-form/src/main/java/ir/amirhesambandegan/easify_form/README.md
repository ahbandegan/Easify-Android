# easify_form

Reactive form state management for Jetpack Compose.

## Features
- **Manage Multiple Fields**: Easily handle a collection of form fields using unique keys.
- **Reactive State**: `FormField` provides reactive state for `value`, `error`, and `isTouched`.
- **Validation**: Customizable validation logic per field with support for aggregate form validity.
- **Compose Integration**: Simple `rememberFormState` hook for use in Composables.

## Usage

```kotlin
// Define fields
val emailField = FormField(
    key = "email",
    validator = { it.contains("@") }
)
val passwordField = FormField(
    key = "password",
    validator = { it.length >= 6 }
)

// Remember form state
val formState = rememberFormState(listOf(emailField, passwordField))
val isValid by formState.isValid.collectAsState()

Column {
    TextField(
        value = emailField.value,
        onValueChange = { 
            emailField.value = it
            formState.updateFormValidity() 
        },
        isError = emailField.error != null,
        label = { Text("Email") }
    )
    
    if (emailField.error != null) {
        Text(text = emailField.error!!, color = Color.Red)
    }

    Button(
        onClick = { 
            if (formState.validateAll()) {
                // Submit form
            }
        },
        enabled = isValid
    ) {
        Text("Submit")
    }
}
```

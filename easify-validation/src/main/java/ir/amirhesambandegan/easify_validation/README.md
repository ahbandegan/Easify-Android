# Easify Validation

Easify Validation provides a set of common regular expressions you can reuse in forms, login screens, and input validation flows.

## What it offers
- `ValidationRegexes.emailRegex`
- `ValidationRegexes.mobileRegex`
- `ValidationRegexes.nationalIdRegex`
- `ValidationRegexes.passwordStrongRegex`
- `ValidationRegexes.postalCodeRegex`
- `ValidationRegexes.landlineRegex`
- `ValidationRegexes.urlRegex`
- `ValidationRegexes.usernameRegex`
- `ValidationRegexes.persianTextRegex`
- `ValidationRegexes.englishTextRegex`

## Usage example

```kotlin
import ir.amirhesambandegan.easify_validation.ValidationRegexes

val email = "user@example.com"
val isEmailValid = email.matches(ValidationRegexes.emailRegex)

val nationalId = "1234567890"
val isNationalIdValid = nationalId.matches(ValidationRegexes.nationalIdRegex)
```

## Notes
- These regexes are useful for quick validation without rewriting common patterns.
- Combine them with your own form state logic or validation UI.

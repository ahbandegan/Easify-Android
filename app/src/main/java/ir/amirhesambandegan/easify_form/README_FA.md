# easify_form

مدیریت وضعیت فرم واکنش‌گرا (Reactive) برای Jetpack Compose.

## ویژگی‌ها
- **مدیریت چندین فیلد**: مدیریت آسان مجموعه‌ای از فیلدهای فرم با استفاده از کلیدهای منحصر به فرد.
- **وضعیت واکنش‌گرا**: کلاس `FormField` وضعیت‌های واکنش‌گرا برای `value` (مقدار)، `error` (خطا) و `isTouched` (لمس شده) فراهم می‌کند.
- **اعتبارسنجی**: منطق اعتبارسنجی قابل تنظیم برای هر فیلد با پشتیبانی از وضعیت اعتبار کلی فرم.
- **یکپارچگی با Compose**: هوک ساده `rememberFormState` برای استفاده در Composableها.

## نحوه استفاده

```kotlin
// تعریف فیلدها
val emailField = FormField(
    key = "email",
    validator = { it.contains("@") }
)
val passwordField = FormField(
    key = "password",
    validator = { it.length >= 6 }
)

// به خاطر سپردن وضعیت فرم
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
        label = { Text("ایمیل") }
    )
    
    if (emailField.error != null) {
        Text(text = emailField.error!!, color = Color.Red)
    }

    Button(
        onClick = { 
            if (formState.validateAll()) {
                // ارسال فرم
            }
        },
        enabled = isValid
    ) {
        Text("ارسال")
    }
}
```

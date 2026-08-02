# مدیریت فایل (Easify File)

ماژول مدرن انتخاب فایل و رسانه برای Jetpack Compose با استفاده از `ActivityResultContracts`.

## ویژگی‌های کلیدی

- **انتخاب رسانه**: پشتیبانی از انتخاب تصویر، ویدیو یا هر دو.
- **انتخاب تکی/چندتایی**: دارای کامپوننت‌های مجزا برای انتخاب یک یا چند فایل.
- **مدیریت خودکار دسترسی‌ها**: درخواست خودکار مجوزهای حافظه/رسانه متناسب با نسخه اندروید (API Level).
- **بهینه‌سازی شده برای Compose**: کامپوننت‌های کاملاً Declarative.

## نحوه استفاده

### ۱. انتخاب تکی فایل
از `FilePicker` برای انتخاب یک فایل توسط کاربر استفاده کنید.

```kotlin
var selectedUri by remember { mutableStateOf<Uri?>(null) }
var showPicker by remember { mutableStateOf(false) }

if (showPicker) {
    FilePicker(
        mediaType = MediaType.ImageOnly,
        onResult = { uri ->
            selectedUri = uri
            showPicker = false
        }
    )
}

Button(onClick = { showPicker = true }) {
    Text("انتخاب تصویر")
}
```

### ۲. انتخاب چندتایی فایل
از `MultipleFilePicker` برای انتخاب چندین فایل با تعیین سقف تعداد استفاده کنید.

```kotlin
var selectedUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
var showMultiplePicker by remember { mutableStateOf(false) }

if (showMultiplePicker) {
    MultipleFilePicker(
        mediaType = MediaType.ImageAndVideo,
        maxItem = 5,
        onResult = { uris ->
            selectedUris = uris
            showMultiplePicker = false
        }
    )
}

Button(onClick = { showMultiplePicker = true }) {
    Text("انتخاب حداکثر ۵ فایل")
}
```

## اجزا و انوع داده

- `FilePicker`: کامپوننت برای انتخاب تکی رسانه.
- `MultipleFilePicker`: کامپوننت برای انتخاب چندتایی رسانه.
- `MediaType`: مشخص‌کننده نوع رسانه مجاز:
    - `ImageOnly` (فقط تصویر)
    - `VideoOnly` (فقط ویدیو)
    - `ImageAndVideo` (تصویر و ویدیو)

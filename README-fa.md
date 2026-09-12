# EasifyAndroid 🚀

[![](https://jitpack.io/v/ahbandegan/Easify-Android.svg)](https://jitpack.io/#ahbandegan/Easify-Android)

[**مستندات رسمی و کامل**](https://ahbandegan.github.io/Easify-Android-docuement) | [English Documentation](./README.md) | **مستندات فارسی**

**EasifyAndroid** یک کتابخانه قدرتمند، ماژولار و آماده‌ی استفاده برای اندروید است که برای حذف کدهای تکراری و ساده‌سازی کارهای پیچیده طراحی شده است. 

از نسخه ۲.۰.۰ به بعد، این کتابخانه **۱۰۰٪ ماژولار** شده است! شما دیگر نیازی ندارید کل کتابخانه را به پروژه خود اضافه کنید؛ می‌توانید دقیقاً فقط بخش‌هایی که نیاز دارید را انتخاب کنید.

---

## 🚀 شروع سریع (JitPack)

### ۱. اضافه کردن مخزن JitPack
این کد را در انتهای بلاک `repositories` در فایل `settings.gradle.kts` پروژه خود اضافه کنید:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

### ۲. اضافه کردن وابستگی‌ها (Dependencies)

#### 🌟 روش اول: اضافه کردن کل امکانات به صورت یکجا
اگر می‌خواهید به تمام قابلیت‌های این کتابخانه به صورت همزمان دسترسی داشته باشید، کافیست پکیج اصلی را اضافه کنید:
```kotlin
dependencies {
    implementation("com.github.ahbandegan.Easify-Android:easify-android:2.2.0")
}
```

#### 🎯 روش دوم: اضافه کردن ماژول‌های دلخواه
اگر می‌خواهید حجم اپلیکیشن شما کم بماند و فقط از چند قابلیت خاص استفاده کنید، می‌توانید از لیست پایین، فقط ماژول‌های مورد نیازتان را اضافه کنید:
```kotlin
dependencies {
    // به جای <module-name> نام یکی از ماژول‌های لیست زیر را قرار دهید
    implementation("com.github.ahbandegan.Easify-Android:<module-name>:2.2.0")
}
```

> **نکته:** سورس کدهای اصلی کاتلین به همراه نسخه JitPack منتشر می‌شوند! با فشردن دکمه `Ctrl` و کلیک روی توابع این کتابخانه در اندروید استودیو، به جای فایل‌های کامپایل شده `.class`، دقیقاً سورس کدهای واقعی کاتلین را مشاهده خواهید کرد.

---

## 📦 راهنمای ماژول‌ها

در این بخش راهنمای دقیق و مثال‌های کدهای تمامی ۲۰ ماژول قرار گرفته است. برای مشاهده روی نام هر ماژول کلیک کنید!

<details>
<summary><b>1. 🔐 easify-security (رمزنگاری و محافظت از صفحه)</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-security:2.2.0")`

جلوگیری از اسکرین‌شات و ضبط صفحه نمایش، به همراه رمزنگاری پیشرفته سخت‌افزاری AES-GCM.

```kotlin
// جلوگیری از اسکرین‌شات در اکتیویتی/کامپوزبل فعلی
val shield = rememberScreenShield()
shield.enable()

// رمزنگاری و رمزگشایی آسان متن
val encrypted = EasifyCrypto.encrypt("my_secret_text")
val decrypted = EasifyCrypto.decrypt(encrypted)
```
</details>

<details>
<summary><b>2. 🛡️ easify-permission (دسترسی‌ها در کامپوز)</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-permission:2.2.0")`

مدیریت کاملاً خودکار دسترسی‌ها (Permissions) در Jetpack Compose بدون نیاز به کدهای تکراری.

```kotlin
val permissionLauncher = rememberEasifyPermission(
    permission = Manifest.permission.CAMERA,
    onGranted = { /* دوربین آماده است */ },
    onDenied = { /* نمایش دلیل نیاز به دسترسی */ }
)

Button(onClick = { permissionLauncher.launch() }) {
    Text("درخواست دوربین")
}
```
</details>

<details>
<summary><b>3. 👆 easify-biometric (اثر انگشت و تشخیص چهره)</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-biometric:2.2.0")`

راه‌اندازی فوق سریع احراز هویت بیومتریک در برنامه.

```kotlin
val biometric = rememberBiometricLauncher(
    title = "تایید هویت",
    subtitle = "برای ادامه از اثر انگشت استفاده کنید",
    onSuccess = { /* ورود موفق */ },
    onError = { error -> /* مدیریت خطا */ }
)

Button(onClick = { biometric.authenticate() }) {
    Text("ورود با اثر انگشت")
}
```
</details>

<details>
<summary><b>4. 🌐 easify-network (مدیریت Ktor و آپلود فایل)</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-network:2.2.0")`

نسخه بهینه‌شده و امن Ktor 3.x با مدیریت خودکار توکن‌ها و آپلود راحت فایل‌ها همراه با نمایش درصد پیشرفت.

```kotlin
// یک درخواست GET ساده
val result: EasifyResult<UserResponse> = EasifyNetwork.get("https://api.example.com/user")

// آپلود فایل با نمایش درصد پیشرفت
EasifyNetwork.uploadMultipart(
    url = "https://api.example.com/upload",
    file = myFile,
    onProgress = { percent -> println("درصد آپلود: $percent%") }
)
```
</details>

<details>
<summary><b>5. 🇮🇷 easify-persian (تاریخ شمسی و اعداد فارسی)</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-persian:2.2.0")`

تبدیل تقویم میلادی به شمسی، تبدیل اعداد انگلیسی به فارسی و حروف‌نویسی اعداد.

```kotlin
// تبدیل تاریخ به شمسی
val jalali = Date().toJalaliString() // خروجی: 1403/05/24

// تبدیل اعداد به فرمت فارسی
val text = "قیمت: 1500".toPersianDigits() // خروجی: قیمت: ۱۵۰۰

// تبدیل عدد به حروف فارسی
val words = 1500000L.toPersianWords() // خروجی: یک میلیون و پانصد هزار
```
</details>

<details>
<summary><b>6. 💳 easify-fintech (بانک‌ها و اعتبارسنجی ایران)</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-fintech:2.2.0")`

اعتبارسنجی کد ملی، شناسایی نام بانک از روی کارت بانکی و اعتبارسنجی شماره شبا.

```kotlin
// اعتبارسنجی کد ملی
val isValid = "0012345678".isValidIranianNationalId()

// دریافت نام بانک از روی شماره کارت
val bankName = BankCardUtils.getBankName("6037991234567890") // خروجی: Bank Melli

// اعتبارسنجی شماره شبا
val isValidSheba = "IR123...".isValidSheba()
```
</details>

<details>
<summary><b>7. 📳 easify-sensor (سنسورهای سخت‌افزاری)</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-sensor:2.2.0")`

دسترسی واکنش‌گرا و ساده به تمام سنسورهای گوشی (شتاب‌سنج، مجاورت و غیره).

```kotlin
val accelerometer by rememberAccelerometerState()
Text("X: ${accelerometer.x}, Y: ${accelerometer.y}, Z: ${accelerometer.z}")

val isClose by rememberProximityState()
if (isClose) { Text("گوشی نزدیک گوش شماست!") }
```
</details>

<details>
<summary><b>8. 🔵 easify-bluetooth (اسکنر بلوتوث)</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-bluetooth:2.2.0")`

اسکنر مدرن BLE که پیچیدگی‌های دسترسی بلوتوث در اندروید ۱۲ به بالا را مدیریت می‌کند.

```kotlin
val btScanner = rememberBluetoothScanner()
val devices by btScanner.devices.collectAsState(initial = emptyList())

Button(onClick = { btScanner.startScanning() }) {
    Text("اسکن بلوتوث")
}
```
</details>

<details>
<summary><b>9. 📍 easify-location (موقعیت‌یابی زنده)</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-location:2.2.0")`

ردیابی موقعیت مکانی زنده تنها با یک خط کد و مدیریت خودکار دسترسی‌ها.

```kotlin
val locationState by rememberLocationTracker()

locationState?.let { loc ->
    Text("Lat: ${loc.latitude}, Lng: ${loc.longitude}")
}
```
</details>

<details>
<summary><b>10. 🎨 easify-ui (افکت‌ها و ابزارهای رابط کاربری)</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-ui:2.2.0")`

ابزارهایی مانند افکت کلیک به سبک iOS، افکت بارگذاری Shimmer و بستن خودکار کیبورد.

```kotlin
// افکت کلیک ارتجاعی (Bounce)
Card(modifier = Modifier.bounceClick { /* اکشن */ }) { ... }

// افکت Shimmer (در حال بارگذاری)
Box(modifier = Modifier.size(100.dp).shimmer(isLoading = true))

// بستن کیبورد با لمس بیرون کادر
Column(modifier = Modifier.hideKeyboardOnTapOutside()) { ... }
```
</details>

<details>
<summary><b>11. 📝 easify-form (مدیریت فرم‌ها)</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-form:2.2.0")`

مدیریت وضعیت فرم‌ها در کامپوز با پشتیبانی از اعتبارسنجی خودکار.

```kotlin
val form = rememberEasifyForm()
val emailState = form.textField(name = "email", validators = listOf(EmailValidator()))

OutlinedTextField(
    value = emailState.value,
    onValueChange = emailState::onChange,
    isError = emailState.hasError
)

Button(onClick = { if(form.validate()) { /* ثبت اطلاعات */ } }) { Text("ارسال") }
```
</details>

<details>
<summary><b>12. 📳 easify-haptic (بازخورد لرزشی هوشمند)</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-haptic:2.2.0")`

ایجاد بازخوردهای لرزشی معنادار (موفقیت، خطا، کلیک) به سادگی هر چه تمام‌تر.

```kotlin
val haptic = LocalHapticFeedback.current

Button(onClick = { 
    // ایجاد لرزش مخصوص پیام موفقیت
    haptic.performEasifyHaptic(EasifyHapticType.SUCCESS) 
}) {
    Text("تکمیل وظیفه")
}
```
</details>

<details>
<summary><b>13. 🔔 easify-notification (نوتیفیکیشن‌های ساده)</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-notification:2.2.0")`

ساخت و مدیریت آسان کانال‌های نوتیفیکیشن و نمایش اعلان‌ها.

```kotlin
EasifyNotification.show(
    context = context,
    title = "دانلود کامل شد",
    message = "فایل شما با موفقیت دریافت شد.",
    channelId = "downloads"
)
```
</details>

<details>
<summary><b>14. 📂 easify-file (انتخابگر فایل و تصویر)</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-file:2.2.0")`

رابط کاربری بصری و ساده برای انتخاب عکس، ویدیو یا فایل.

```kotlin
val filePicker = rememberEasifyFilePicker(
    type = FileType.IMAGE,
    onResult = { uri -> /* عکس انتخاب شده */ }
)

Button(onClick = { filePicker.launch() }) { Text("انتخاب عکس") }
```
</details>

<details>
<summary><b>15. 🗄️ easify-storage (ذخیره‌سازی امن DataStore)</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-storage:2.2.0")`

ترکیبی از Preferences DataStore و `easify-security` برای ذخیره کاملاً امن و رمزنگاری‌شده داده‌ها.

```kotlin
val storage = rememberEasifyStorage()

// داده‌ها به صورت خودکار رمزنگاری می‌شوند
storage.saveSecure("auth_token", "jwt_abc123")

// هنگام دریافت اطلاعات رمزگشایی می‌شوند
val token = storage.getSecure("auth_token")
```
</details>

<details>
<summary><b>16. ⚙️ easify-context (ابزارهای سیستم و اینتنت)</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-context:2.2.0")`

ابزارهایی برای کپی در کلیپ‌بورد، اشتراک‌گذاری متن و باز کردن تنظیمات سیستم.

```kotlin
context.copyToClipboard("متن برای کپی")
context.shareText("این برنامه را امتحان کن!")
context.openAppSettings()
```
</details>

<details>
<summary><b>17. 🔄 easify-lifecycle (چرخه حیات در کامپوز)</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-lifecycle:2.2.0")`

پوشش‌های آسان برای استفاده از LifeCycle درون کدهای Jetpack Compose.

```kotlin
useLifecycleEvent { event ->
    when(event) {
        Lifecycle.Event.ON_RESUME -> { /* برنامه فعال شد */ }
        Lifecycle.Event.ON_PAUSE -> { /* برنامه در پس‌زمینه رفت */ }
        else -> {}
    }
}
```
</details>

<details>
<summary><b>18. ✅ easify-validation (اعتبارسنجی با Regex)</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-validation:2.2.0")`

مجموعه‌ای از الگوهای رایج برای اعتبارسنجی عبارات متنی.

```kotlin
val emailValid = "test@test.com".isEmailValid()
val phoneValid = "+989123456789".isPhoneNumberValid()
val passwordStrong = "Aa!123456".isStrongPassword()
```
</details>

<details>
<summary><b>19. 🖼️ easify-image (پردازش تصاویر)</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-image:2.2.0")`

تغییر اندازه و فشرده‌سازی بسیار ساده عکس‌ها قبل از آپلود.

```kotlin
val optimizedBitmap = originalBitmap.compress(
    maxWidth = 1080,
    quality = 80
)
```
</details>

<details>
<summary><b>20. 🔢 easify-format (قالب‌بندی رشته‌ها)</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-format:2.2.0")`

جداکننده سه‌رقمی قیمت‌ها و تبدیل زمان به فرمت قابل خواندن.

```kotlin
val price = 1500000.toCurrencyFormat() // خروجی: "1,500,000"
val time = 3661000L.formatDuration()   // خروجی: "01:01:01"
```
</details>

<details>
<summary><b>21. 📷 easify-camera (مدیریت دوربین CameraX و اسکنر بارکد/QR)</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-camera:2.2.0")`

پیش‌نمایش آسان دوربین با CameraX، عکاسی و اسکن کدهای QR و بارکد با MLKit در کامپوز.

```kotlin
CameraPreview(
    modifier = Modifier.fillMaxSize(),
    onBarcodeScanned = { barcode ->
        println("بارکد اسکن شد: ${barcode.rawValue}")
    }
)
```
</details>

<details>
<summary><b>22. ⚡ easify-state (معماری کامل Flutter BLoC و Cubit برای Jetpack Compose)</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-state:2.2.0")`

پیاده‌سازی کامل و قدرتمند الگوی محبوب Flutter BLoC در دنیای اندروید و کامپوز. شامل `Bloc`، `Cubit`، `BlocViewModel`، ترنسفورمرهای ۴گانه همزمانی رویدادها (`sequential`, `restartable`, `droppable`, `concurrent`) و اکستنشن‌های شگفت‌انگیز `read` و `watch` در کانتکست.

```kotlin
// ۱. تعریف استیت و رویدادها
data class CounterState(val count: Int = 0) : BlocState
sealed interface CounterEvent : BlocEvent {
    data object Increment : CounterEvent
}

// ۲. ساخت Bloc
class CounterBloc : Bloc<CounterEvent, CounterState>(CounterState(0)) {
    init {
        on<CounterEvent.Increment> { _, emit ->
            emit(state.value.copy(count = state.value.count + 1))
        }
    }
}

// ۳. استفاده آسان در کامپوز با read و watch
@Composable
fun CounterScreen() {
    val state = watch<CounterBloc, CounterState>()

    Column {
        Text("تعداد: ${state.count}")
        Button(onClick = { read<CounterBloc>().add(CounterEvent.Increment) }) {
            Text("افزایش")
        }
    }
}
```
</details>

---

## 🤝 مشارکت
ما از مشارکت شما استقبال می‌کنیم! اگر ایده‌ای برای قابلیت‌های جدید "Easify" دارید، لطفاً با ثبت Issue یا Pull Request به ما اطلاع دهید.

## ⚖️ لایسنس
لایسنس MIT. استفاده شخصی و تجاری کاملاً رایگان است.

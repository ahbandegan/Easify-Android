# EasifyAndroid 🚀

[![](https://jitpack.io/v/ahbandegan/Easify-Android.svg)](https://jitpack.io/#ahbandegan/Easify-Android)

[**مستندات رسمی و کامل**](https://ahbandegan.github.io/Easify-Android-docuement) | [English Documentation](./README.md) | **مستندات فارسی**

**EasifyAndroid** یک مجموعه ابزار جامع، مدرن و ماژولار برای **Kotlin Multiplatform (KMP)**، **Compose Multiplatform (CMP)** و **Android** است که برای حذف کدهای تکراری در رابط کاربری، مدیریت وضعیت (State)، فرم‌ها، شبکه، ابزارهای فارسی و فین‌تک، امنیت و سنسورهای دستگاه طراحی شده است.

از نسخه **۳.۱.۰**، این کتابخانه شامل **۳۲ ماژول مستقل** در چهار لایه زیر است:
- **۹ ماژول Pure Kotlin Multiplatform / Console Core:** بدون هیچ‌گونه وابستگی به Android یا Compose — قابل اجرا در پروژه‌های کنسول JVM/Native/JS/Wasm، سرورهای Ktor، ابزارهای CLI و ماژول‌های مشترک KMP
- **۸ ماژول Compose Multiplatform UI:** سازگار با Android، iOS، Desktop (JVM) و Web (JS / WasmJS)
- **۱۴ ماژول تخصصی Android:** رمزنگاری سخت‌افزاری با **Google Tink AEAD و Streaming AEAD**، Credential Manager، CameraX، ML Kit، بلوتوث BLE، موقعیت‌یابی، سنسورها و بیومتریک
- **۱ ماژول جامع (`easify-android`):** تجمیع تمام ۳۱ ماژول کاربردی برای اندروید

---

## ✨ قابلیت‌های کلیدی نسخه ۳.۱.۰

- **۹ ماژول خالص کاتلین و کنسولی (`-core` و `easify-validation`):** امکان استفاده مستقل از منطق اعتبارسنجی، فرمت‌دهی، تقویم شمسی، فین‌تک ایران، BLoC/Cubit، فرم‌ساز، Ktor Client، PKCE/OTP و ریاضیات مکانی در پروژه‌های غیراندرویدی
- **۸ ماژول رابط کاربری Compose Multiplatform:** اجرای مستقیم کامپوننت‌ها در Android، iOS، Desktop و Web
- **ارتقای `SuspendButton` در `easify-ui`:** پشتیبانی از تغییر شکل دلخواه (`shape: Shape? = null`)، رنگ سفارشی (`colors`) و `contentPadding` همراه با محافظت همزمانی در برابر کلیک‌های رگباری
- **امنیت پیشرفته:** جایگزینی کامل کتابخانه منسوخ `security-crypto` با **Google Tink (`1.24.0`)** (`AES256_SIV` + `AES256_GCM`) در `easify-security` و **Streaming AEAD (`AES256_GCM_HKDF_4KB`)** در `easify-storage`؛ بررسی زمان‌ثابت `state` در OAuth؛ محافظت از ZIP-Slip و هدرهای Multipart؛ و بررسی امن URL در `openBrowser`
- **تکنولوژی‌های روز و بهبود عملکرد:** Kotlin `2.4.21`، AGP `9.5.0` (`compileSdk = 37`)، Gradle `9.9.1`، CMP `1.12.2`، Ktor `3.6.1`، مادیفایرهای مدرن `Modifier.Node` و مدیریت ایمن Structured Concurrency

---

## 🚀 شروع سریع (JitPack)

### ۱. اضافه کردن مخزن JitPack
مخزن JitPack را در فایل `settings.gradle.kts` اضافه کنید:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

### ۲. اضافه کردن وابستگی‌ها (Dependencies)

#### 🌟 روش اول: نصب یکجا برای اندروید
```kotlin
dependencies {
    implementation("com.github.ahbandegan.Easify-Android:easify-android:3.1.0")
}
```

#### 🎯 روش دوم: نصب ماژولار (اندروید، CMP یا کنسول کاتلین)
```kotlin
dependencies {
    // ماژول‌های خالص کاتلین / کنسول / سرور / commonMain
    implementation("com.github.ahbandegan.Easify-Android:easify-validation:3.1.0")
    implementation("com.github.ahbandegan.Easify-Android:easify-persian-core:3.1.0")
    implementation("com.github.ahbandegan.Easify-Android:easify-fintech-core:3.1.0")

    // ماژول‌های رابط کاربری Compose Multiplatform
    implementation("com.github.ahbandegan.Easify-Android:easify-ui:3.1.0")
    implementation("com.github.ahbandegan.Easify-Android:easify-state:3.1.0")

    // ماژول‌های اختصاصی اندروید
    implementation("com.github.ahbandegan.Easify-Android:easify-security:3.1.0")
}
```

> **نکته:** سورس‌کدهای کاتلین همراه با هر پکیج منتشر می‌شوند تا با `Ctrl + Click` در IDE مستقیماً کد اصلی قابل مطالعه باشد.

---

## 💻 نمونه اجرا در پروژه کنسول خالص کاتلین (Pure Kotlin Console)

```kotlin
import ir.amirhesambandegan.easify_validation.isValidEmail
import ir.amirhesambandegan.easify_persian.JalaliConverter
import ir.amirhesambandegan.easify_fintech.getBankName
import ir.amirhesambandegan.easify_format.toCurrencyString
import ir.amirhesambandegan.easify_form.Validators
import ir.amirhesambandegan.easify_form.buildForm
import ir.amirhesambandegan.easify_location.GeoPoint
import ir.amirhesambandegan.easify_location.LocationMath

fun main() {
    println("ایمیل معتبر است؟ ${"dev@easify.ir".isValidEmail()}")
    println("تاریخ شمسی: ${JalaliConverter.gregorianToJalali(2026, 3, 21).toReadablePersianDate()}")
    println("نام بانک: ${"6037991234567890".getBankName()}")
    println("مبلغ: ${1_500_000L.toCurrencyString(symbol = "IRR", decimals = 0)}")

    val tehran = GeoPoint(35.6892, 51.3890)
    val shiraz = GeoPoint(29.5918, 52.5837)
    println("فاصله (کیلومتر): ${LocationMath.distanceBetween(tehran, shiraz) / 1000f}")

    val form = buildForm {
        field("email", initialValue = "dev@easify.ir", rules = listOf(Validators.email()))
    }
    println("اعتبارسنجی فرم: ${form.validateAll()}")
}
```

---

## 📦 فهرست کامل ماژول‌ها (۳۲ ماژول)

### ماژول‌های خالص Kotlin Multiplatform / Console Core (۹ ماژول)

| نام ماژول | پلتفرم‌ها | کاربرد |
| :--- | :--- | :--- |
| **`easify-validation`** | Android, JVM, Apple, Linux, Mingw, JS, WasmJS | Regexهای اعتبارسنجی، `PasswordPolicy`، کد ملی، Luhn کارت، IPv4/IPv6، UUID |
| **`easify-format-core`** | Android, JVM, Apple, Linux, Mingw, JS, WasmJS | فرمت قیمت، اعداد فشرده، تغییر حروف، استخراجگر متن، ماسک و تایمر |
| **`easify-persian-core`** | Android, JVM, Apple, Linux, Mingw, JS, WasmJS | `JalaliDate`، تبدیل شمسی/میلادی، استانداردسازی حروف و اعداد فارسی، تبدیل عدد به حروف |
| **`easify-fintech-core`** | Android, JVM, Apple, Linux, Mingw, JS, WasmJS | تشخیص بانک کارت‌های شتاب، شبا، `BillValidator`، کد پستی، شناسه حقوقی و محاسبه قیمت |
| **`easify-state-core`** | Android, JVM, Apple, Linux, Mingw, JS, WasmJS | هسته خالص `Bloc`، `Cubit`، `EffectBloc` و ترنسفورمرهای همزمانی `Transformers` |
| **`easify-form-core`** | Android, JVM, Apple, Linux, Mingw, JS, WasmJS | فرم‌ساز بدون UI (`buildForm`)، قوانین `Validators`، فیلترهای ورودی و اعتبارسنجی ناهمگام |
| **`easify-network-core`** | Android, JVM, Apple, Linux, JS, WasmJS | کلاینت چندپلتفرمی `createKtorClient()`، `safeRequest`، Retry هوشمند، `CurlLoggerPlugin` |
| **`easify-auth-core`** | Android, JVM, Apple, Linux, Mingw, JS, WasmJS | `PkceHelper` (RFC 7636)، `OtpExtractor` (ارقام فارسی/انگلیسی)، مدل‌های OAuth |
| **`easify-location-core`** | Android, JVM, Apple, Linux, Mingw, JS, WasmJS | `GeoPoint`، `LocationMath` (فاصله، آزیموت، geofence)، `filterPointJumps`، محاسبه ETA |

---

### راهنمای عملی و مثال‌های کد هر ماژول

<details>
<summary><b>1. 🔐 easify-security (رمزنگاری Google Tink و محافظت از صفحه) — اندروید</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-security:3.1.0")`

ذخیره‌سازی رمزنگاری‌شده با Google Tink (`AES256_SIV` + `AES256_GCM`)، رمزنگاری کلید در Android Keystore (`EncryptionManager`) و محافظت از اسکرین‌شات و Tapjacking.

```kotlin
// جلوگیری از اسکرین‌شات و پنهان‌سازی محتوا در پیش‌نمایش Recent Apps
ScreenShield()
BackgroundPrivacyShield()

PreventTapjacking {
    // بخش‌های حساس رابط کاربری
}

// SharedPreferences رمزنگاری‌شده با Google Tink
val prefs = SecureStorage.getEncryptedPreferences(context, "vault_prefs")
prefs.edit().putString("api_key", "secret_value").apply()

// رمزنگاری متن با کلید سخت‌افزاری Android Keystore AES-256-GCM
val encrypted = EncryptionManager.encrypt(alias = "session_key", plainText = "my_secret_text")
val decrypted = EncryptionManager.decrypt(alias = "session_key", encryptedText = encrypted)
```
</details>

<details>
<summary><b>2. 🗄️ easify-storage (DataStore رمزنگاری‌شده و Tink Streaming AEAD) — اندروید</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-storage:3.1.0")`

مدیریت امن DataStore (`DataStoreController`)، رمزنگاری جریانی فایل‌های حجیم با **Google Tink Streaming AEAD** (`SecureFileStorage`)، کش زمان‌دار (`SmartCacheManager`) و پشتیبان‌گیری ZIP.

```kotlin
val controller = DataStoreController(context)
val tokenKey = PreferenceKeys.stringKey("auth_token")

// ذخیره و بازیابی رمزنگاری‌شده در DataStore
controller.saveSecure(tokenKey, "jwt_abc123")
val token = controller.getSecure(tokenKey)

// رمزنگاری فایل‌ها با Google Tink Streaming AEAD (AES256_GCM_HKDF_4KB)
val secretFile = File(context.filesDir, "report.enc")
SecureFileStorage.writeSecureString(context, secretFile, "گزارش محرمانه")
val restored = SecureFileStorage.readSecureString(context, secretFile)
```
</details>

<details>
<summary><b>3. 🎨 easify-ui (ابزارهای UI و انیمیشن تم Compose Multiplatform) — CMP</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-ui:3.1.0")`

شامل `SuspendButton` با پشتیبانی از `shape` و `colors` سفارشی، ۷ مدل انیمیشن تغییر تم در `EasifyThemeContainer`، `EasifySwipeToAction`، `EasifyOtpInput`، افکت‌های شیشه‌ای، Shimmer، Shake و `Modifier.bounceClick`.

```kotlin
var isDark by remember { mutableStateOf(false) }

EasifyThemeContainer(
    isDark = isDark,
    revealType = ThemeRevealType.CIRCULAR,
    anchor = ThemeRevealAnchor.TopEnd,
    lightColors = lightColorScheme(),
    darkColors = darkColorScheme()
) {
    Column(modifier = Modifier.hideKeyboardOnTapOutside()) {
        SuspendButton(
            onClick = { delay(1500) },
            type = SuspendButtonType.FILLED,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("ارسال اطلاعات")
        }

        EasifySwipeToAction(
            onSwipeRight = { /* آرشیو */ },
            onSwipeLeft = { /* حذف */ }
        ) {
            Text("برای اکشن به چپ یا راست بکشید")
        }

        Box(modifier = Modifier.size(100.dp).shimmer(showShimmer = true))
    }
}
```
</details>

<details>
<summary><b>4. ⚡ easify-state و easify-state-core (معماری BLoC و Cubit) — CMP و کنسول</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-state:3.1.0")` یا `easify-state-core:3.1.0`

پیاده‌سازی الگوی BLoC با `Bloc`، `Cubit`، `EffectBloc`، `BlocViewModel`، ترنسفورمرهای همزمانی `Transformers` و ابزارهای `BlocProvider`، `BlocBuilder`، `read`، `watch` و `select`.

```kotlin
data class CounterState(val count: Int = 0) : BlocState
sealed interface CounterEvent : BlocEvent {
    data object Increment : CounterEvent
}

class CounterBloc : Bloc<CounterEvent, CounterState>(CounterState(0)) {
    init {
        on<CounterEvent.Increment>(transformer = Transformers.sequential()) { _, emit ->
            emit(state.value.copy(count = state.value.count + 1))
        }
    }
}

@Composable
fun CounterScreen() {
    val state = watch<CounterBloc, CounterState>()
    val bloc = read<CounterBloc>()

    Column {
        Text("تعداد: ${state.count}")
        Button(onClick = { bloc.add(CounterEvent.Increment) }) {
            Text("افزایش")
        }
    }
}
```
</details>

<details>
<summary><b>5. 📝 easify-form و easify-form-core (مدیریت فرم‌ها) — CMP و کنسول</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-form:3.1.0")` یا `easify-form-core:3.1.0`

مدیریت فرم‌ها در کامپوز (`rememberForm`) و هسته کنسولی (`buildForm`)، فیلدهای آرایه‌ای، فیلترهای ورودی، ماسک نمایش و ذخیره پیش‌نویس (`saveDraft`).

```kotlin
val form = rememberForm {
    field(
        key = "email",
        rules = listOf(Validators.required(), Validators.email()),
        inputFilter = InputFilters.noSpaces()
    )
}
val emailField = form.getField("email")!!

OutlinedTextField(
    value = emailField.value,
    onValueChange = { emailField.value = it },
    isError = emailField.error != null,
    supportingText = emailField.error?.let { { Text(it) } }
)

Button(onClick = { if (form.validateAll()) { /* ثبت فرم */ } }) {
    Text("ارسال")
}
```
</details>

<details>
<summary><b>6. 🌐 easify-network و easify-network-core (مدیریت Ktor 3.6.1) — اندروید و کنسول</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-network:3.1.0")` یا `easify-network-core:3.1.0`

راه‌اندازی سریع Ktor `3.6.1` با `createKtorClient()`، پوشش ایمن `safeRequest` و `retryNetworkCall`، لاگر امن cURL، دانلود جریانی، آپلود Multipart و پایش اتصال شبکه (`ConnectivityObserver`).

```kotlin
val client = createKtorClient(baseUrl = "https://api.example.com")

val result: EasifyResult<UserResponse> = client.safeRequest {
    get("/user")
}

result.onSuccess { user -> println(user.name) }
    .onApiError { code, msg -> println("خطای سرور $code: $msg") }

// پایش آنلاین/آفلاین بودن در اندروید
val online by ConnectivityObserver(context).online.collectAsState(initial = true)
```
</details>

<details>
<summary><b>7. 🔑 easify-auth و easify-auth-core (Google Sign-In، OTP و PKCE) — اندروید و کنسول</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-auth:3.1.0")` یا `easify-auth-core:3.1.0`

ورود مدرن با Credential Manager گوگل، دریافت خودکار پیامک OTP با `SmsOtpEffect` و احراز هویت OAuth 2.0 / PKCE با تایید زمان‌ثابت `state`.

```kotlin
val authClient = rememberEasifyAuth()

LaunchedEffect(Unit) {
    when (val result = authClient.signInWithGoogle(serverClientId = "YOUR_WEB_CLIENT_ID")) {
        is AuthResult.Success -> println("خوش آمدید ${result.data.displayName}")
        is AuthResult.Error -> println("خطا: ${result.throwable.message}")
    }
}

SmsOtpEffect(
    otpLengthRange = 4..6,
    onOtpReceived = { otp -> println("کد تایید: $otp") }
)

val oauthConfig = OAuthConfig(
    clientId = "YOUR_GITHUB_CLIENT_ID",
    clientSecret = "YOUR_GITHUB_CLIENT_SECRET",
    redirectUri = "myapp://oauth-callback",
    scopes = listOf("read:user", "user:email")
)
```
</details>

<details>
<summary><b>8. 🇮🇷 easify-persian و easify-persian-core (تقویم شمسی و ابزار فارسی) — CMP و کنسول</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-persian:3.1.0")` یا `easify-persian-core:3.1.0`

تقویم شمسی دقیق، استانداردسازی حروف و ارقام فارسی (`ی/ک`)، تبدیل عدد به حروف و انتخابگر تاریخ/زمان متریال ۳ (`PersianDatePickerDialog`).

```kotlin
val today = JalaliDate.now()
val formatted = today.format(persianDigits = true)            // ۱۴۰۵/۰۸/۲۱
val readable = today.toReadablePersianDate()                  // پنج‌شنبه ۲۱ آبان ۱۴۰۵
val text = "قیمت: 1500".toPersianDigits()                     // قیمت: ۱۵۰۰
val words = 1_500_000L.toPersianWords()                       // یک میلیون و پانصد هزار
val phoneValid = "۰۹۱۲۳۴۵۶۷۸۹".isValidPersianMobile()         // true
```
</details>

<details>
<summary><b>9. 💳 easify-fintech و easify-fintech-core (بانکداری ایران و فرم‌ها) — CMP و کنسول</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-fintech:3.1.0")` یا `easify-fintech-core:3.1.0`

تشخیص نام بانک از شماره کارت و شبا، اعتبارسنجی کد ملی و شناسه حقوقی، تحلیل قبوض خدماتی (`BillValidator`)، ماسک‌های متنی (`CardNumberVisualTransformation`) و ویجت `EasifyBalanceText`.

```kotlin
val isValid = "0012345678".isValidNationalId()
val bankName = "6037991234567890".getBankName()          // بانک ملی ایران
val isValidSheba = "IR820540102680020817909002".isValidSheba()

// نمایش موجودی با قابلیت مخفی‌سازی محرمانه
EasifyBalanceText(balance = 25_000_000L, currency = "تومان")
```
</details>

<details>
<summary><b>10. ✅ easify-validation (اعتبارسنجی خالص Regex و سیاست رمز) — KMP</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-validation:3.1.0")`

ماژول خالص Kotlin Multiplatform بدون هیچ وابستگی خارجی برای ایمیل، موبایل، کد ملی، کد پستی، رمز عبور، IPv4/IPv6 و کارت اعتباری.

```kotlin
val emailOk = "test@easify.dev".isValidEmail()
val phoneOk = "+989123456789".isValidMobile(allowInternational = true)
val strongOk = "Aa!123456".isValidStrongPassword()
val policyOk = "Secret12".isValidPassword(PasswordPolicy(minLength = 8, requireSpecialChar = false))
val cardOk = "6037991234567890".isValidCreditCardLuhn()
```
</details>

<details>
<summary><b>11. 🔢 easify-format و easify-format-core (قالب‌بندی عدد، متن و زمان) — CMP و کنسول</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-format:3.1.0")` یا `easify-format-core:3.1.0`

جداکننده سه‌رقمی قیمت، فرمت فشرده (`1.5M`)، شباهت متنی، ماسک ایمیل و کارت، تایمر و هایلایت کلمات در Compose.

```kotlin
val price = 1500000L.toCurrencyString(symbol = "$", decimals = 0) // "$1,500,000"
val compact = 1_500_000L.toCompactFormat()                        // "1.5M"
val timer = 3661L.toTimerFormat()                                 // "01:01:01"
val duration = 3661L.toReadableDuration()                         // "1h 1m 1s"
```
</details>

<details>
<summary><b>12. 🖼️ easify-image (بزرگ‌نمایی و پردازش تصویر) — CMP</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-image:3.1.0")`

نمایشگر زوم‌پذیر چندپلتفرمی (`EasifyZoomableImage`)، تصویربرداری از کامپوزبل (`ImageCapturable`)، اصلاح جهت EXIF و فشرده‌سازی هوشمند بر اساس حجم هدف (کیلوبایت).

```kotlin
EasifyZoomableImage(imageBitmap = myImageBitmap)

// اصلاح چرخش EXIF و فشرده‌سازی به حجم هدف در اندروید
val bitmap = imageUri.toCorrectedBitmap(context)
val compressedBytes = bitmap?.compressToTargetSize(maxSizeKB = 250)
```
</details>

<details>
<summary><b>13. 🔄 easify-lifecycle (چرخه حیات و پایشگر وضعیت) — CMP</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-lifecycle:3.1.0")`

افکت‌های آماده چرخه حیات در Compose (`OnResumeEffect`، `OnPauseEffect`، `LifecycleTimer`، `OnFirstCreateEffect`) و وضعیت کیبورد و پیش‌زمینه اپلیکیشن در اندروید.

```kotlin
OnResumeEffect { /* صفحه فعال شد */ }
OnPauseEffect { /* صفحه متوقف شد */ }

LifecycleTimer(intervalMillis = 1_000L) {
    // فقط در وضعیت RESUMED اجرا می‌شود
}

val isForeground by rememberAppForegroundState()
val keyboard by rememberKeyboardState()
```
</details>

<details>
<summary><b>14. 🛡️ easify-permission (مدیریت دسترسی‌ها) — اندروید</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-permission:3.1.0")`

درخواست دسترسی‌های زمان اجرا با `rememberPermissionState` یا کامپوننت `RequestPermission` به همراه دسترسی‌های خاص Overlay و باتری.

```kotlin
val cameraPermission = rememberPermissionState(Manifest.permission.CAMERA)

Button(onClick = { cameraPermission.launchPermissionRequest() }) {
    Text("وضعیت: ${cameraPermission.status}")
}

RequestPermission(
    permission = Manifest.permission.CAMERA,
    onGrant = { /* مجوز تایید شد */ },
    onDenied = { /* مجوز رد شد */ }
)
```
</details>

<details>
<summary><b>15. 👆 easify-biometric (اثر انگشت و تشخیص چهره) — اندروید</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-biometric:3.1.0")`

راه‌اندازی سریع احراز هویت بیومتریک (`rememberBiometricLauncher`)، `BiometricAutoShield`، تشخیص نوع سخت‌افزار و پشتیبانی از Crypto Keystore.

```kotlin
val biometricLauncher = rememberBiometricLauncher { result ->
    when (result) {
        is BiometricResult.Success -> { /* تایید شد */ }
        is BiometricResult.Error -> println(result.errString)
    }
}

Button(onClick = {
    if (BiometricUtils.canAuthenticate(context)) {
        biometricLauncher?.launch(
            title = "تایید هویت",
            subtitle = "برای ادامه از اثر انگشت استفاده کنید"
        )
    }
}) {
    Text("ورود با اثر انگشت")
}
```
</details>

<details>
<summary><b>16. 📳 easify-haptic (الگوهای لرزش و بازخورد لمسی) — اندروید</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-haptic:3.1.0")`

اکستنشن‌های `LocalHapticFeedback`، مادیفایر `Modifier.hapticClickable` و الگوهای لرزش دقیق برای اندروید‌های جدید (`hapticConfirm`، `hapticReject`، `hapticHeartbeat`).

```kotlin
val haptic = LocalHapticFeedback.current

Button(onClick = { haptic.performSuccess() }) {
    Text("تکمیل عملیات")
}

Box(modifier = Modifier.hapticClickable { context.hapticConfirm() }) {
    Text("لمس برای بازخورد لرزشی")
}
```
</details>

<details>
<summary><b>17. 🔔 easify-notification (اعلان‌ها، WorkManager و بنر داخلی) — اندروید</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-notification:3.1.0")`

مدیریت کانال‌های نوتیفیکیشن، اعلان‌های پیشرفت‌دار، دسترسی اندروید ۱۳+ (`rememberNotificationPermission`) و بنر درون‌برنامه‌ای (`InAppBanner`).

```kotlin
val notificationPermission = rememberNotificationPermission()

NotificationHelper.showNotification(
    context = context,
    channelId = "downloads",
    channelName = "دانلودها",
    title = "دانلود کامل شد",
    message = "فایل با موفقیت ذخیره گردید.",
    smallIcon = R.drawable.ic_notification
)
```
</details>

<details>
<summary><b>18. 📂 easify-file (انتخابگر فایل، MediaStore و آرشیو امن) — اندروید</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-file:3.1.0")`

انتخابگر مدرن PhotoPicker / Document، ذخیره مستقیم در گالری و پوشه دانلودها، فشرده‌سازی ZIP با محافظت از ZIP-Slip و محاسبه SHA-256.

```kotlin
var pickImage by remember { mutableStateOf(false) }

if (pickImage) {
    FilePicker(mediaType = MediaType.ImageOnly) { uri ->
        pickImage = false
        val name = uri?.getFileName(context)
    }
}
```
</details>

<details>
<summary><b>19. ⚙️ easify-context (اینتنت‌ها و ابزارهای سیستم) — اندروید</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-context:3.1.0")`

ابزارهای کاربردی کلیپ‌بورد، اشتراک‌گذاری، باز کردن امن مرورگر (`http/https`)، شماره‌گیری و پنل تنظیمات اندروید.

```kotlin
context.copyToClipboard("متن برای کپی")
context.shareText("این اپلیکیشن را ببینید!")
context.openBrowser("https://github.com/ahbandegan/Easify-Android")
context.openAppSettings()
```
</details>

<details>
<summary><b>20. 📍 easify-location و easify-location-core (GPS زنده و ضدتقلب) — اندروید و کنسول</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-location:3.1.0")` یا `easify-location-core:3.1.0`

جریان زنده مکان (`LocationTracker`)، تشخیص لوکیشن جعلی (`isMocked`)، فیلتر پرش ناگهانی (`filterJumps`) و هسته محاسباتی KMP.

```kotlin
val locationState = rememberLocationTracker()
val currentLocation by locationState.tracker
    .getLocationUpdates(interval = 5_000L)
    .filterJumps(maxSpeedKmh = 200f)
    .collectAsState(initial = null)

currentLocation?.let { loc ->
    Text("Lat: ${loc.latitude}, Lng: ${loc.longitude}, Mock: ${loc.isMocked()}")
}
```
</details>

<details>
<summary><b>21. 🔵 easify-bluetooth (اسکنر BLE، GATT و تبلیغ‌کننده) — اندروید</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-bluetooth:3.1.0")`

اسکنر بلوتوث کم‌مصرف با مدیریت خودکار مجوزهای اندروید ۱۲+ (`rememberBluetoothLauncher`)، مدیریت اتصال GATT و تخمین فاصله با RSSI.

```kotlin
val btLauncher = rememberBluetoothLauncher()
val devices by btLauncher.scannedDevices.collectAsState(initial = emptyList())

Button(onClick = { btLauncher.startScanning() }) {
    Text("اسکن دستگاه‌های BLE (${devices.size})")
}
```
</details>

<details>
<summary><b>22. 📷 easify-camera (دوربین CameraX و اسکنر بارکد ML Kit) — اندروید</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-camera:3.1.0")`

پیش‌نمایش CameraX، عکاسی ساسپند (`takePicture()`)، کنترل فلاش/زوم و اسکنر آنی بارکد و QR.

```kotlin
val cameraState = rememberCameraState()
CameraPreview(cameraState = cameraState, modifier = Modifier.fillMaxSize())

// یا اسکنر مستقیم QR و بارکد
QrCodeScannerView(
    modifier = Modifier.fillMaxSize(),
    onBarcodeDetected = { rawValue -> println("بارکد اسکن شد: $rawValue") }
)
```
</details>

<details>
<summary><b>23. 📳 easify-sensor (سنسورهای سخت‌افزاری واکنش‌گرا) — اندروید</b></summary>

**کد نصب:** `implementation("com.github.ahbandegan.Easify-Android:easify-sensor:3.1.0")`

دریافت جریان داده‌های شتاب‌سنج، ژیروسکوپ، مجاورت، نور و فشارسنج به صورت Flow در Compose.

```kotlin
val observer = rememberSensorObserver(Sensor.TYPE_ACCELEROMETER)
val sensorEvent by observer.sensorData.collectAsState(initial = null)

sensorEvent?.toThreeAxisData()?.let { axis ->
    Text("X: ${axis.x}, Y: ${axis.y}, Z: ${axis.z}")
}
```
</details>

---

## 🤝 مشارکت
از مشارکت شما استقبال می‌کنیم! در صورت داشتن ایده یا بهبود جدید، لطفاً Issue یا Pull Request ثبت کنید.

## ⚖️ لایسنس
منتشرشده تحت لایسنس [GNU General Public License v3.0 (GPL-3.0)](./LICENSE).

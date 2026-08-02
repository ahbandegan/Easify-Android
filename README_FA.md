# EasifyAndroid 🚀

**EasifyAndroid** یک کتابخانه جامع و آماده استفاده برای اندروید است که با هدف حذف کدهای تکراری (Boilerplate) و ساده‌سازی وظایف پیچیده طراحی شده است. از سنسورهای واکنشی و بلوتوث گرفته تا ذخیره‌سازی امن و ابزارهای بومی‌سازی شده برای ایران، این کتابخانه یک API مدرن و تمیز برای هر توسعه‌دهنده اندرویدی فراهم می‌کند.

---

## 📦 ماژول‌ها و قابلیت‌ها

### 🔐 امنیت و مجوزها
- **Easify-Security**: رمزگذاری AES-GCM مبتنی بر سخت‌افزار و محافظت از صفحه (جلوگیری از اسکرین‌شات و ضبط صفحه).
- **Easify-Permission**: مدیریت خودکار و هوشمند مجوزها در Jetpack Compose.
- **Easify-Biometric**: لانچر آماده برای احراز هویت با اثر انگشت و تشخیص چهره.

### 🌐 شبکه
- **Easify-Network**: پوشش امن برای Ktor 3.x با مدیریت وضعیت `EasifyResult` و آپلود فوق‌العاده راحت فایل با نمایش پیشرفت (Progress).
- **Connectivity-Observer**: مانیتورینگ زنده وضعیت اینترنت با استفاده از Kotlin Flow.

### 🇮🇷 ابزارهای فارسی (Persian Kit)
- **Easify-Persian**: مبدل تاریخ شمسی، تبدیل اعداد به فارسی و تبدیل عدد به حروف (مبلغ به حروف).
- **Easify-Fintech**: اعتبارسنجی کد ملی، تشخیص بانک از روی شماره کارت و اعتبارسنجی شبا.

### 📱 سخت‌افزار و سیستم‌عامل
- **Easify-Sensor**: دسترسی مستقیم و واکنشی به تمام سنسورها (شتاب‌سنج، مجاورت و غیره) با مدل‌های داده‌ای خوانا.
- **Easify-Bluetooth**: اسکنر مدرن BLE که پیچیدگی‌های مجوزهای اندروید ۱۲ به بالا را به تنهایی هندل می‌کند.
- **Easify-Location**: مکان‌یابی زنده با یک خط کد و مدیریت خودکار مجوزها.

### 🎨 رابط کاربری و تجربه کاربری (UI/UX)
- **Easify-Modifiers**: افکت‌های جذاب مانند `.bounceClick()` (مشابه iOS)، اسکلتون لودینگ `.shimmer()` و بستن خودکار کیبورد.
- **Easify-Form**: مدیریت هوشمند وضعیت فرم‌ها و اعتبارسنجی خودکار ورودی‌ها.
- **Easify-Haptic**: ایجاد لرزش‌های معنایی (موفقیت، خطا، کلیک) با دستورات ساده.

---

## 🚀 شروع سریع

### نصب با JitPack
برای استفاده از EasifyAndroid از طریق JitPack، باید مخزن و وابستگی را در تنظیمات Gradle پروژه‌تان اضافه کنید.

#### مرحله ۱: افزودن JitPack به repositories
در فایل `settings.gradle.kts`، JitPack را در انتهای بلوک repositories اضافه کنید:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

#### مرحله ۲: افزودن وابستگی
در فایل `build.gradle.kts` ماژول اپ خود:

```kotlin
dependencies {
    implementation("com.github.ahbandegan:Easify-Android:latest")
}
```

### مثال: ذخیره‌سازی امن داده‌ها
```kotlin
val controller = DataStoreController(context)
controller.saveSecure(PreferenceKeys.stringKey("token"), "secret_123")
```

### مثال: اسکن زنده بلوتوث
```kotlin
val bt = rememberBluetoothLauncher()
val devices by bt.scannedDevices.collectAsState(emptyList())

Button(onClick = { bt.askAndScan() }) {
    Text("جستجوی دستگاه‌ها")
}
```

---

## 📖 مستندات کامل پکیج‌ها

برای جزییات بیشتر و مثال‌های کد، راهنمای هر بخش را مطالعه کنید:

### ماژول‌های اصلی
- [**شبکه (Network)**](./app/src/main/java/ir/amirhesambandegan/easify_network/README_FA.md): درخواست‌های امن Ktor و آپلود فایل.
- [**امنیت (Security)**](./app/src/main/java/ir/amirhesambandegan/easify_security/README_FA.md): رمزگذاری و محافظت از صفحه.
- [**مجوزها (Permissions)**](./app/src/main/java/ir/amirhesambandegan/easify_permission/README_FA.md): مدیریت خودکار پریمیشن‌ها.
- [**بیومتریک (Biometric)**](./app/src/main/java/ir/amirhesambandegan/easify_biometric/README_FA.md): اثر انگشت و تشخیص چهره.

### بومی‌سازی (ایران)
- [**ابزارهای فارسی**](./app/src/main/java/ir/amirhesambandegan/easify_persian/README_FA.md): تاریخ شمسی، اعداد و حروف.
- [**فین‌تک (Fintech)**](./app/src/main/java/ir/amirhesambandegan/easify_fintech/README_FA.md): کد ملی، کارت بانکی و شبا.

### سخت‌افزار
- [**بلوتوث (Bluetooth)**](./app/src/main/java/ir/amirhesambandegan/easify_bluetooth/README_FA.md): اسکن BLE ساده.
- [**سنسورها (Sensors)**](./app/src/main/java/ir/amirhesambandegan/easify_sensor/README_FA.md): شتاب‌سنج، مجاورت و غیره.
- [**مکان‌یابی (Location)**](./app/src/main/java/ir/amirhesambandegan/easify_location/README_FA.md): ردیابی زنده مختصات.

### ظاهر و تجربه کاربری
- [**مودیفایرهای UI**](./app/src/main/java/ir/amirhesambandegan/easify_ui/README_FA.md): افکت‌های Bounce، Shimmer و فاصله.
- [**مدیریت فرم (Form)**](./app/src/main/java/ir/amirhesambandegan/easify_form/README_FA.md): اعتبارسنجی واکنشی فرم‌ها.
- [**لرزش (Haptics)**](./app/src/main/java/ir/amirhesambandegan/easify_haptic/README_FA.md): بازخورد لمسی.
- [**نوتیفیکیشن**](./app/src/main/java/ir/amirhesambandegan/easify_notification/README_FA.md): ساخت سریع کانال و اعلان.

### ابزارهای کمکی
- [**اینتنت‌ها و کلیپ‌بورد**](./app/src/main/java/ir/amirhesambandegan/easify_context/README_FA.md): اشتراک‌گذاری، تماس و غیره.
- [**ذخیره‌سازی (Storage)**](./app/src/main/java/ir/amirhesambandegan/easify_storage/README_FA.md): مدیریت امن DataStore.
- [**انتخاب فایل**](./app/src/main/java/ir/amirhesambandegan/easify_file/README_FA.md): انتخاب عکس و ویدیو از گالری.
- [**چرخه حیات (Lifecycle)**](./app/src/main/java/ir/amirhesambandegan/easify_lifecycle/README_FA.md): مدیریت رویدادهای اکتیویتی.
- [**اعتبارسنجی (Validation)**](./app/src/main/java/ir/amirhesambandegan/easify_validation/README_FA.md): مجموعه‌ی ریجکس‌های کاربردی.
- [**پردازش تصویر**](./app/src/main/java/ir/amirhesambandegan/easify_image/README_FA.md): تغییر سایز و فشرده‌سازی عکس.
- [**فرمت‌دهی**](./app/src/main/java/ir/amirhesambandegan/easify_format/README_FA.md): زمان و مبالغ.

---

## ⚖️ لایسنس
MIT License. استفاده شخصی و تجاری کاملاً رایگان است.

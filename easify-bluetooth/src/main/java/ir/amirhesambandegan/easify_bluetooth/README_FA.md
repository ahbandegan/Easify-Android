# ماژول بلوتوث (Easify Bluetooth)

یک ماژول ساده‌سازی شده برای مدیریت بلوتوث کم‌مصرف (BLE) در Jetpack Compose.

## ویژگی‌های کلیدی

- **اسکن BLE**: شناسایی آسان دستگاه‌های اطراف با `BluetoothScanner`.
- **مدیریت دسترسی‌ها**: مدیریت خودکار مجوزهای بلوتوث متناسب با نسخه‌های مختلف اندروید.
- **مانیتورینگ وضعیت**: مشاهده وضعیت روشن یا خاموش بودن بلوتوث با استفاده از Kotlin Flows.
- **یکپارچگی با Compose**: استفاده آسان در رابط کاربری با `rememberBluetoothLauncher`.

## نحوه استفاده

### ۱. راه‌اندازی لانچر بلوتوث
در Composable خود، از `rememberBluetoothLauncher` برای مدیریت دسترسی‌ها و اسکن استفاده کنید.

```kotlin
val bluetoothLauncher = rememberBluetoothLauncher(
    onPermissionDenied = {
        // مدیریت عدم تایید دسترسی
    }
)

val devices by bluetoothLauncher.scannedDevices.collectAsState(initial = emptyList())
val isEnabled by bluetoothLauncher.isEnabled.collectAsState(initial = false)

Column {
    if (!isEnabled) {
        Text("لطفاً بلوتوث را روشن کنید")
    }
    
    Button(onClick = { bluetoothLauncher.askAndScan() }) {
        Text("شروع جستجو")
    }

    LazyColumn {
        items(devices) { device ->
            Text("${device.name ?: "نامشخص"} - ${device.address}")
        }
    }
}
```

### ۲. اسکن دستی
می‌توانید مستقیماً از `BluetoothScanner` استفاده کنید.

```kotlin
val scanner = BluetoothScanner(context)
scope.launch {
    scanner.startScan().collect {
        // اسکن شروع شد
    }
}

val deviceListFlow = scanner.scannedDevices
```

### ۳. بررسی وضعیت بلوتوث
از `BluetoothObserver` برای مانیتور کردن وضعیت بلوتوث سیستم استفاده کنید.

```kotlin
val observer = BluetoothObserver(context)
val isEnabledFlow = observer.isBluetoothEnabled
```

## اجزا و کلاس‌ها

- `BluetoothScanner`: منطق اصلی برای یافتن دستگاه‌های BLE.
- `BluetoothLauncher`: مدیریت دسترسی‌ها، وضعیت آداپتور و اسکن در Compose.
- `BluetoothObserver`: دریافت‌کننده پیام‌های سیستمی برای بررسی وضعیت روشن/خاموش بودن بلوتوث.
- `EasifyBluetoothDevice`: یک کلاس داده برای نمایش اطلاعات دستگاه یافت شده.
- `BluetoothUtils`: ابزارهای کمکی برای مجوزها و باز کردن تنظیمات سیستم.

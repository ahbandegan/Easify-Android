# easify_image

Utility functions for Bitmap processing and conversion in Android.

## Features
- **URI to Bitmap**: Safely decode an image URI into a `Bitmap` object.
- **Compression**: Easily compress bitmaps to `ByteArray` with adjustable quality and format.
- **Base64 Conversion**: Convert bitmaps to Base64 strings for network transmission.
- **Resizing**: Scale bitmaps while maintaining their original aspect ratio.

## Usage

```kotlin
val bitmap = uri.toBitmap(context)

// Resize to a maximum of 1024x1024
val resizedBitmap = bitmap?.resize(1024, 1024)

// Convert to Base64
val base64String = resizedBitmap?.toBase64(quality = 80)

// Get raw compressed bytes
val bytes = resizedBitmap?.compressToBytes(format = Bitmap.CompressFormat.WEBP)
```

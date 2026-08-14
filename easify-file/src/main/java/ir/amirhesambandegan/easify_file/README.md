# Easify File

Modern file and media selection module for Jetpack Compose using `ActivityResultContracts`.

## Key Features

- **Media Selection**: Support for picking images, videos, or both.
- **Single/Multiple Selection**: Components for selecting one or multiple files.
- **Automatic Permission Handling**: Automatically requests the necessary storage/media permissions based on the Android API level.
- **Compose Optimized**: Fully declarative components.

## Usage

### 1. Single File Picker
Use the `FilePicker` composable to let the user select a single file.

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
    Text("Select Image")
}
```

### 2. Multiple File Picker
Use `MultipleFilePicker` to select multiple items with a specified limit.

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
    Text("Select up to 5 files")
}
```

## Components and Enums

- `FilePicker`: Composable for single media selection.
- `MultipleFilePicker`: Composable for selecting multiple media items.
- `MediaType`: Enum to specify allowed types:
    - `ImageOnly`
    - `VideoOnly`
    - `ImageAndVideo`

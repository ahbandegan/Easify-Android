package ir.amirhesambandegan.easify_file

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun DocumentPicker(
    allowedMimeTypes: Array<String> = arrayOf("*/*"),
    onResult: (Uri?) -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        onResult(uri)
    }
    
    LaunchedEffect(Unit) {
        launcher.launch(allowedMimeTypes)
    }
}

@Composable
fun DirectoryPicker(
    onResult: (Uri?) -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocumentTree()
    ) { uri ->
        onResult(uri)
    }
    
    LaunchedEffect(Unit) {
        launcher.launch(null)
    }
}

@Composable
fun SystemCameraCapture(
    onResult: (Bitmap?) -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        onResult(bitmap)
    }
    
    LaunchedEffect(Unit) {
        launcher.launch(null)
    }
}

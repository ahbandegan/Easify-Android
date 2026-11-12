package ir.amirhesambandegan.easify_camera

import android.content.Context
import android.net.Uri
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.resume

enum class EasifyCameraLens {
    BACK, FRONT
}

enum class EasifyFlashMode {
    OFF, ON, AUTO
}

class CameraState(
    context: Context,
    private val lifecycleOwner: LifecycleOwner
) {
    private val appContext = context.applicationContext
    private var camera: Camera? = null
    private var imageCapture: ImageCapture? = null
    private var preview: Preview? = null

    var lensFacing by mutableStateOf(EasifyCameraLens.BACK)
        private set

    var flashMode by mutableStateOf(EasifyFlashMode.OFF)
        private set

    var isTorchEnabled by mutableStateOf(false)
        private set

    internal fun bindCamera(previewView: PreviewView) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(appContext)
        cameraProviderFuture.addListener({
            val cameraProvider = runCatching { cameraProviderFuture.get() }.getOrNull() ?: return@addListener

            val selector = when (lensFacing) {
                EasifyCameraLens.BACK -> CameraSelector.DEFAULT_BACK_CAMERA
                EasifyCameraLens.FRONT -> CameraSelector.DEFAULT_FRONT_CAMERA
            }

            preview = Preview.Builder().build().also {
                it.surfaceProvider = previewView.surfaceProvider
            }

            val flash = when (flashMode) {
                EasifyFlashMode.OFF -> ImageCapture.FLASH_MODE_OFF
                EasifyFlashMode.ON -> ImageCapture.FLASH_MODE_ON
                EasifyFlashMode.AUTO -> ImageCapture.FLASH_MODE_AUTO
            }

            imageCapture = ImageCapture.Builder()
                .setFlashMode(flash)
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            try {
                cameraProvider.unbindAll()
                camera = cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    selector,
                    preview,
                    imageCapture
                )
                camera?.cameraControl?.enableTorch(isTorchEnabled)
            } catch (_: Exception) {
            }
        }, ContextCompat.getMainExecutor(appContext))
    }

    fun toggleLens(previewView: PreviewView) {
        lensFacing =
            if (lensFacing == EasifyCameraLens.BACK) EasifyCameraLens.FRONT else EasifyCameraLens.BACK
        bindCamera(previewView)
    }

    fun toggleTorch() {
        val next = !isTorchEnabled
        isTorchEnabled = next
        camera?.cameraControl?.enableTorch(next)
    }

    fun setFlash(mode: EasifyFlashMode) {
        flashMode = mode
        val flash = when (mode) {
            EasifyFlashMode.OFF -> ImageCapture.FLASH_MODE_OFF
            EasifyFlashMode.ON -> ImageCapture.FLASH_MODE_ON
            EasifyFlashMode.AUTO -> ImageCapture.FLASH_MODE_AUTO
        }
        imageCapture?.flashMode = flash
    }

    fun setZoom(linearZoom: Float) {
        camera?.cameraControl?.setLinearZoom(linearZoom.coerceIn(0f, 1f))
    }

    suspend fun takePicture(outputDirectory: File = appContext.cacheDir): Result<Uri> =
        suspendCancellableCoroutine { continuation ->
            val capture = imageCapture ?: run {
                continuation.resume(Result.failure(IllegalStateException("Camera not ready")))
                return@suspendCancellableCoroutine
            }

            outputDirectory.mkdirs()
            val fileName = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date()) + ".jpg"
            val photoFile = File(outputDirectory, fileName)
            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

            capture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(appContext),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        val savedUri = outputFileResults.savedUri ?: Uri.fromFile(photoFile)
                        continuation.resume(Result.success(savedUri))
                    }

                    override fun onError(exception: ImageCaptureException) {
                        continuation.resume(Result.failure(exception))
                    }
                }
            )
        }
}

@Composable
fun rememberCameraState(): CameraState {
    val appContext = LocalContext.current.applicationContext
    val lifecycleOwner = LocalLifecycleOwner.current
    return remember(appContext, lifecycleOwner) {
        CameraState(appContext, lifecycleOwner)
    }
}

package ir.amirhesambandegan.easify_camera

import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun CameraPreview(
    cameraState: CameraState,
    modifier: Modifier = Modifier,
    scaleType: PreviewView.ScaleType = PreviewView.ScaleType.FILL_CENTER
) {
    AndroidView(
        factory = { ctx ->
            PreviewView(ctx).apply {
                this.scaleType = scaleType
                cameraState.bindCamera(this)
            }
        },
        modifier = modifier
    )
}
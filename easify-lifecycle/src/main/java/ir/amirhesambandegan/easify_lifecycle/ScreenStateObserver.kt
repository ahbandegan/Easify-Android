package ir.amirhesambandegan.easify_lifecycle

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext

/**
 * A composable side-effect that executes a callback when the physical screen turns ON or OFF.
 *
 * It registers a [BroadcastReceiver] listening for [Intent.ACTION_SCREEN_ON] and [Intent.ACTION_SCREEN_OFF]
 * actions and unregisters it when the composable leaves the composition.
 *
 * @param onStateChanged The callback to execute when the screen state changes. Receives `true` if the screen turns on, `false` if it turns off.
 */
@Composable
fun OnScreenStateChangedEffect(onStateChanged: (isAwake: Boolean) -> Unit) {
    val context = LocalContext.current
    val currentOnStateChanged = rememberUpdatedState(onStateChanged)

    DisposableEffect(context) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    Intent.ACTION_SCREEN_ON -> currentOnStateChanged.value(true)
                    Intent.ACTION_SCREEN_OFF -> currentOnStateChanged.value(false)
                }
            }
        }
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_SCREEN_OFF)
        }
        context.registerReceiver(receiver, filter)

        onDispose {
            context.unregisterReceiver(receiver)
        }
    }
}

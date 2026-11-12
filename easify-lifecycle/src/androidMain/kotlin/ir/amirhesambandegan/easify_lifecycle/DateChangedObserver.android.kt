package ir.amirhesambandegan.easify_lifecycle

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

/**
 * Fires [onDateChanged] when the system date or timezone changes.
 */
@Composable
fun OnDateChangedEffect(onDateChanged: () -> Unit) {
    val appContext = LocalContext.current.applicationContext
    val currentOnDateChanged = rememberUpdatedState(onDateChanged)

    DisposableEffect(appContext) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    Intent.ACTION_DATE_CHANGED,
                    Intent.ACTION_TIMEZONE_CHANGED -> currentOnDateChanged.value()
                }
            }
        }
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_DATE_CHANGED)
            addAction(Intent.ACTION_TIMEZONE_CHANGED)
        }
        ContextCompat.registerReceiver(appContext, receiver, filter, ContextCompat.RECEIVER_EXPORTED)

        onDispose {
            runCatching { appContext.unregisterReceiver(receiver) }
        }
    }
}

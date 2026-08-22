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
 * Fires when the date changes (midnight passes) or timezone changes.
 */
@Composable
fun OnDateChangedEffect(onDateChanged: () -> Unit) {
    val context = LocalContext.current
    val currentOnDateChanged = rememberUpdatedState(onDateChanged)

    DisposableEffect(context) {
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
        context.registerReceiver(receiver, filter)

        onDispose {
            context.unregisterReceiver(receiver)
        }
    }
}

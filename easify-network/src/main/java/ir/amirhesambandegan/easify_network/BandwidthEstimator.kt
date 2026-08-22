package ir.amirhesambandegan.easify_network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay

enum class NetworkBandwidth {
    OFFLINE, POOR, MODERATE, GOOD, EXCELLENT
}

@Composable
fun rememberBandwidthState(): State<NetworkBandwidth> {
    val context = LocalContext.current
    return produceState(initialValue = NetworkBandwidth.OFFLINE, context) {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        
        while (true) {
            val network = cm.activeNetwork
            val capabilities = cm.getNetworkCapabilities(network)
            
            if (capabilities == null || !capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                value = NetworkBandwidth.OFFLINE
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val bandwidthKbps = capabilities.linkDownstreamBandwidthKbps
                    value = when {
                        bandwidthKbps < 500 -> NetworkBandwidth.POOR // < 0.5 Mbps
                        bandwidthKbps < 2000 -> NetworkBandwidth.MODERATE // < 2 Mbps
                        bandwidthKbps < 10000 -> NetworkBandwidth.GOOD // < 10 Mbps
                        else -> NetworkBandwidth.EXCELLENT
                    }
                } else {
                    value = NetworkBandwidth.GOOD // Fallback
                }
            }
            delay(2000)
        }
    }
}

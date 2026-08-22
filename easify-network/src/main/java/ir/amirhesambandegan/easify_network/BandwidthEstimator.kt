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

/**
 * Represents the estimated network bandwidth quality classification.
 */
enum class NetworkBandwidth {
    /**
     * Indicates there is no active internet connection.
     */
    OFFLINE,

    /**
     * Poor connection quality (downstream bandwidth < 0.5 Mbps).
     */
    POOR,

    /**
     * Moderate connection quality (downstream bandwidth between 0.5 Mbps and 2 Mbps).
     */
    MODERATE,

    /**
     * Good connection quality (downstream bandwidth between 2 Mbps and 10 Mbps).
     */
    GOOD,

    /**
     * Excellent connection quality (downstream bandwidth >= 10 Mbps).
     */
    EXCELLENT
}

/**
 * A Composable function that continuously monitors and estimates the current network bandwidth quality.
 *
 * This function periodically samples the active network's downstream bandwidth using [ConnectivityManager]
 * and provides the estimated [NetworkBandwidth] level as a Compose [State].
 *
 * @return A [State] holding the current estimated [NetworkBandwidth].
 */
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

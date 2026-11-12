package ir.amirhesambandegan.easify_network

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * Represents the estimated network bandwidth quality classification.
 */
enum class NetworkBandwidth {
    OFFLINE,
    POOR,
    MODERATE,
    GOOD,
    EXCELLENT
}

/**
 * Event-driven Composable monitoring downstream bandwidth via [ConnectivityManager] callbacks
 * without periodic timer polling.
 */
@Composable
fun rememberBandwidthState(): State<NetworkBandwidth> {
    val appContext = LocalContext.current.applicationContext
    return produceState(initialValue = NetworkBandwidth.OFFLINE, appContext) {
        val cm = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        fun classify(capabilities: NetworkCapabilities?): NetworkBandwidth {
            if (capabilities == null ||
                !capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            ) {
                return NetworkBandwidth.OFFLINE
            }
            val bandwidthKbps = capabilities.linkDownstreamBandwidthKbps
            return when {
                bandwidthKbps < 500 -> NetworkBandwidth.POOR
                bandwidthKbps < 2000 -> NetworkBandwidth.MODERATE
                bandwidthKbps < 10000 -> NetworkBandwidth.GOOD
                else -> NetworkBandwidth.EXCELLENT
            }
        }

        callbackFlow {
            fun hasNetworkStatePermission(): Boolean =
                ContextCompat.checkSelfPermission(
                    appContext,
                    Manifest.permission.ACCESS_NETWORK_STATE
                ) == PackageManager.PERMISSION_GRANTED

            if (!hasNetworkStatePermission()) {
                trySend(NetworkBandwidth.OFFLINE)
                awaitClose {}
                return@callbackFlow
            }

            val current = cm.activeNetwork?.let { cm.getNetworkCapabilities(it) }
            trySend(classify(current))

            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    trySend(classify(networkCapabilities))
                }

                override fun onLost(network: Network) {
                    if (!hasNetworkStatePermission()) {
                        trySend(NetworkBandwidth.OFFLINE)
                        return
                    }
                    val active = cm.activeNetwork?.let { cm.getNetworkCapabilities(it) }
                    trySend(classify(active))
                }
            }

            cm.registerDefaultNetworkCallback(callback)
            awaitClose { runCatching { cm.unregisterNetworkCallback(callback) } }
        }
            .distinctUntilChanged()
            .collect { value = it }
    }
}

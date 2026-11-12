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
 * Event-driven Composable observing VPN transport and captive portal status
 * using [ConnectivityManager] callbacks.
 */
@Composable
fun rememberNetworkSecurityStatus(): State<NetworkSecurityStatus> {
    val appContext = LocalContext.current.applicationContext
    return produceState(initialValue = NetworkSecurityStatus(false, false), appContext) {
        val cm = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        fun inspect(capabilities: NetworkCapabilities?): NetworkSecurityStatus {
            if (capabilities == null) return NetworkSecurityStatus(false, false)
            return NetworkSecurityStatus(
                isVpnActive = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN),
                isCaptivePortal = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_CAPTIVE_PORTAL)
            )
        }

        callbackFlow {
            fun hasNetworkStatePermission(): Boolean =
                ContextCompat.checkSelfPermission(
                    appContext,
                    Manifest.permission.ACCESS_NETWORK_STATE
                ) == PackageManager.PERMISSION_GRANTED

            if (!hasNetworkStatePermission()) {
                trySend(NetworkSecurityStatus(false, false))
                awaitClose {}
                return@callbackFlow
            }

            trySend(inspect(cm.activeNetwork?.let { cm.getNetworkCapabilities(it) }))

            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    trySend(inspect(networkCapabilities))
                }

                override fun onLost(network: Network) {
                    if (!hasNetworkStatePermission()) {
                        trySend(NetworkSecurityStatus(false, false))
                        return
                    }
                    trySend(inspect(cm.activeNetwork?.let { cm.getNetworkCapabilities(it) }))
                }
            }

            cm.registerDefaultNetworkCallback(callback)
            awaitClose { runCatching { cm.unregisterNetworkCallback(callback) } }
        }
            .distinctUntilChanged()
            .collect { value = it }
    }
}

/**
 * Security and captive portal state of the active network.
 */
data class NetworkSecurityStatus(
    val isVpnActive: Boolean,
    val isCaptivePortal: Boolean
)

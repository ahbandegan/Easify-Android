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
 * Analyzes the network for VPN usage and captive portals (e.g., hotel or airport Wi-Fi login screens).
 *
 * Continuously polls the active network's capabilities every 2 seconds and provides the updated
 * [NetworkSecurityStatus] as a Compose [State].
 *
 * @return A [State] holding the latest [NetworkSecurityStatus].
 */
@Composable
fun rememberNetworkSecurityStatus(): State<NetworkSecurityStatus> {
    val context = LocalContext.current
    return produceState(initialValue = NetworkSecurityStatus(false, false), context) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        
        while (true) {
            var vpnActive = false
            var captivePortal = false
            
            val activeNetwork = connectivityManager.activeNetwork
            if (activeNetwork != null) {
                val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
                if (capabilities != null) {
                    vpnActive = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
                    
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        captivePortal = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_CAPTIVE_PORTAL)
                    }
                }
            }
            
            value = NetworkSecurityStatus(vpnActive, captivePortal)
            delay(2000) // Poll every 2 seconds
        }
    }
}

/**
 * Data class holding the security and captive portal state of the current network.
 *
 * @property isVpnActive `true` if the active network uses a VPN transport; `false` otherwise.
 * @property isCaptivePortal `true` if the active network requires user login/portal authentication; `false` otherwise.
 */
data class NetworkSecurityStatus(
    val isVpnActive: Boolean,
    val isCaptivePortal: Boolean
)

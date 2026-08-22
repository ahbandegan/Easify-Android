package ir.amirhesambandegan.easify_network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * Observes the network connectivity status of the device using Android's [ConnectivityManager].
 *
 * Provides a reactive [Flow] that emits connectivity changes in real time.
 *
 * @constructor Creates a [ConnectivityObserver] instance with the specified Android [context].
 * @param context The [Context] used to retrieve the system [ConnectivityManager].
 */
class ConnectivityObserver(
    context: Context
) {

    /**
     * System [ConnectivityManager] instance for monitoring network state.
     */
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    /**
     * A [Flow] that emits `true` when the device is online and `false` otherwise.
     *
     * It emits the initial network state immediately upon collection and emits updates whenever
     * the network connection becomes available or is lost, filtering out duplicate consecutive emissions.
     */
    val online: Flow<Boolean> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                trySend(true)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                trySend(false)
            }
        }

        // Emit initial state
        trySend(isCurrentlyOnline())

        connectivityManager.registerDefaultNetworkCallback(callback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.distinctUntilChanged()

    /**
     * Checks if the device is currently connected to a validated internet network.
     *
     * @return `true` if an active network is present with both [NetworkCapabilities.NET_CAPABILITY_INTERNET]
     * and [NetworkCapabilities.NET_CAPABILITY_VALIDATED] capabilities; `false` otherwise.
     */
    private fun isCurrentlyOnline(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}

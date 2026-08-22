package ir.amirhesambandegan.easify_network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.net.InetSocketAddress
import java.net.Socket

/**
 * Periodically measures real network latency (round-trip time in milliseconds) by opening a socket connection to a host.
 *
 * Emits the measured latency in milliseconds on [Dispatchers.IO]. If the connection fails or times out (2000ms),
 * it emits `-1` indicating an offline or unreachable host.
 *
 * @param host The remote host or IP address to connect to. Defaults to Google DNS (`"8.8.8.8"`).
 * @param port The port number on the remote host. Defaults to `53` (DNS port).
 * @param intervalMs The delay interval between subsequent ping attempts in milliseconds. Defaults to `3000L` (3 seconds).
 * @return A cold [Flow] emitting the round-trip latency in milliseconds, or `-1` on failure.
 */
fun observePing(host: String = "8.8.8.8", port: Int = 53, intervalMs: Long = 3000): Flow<Int> = flow {
    while (true) {
        val start = System.currentTimeMillis()
        var ping = -1
        try {
            Socket().use { socket ->
                socket.connect(InetSocketAddress(host, port), 2000)
                ping = (System.currentTimeMillis() - start).toInt()
            }
        } catch (e: Exception) {
            ping = -1 // Error / Offline
        }
        
        emit(ping)
        delay(intervalMs)
    }
}.flowOn(Dispatchers.IO)

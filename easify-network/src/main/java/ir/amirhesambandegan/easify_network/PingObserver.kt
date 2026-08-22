package ir.amirhesambandegan.easify_network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.net.InetSocketAddress
import java.net.Socket

/**
 * Pings a server periodically to measure actual latency (ms).
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

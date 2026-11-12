package ir.amirhesambandegan.easify_network

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.net.InetSocketAddress
import java.net.Socket

/**
 * Periodically measures network TCP RTT latency to [host]:[port] on [Dispatchers.IO].
 * Emits `-1` when unreachable or timed out, while properly propagating coroutine cancellation.
 */
fun observePing(host: String = "8.8.8.8", port: Int = 53, intervalMs: Long = 3000): Flow<Int> = flow {
    val safeInterval = intervalMs.coerceAtLeast(100L)
    while (true) {
        val start = System.currentTimeMillis()
        val ping = try {
            Socket().use { socket ->
                socket.connect(InetSocketAddress(host, port), 2000)
                (System.currentTimeMillis() - start).toInt()
            }
        } catch (e: CancellationException) {
            throw e
        } catch (_: Exception) {
            -1
        }

        emit(ping)
        delay(safeInterval)
    }
}.flowOn(Dispatchers.IO)

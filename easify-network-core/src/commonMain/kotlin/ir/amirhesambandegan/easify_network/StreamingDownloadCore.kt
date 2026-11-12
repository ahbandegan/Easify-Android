package ir.amirhesambandegan.easify_network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.onDownload
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.isSuccess
import io.ktor.utils.io.readAvailable
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.io.Sink

/**
 * Streams a remote file from [url] directly into a multiplatform [kotlinx.io.Sink] in chunks
 * without buffering the entire payload in memory.
 *
 * Emits integer progress values (`0..100`) on success, or `-1` on HTTP error status or network failure.
 */
fun HttpClient.downloadToSink(url: String, sink: Sink): Flow<Int> = channelFlow {
    try {
        prepareGet(url) {
            onDownload { bytesReceived, contentLength ->
                if (contentLength != null && contentLength > 0L) {
                    val progress = ((bytesReceived * 100L) / contentLength).toInt().coerceIn(0, 99)
                    trySend(progress)
                }
            }
        }.execute { response ->
            if (!response.status.isSuccess()) {
                send(-1)
                return@execute
            }

            val channel = response.bodyAsChannel()
            val buffer = ByteArray(DEFAULT_DOWNLOAD_BUFFER_SIZE)
            while (!channel.isClosedForRead) {
                val bytesRead = channel.readAvailable(buffer, 0, buffer.size)
                if (bytesRead <= 0) {
                    if (channel.isClosedForRead) break
                } else {
                    sink.write(buffer, 0, bytesRead)
                }
            }
            sink.flush()
            send(100)
        }
    } catch (e: CancellationException) {
        throw e
    } catch (_: Exception) {
        trySend(-1)
    }
}

private const val DEFAULT_DOWNLOAD_BUFFER_SIZE = 8192

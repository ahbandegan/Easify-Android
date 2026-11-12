package ir.amirhesambandegan.easify_network

import io.ktor.client.HttpClient
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.io.asSink
import kotlinx.io.buffered
import java.io.File

/**
 * Streams a remote file from [url] directly into [destFile] in chunks without loading the entire
 * payload into memory, emitting percentage progress values (`0..100`) or `-1` on failure.
 */
fun HttpClient.downloadFile(url: String, destFile: File): Flow<Int> = flow {
    var failed = false
    try {
        destFile.parentFile?.mkdirs()
        destFile.outputStream().asSink().buffered().use { sink ->
            downloadToSink(url, sink).collect { progress ->
                if (progress < 0) failed = true
                emit(progress)
            }
        }
        if (failed && destFile.exists()) {
            destFile.delete()
        }
    } catch (e: CancellationException) {
        if (destFile.exists()) destFile.delete()
        throw e
    } catch (_: Exception) {
        if (destFile.exists()) destFile.delete()
        emit(-1)
    }
}.flowOn(Dispatchers.IO)

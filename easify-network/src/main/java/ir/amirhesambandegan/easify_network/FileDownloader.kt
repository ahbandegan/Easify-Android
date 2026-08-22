package ir.amirhesambandegan.easify_network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.onDownload
import io.ktor.client.request.get
import io.ktor.client.statement.readBytes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

/**
 * Downloads a file and emits the progress (0 to 100).
 */
fun HttpClient.downloadFile(url: String, destFile: File): Flow<Int> = flow {
    try {
        val response = get(url) {
            onDownload { bytesReceived, contentLength ->
                if (contentLength != null && contentLength > 0) {
                    val progress = (bytesReceived * 100f / contentLength).toInt()
                    emit(progress)
                }
            }
        }
        val bytes = response.readBytes()
        destFile.writeBytes(bytes)
        emit(100)
    } catch (e: Exception) {
        e.printStackTrace()
        emit(-1) // Error state
    }
}

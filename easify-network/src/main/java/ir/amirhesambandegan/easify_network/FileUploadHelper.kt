package ir.amirhesambandegan.easify_network

import android.content.Context
import android.net.Uri
import io.ktor.client.HttpClient
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import java.io.FileNotFoundException

@PublishedApi
internal fun sanitizeMultipartFilename(rawName: String?): String {
    val sanitized = rawName
        ?.replace(Regex("[\"\\r\\n\\\\/]"), "_")
        ?.trim()
    return if (sanitized.isNullOrEmpty()) "upload_file" else sanitized
}

/**
 * Uploads a file from an Android [Uri] using `multipart/form-data` with sanitized headers
 * and progress tracking.
 */
suspend inline fun <reified T> HttpClient.uploadFile(
    url: String,
    fileUri: Uri,
    context: Context,
    fileKey: String = "file",
    extraFields: Map<String, String> = emptyMap(),
    crossinline onProgress: (bytesSent: Long, totalBytes: Long) -> Unit = { _, _ -> }
): EasifyResult<T> = safeRequest {
    val inputStream = context.contentResolver.openInputStream(fileUri)
        ?: throw FileNotFoundException("Unable to open input stream for URI: $fileUri")
    val fileBytes = inputStream.use { it.readBytes() }
    val fileName = sanitizeMultipartFilename(fileUri.lastPathSegment)
    val mimeType = context.contentResolver.getType(fileUri) ?: "application/octet-stream"

    post(url) {
        setBody(MultiPartFormDataContent(formData {
            append(fileKey, fileBytes, Headers.build {
                append(HttpHeaders.ContentType, mimeType)
                append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
            })

            extraFields.forEach { (key, value) ->
                append(key, value)
            }
        }))

        onUpload { bytesSent, totalBytes ->
            onProgress(bytesSent, totalBytes ?: 0L)
        }
    }
}

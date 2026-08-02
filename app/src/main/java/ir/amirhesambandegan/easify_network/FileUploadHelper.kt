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

/**
 * Uploads a file from a [Uri] using Multi-part form data with progress tracking.
 *
 * @param url The destination endpoint.
 * @param fileUri The Android Uri of the file to upload.
 * @param context The application context to resolve the Uri.
 * @param fileKey The form key for the file (default is "file").
 * @param extraFields Additional text fields to include in the form.
 * @param onProgress Callback invoked during upload with (bytesSent, totalBytes).
 * @return An [EasifyResult] with the server response.
 */
suspend inline fun <reified T> HttpClient.uploadFile(
    url: String,
    fileUri: Uri,
    context: Context,
    fileKey: String = "file",
    extraFields: Map<String, String> = emptyMap(),
    crossinline onProgress: (bytesSent: Long, totalBytes: Long) -> Unit = { _, _ -> }
): EasifyResult<T> = safeRequest {
    post(url) {
        val inputStream = context.contentResolver.openInputStream(fileUri)
        val fileBytes = inputStream?.use { it.readBytes() } ?: byteArrayOf()
        val fileName = fileUri.lastPathSegment ?: "upload_file"
        val mimeType = context.contentResolver.getType(fileUri) ?: "application/octet-stream"

        setBody(MultiPartFormDataContent(formData {
            // Add the file part
            append(fileKey, fileBytes, Headers.build {
                append(HttpHeaders.ContentType, mimeType)
                append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
            })

            // Add extra text fields
            extraFields.forEach { (key, value) ->
                append(key, value)
            }
        }))

        // Progress tracking
        onUpload { bytesSent, totalBytes ->
            onProgress(bytesSent, totalBytes ?: 0L)
        }
    }
}

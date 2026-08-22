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
 * Uploads a file from an Android [Uri] using multipart/form-data content with progress tracking.
 *
 * This function resolves the file bytes, MIME type, and file name from the provided [fileUri] using
 * the given [context]'s content resolver, constructs a multipart form payload with any [extraFields],
 * and executes a safe HTTP POST request with upload progress updates.
 *
 * @param T The expected deserialized response model type.
 * @receiver The [HttpClient] instance used to execute the request.
 * @param url The remote endpoint URL to which the file will be uploaded.
 * @param fileUri The Android [Uri] referencing the local file or content stream to upload.
 * @param context The Android [Context] used to resolve file streams and MIME types via [android.content.ContentResolver].
 * @param fileKey The form field key associated with the file part. Defaults to `"file"`.
 * @param extraFields A map of additional key-value string pairs to include as form fields in the request body.
 * @param onProgress A callback invoked during upload progress with `(bytesSent, totalBytes)`.
 * @return An [EasifyResult] representing either a [EasifyResult.Success], [EasifyResult.ApiError], or [EasifyResult.NetworkError].
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

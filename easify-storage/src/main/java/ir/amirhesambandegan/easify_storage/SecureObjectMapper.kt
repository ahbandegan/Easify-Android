package ir.amirhesambandegan.easify_storage

import androidx.datastore.preferences.core.Preferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@PublishedApi
internal val easifyStorageGson: Gson = Gson()

/**
 * Serializes, encrypts, and stores a value (including generic collections like `List<T>`) in DataStore.
 */
suspend inline fun <reified T> DataStoreController.saveObjectSecure(
    key: Preferences.Key<String>,
    value: T
): Boolean {
    val jsonString = easifyStorageGson.toJson(value)
    return saveSecure(key, jsonString)
}

/**
 * Reads, decrypts, and deserializes a securely stored value, preserving generic type arguments via [TypeToken].
 */
suspend inline fun <reified T> DataStoreController.getObjectSecure(
    key: Preferences.Key<String>
): T? {
    val jsonString = getSecure(key) ?: return null
    return try {
        val type = object : TypeToken<T>() {}.type
        easifyStorageGson.fromJson<T>(jsonString, type)
    } catch (_: Exception) {
        null
    }
}

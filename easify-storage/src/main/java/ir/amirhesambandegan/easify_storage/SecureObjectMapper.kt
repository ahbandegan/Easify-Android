package ir.amirhesambandegan.easify_storage

import androidx.datastore.preferences.core.Preferences
import com.google.gson.Gson

/**
 * Extension for DataStoreController to save and retrieve complex Objects using Gson.
 */
suspend inline fun <reified T> DataStoreController.saveObjectSecure(key: Preferences.Key<String>, value: T): Boolean {
    val jsonString = Gson().toJson(value)
    return this.saveSecure(key, jsonString)
}

suspend inline fun <reified T> DataStoreController.getObjectSecure(key: Preferences.Key<String>): T? {
    val jsonString = this.getSecure(key) ?: return null
    return try {
        Gson().fromJson(jsonString, T::class.java)
    } catch (e: Exception) {
        null
    }
}

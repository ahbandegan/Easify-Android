package ir.amirhesambandegan.easify_storage

import androidx.datastore.preferences.core.Preferences
import com.google.gson.Gson

/**
 * Extension for DataStoreController to serialize, encrypt, and save complex objects using Gson.
 *
 * @param key The preference key mapped to a String which will hold the encrypted JSON.
 * @param value The complex object instance to securely save.
 * @return True if the operation was successful, false otherwise.
 */
suspend inline fun <reified T> DataStoreController.saveObjectSecure(key: Preferences.Key<String>, value: T): Boolean {
    val jsonString = Gson().toJson(value)
    return this.saveSecure(key, jsonString)
}

/**
 * Extension for DataStoreController to read, decrypt, and deserialize a securely saved complex object using Gson.
 *
 * @param key The preference key mapped to a String which holds the encrypted JSON.
 * @return The deserialized object of type [T], or null if the key doesn't exist or an error occurs.
 */
suspend inline fun <reified T> DataStoreController.getObjectSecure(key: Preferences.Key<String>): T? {
    val jsonString = this.getSecure(key) ?: return null
    return try {
        Gson().fromJson(jsonString, T::class.java)
    } catch (e: Exception) {
        null
    }
}

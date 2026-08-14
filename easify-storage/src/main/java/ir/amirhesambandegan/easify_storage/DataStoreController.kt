package ir.amirhesambandegan.easify_storage

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import ir.amirhesambandegan.easify_security.EncryptionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 * A reactive and secure controller for Jetpack Preferences DataStore.
 */
class DataStoreController(context: Context) {
    private val dataStore = context.dataStore
    private val securityAlias = "easify_storage_key"

    /**
     * Updates the value associated with the given [key].
     * @return True if successful, false otherwise.
     */
    suspend fun <T> update(key: Preferences.Key<T>, value: T): Boolean = try {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }

    /**
     * Retrieves the current value for the [key] once.
     */
    suspend fun <T> get(key: Preferences.Key<T>): T? = try {
        dataStore.data.first()[key]
    } catch (e: Exception) {
        null
    }

    /**
     * Returns a [Flow] that emits the value of the [key] whenever it changes.
     */
    fun <T> observe(key: Preferences.Key<T>): Flow<T?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[key]
        }

    /**
     * Removes the value associated with the [key].
     */
    suspend fun <T> remove(key: Preferences.Key<T>) {
        dataStore.edit { it.remove(key) }
    }

    /**
     * Clears all stored preferences in this DataStore.
     */
    suspend fun clear() {
        dataStore.edit { it.clear() }
    }

    /**
     * Encrypts and saves a string value securely.
     */
    suspend fun saveSecure(key: Preferences.Key<String>, value: String): Boolean {
        val encrypted = EncryptionManager.encrypt(securityAlias, value)
        return update(key, encrypted)
    }

    /**
     * Decrypts and retrieves a securely saved string value.
     */
    suspend fun getSecure(key: Preferences.Key<String>): String? {
        val encrypted = get(key) ?: return null
        return EncryptionManager.decrypt(securityAlias, encrypted)
    }

    /**
     * Returns a [Flow] that emits decrypted string values for a secure key.
     */
    fun observeSecure(key: Preferences.Key<String>): Flow<String?> = observe(key)
        .map { encrypted ->
            encrypted?.let { EncryptionManager.decrypt(securityAlias, it) }
        }
}

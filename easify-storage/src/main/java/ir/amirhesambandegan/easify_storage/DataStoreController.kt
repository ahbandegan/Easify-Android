package ir.amirhesambandegan.easify_storage

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import ir.amirhesambandegan.easify_security.EncryptionManager
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 * Reactive and AES-256-GCM encrypted controller for Jetpack Preferences DataStore.
 */
class DataStoreController(context: Context) {
    private val dataStore = context.applicationContext.dataStore
    private val securityAlias = "easify_storage_key"

    /**
     * Updates the value associated with [key].
     */
    suspend fun <T> update(key: Preferences.Key<T>, value: T): Boolean = try {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
        true
    } catch (e: CancellationException) {
        throw e
    } catch (_: Exception) {
        false
    }

    /**
     * Retrieves the current value for [key] once, or `null` if absent or on error.
     */
    suspend fun <T> get(key: Preferences.Key<T>): T? = try {
        dataStore.data.first()[key]
    } catch (e: CancellationException) {
        throw e
    } catch (_: Exception) {
        null
    }

    /**
     * Returns a [Flow] emitting the value of [key] whenever preferences change.
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
     * Removes [key] from DataStore.
     */
    suspend fun <T> remove(key: Preferences.Key<T>) {
        dataStore.edit { it.remove(key) }
    }

    /**
     * Clears all stored preferences in DataStore.
     */
    suspend fun clear() {
        dataStore.edit { it.clear() }
    }

    /**
     * Encrypts and stores [value] under [key]. Aborts cleanly and returns `false` if encryption fails.
     */
    suspend fun saveSecure(key: Preferences.Key<String>, value: String): Boolean {
        val encrypted = EncryptionManager.encrypt(securityAlias, value)
        if (encrypted.isEmpty()) return false
        return update(key, encrypted)
    }

    /**
     * Retrieves and decrypts the string value stored under [key], or `null` if absent or invalid.
     */
    suspend fun getSecure(key: Preferences.Key<String>): String? {
        val encrypted = get(key) ?: return null
        return EncryptionManager.decrypt(securityAlias, encrypted)
    }

    /**
     * Returns a [Flow] emitting decrypted values for [key] on every preference update.
     */
    fun observeSecure(key: Preferences.Key<String>): Flow<String?> = observe(key)
        .map { encrypted ->
            encrypted?.let { EncryptionManager.decrypt(securityAlias, it) }
        }
}

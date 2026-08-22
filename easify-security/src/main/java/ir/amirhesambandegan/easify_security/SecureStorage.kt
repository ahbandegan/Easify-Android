package ir.amirhesambandegan.easify_security

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

/**
 * A utility object for securely storing data using [EncryptedSharedPreferences].
 */
object SecureStorage {

    /**
     * Creates and returns an instance of [EncryptedSharedPreferences] for secure data storage.
     * It uses AES256 for both key (SIV) and value (GCM) encryption.
     *
     * @param context The application context.
     * @param fileName The name of the preferences file. Defaults to "secure_prefs".
     * @return An instance of [SharedPreferences] that transparently encrypts and decrypts data.
     */
    fun getEncryptedPreferences(context: Context, fileName: String = "secure_prefs"): SharedPreferences {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        return EncryptedSharedPreferences.create(
            fileName,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}

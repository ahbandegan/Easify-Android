package ir.amirhesambandegan.easify_storage

import android.content.Context
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import java.io.File

/**
 * A utility object providing methods to safely read and write encrypted files
 * utilizing Android's Jetpack Security Crypto library.
 */
object SecureFileStorage {

    /**
     * Initializes and returns an [EncryptedFile] instance for the provided file,
     * configured to use AES256 GCM encryption scheme.
     *
     * @param context The application context.
     * @param file The underlying file intended to be encrypted.
     * @return A fully configured [EncryptedFile] instance.
     */
    fun getEncryptedFile(context: Context, file: File): EncryptedFile {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        return EncryptedFile.Builder(
            file,
            context,
            masterKeyAlias,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()
    }
    
    /**
     * Encrypts and writes a string of text securely into a given file.
     *
     * @param context The application context.
     * @param file The target file where the encrypted content will be stored.
     * @param content The plain-text string content to encrypt and save.
     */
    fun writeSecureString(context: Context, file: File, content: String) {
        val encryptedFile = getEncryptedFile(context, file)
        encryptedFile.openFileOutput().use { outputStream ->
            outputStream.write(content.toByteArray(Charsets.UTF_8))
        }
    }
    
    /**
     * Reads and decrypts a string of text from a securely encrypted file.
     *
     * @param context The application context.
     * @param file The encrypted file containing the data.
     * @return The decrypted plain-text string read from the file.
     */
    fun readSecureString(context: Context, file: File): String {
        val encryptedFile = getEncryptedFile(context, file)
        return encryptedFile.openFileInput().use { inputStream ->
            inputStream.bufferedReader(Charsets.UTF_8).readText()
        }
    }
}

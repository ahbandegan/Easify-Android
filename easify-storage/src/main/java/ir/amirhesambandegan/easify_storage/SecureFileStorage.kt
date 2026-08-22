package ir.amirhesambandegan.easify_storage

import android.content.Context
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import java.io.File

object SecureFileStorage {

    fun getEncryptedFile(context: Context, file: File): EncryptedFile {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        return EncryptedFile.Builder(
            file,
            context,
            masterKeyAlias,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()
    }
    
    fun writeSecureString(context: Context, file: File, content: String) {
        val encryptedFile = getEncryptedFile(context, file)
        encryptedFile.openFileOutput().use { outputStream ->
            outputStream.write(content.toByteArray(Charsets.UTF_8))
        }
    }
    
    fun readSecureString(context: Context, file: File): String {
        val encryptedFile = getEncryptedFile(context, file)
        return encryptedFile.openFileInput().use { inputStream ->
            inputStream.bufferedReader(Charsets.UTF_8).readText()
        }
    }
}

package ir.amirhesambandegan.easify_storage

import android.content.Context
import java.io.File

/**
 * A utility object to monitor and manage the application's storage and cache.
 */
object AppStorageMonitor {

    /**
     * Calculates the total size of the application's internal and external cache directories.
     *
     * @param context The application context used to access cache directories.
     * @return The total size of the cache directories in bytes.
     */
    fun getAppCacheSize(context: Context): Long {
        return getFolderSize(context.cacheDir) + getFolderSize(context.externalCacheDir)
    }

    /**
     * Clears both the internal and external cache directories of the application.
     *
     * @param context The application context used to access cache directories.
     */
    fun clearAppCache(context: Context) {
        deleteFolder(context.cacheDir)
        deleteFolder(context.externalCacheDir)
    }

    /**
     * Recursively calculates the size of a given file or directory.
     *
     * @param file The file or directory to calculate the size for.
     * @return The size of the file or directory in bytes.
     */
    private fun getFolderSize(file: File?): Long {
        if (file == null || !file.exists()) return 0
        if (!file.isDirectory) return file.length()
        return file.listFiles()?.sumOf { getFolderSize(it) } ?: 0L
    }

    /**
     * Recursively deletes a given file or directory.
     *
     * @param file The file or directory to delete.
     * @return True if the deletion was successful or if the file doesn't exist, false otherwise.
     */
    private fun deleteFolder(file: File?): Boolean {
        if (file == null || !file.exists()) return true
        if (file.isDirectory) {
            file.listFiles()?.forEach { deleteFolder(it) }
        }
        return file.delete()
    }
}

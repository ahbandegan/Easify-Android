package ir.amirhesambandegan.easify_storage

import android.content.Context
import java.io.File

object AppStorageMonitor {

    fun getAppCacheSize(context: Context): Long {
        return getFolderSize(context.cacheDir) + getFolderSize(context.externalCacheDir)
    }

    fun clearAppCache(context: Context) {
        deleteFolder(context.cacheDir)
        deleteFolder(context.externalCacheDir)
    }

    private fun getFolderSize(file: File?): Long {
        if (file == null || !file.exists()) return 0
        if (!file.isDirectory) return file.length()
        return file.listFiles()?.sumOf { getFolderSize(it) } ?: 0L
    }

    private fun deleteFolder(file: File?): Boolean {
        if (file == null || !file.exists()) return true
        if (file.isDirectory) {
            file.listFiles()?.forEach { deleteFolder(it) }
        }
        return file.delete()
    }
}

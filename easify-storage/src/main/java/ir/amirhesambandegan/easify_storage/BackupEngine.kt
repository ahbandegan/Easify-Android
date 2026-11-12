package ir.amirhesambandegan.easify_storage

import android.content.Context
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * Handles creating ZIP backups of application databases, DataStore, and shared preferences.
 */
object BackupEngine {

    /**
     * Creates a backup ZIP archive containing the app's `databases`, `datastore`, and `shared_prefs`
     * directories at [outputFile].
     *
     * Safely skips [outputFile] itself if located inside app data directories, normalizes ZIP paths
     * across platforms, and writes via a temporary file to prevent partial/corrupted archives.
     */
    fun exportAppBackup(context: Context, outputFile: File): Boolean {
        outputFile.parentFile?.mkdirs()
        val tempFile = File(outputFile.parentFile, ".${outputFile.name}.tmp")
        val targetCanonical = outputFile.canonicalFile
        val tempCanonical = tempFile.canonicalFile

        return try {
            val dataDir = File(context.applicationContext.applicationInfo.dataDir).canonicalFile
            val dirsToBackup = listOf(
                File(dataDir, "databases"),
                File(dataDir, "datastore"),
                File(dataDir, "shared_prefs")
            )

            FileOutputStream(tempFile).use { fos ->
                ZipOutputStream(fos).use { zos ->
                    dirsToBackup.filter { it.exists() && it.isDirectory }.forEach { dir ->
                        dir.walkTopDown().forEach { file ->
                            val canonical = file.canonicalFile
                            if (file.isFile && canonical != targetCanonical && canonical != tempCanonical) {
                                val entryName = canonical.toRelativeString(dataDir).replace('\\', '/')
                                zos.putNextEntry(ZipEntry(entryName))
                                FileInputStream(canonical).use { fis -> fis.copyTo(zos) }
                                zos.closeEntry()
                            }
                        }
                    }
                }
            }

            if (outputFile.exists()) outputFile.delete()
            if (!tempFile.renameTo(outputFile)) {
                tempFile.copyTo(outputFile, overwrite = true)
                tempFile.delete()
            }
            true
        } catch (_: Exception) {
            tempFile.delete()
            false
        }
    }
}

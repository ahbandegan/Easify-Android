package ir.amirhesambandegan.easify_storage

import android.content.Context
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * A utility object that handles the backup and export of the application's local data.
 */
object BackupEngine {

    /**
     * Creates a standard backup zip archive containing the application's
     * databases, DataStore, and shared preferences.
     *
     * @param context The application context used to locate internal data directories.
     * @param outputFile The destination file where the backup zip will be saved.
     * @return True if the backup was created successfully, false if an error occurred.
     */
    fun exportAppBackup(context: Context, outputFile: File): Boolean {
        return try {
            FileOutputStream(outputFile).use { fos ->
                ZipOutputStream(fos).use { zos ->
                    val dataDir = File(context.applicationInfo.dataDir)
                    
                    val dirsToBackup = listOf(
                        File(dataDir, "databases"),
                        File(dataDir, "datastore"),
                        File(dataDir, "shared_prefs")
                    )
                    
                    dirsToBackup.filter { it.exists() }.forEach { dir ->
                        dir.walkTopDown().forEach { file ->
                            if (file.isFile) {
                                val entryName = file.absolutePath.substringAfter(dataDir.absolutePath + "/")
                                zos.putNextEntry(ZipEntry(entryName))
                                FileInputStream(file).use { fis -> fis.copyTo(zos) }
                                zos.closeEntry()
                            }
                        }
                    }
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}

package ir.amirhesambandegan.easify_storage

import android.content.Context
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object BackupEngine {

    /**
     * Creates a standard backup zip of the DataStore and Databases.
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

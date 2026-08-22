package ir.amirhesambandegan.easify_file

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

fun List<File>.compressToZip(zipFile: File): Boolean {
    return try {
        ZipOutputStream(FileOutputStream(zipFile)).use { zos ->
            this.forEach { file ->
                if (file.exists() && file.isFile) {
                    val entry = ZipEntry(file.name)
                    zos.putNextEntry(entry)
                    FileInputStream(file).use { fis ->
                        fis.copyTo(zos)
                    }
                    zos.closeEntry()
                }
            }
        }
        true
    } catch (e: Exception) {
        false
    }
}

fun File.unzipTo(targetFolder: File): Boolean {
    return try {
        if (!targetFolder.exists()) targetFolder.mkdirs()
        ZipInputStream(FileInputStream(this)).use { zis ->
            var zipEntry: ZipEntry? = zis.nextEntry
            while (zipEntry != null) {
                val newFile = File(targetFolder, zipEntry.name)
                if (zipEntry.isDirectory) {
                    newFile.mkdirs()
                } else {
                    newFile.parentFile?.mkdirs()
                    FileOutputStream(newFile).use { fos ->
                        zis.copyTo(fos)
                    }
                }
                zipEntry = zis.nextEntry
            }
            zis.closeEntry()
        }
        true
    } catch (e: Exception) {
        false
    }
}

package ir.amirhesambandegan.easify_file

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

/**
 * Compresses the files in this list into [zipFile]. Skips missing or duplicate filenames cleanly.
 */
fun List<File>.compressToZip(zipFile: File): Boolean {
    return try {
        zipFile.parentFile?.mkdirs()
        val addedNames = HashSet<String>()
        ZipOutputStream(FileOutputStream(zipFile)).use { zos ->
            forEach { file ->
                if (file.exists() && file.isFile && addedNames.add(file.name)) {
                    zos.putNextEntry(ZipEntry(file.name))
                    FileInputStream(file).use { fis ->
                        fis.copyTo(zos)
                    }
                    zos.closeEntry()
                }
            }
        }
        true
    } catch (_: Exception) {
        false
    }
}

/**
 * Extracts a ZIP archive into [targetFolder] with strict **Zip Slip** canonical path validation.
 */
fun File.unzipTo(targetFolder: File): Boolean {
    return try {
        if (!targetFolder.exists()) targetFolder.mkdirs()
        val canonicalTarget = targetFolder.canonicalFile
        val canonicalPrefix = canonicalTarget.path + File.separator

        ZipInputStream(FileInputStream(this)).use { zis ->
            var zipEntry: ZipEntry? = zis.nextEntry
            while (zipEntry != null) {
                val destFile = File(canonicalTarget, zipEntry.name)
                val canonicalDest = destFile.canonicalFile
                if (!canonicalDest.path.startsWith(canonicalPrefix) && canonicalDest != canonicalTarget) {
                    throw SecurityException("Blocked Zip Slip path traversal entry: ${zipEntry.name}")
                }

                if (zipEntry.isDirectory) {
                    canonicalDest.mkdirs()
                } else {
                    canonicalDest.parentFile?.mkdirs()
                    FileOutputStream(canonicalDest).use { fos ->
                        zis.copyTo(fos)
                    }
                }
                zis.closeEntry()
                zipEntry = zis.nextEntry
            }
        }
        true
    } catch (_: Exception) {
        false
    }
}

package ir.amirhesambandegan.easify_storage

import android.content.Context
import java.io.File

object SmartCacheManager {

    fun saveWithTTL(context: Context, key: String, data: String, ttlMillis: Long) {
        val cacheFile = File(context.cacheDir, "$key.cache")
        val expiryTime = System.currentTimeMillis() + ttlMillis
        cacheFile.writeText("$expiryTime\n$data")
    }

    fun getOrNull(context: Context, key: String): String? {
        val cacheFile = File(context.cacheDir, "$key.cache")
        if (!cacheFile.exists()) return null
        
        val lines = cacheFile.readLines()
        if (lines.isEmpty()) return null
        
        val expiryTime = lines.first().toLongOrNull() ?: 0L
        if (System.currentTimeMillis() > expiryTime) {
            cacheFile.delete()
            return null
        }
        
        return lines.drop(1).joinToString("\n")
    }
    
    fun cleanExpiredCaches(context: Context) {
        context.cacheDir.listFiles()?.forEach { file ->
            if (file.name.endsWith(".cache")) {
                val lines = file.readLines()
                val expiryTime = lines.firstOrNull()?.toLongOrNull() ?: 0L
                if (System.currentTimeMillis() > expiryTime) {
                    file.delete()
                }
            }
        }
    }
}

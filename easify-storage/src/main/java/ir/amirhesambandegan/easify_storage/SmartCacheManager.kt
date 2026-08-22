package ir.amirhesambandegan.easify_storage

import android.content.Context
import java.io.File

/**
 * A utility object to handle transient cache storage with Time-To-Live (TTL) expiration rules.
 */
object SmartCacheManager {

    /**
     * Saves a specific string of data to a cache file mapped to the given key, 
     * associating it with a Time-To-Live (TTL).
     *
     * @param context The application context used to access the cache directory.
     * @param key A unique identifier for this cached data.
     * @param data The string data to be stored.
     * @param ttlMillis The Time-To-Live duration in milliseconds.
     */
    fun saveWithTTL(context: Context, key: String, data: String, ttlMillis: Long) {
        val cacheFile = File(context.cacheDir, "$key.cache")
        val expiryTime = System.currentTimeMillis() + ttlMillis
        cacheFile.writeText("$expiryTime\n$data")
    }

    /**
     * Retrieves cached string data associated with the specified key if it has not yet expired.
     * If the cache entry is expired, it is automatically deleted.
     *
     * @param context The application context used to access the cache directory.
     * @param key The unique identifier for the cached data to fetch.
     * @return The cached string data, or null if it doesn't exist or has expired.
     */
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
    
    /**
     * Sweeps the application's cache directory and proactively deletes any ".cache" 
     * files that have surpassed their calculated expiration time.
     *
     * @param context The application context used to access the cache directory.
     */
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

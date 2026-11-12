package ir.amirhesambandegan.easify_storage

import android.content.Context
import java.io.File
import java.security.MessageDigest

/**
 * Handles transient cache storage with Time-To-Live (TTL) expiration rules.
 * Cache keys are hashed with SHA-256 to eliminate path-traversal risks and illegal filename characters.
 */
object SmartCacheManager {

    private fun cacheFileFor(context: Context, key: String): File {
        val digest = MessageDigest.getInstance("SHA-256")
            .digest(key.toByteArray(Charsets.UTF_8))
            .joinToString("") { "%02x".format(it) }
        return File(context.applicationContext.cacheDir, "$digest.cache")
    }

    /**
     * Saves string [data] mapped to [key] with an expiration duration of [ttlMillis].
     */
    fun saveWithTTL(context: Context, key: String, data: String, ttlMillis: Long) {
        if (ttlMillis <= 0L) {
            invalidate(context, key)
            return
        }
        val cacheFile = cacheFileFor(context, key)
        val expiryTime = System.currentTimeMillis() + ttlMillis
        cacheFile.writeText("$expiryTime\n$data", Charsets.UTF_8)
    }

    /**
     * Retrieves cached data for [key] if present and not expired, automatically removing expired entries.
     */
    fun getOrNull(context: Context, key: String): String? {
        val cacheFile = cacheFileFor(context, key)
        if (!cacheFile.exists() || !cacheFile.isFile) return null

        return cacheFile.bufferedReader(Charsets.UTF_8).use { reader ->
            val firstLine = reader.readLine() ?: return null
            val expiryTime = firstLine.toLongOrNull() ?: 0L
            if (System.currentTimeMillis() > expiryTime) {
                reader.close()
                cacheFile.delete()
                return null
            }
            reader.readText()
        }
    }

    /**
     * Deletes the cached entry for [key], if it exists.
     */
    fun invalidate(context: Context, key: String): Boolean {
        val cacheFile = cacheFileFor(context, key)
        return !cacheFile.exists() || cacheFile.delete()
    }

    /**
     * Sweeps the cache directory and deletes any `.cache` files whose TTL has expired
     * without loading the full payload into memory.
     */
    fun cleanExpiredCaches(context: Context) {
        val now = System.currentTimeMillis()
        context.applicationContext.cacheDir.listFiles()?.forEach { file ->
            if (file.isFile && file.name.endsWith(".cache")) {
                val expiryTime = runCatching {
                    file.bufferedReader(Charsets.UTF_8).use { it.readLine()?.toLongOrNull() }
                }.getOrNull() ?: 0L
                if (now > expiryTime) {
                    file.delete()
                }
            }
        }
    }
}

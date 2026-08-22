package ir.amirhesambandegan.easify_network

import android.content.Context
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.cache.HttpCache

/**
 * Configures an offline-first / cache-first caching strategy on the [HttpClientConfig].
 *
 * Installs Ktor's [HttpCache] plugin to enable automatic caching of HTTP responses in memory.
 *
 * @receiver The [HttpClientConfig] to install the cache plugin on.
 * @param context The Android [Context] (reserved for persistent cache storage integrations).
 */
fun HttpClientConfig<*>.installOfflineFirstCache(context: Context) {
    install(HttpCache) {
        // In Ktor 3.x, you can configure storage here if you include ktor-client-cache.
        // For simplicity, we just install the default memory cache.
    }
}

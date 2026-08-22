package ir.amirhesambandegan.easify_network

import android.content.Context
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.cache.HttpCache

/**
 * Configures Offline-First / Cache-First strategy.
 */
fun HttpClientConfig<*>.installOfflineFirstCache(context: Context) {
    install(HttpCache) {
        // In Ktor 3.x, you can configure storage here if you include ktor-client-cache.
        // For simplicity, we just install the default memory cache.
    }
}

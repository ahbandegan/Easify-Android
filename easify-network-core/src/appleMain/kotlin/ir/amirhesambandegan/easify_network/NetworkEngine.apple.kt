package ir.amirhesambandegan.easify_network

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.darwin.Darwin

internal actual fun platformDefaultHttpEngine(): HttpClientEngineFactory<*>? = Darwin

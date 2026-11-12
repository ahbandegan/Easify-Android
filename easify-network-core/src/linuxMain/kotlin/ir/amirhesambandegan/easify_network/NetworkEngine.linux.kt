package ir.amirhesambandegan.easify_network

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.cio.CIO

internal actual fun platformDefaultHttpEngine(): HttpClientEngineFactory<*>? = CIO

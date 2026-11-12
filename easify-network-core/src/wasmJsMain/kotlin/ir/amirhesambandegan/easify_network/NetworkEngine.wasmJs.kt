package ir.amirhesambandegan.easify_network

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.js.Js

internal actual fun platformDefaultHttpEngine(): HttpClientEngineFactory<*>? = Js

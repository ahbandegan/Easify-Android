package ir.amirhesambandegan.easify_lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.mutableStateOf

/**
 * A composable side-effect that guarantees the provided block runs strictly once across the lifespan
 * of the host composable, surviving recompositions.
 *
 * It uses [rememberSaveable] to store a boolean flag indicating whether the block has already been executed.
 *
 * @param key An optional key to uniquely identify this execution state. Defaults to `"run_once"`.
 * @param block The suspending block to execute exactly once.
 */
@Composable
fun OnFirstCreateEffect(key: String = "run_once", block: suspend () -> Unit) {
    val hasRun = rememberSaveable(key) { mutableStateOf(false) }
    
    if (!hasRun.value) {
        LaunchedEffect(key1 = Unit) {
            block()
            hasRun.value = true
        }
    }
}

package ir.amirhesambandegan.easify_lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable

/**
 * Multiplatform composable side-effect that guarantees [block] runs at most once per saved [key].
 */
@Composable
fun OnFirstCreateEffect(
    key: String = "run_once",
    block: suspend () -> Unit
) {
    val hasRun = rememberSaveable(key) { mutableStateOf(false) }
    val currentBlock by rememberUpdatedState(block)

    if (!hasRun.value) {
        LaunchedEffect(key, hasRun.value) {
            currentBlock()
            hasRun.value = true
        }
    }
}

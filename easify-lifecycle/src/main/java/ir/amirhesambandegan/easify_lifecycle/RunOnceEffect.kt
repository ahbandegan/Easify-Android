package ir.amirhesambandegan.easify_lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.mutableStateOf

/**
 * Guarantees that the block runs strictly ONCE across the lifespan of the host 
 * (survives Recomposition).
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

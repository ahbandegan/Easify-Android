package ir.amirhesambandegan.easify_security

import android.view.Window
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.ObserverModifierNode
import androidx.compose.ui.node.currentValueOf
import androidx.compose.ui.node.observeReads
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.LocalContext
import ir.amirhesambandegan.easify_context.findActivity
import java.util.WeakHashMap

internal object WindowSecureFlagTracker {
    private data class State(var refCount: Int, val originallySecure: Boolean)

    private val windowStates = WeakHashMap<Window, State>()

    fun acquire(window: Window?) {
        if (window == null) return
        synchronized(this) {
            val state = windowStates[window]
            if (state == null) {
                val hadFlag = (window.attributes.flags and WindowManager.LayoutParams.FLAG_SECURE) != 0
                windowStates[window] = State(refCount = 1, originallySecure = hadFlag)
                window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
            } else {
                state.refCount++
                window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
            }
        }
    }

    fun release(window: Window?) {
        if (window == null) return
        synchronized(this) {
            val state = windowStates[window] ?: return
            state.refCount = (state.refCount - 1).coerceAtLeast(0)
            if (state.refCount == 0) {
                windowStates.remove(window)
                if (!state.originallySecure) {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
                }
            }
        }
    }
}

/**
 * Prevents screenshots, screen recording, and unverified casting while in composition.
 * Uses reference counting per [Window] so overlapping shields never clear each other prematurely.
 */
@Composable
fun ScreenShield() {
    val context = LocalContext.current

    DisposableEffect(context) {
        val window = context.findActivity()?.window
        WindowSecureFlagTracker.acquire(window)
        onDispose {
            WindowSecureFlagTracker.release(window)
        }
    }
}

/**
 * Modern [Modifier.Node] modifier preventing screenshots and screen recording while attached.
 */
fun Modifier.screenShield(): Modifier = this then ScreenShieldElement

private data object ScreenShieldElement : ModifierNodeElement<ScreenShieldNode>() {
    override fun create(): ScreenShieldNode = ScreenShieldNode()
    override fun update(node: ScreenShieldNode) = Unit
    override fun InspectorInfo.inspectableProperties() {
        name = "screenShield"
    }
}

private class ScreenShieldNode :
    Modifier.Node(),
    CompositionLocalConsumerModifierNode,
    ObserverModifierNode {
    private var securedWindow: Window? = null

    override fun onAttach() {
        updateWindowShield()
    }

    override fun onObservedReadsChanged() {
        updateWindowShield()
    }

    override fun onDetach() {
        WindowSecureFlagTracker.release(securedWindow)
        securedWindow = null
    }

    private fun updateWindowShield() {
        var window: Window? = null
        observeReads {
            window = currentValueOf(LocalContext).findActivity()?.window
        }
        if (window != securedWindow) {
            WindowSecureFlagTracker.release(securedWindow)
            securedWindow = window
            WindowSecureFlagTracker.acquire(window)
        }
    }
}

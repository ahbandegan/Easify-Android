package ir.amirhesambandegan.easify_ui

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.runtime.Composable
import androidx.compose.ui.composed

/**
 * Mirrors the element horizontally if the current layout direction is [LayoutDirection.Rtl].
 * Useful for icons like back arrows that should face the other way in RTL.
 */
fun Modifier.rtlMirror(): Modifier = composed {
    val layoutDirection = LocalLayoutDirection.current
    if (layoutDirection == LayoutDirection.Rtl) {
        this.scale(scaleX = -1f, scaleY = 1f)
    } else {
        this
    }
}

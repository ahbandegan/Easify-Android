package ir.amirhesambandegan.easify_security

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView

/**
 * Wraps Content inside an Android View that has filterTouchesWhenObscured = true.
 * This prevents Tapjacking attacks where another app draws over this window.
 */
@Composable
fun PreventTapjacking(content: @Composable () -> Unit) {
    AndroidView(
        factory = { context ->
            FrameLayout(context).apply {
                filterTouchesWhenObscured = true
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                val composeView = ComposeView(context).apply {
                    setContent { content() }
                }
                addView(composeView)
            }
        }
    )
}

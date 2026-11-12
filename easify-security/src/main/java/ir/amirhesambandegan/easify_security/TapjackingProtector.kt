package ir.amirhesambandegan.easify_security

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView

/**
 * Wraps [content] inside an Android container with `filterTouchesWhenObscured = true` to block
 * tapjacking attacks while another window overlays the protected UI.
 */
@Composable
fun PreventTapjacking(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val latestContent by rememberUpdatedState(content)

    AndroidView(
        modifier = modifier,
        factory = { context ->
            FrameLayout(context).apply {
                filterTouchesWhenObscured = true
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                val composeView = ComposeView(context).apply {
                    setContent { latestContent() }
                }
                addView(composeView)
            }
        }
    )
}

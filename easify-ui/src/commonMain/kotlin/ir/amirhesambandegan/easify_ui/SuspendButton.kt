package ir.amirhesambandegan.easify_ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Defines the style and appearance of the [SuspendButton].
 */
enum class SuspendButtonType {
    /** A filled button, typically used for primary actions. */
    FILLED,
    /** An elevated button with a shadow, indicating it can be clicked. */
    ELEVATED,
    /** A filled tonal button, offering a softer background than a standard filled button. */
    TONAL,
    /** An outlined button, typically used for secondary actions. */
    OUTLINED,
    /** A text-only button, usually used for less prominent actions. */
    TEXT
}

/**
 * A customizable button that supports Kotlin coroutines and automatic single-flight loading protection.
 *
 * When clicked, it executes [onClick] in a composition-bound [CoroutineScope], prevents overlapping taps
 * both via external [isLoading] and internal in-flight state, and supports customizing its [shape],
 * [colors], [contentPadding], and [type].
 *
 * @param onClick The suspendable action to be executed when the button is clicked.
 * @param modifier The [Modifier] to be applied to the button.
 * @param type The visual style of the button, defined by [SuspendButtonType]. Default is [SuspendButtonType.FILLED].
 * @param isLoading External loading override. When `true`, displays [loadingContent] and disables clicks. Default is `false`.
 * @param enabled Whether the button is enabled and clickable. Overridden while loading or executing. Default is `true`.
 * @param shape Optional custom [Shape] for the button container (e.g., `RoundedCornerShape(12.dp)` or `CircleShape`). When `null`, uses the Material3 default shape for [type].
 * @param colors Optional custom [ButtonColors]. When `null`, uses the Material3 default colors for [type].
 * @param contentPadding The spacing applied internally around the button content. Default is [ButtonDefaults.ContentPadding].
 * @param loadingContent The composable content displayed while loading. Default is a 20.dp [CircularProgressIndicator].
 * @param content The normal composable content (e.g., Text or Icon) to display inside the button.
 */
@Composable
fun SuspendButton(
    onClick: suspend CoroutineScope.() -> Unit,
    modifier: Modifier = Modifier,
    type: SuspendButtonType = SuspendButtonType.FILLED,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    shape: Shape? = null,
    colors: ButtonColors? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    loadingContent: @Composable () -> Unit = {
        CircularProgressIndicator(
            modifier = Modifier.size(20.dp),
            strokeWidth = 2.5.dp
        )
    },
    content: @Composable RowScope.() -> Unit
) {
    val scope = rememberCoroutineScope()
    val latestOnClick by rememberUpdatedState(onClick)
    var isExecuting by remember { mutableStateOf(false) }

    val effectiveLoading = isLoading || isExecuting
    val isButtonEnabled = enabled && !effectiveLoading

    val buttonClick: () -> Unit = {
        if (!effectiveLoading && enabled) {
            isExecuting = true
            scope.launch {
                try {
                    latestOnClick()
                } finally {
                    isExecuting = false
                }
            }
        }
    }

    val buttonContent: @Composable RowScope.() -> Unit = {
        AnimatedContent(
            targetState = effectiveLoading,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "SuspendButtonAnimation"
        ) { loading ->
            if (loading) {
                Box(contentAlignment = Alignment.Center) {
                    loadingContent()
                }
            } else {
                content()
            }
        }
    }

    when (type) {
        SuspendButtonType.FILLED -> Button(
            onClick = buttonClick,
            modifier = modifier,
            enabled = isButtonEnabled,
            shape = shape ?: ButtonDefaults.shape,
            colors = colors ?: ButtonDefaults.buttonColors(),
            contentPadding = contentPadding,
            content = buttonContent
        )
        SuspendButtonType.ELEVATED -> ElevatedButton(
            onClick = buttonClick,
            modifier = modifier,
            enabled = isButtonEnabled,
            shape = shape ?: ButtonDefaults.elevatedShape,
            colors = colors ?: ButtonDefaults.elevatedButtonColors(),
            contentPadding = contentPadding,
            content = buttonContent
        )
        SuspendButtonType.TONAL -> FilledTonalButton(
            onClick = buttonClick,
            modifier = modifier,
            enabled = isButtonEnabled,
            shape = shape ?: ButtonDefaults.filledTonalShape,
            colors = colors ?: ButtonDefaults.filledTonalButtonColors(),
            contentPadding = contentPadding,
            content = buttonContent
        )
        SuspendButtonType.OUTLINED -> OutlinedButton(
            onClick = buttonClick,
            modifier = modifier,
            enabled = isButtonEnabled,
            shape = shape ?: ButtonDefaults.outlinedShape,
            colors = colors ?: ButtonDefaults.outlinedButtonColors(),
            contentPadding = contentPadding,
            content = buttonContent
        )
        SuspendButtonType.TEXT -> TextButton(
            onClick = buttonClick,
            modifier = modifier,
            enabled = isButtonEnabled,
            shape = shape ?: ButtonDefaults.textShape,
            colors = colors ?: ButtonDefaults.textButtonColors(),
            contentPadding = contentPadding,
            content = buttonContent
        )
    }
}

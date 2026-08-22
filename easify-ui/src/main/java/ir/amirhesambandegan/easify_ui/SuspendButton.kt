package ir.amirhesambandegan.easify_ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
 * A customizable button that supports Kotlin coroutines and a loading state.
 * When clicked, it executes a suspendable action, automatically preventing concurrent clicks if it's already loading.
 *
 * @param onClick The suspendable action to be executed when the button is clicked.
 * @param modifier The [Modifier] to be applied to the button.
 * @param type The visual style of the button, defined by [SuspendButtonType]. Default is [SuspendButtonType.FILLED].
 * @param isLoading Determines whether the button should display a loading indicator instead of its content. Default is false.
 * @param enabled Whether the button is enabled and clickable. Overridden by [isLoading] (if loading, it becomes unclickable). Default is true.
 * @param loadingContent The composable content to display when [isLoading] is true. Default is a [CircularProgressIndicator].
 * @param content The normal composable content (e.g., Text or Icon) to display inside the button.
 */
@Composable
fun SuspendButton(
    onClick: suspend CoroutineScope.() -> Unit,
    modifier: Modifier = Modifier,
    type: SuspendButtonType = SuspendButtonType.FILLED,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    loadingContent: @Composable () -> Unit = {
        CircularProgressIndicator(
            modifier = Modifier.size(20.dp),
            strokeWidth = 2.5.dp
        )
    },
    content: @Composable RowScope.() -> Unit
) {
    val scope = rememberCoroutineScope()
    val isButtonEnabled = enabled && !isLoading

    val buttonClick: () -> Unit = {
        if (!isLoading) {
            scope.launch { onClick() }
        }
    }

    val buttonContent: @Composable RowScope.() -> Unit = {
        AnimatedContent(
            targetState = isLoading,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "SuspendButtonAnimation"
        ) { loading ->
            if (loading) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
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
            content = buttonContent
        )
        SuspendButtonType.ELEVATED -> ElevatedButton(
            onClick = buttonClick,
            modifier = modifier,
            enabled = isButtonEnabled,
            content = buttonContent
        )
        SuspendButtonType.TONAL -> FilledTonalButton(
            onClick = buttonClick,
            modifier = modifier,
            enabled = isButtonEnabled,
            content = buttonContent
        )
        SuspendButtonType.OUTLINED -> OutlinedButton(
            onClick = buttonClick,
            modifier = modifier,
            enabled = isButtonEnabled,
            content = buttonContent
        )
        SuspendButtonType.TEXT -> TextButton(
            onClick = buttonClick,
            modifier = modifier,
            enabled = isButtonEnabled,
            content = buttonContent
        )
    }
}
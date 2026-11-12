package ir.amirhesambandegan.easify_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp

/**
 * A composable that provides horizontal swipe-to-action functionality for its content,
 * supporting distinct actions and visuals for start-to-end and end-to-start swipes.
 *
 * Uses the modern Material3 `SwipeToDismissBox` state transition API and optionally snaps back
 * to the settled position via [resetAfterAction].
 *
 * @param modifier The [Modifier] to be applied to the swipeable box.
 * @param onSwipeRight An optional callback invoked when swiping start-to-end.
 * @param onSwipeLeft An optional callback invoked when swiping end-to-start.
 * @param rightIcon The optional icon shown during start-to-end swipe.
 * @param leftIcon The optional icon shown during end-to-start swipe.
 * @param rightBackgroundColor Background color shown during a start-to-end swipe.
 * @param leftBackgroundColor Background color shown during an end-to-start swipe.
 * @param rightIconColor Tint color of [rightIcon].
 * @param leftIconColor Tint color of [leftIcon].
 * @param resetAfterAction Whether the box snaps back after invoking the swipe callback. Default is `true`.
 * @param content The foreground composable content that can be swiped.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EasifySwipeToAction(
    modifier: Modifier = Modifier,
    onSwipeRight: (() -> Unit)? = null,
    onSwipeLeft: (() -> Unit)? = null,
    rightIcon: ImageVector? = null,
    leftIcon: ImageVector? = null,
    rightBackgroundColor: Color = MaterialTheme.colorScheme.errorContainer,
    leftBackgroundColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    rightIconColor: Color = MaterialTheme.colorScheme.onErrorContainer,
    leftIconColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    resetAfterAction: Boolean = true,
    content: @Composable () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val latestOnSwipeRight by rememberUpdatedState(onSwipeRight)
    val latestOnSwipeLeft by rememberUpdatedState(onSwipeLeft)
    val latestResetAfterAction by rememberUpdatedState(resetAfterAction)

    val dismissState = rememberSwipeToDismissBoxState()

    LaunchedEffect(dismissState.currentValue) {
        when (dismissState.currentValue) {
            SwipeToDismissBoxValue.StartToEnd -> {
                val callback = latestOnSwipeRight
                if (callback != null) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    callback.invoke()
                }
                if (latestResetAfterAction || callback == null) {
                    dismissState.reset()
                }
            }
            SwipeToDismissBoxValue.EndToStart -> {
                val callback = latestOnSwipeLeft
                if (callback != null) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    callback.invoke()
                }
                if (latestResetAfterAction || callback == null) {
                    dismissState.reset()
                }
            }
            SwipeToDismissBoxValue.Settled -> Unit
        }
    }

    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier,
        enableDismissFromStartToEnd = onSwipeRight != null,
        enableDismissFromEndToStart = onSwipeLeft != null,
        backgroundContent = {
            val direction = dismissState.dismissDirection

            val color = when (direction) {
                SwipeToDismissBoxValue.StartToEnd -> rightBackgroundColor
                SwipeToDismissBoxValue.EndToStart -> leftBackgroundColor
                else -> Color.Transparent
            }

            val icon = when (direction) {
                SwipeToDismissBoxValue.StartToEnd -> rightIcon
                SwipeToDismissBoxValue.EndToStart -> leftIcon
                else -> null
            }

            val iconColor = when (direction) {
                SwipeToDismissBoxValue.StartToEnd -> rightIconColor
                SwipeToDismissBoxValue.EndToStart -> leftIconColor
                else -> Color.Transparent
            }

            val alignment = when (direction) {
                SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
                SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
                else -> Alignment.Center
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = alignment
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "Swipe Action",
                        tint = iconColor
                    )
                }
            }
        },
        content = {
            content()
        }
    )
}

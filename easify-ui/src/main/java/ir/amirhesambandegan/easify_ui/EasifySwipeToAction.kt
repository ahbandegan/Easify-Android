package ir.amirhesambandegan.easify_ui

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp

/**
 * A composable that provides horizontal swipe-to-action functionality for its content,
 * supporting distinct actions and visuals for left and right swipes.
 *
 * @param modifier The [Modifier] to be applied to the swipeable box.
 * @param onSwipeRight An optional callback invoked when the user swipes from start to end (left to right).
 * @param onSwipeLeft An optional callback invoked when the user swipes from end to start (right to left).
 * @param rightIcon The icon to display when swiping from start to end. Default is [Icons.Default.Delete].
 * @param leftIcon The icon to display when swiping from end to start. Default is [Icons.Default.Email].
 * @param rightBackgroundColor The background color shown during a start-to-end swipe. Default is the error container color.
 * @param leftBackgroundColor The background color shown during an end-to-start swipe. Default is the secondary container color.
 * @param rightIconColor The color of the icon shown during a start-to-end swipe. Default is the on-error container color.
 * @param leftIconColor The color of the icon shown during an end-to-start swipe. Default is the on-secondary container color.
 * @param content The composable content that can be swiped.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EasifySwipeToAction(
    modifier: Modifier = Modifier,
    onSwipeRight: (() -> Unit)? = null,
    onSwipeLeft: (() -> Unit)? = null,
    rightIcon: ImageVector = Icons.Default.Delete,
    leftIcon: ImageVector = Icons.Default.Email,
    rightBackgroundColor: Color = MaterialTheme.colorScheme.errorContainer,
    leftBackgroundColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    rightIconColor: Color = MaterialTheme.colorScheme.onErrorContainer,
    leftIconColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            when (dismissValue) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    if (onSwipeRight != null) {
                        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                        onSwipeRight.invoke()
                        true
                    } else false
                }
                SwipeToDismissBoxValue.EndToStart -> {
                    if (onSwipeLeft != null) {
                        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                        onSwipeLeft.invoke()
                        true
                    } else false
                }
                else -> false
            }
        }
    )

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

package ir.amirhesambandegan.easify_haptic

import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType


/**
 * Extension for Compose [HapticFeedback] to provide semantic feedback.
 * Represents a standard click or text handle move action.
 *
 * @receiver The [HapticFeedback] instance.
 */
fun HapticFeedback.performClick() = this.performHapticFeedback(HapticFeedbackType.TextHandleMove)

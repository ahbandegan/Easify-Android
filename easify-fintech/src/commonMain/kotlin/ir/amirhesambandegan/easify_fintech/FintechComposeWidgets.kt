package ir.amirhesambandegan.easify_fintech

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp

/**
 * A Compose Multiplatform text component that displays a formatted monetary balance with
 * tap-to-hide privacy toggling (via blur or asterisk mask).
 *
 * @param balance The numerical balance to display.
 * @param modifier The [Modifier] to be applied to the text.
 * @param currency The currency label appended to the formatted balance. Defaults to `"تومان"`.
 * @param initiallyHidden Whether the balance starts hidden. Defaults to `false`.
 * @param useBlurEffect Whether to blur the text when hidden (`true`) or mask with asterisks (`false`). Defaults to `true`.
 * @param onTogglePrivacy Optional callback notified whenever the user toggles visibility.
 */
@Composable
fun EasifyBalanceText(
    balance: Long,
    modifier: Modifier = Modifier,
    currency: String = "تومان",
    initiallyHidden: Boolean = false,
    useBlurEffect: Boolean = true,
    onTogglePrivacy: ((isHidden: Boolean) -> Unit)? = null
) {
    var isHidden by remember(initiallyHidden) { mutableStateOf(initiallyHidden) }
    val formattedText = balance.toPriceFormat(currency)
    val hiddenMask = if (currency.isBlank()) "***,***" else "***,*** $currency"

    val toggle: () -> Unit = {
        isHidden = !isHidden
        onTogglePrivacy?.invoke(isHidden)
    }

    if (useBlurEffect) {
        Text(
            text = formattedText,
            modifier = modifier
                .clickable(onClick = toggle)
                .blur(radius = if (isHidden) 8.dp else 0.dp)
        )
    } else {
        Text(
            text = if (isHidden) hiddenMask else formattedText,
            modifier = modifier.clickable(onClick = toggle)
        )
    }
}

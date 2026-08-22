package ir.amirhesambandegan.easify_fintech

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp

/**
 * A composable function that displays a financial balance with a currency symbol.
 * It provides a toggleable hidden state (e.g., to hide the balance for privacy).
 *
 * @param balance The numerical balance to display.
 * @param modifier The [Modifier] to be applied to the text.
 * @param currency The currency string to append to the formatted balance. Default is "تومان" (Toman).
 * @param initiallyHidden Whether the balance should be initially hidden when the composable is loaded. Default is `false`.
 * @param useBlurEffect Whether to use a blur effect to hide the balance, or replace it with asterisks. Default is `true`.
 */
@Composable
fun EasifyBalanceText(
    balance: Long,
    modifier: Modifier = Modifier,
    currency: String = "تومان",
    initiallyHidden: Boolean = false,
    useBlurEffect: Boolean = true
) {
    var isHidden by remember { mutableStateOf(initiallyHidden) }
    
    val formattedText = balance.toPriceFormat(currency)
    
    if (useBlurEffect) {
        Text(
            text = formattedText,
            modifier = modifier
                .clickable { isHidden = !isHidden }
                .blur(radius = if (isHidden) 8.dp else 0.dp)
        )
    } else {
        Text(
            text = if (isHidden) "***,*** $currency" else formattedText,
            modifier = modifier.clickable { isHidden = !isHidden }
        )
    }
}

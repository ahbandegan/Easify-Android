package ir.amirhesambandegan.easify_fintech

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp

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

package ir.amirhesambandegan.easify_ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun EasifyExpandableText(
    text: String,
    modifier: Modifier = Modifier,
    collapsedMaxLines: Int = 3,
    expandText: String = "Read more",
    collapseText: String = "Show less",
    textStyle: TextStyle = LocalTextStyle.current,
    expandColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary
) {
    var isExpanded by remember { mutableStateOf(false) }
    var isOverflowing by remember { mutableStateOf(false) }

    Column(modifier = modifier.animateContentSize()) {
        Text(
            text = text,
            maxLines = if (isExpanded) Int.MAX_VALUE else collapsedMaxLines,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { textLayoutResult ->
                if (!isExpanded && textLayoutResult.hasVisualOverflow) {
                    isOverflowing = true
                }
            },
            style = textStyle
        )

        if (isOverflowing) {
            Text(
                text = if (isExpanded) collapseText else expandText,
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null, // Removes ripple for a cleaner text click
                        onClick = { isExpanded = !isExpanded }
                    )
                    .padding(top = 4.dp),
                color = expandColor,
                fontWeight = FontWeight.Bold,
                style = textStyle
            )
        }
    }
}

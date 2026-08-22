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

/**
 * A composable text view that can be expanded or collapsed to show or hide overflowing text.
 *
 * @param text The full text content to be displayed.
 * @param modifier The [Modifier] to be applied to the component.
 * @param collapsedMaxLines The maximum number of lines to show when the text is collapsed. Default is 3.
 * @param expandText The label for the button to expand the text. Default is "Read more".
 * @param collapseText The label for the button to collapse the text. Default is "Show less".
 * @param textStyle The typography style to be applied to the text. Default is [LocalTextStyle.current].
 * @param expandColor The color of the expand/collapse button text. Default is the primary color from [MaterialTheme.colorScheme].
 */
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

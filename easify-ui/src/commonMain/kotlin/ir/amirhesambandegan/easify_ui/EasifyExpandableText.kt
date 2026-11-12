package ir.amirhesambandegan.easify_ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    expandColor: Color = MaterialTheme.colorScheme.primary
) {
    val safeCollapsedLines = collapsedMaxLines.coerceAtLeast(1)
    var isExpanded by remember(text, safeCollapsedLines) { mutableStateOf(false) }
    var isOverflowing by remember(text, safeCollapsedLines) { mutableStateOf(false) }

    Column(modifier = modifier.animateContentSize()) {
        Text(
            text = text,
            maxLines = if (isExpanded) Int.MAX_VALUE else safeCollapsedLines,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { textLayoutResult ->
                if (!isExpanded) {
                    isOverflowing = textLayoutResult.hasVisualOverflow
                }
            },
            style = textStyle
        )

        if (isOverflowing || isExpanded) {
            Text(
                text = if (isExpanded) collapseText else expandText,
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
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

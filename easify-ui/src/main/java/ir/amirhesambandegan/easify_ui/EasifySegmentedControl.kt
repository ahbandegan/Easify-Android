package ir.amirhesambandegan.easify_ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun EasifySegmentedControl(
    items: List<String>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 48.dp,
    containerColor: Color = Color.LightGray.copy(alpha = 0.3f),
    indicatorColor: Color = MaterialTheme.colorScheme.surface,
    activeTextColor: Color = MaterialTheme.colorScheme.onSurface,
    inactiveTextColor: Color = Color.Gray,
    cornerRadius: Dp = 24.dp
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(color = containerColor, shape = RoundedCornerShape(cornerRadius))
            .padding(4.dp)
    ) {
        val segmentWidth = maxWidth / items.size
        
        val indicatorOffset by animateFloatAsState(
            targetValue = (selectedIndex * segmentWidth.value),
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessLow
            ),
            label = "IndicatorAnimation"
        )
        
        // The sliding indicator
        Surface(
            modifier = Modifier
                .width(segmentWidth)
                .fillMaxHeight()
                .offset(x = indicatorOffset.dp),
            shape = RoundedCornerShape(cornerRadius - 4.dp),
            color = indicatorColor,
            shadowElevation = 2.dp
        ) {}

        // The items (text)
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            items.forEachIndexed { index, title ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onItemSelected(index) }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = if (selectedIndex == index) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedIndex == index) activeTextColor else inactiveTextColor,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

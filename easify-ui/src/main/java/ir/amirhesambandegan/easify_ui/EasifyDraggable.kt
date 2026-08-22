package ir.amirhesambandegan.easify_ui

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.zIndex
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * A free-floating draggable box that can be moved around the screen.
 * Perfect for chat heads, floating action buttons, or picture-in-picture views.
 */
@Composable
fun EasifyFloatingDraggable(
    modifier: Modifier = Modifier,
    initialPosition: Offset = Offset.Zero,
    content: @Composable () -> Unit
) {
    var offsetX by remember { mutableStateOf(initialPosition.x) }
    var offsetY by remember { mutableStateOf(initialPosition.y) }

    Box(
        modifier = modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            }
    ) {
        content()
    }
}

/**
 * A simple reorderable list where items can be dragged to change their position.
 * Uses long-press to pick up an item.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T : Any> EasifyReorderableList(
    items: List<T>,
    onReorder: (from: Int, to: Int) -> Unit,
    modifier: Modifier = Modifier,
    key: ((T) -> Any)? = null,
    listState: LazyListState = rememberLazyListState(),
    itemContent: @Composable LazyItemScope.(item: T, isDragging: Boolean) -> Unit
) {
    var draggedIndex by remember { mutableStateOf<Int?>(null) }
    var dragOffset by remember { mutableStateOf(0f) }
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        state = listState,
        modifier = modifier.pointerInput(items) {
            detectDragGesturesAfterLongPress(
                onDragStart = { offset ->
                    // Find which item was long-pressed
                    listState.layoutInfo.visibleItemsInfo.firstOrNull { item ->
                        offset.y.toInt() in item.offset..(item.offset + item.size)
                    }?.let { itemInfo ->
                        draggedIndex = itemInfo.index
                    }
                },
                onDrag = { change, dragAmount ->
                    change.consume()
                    dragOffset += dragAmount.y
                    
                    val currentDraggedIndex = draggedIndex ?: return@detectDragGesturesAfterLongPress
                    
                    // Determine if we dragged over another item
                    val draggedItem = listState.layoutInfo.visibleItemsInfo.firstOrNull { it.index == currentDraggedIndex }
                    if (draggedItem != null) {
                        val currentY = draggedItem.offset + dragOffset + (draggedItem.size / 2)
                        
                        val targetItem = listState.layoutInfo.visibleItemsInfo.firstOrNull {
                            it.index != currentDraggedIndex && 
                            currentY in it.offset.toFloat()..(it.offset + it.size).toFloat()
                        }
                        
                        if (targetItem != null) {
                            onReorder(currentDraggedIndex, targetItem.index)
                            draggedIndex = targetItem.index
                            // Adjust drag offset to prevent snapping jump
                            dragOffset -= (targetItem.offset - draggedItem.offset)
                        }
                    }
                },
                onDragEnd = {
                    draggedIndex = null
                    dragOffset = 0f
                },
                onDragCancel = {
                    draggedIndex = null
                    dragOffset = 0f
                }
            )
        }
    ) {
        itemsIndexed(items = items, key = { index, item -> key?.invoke(item) ?: index }) { index, item ->
            val isDragging = index == draggedIndex
            
            // Add visual elevation when dragging
            val zIndex = if (isDragging) 1f else 0f
            val translationY = if (isDragging) dragOffset else 0f
            
            Box(
                modifier = Modifier
                    .zIndex(zIndex)
                    .graphicsLayer {
                        this.translationY = translationY
                        scaleX = if (isDragging) 1.05f else 1f
                        scaleY = if (isDragging) 1.05f else 1f
                        alpha = if (isDragging) 0.9f else 1f
                    }
            ) {
                itemContent(item, isDragging)
            }
        }
    }
}

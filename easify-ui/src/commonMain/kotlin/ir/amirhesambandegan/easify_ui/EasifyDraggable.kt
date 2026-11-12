package ir.amirhesambandegan.easify_ui

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.zIndex
import kotlin.math.roundToInt

/**
 * A free-floating draggable box that can be moved around the screen.
 * Perfect for chat heads, floating action buttons, or picture-in-picture views.
 *
 * @param modifier The [Modifier] to be applied to the draggable box.
 * @param initialPosition The starting [Offset] position of the draggable box. Default is [Offset.Zero].
 * @param content The composable content to be displayed inside the draggable box.
 */
@Composable
fun EasifyFloatingDraggable(
    modifier: Modifier = Modifier,
    initialPosition: Offset = Offset.Zero,
    content: @Composable () -> Unit
) {
    var offsetX by remember(initialPosition) { mutableFloatStateOf(initialPosition.x) }
    var offsetY by remember(initialPosition) { mutableFloatStateOf(initialPosition.y) }

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
 * A simple reorderable list where items can be dragged via long-press to change their position.
 *
 * Keeps the active drag gesture intact across item list updates while [onReorder] executes.
 *
 * @param T The type of items in the list.
 * @param items The list of items to display.
 * @param onReorder Callback invoked when an item is moved, providing old and new indices.
 * @param modifier The [Modifier] to be applied to the list.
 * @param key An optional key provider to help Compose track moved items reliably.
 * @param listState The state object controlling the underlying [LazyColumn].
 * @param itemContent The composable content rendered for each item.
 */
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
    var dragOffset by remember { mutableFloatStateOf(0f) }

    val latestItems by rememberUpdatedState(items)
    val latestOnReorder by rememberUpdatedState(onReorder)

    LazyColumn(
        state = listState,
        modifier = modifier.pointerInput(listState) {
            detectDragGesturesAfterLongPress(
                onDragStart = { offset ->
                    listState.layoutInfo.visibleItemsInfo.firstOrNull { item ->
                        offset.y.toInt() in item.offset..(item.offset + item.size)
                    }?.let { itemInfo ->
                        if (itemInfo.index in latestItems.indices) {
                            draggedIndex = itemInfo.index
                        }
                    }
                },
                onDrag = { change, dragAmount ->
                    change.consume()
                    dragOffset += dragAmount.y

                    val currentDraggedIndex = draggedIndex ?: return@detectDragGesturesAfterLongPress
                    val visibleItems = listState.layoutInfo.visibleItemsInfo
                    val draggedItem = visibleItems.firstOrNull { it.index == currentDraggedIndex }
                        ?: return@detectDragGesturesAfterLongPress

                    val currentY = draggedItem.offset + dragOffset + (draggedItem.size / 2f)
                    val targetItem = visibleItems.firstOrNull {
                        it.index != currentDraggedIndex &&
                            it.index in latestItems.indices &&
                            currentY in it.offset.toFloat()..(it.offset + it.size).toFloat()
                    }

                    if (targetItem != null && currentDraggedIndex in latestItems.indices) {
                        latestOnReorder(currentDraggedIndex, targetItem.index)
                        draggedIndex = targetItem.index
                        dragOffset -= (targetItem.offset - draggedItem.offset)
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
        itemsIndexed(
            items = items,
            key = { index, item -> key?.invoke(item) ?: index }
        ) { index, item ->
            val isDragging = index == draggedIndex

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

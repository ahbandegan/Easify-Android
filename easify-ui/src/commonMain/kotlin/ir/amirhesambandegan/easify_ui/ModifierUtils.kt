package ir.amirhesambandegan.easify_ui

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.currentValueOf
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

/**
 * Mirrors the element horizontally when the active layout direction is [LayoutDirection.Rtl].
 * Useful for directional icons such as back arrows or chevrons.
 *
 * Uses a [Modifier.Node] with [CompositionLocalConsumerModifierNode] instead of `Modifier.composed`.
 *
 * @return A [Modifier] that mirrors horizontally in RTL layouts.
 */
fun Modifier.rtlMirror(): Modifier = this.then(RtlMirrorElement)

private object RtlMirrorElement : ModifierNodeElement<RtlMirrorNode>() {
    override fun create() = RtlMirrorNode()

    override fun update(node: RtlMirrorNode) = Unit

    override fun equals(other: Any?): Boolean = other === this

    override fun hashCode(): Int = 0x52544C4D

    override fun InspectorInfo.inspectableProperties() {
        name = "rtlMirror"
    }
}

private class RtlMirrorNode :
    Modifier.Node(),
    CompositionLocalConsumerModifierNode,
    DrawModifierNode {

    override fun ContentDrawScope.draw() {
        if (currentValueOf(LocalLayoutDirection) == LayoutDirection.Rtl) {
            scale(scaleX = -1f, scaleY = 1f, pivot = center) {
                this@draw.drawContent()
            }
        } else {
            drawContent()
        }
    }
}

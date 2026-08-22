package ir.amirhesambandegan.easify_ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

/**
 * A convenient wrapper around [Spacer] to create a horizontal space of a specific width.
 *
 * @param width The exact width of the spacer in [Dp].
 */
@Composable
fun SpacerWidth(width: Dp) = Spacer(Modifier.width(width))

/**
 * A convenient wrapper around [Spacer] to create a horizontal space taking up a specific fraction of available width.
 *
 * @param fraction The fraction of the available width to fill. Must be between 0.0 and 1.0. Default is 1f.
 */
@Composable
fun SpacerWidth(fraction: Float = 1f) = Spacer(Modifier.fillMaxWidth(fraction))

/**
 * A convenient wrapper around [Spacer] to create a vertical space of a specific height.
 *
 * @param height The exact height of the spacer in [Dp].
 */
@Composable
fun SpacerHeight(height: Dp) = Spacer(Modifier.height(height))

/**
 * A convenient wrapper around [Spacer] to create a vertical space taking up a specific fraction of available height.
 *
 * @param fraction The fraction of the available height to fill. Must be between 0.0 and 1.0. Default is 1f.
 */
@Composable
fun SpacerHeight(fraction: Float = 1f) = Spacer(Modifier.fillMaxHeight(fraction))
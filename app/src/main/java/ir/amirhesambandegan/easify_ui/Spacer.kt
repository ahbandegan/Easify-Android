package ir.amirhesambandegan.easify_ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun SpacerWidth(width: Dp) = Spacer(Modifier.width(width))

@Composable
fun SpacerWidth(fraction: Float = 1f) = Spacer(Modifier.fillMaxWidth(fraction))

@Composable
fun SpacerHeight(height: Dp) = Spacer(Modifier.height(height))

@Composable
fun SpacerHeight(fraction: Float = 1f) = Spacer(Modifier.fillMaxHeight(fraction))
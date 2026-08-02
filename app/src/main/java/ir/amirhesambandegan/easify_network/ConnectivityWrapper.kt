package ir.amirhesambandegan.easify_network

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * A Composable wrapper that monitors internet connectivity and overlays a "No Connection" 
 * UI when the device is offline.
 *
 * @param noConnectionContent The UI to display when offline. Defaults to [DefaultNoInternetUI].
 * @param content The main content of the screen to be wrapped.
 */
@Composable
fun ConnectivityWrapper(
    noConnectionContent: @Composable () -> Unit = { DefaultNoInternetUI() },
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val observer = remember { ConnectivityObserver(context) }
    val isOnline by observer.online.collectAsState(initial = true)

    Box(modifier = Modifier.fillMaxSize()) {
        // Main Content
        content()

        // No Internet Overlay
        if (!isOnline) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
                    // Block all touch events to the underlying content
                    .pointerInput(Unit) {},
                contentAlignment = Alignment.Center
            ) {
                noConnectionContent()
            }
        }
    }
}

/**
 * The default UI displayed by [ConnectivityWrapper] when there is no internet connection.
 */
@Composable
private fun DefaultNoInternetUI() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(24.dp)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.medium
            )
            .padding(32.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = "No Internet",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Internet Connection",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Please check your network settings.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

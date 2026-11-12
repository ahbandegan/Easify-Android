package ir.amirhesambandegan.easify_persian

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A Compose Multiplatform dialog for selecting a time.
 *
 * @param onDismissRequest Callback to be invoked when the dialog is dismissed without confirming.
 * @param onTimeSelected Callback to be invoked when a time is confirmed. Receives `(hour, minute)`.
 * @param initialHour Initial hour (`0..23`). Defaults to `12`.
 * @param initialMinute Initial minute (`0..59`). Defaults to `0`.
 * @param is24Hour Whether the picker uses 24-hour clock format. Defaults to `true`.
 * @param title Optional dialog title text.
 * @param confirmText Optional confirm button label.
 * @param dismissText Optional dismiss button label.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersianTimePickerDialog(
    onDismissRequest: () -> Unit,
    onTimeSelected: (hour: Int, minute: Int) -> Unit,
    initialHour: Int = 12,
    initialMinute: Int = 0,
    is24Hour: Boolean = true,
    title: String = "انتخاب زمان",
    confirmText: String = "تایید",
    dismissText: String = "انصراف"
) {
    val state = rememberTimePickerState(
        initialHour = initialHour.coerceIn(0, 23),
        initialMinute = initialMinute.coerceIn(0, 59),
        is24Hour = is24Hour
    )

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = { onTimeSelected(state.hour, state.minute) }) {
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(dismissText)
            }
        },
        title = { Text(title) },
        text = {
            TimePicker(state = state, modifier = Modifier.fillMaxWidth())
        }
    )
}

package ir.amirhesambandegan.easify_persian

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersianTimePickerDialog(
    onDismissRequest: () -> Unit,
    onTimeSelected: (hour: Int, minute: Int) -> Unit,
    initialHour: Int = 12,
    initialMinute: Int = 0
) {
    val state = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = true
    )

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = { onTimeSelected(state.hour, state.minute) }) {
                Text("تایید")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("انصراف")
            }
        },
        title = { Text("انتخاب زمان") },
        text = {
            TimePicker(state = state, modifier = Modifier.fillMaxWidth())
        }
    )
}

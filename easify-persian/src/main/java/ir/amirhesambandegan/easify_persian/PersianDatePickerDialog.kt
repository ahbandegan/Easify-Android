package ir.amirhesambandegan.easify_persian

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersianDatePickerDialog(
    onDismissRequest: () -> Unit,
    onDateSelected: (JalaliDate) -> Unit,
    initialDate: JalaliDate = Date().toJalali()
) {
    var year by remember { mutableStateOf(initialDate.year) }
    var month by remember { mutableStateOf(initialDate.month) }
    var day by remember { mutableStateOf(initialDate.day) }
    
    val persianMonths = listOf("فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور", "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند")

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = { onDateSelected(JalaliDate(year, month, day)) }) {
                Text("تایید")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("انصراف")
            }
        },
        title = { Text("انتخاب تاریخ") },
        text = {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                DateSelectorRow(
                    label = "سال",
                    value = year,
                    onIncrement = { year++ },
                    onDecrement = { year-- }
                )
                DateSelectorRow(
                    label = "ماه",
                    valueText = persianMonths[month - 1],
                    onIncrement = { if (month < 12) month++ else month = 1 },
                    onDecrement = { if (month > 1) month-- else month = 12 }
                )
                DateSelectorRow(
                    label = "روز",
                    value = day,
                    onIncrement = { if (day < 31) day++ else day = 1 },
                    onDecrement = { if (day > 1) day-- else day = 31 }
                )
            }
        }
    )
}

@Composable
private fun DateSelectorRow(
    label: String,
    value: Int = 0,
    valueText: String = value.toString().toPersianDigits(),
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Text(label)
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            FilledTonalIconButton(onClick = onDecrement) { Text("-") }
            Text(valueText, modifier = Modifier.padding(horizontal = 16.dp))
            FilledTonalIconButton(onClick = onIncrement) { Text("+") }
        }
    }
}

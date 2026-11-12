package ir.amirhesambandegan.easify_persian

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private val PERSIAN_MONTH_NAMES = listOf(
    "فروردین", "اردیبهشت", "خرداد",
    "تیر", "مرداد", "شهریور",
    "مهر", "آبان", "آذر",
    "دی", "بهمن", "اسفند"
)

internal fun clampJalaliDay(year: Int, month: Int, day: Int): Int {
    val safeMonth = month.coerceIn(1, 12)
    val maxDay = JalaliDate(year, safeMonth, 1).daysInMonth().coerceAtLeast(1)
    return day.coerceIn(1, maxDay)
}

/**
 * A Compose Multiplatform dialog for selecting a Persian (Jalali) date.
 * Automatically clamps day boundaries to exact month lengths (29/30/31) including Esfand leap years.
 *
 * @param onDismissRequest Callback to be invoked when the dialog is dismissed without confirming.
 * @param onDateSelected Callback to be invoked when a date is confirmed. Receives the selected [JalaliDate].
 * @param initialDate The initial [JalaliDate] to display in the picker. Defaults to today's Jalali date.
 * @param title Optional dialog title text.
 * @param confirmText Optional confirm button label.
 * @param dismissText Optional dismiss button label.
 * @param showReadablePreview Whether to display the selected date preview with weekday name.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersianDatePickerDialog(
    onDismissRequest: () -> Unit,
    onDateSelected: (JalaliDate) -> Unit,
    initialDate: JalaliDate = JalaliDate.now(),
    title: String = "انتخاب تاریخ",
    confirmText: String = "تایید",
    dismissText: String = "انصراف",
    showReadablePreview: Boolean = true
) {
    val initialMonth = initialDate.month.coerceIn(1, 12)
    var year by remember(initialDate) { mutableIntStateOf(initialDate.year) }
    var month by remember(initialDate) { mutableIntStateOf(initialMonth) }
    var day by remember(initialDate) {
        mutableIntStateOf(clampJalaliDay(initialDate.year, initialMonth, initialDate.day))
    }

    val maxDayInMonth = JalaliDate(year, month, 1).daysInMonth().coerceAtLeast(1)
    val selectedDate = JalaliDate(year, month, day.coerceIn(1, maxDayInMonth))

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = { onDateSelected(selectedDate) }) {
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(dismissText)
            }
        },
        title = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(title)
                if (showReadablePreview) {
                    Text(
                        text = selectedDate.toReadablePersianDate(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        text = {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                DateSelectorRow(
                    label = "سال",
                    value = year,
                    onIncrement = {
                        year++
                        day = clampJalaliDay(year, month, day)
                    },
                    onDecrement = {
                        year--
                        day = clampJalaliDay(year, month, day)
                    }
                )
                DateSelectorRow(
                    label = "ماه",
                    valueText = PERSIAN_MONTH_NAMES[month - 1],
                    onIncrement = {
                        month = if (month < 12) month + 1 else 1
                        day = clampJalaliDay(year, month, day)
                    },
                    onDecrement = {
                        month = if (month > 1) month - 1 else 12
                        day = clampJalaliDay(year, month, day)
                    }
                )
                DateSelectorRow(
                    label = "روز",
                    value = day,
                    onIncrement = {
                        day = if (day < maxDayInMonth) day + 1 else 1
                    },
                    onDecrement = {
                        day = if (day > 1) day - 1 else maxDayInMonth
                    }
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
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label)
        Row(verticalAlignment = Alignment.CenterVertically) {
            FilledTonalIconButton(onClick = onDecrement) { Text("-") }
            Text(valueText, modifier = Modifier.padding(horizontal = 16.dp))
            FilledTonalIconButton(onClick = onIncrement) { Text("+") }
        }
    }
}

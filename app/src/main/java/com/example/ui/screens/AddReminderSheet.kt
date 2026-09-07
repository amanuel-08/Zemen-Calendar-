package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calendar.EthiopianCalendarEngine
import com.example.calendar.EthiopianDate
import com.example.ui.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReminderSheet(
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val settings by viewModel.userSettings.collectAsState()
    val isAmharic = settings.language == "am"

    val defaultDate = viewModel.selectedDate.collectAsState().value ?: viewModel.todayEth

    var title by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var ethYear by remember { mutableStateOf(defaultDate.year) }
    var ethMonth by remember { mutableStateOf(defaultDate.month) }
    var ethDay by remember { mutableStateOf(defaultDate.day) }
    var hour by remember { mutableStateOf(9) }
    var minute by remember { mutableStateOf(0) }
    var selectedType by remember { mutableStateOf("Reminder") }
    var repeatMode by remember { mutableStateOf("None") }
    var leadTimeMinutes by remember { mutableStateOf(1440) } // 1 day before

    val typesList = listOf("Reminder", "Exam", "Work", "Birthday", "Religious", "Holiday", "Custom")
    val repeatsList = listOf("None", "Daily", "Weekly", "Monthly", "Yearly")
    val leadTimesList = listOf(
        0 to (if (isAmharic) "በሰዓቱ" else "At time of event"),
        15 to (if (isAmharic) "ከ15 ደቂቃ በፊት" else "15 minutes before"),
        60 to (if (isAmharic) "ከ1 ሰዓት በፊት" else "1 hour before"),
        1440 to (if (isAmharic) "ከ1 ቀን በፊት" else "1 day before")
    )

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isAmharic) "አዲስ ማስታወሻ / ክስተት" else "New Event / Reminder",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Title input
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(if (isAmharic) "የማስታወሻው ርዕስ *" else "Title *") },
                placeholder = { Text(if (isAmharic) "ምሳሌ፦ የፊዚክስ ፈተና፣ ልደት..." else "e.g., Physics Exam, Birthday...") },
                shape = RoundedCornerShape(14.dp),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_reminder_title")
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Date selectors (Ethiopian Year, Month, Day)
            Text(
                text = if (isAmharic) "ቀን (በኢትዮጵያ የቀን መቁጠሪያ)" else "Date (Ethiopian Calendar)",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                NumberWheelSelector(
                    label = if (isAmharic) "ዓመት" else "Year",
                    value = ethYear,
                    min = 1900,
                    max = 2100,
                    modifier = Modifier.weight(1.2f),
                    onValueChange = { ethYear = it }
                )

                EthiopianMonthDropdown(
                    label = if (isAmharic) "ወር" else "Month",
                    selectedMonth = ethMonth,
                    isAmharic = isAmharic,
                    modifier = Modifier.weight(1.8f),
                    onMonthSelected = { ethMonth = it }
                )

                val maxDays = EthiopianCalendarEngine.getDaysInEthiopianMonth(ethYear, ethMonth)
                NumberWheelSelector(
                    label = if (isAmharic) "ቀን" else "Day",
                    value = ethDay.coerceIn(1, maxDays),
                    min = 1,
                    max = maxDays,
                    modifier = Modifier.weight(1f),
                    onValueChange = { ethDay = it }
                )
            }

            // Gregorian Equivalent preview
            val gregEquiv = remember(ethYear, ethMonth, ethDay) {
                val maxD = EthiopianCalendarEngine.getDaysInEthiopianMonth(ethYear, ethMonth)
                val safeDay = ethDay.coerceIn(1, maxD)
                EthiopianCalendarEngine.toGregorian(EthiopianDate(ethYear, ethMonth, safeDay))
            }
            val gregMonthName = EthiopianCalendarEngine.getGregorianMonthName(gregEquiv.month, isAmharic)
            Text(
                text = "➔ $gregMonthName ${gregEquiv.day}, ${gregEquiv.year} GC",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Time Selector (Hour & Minute)
            Text(
                text = if (isAmharic) "ሰዓት" else "Time",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                NumberWheelSelector(
                    label = if (isAmharic) "ሰዓት (Hour 0-23)" else "Hour (0-23)",
                    value = hour,
                    min = 0,
                    max = 23,
                    modifier = Modifier.weight(1f),
                    onValueChange = { hour = it }
                )

                NumberWheelSelector(
                    label = if (isAmharic) "ደቂቃ (Minute)" else "Minute",
                    value = minute,
                    min = 0,
                    max = 59,
                    modifier = Modifier.weight(1f),
                    onValueChange = { minute = it }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Type & Repeat Dropdowns
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Type selector
                SimpleDropdownSelector(
                    label = if (isAmharic) "ዓይነት" else "Category",
                    selectedText = selectedType,
                    options = typesList,
                    modifier = Modifier.weight(1f),
                    onSelected = { selectedType = it }
                )

                // Repeat selector
                SimpleDropdownSelector(
                    label = if (isAmharic) "ድግግሞሽ" else "Repeat",
                    selectedText = repeatMode,
                    options = repeatsList,
                    modifier = Modifier.weight(1f),
                    onSelected = { repeatMode = it }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Reminder alert timing
            Text(
                text = if (isAmharic) "የማንቂያ ሰዓት (Notification)" else "Notification Alert",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                for ((mins, label) in leadTimesList) {
                    val isSelected = leadTimeMinutes == mins
                    Surface(
                        onClick = { leadTimeMinutes = mins },
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = label,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                            maxLines = 2
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Notes input
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text(if (isAmharic) "ማስታወሻ / ዝርዝር (አማራጭ)" else "Notes / Details (Optional)") },
                shape = RoundedCornerShape(14.dp),
                maxLines = 3,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_reminder_notes")
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Save Button
            Button(
                onClick = {
                    if (title.isBlank()) {
                        Toast.makeText(
                            context,
                            if (isAmharic) "እባክዎ የማስታወሻውን ርዕስ ያስገቡ" else "Please enter a title",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }
                    val maxD = EthiopianCalendarEngine.getDaysInEthiopianMonth(ethYear, ethMonth)
                    val safeDay = ethDay.coerceIn(1, maxD)
                    viewModel.saveReminder(
                        title = title.trim(),
                        ethDate = EthiopianDate(ethYear, ethMonth, safeDay),
                        hour = hour,
                        minute = minute,
                        type = selectedType,
                        repeatMode = repeatMode,
                        leadTimeMinutes = leadTimeMinutes,
                        notes = notes.trim()
                    )
                    Toast.makeText(
                        context,
                        if (isAmharic) "ማስታወሻው በሚገባ ተቀምጧል!" else "Reminder saved successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("btn_save_reminder")
            ) {
                Text(
                    text = if (isAmharic) "ማስታወሻውን መዝግብ" else "Save Reminder",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
fun SimpleDropdownSelector(
    label: String,
    selectedText: String,
    options: List<String>,
    modifier: Modifier = Modifier,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        OutlinedCard(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = selectedText,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            for (opt in options) {
                DropdownMenuItem(
                    text = { Text(text = opt) },
                    onClick = {
                        onSelected(opt)
                        expanded = false
                    }
                )
            }
        }
    }
}

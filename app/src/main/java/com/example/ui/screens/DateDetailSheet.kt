package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calendar.EthiopianCalendarEngine
import com.example.calendar.EthiopianDate
import com.example.calendar.HolidaysData
import com.example.ui.AppNavTab
import com.example.ui.MainViewModel
import com.example.ui.components.CategoryChip
import com.example.ui.components.PublicHolidayBadge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateDetailSheet(
    ethDate: EthiopianDate,
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val settings by viewModel.userSettings.collectAsState()
    val reminders by viewModel.reminders.collectAsState()

    val isAmharic = settings.language == "am"
    val useGeez = settings.useGeezNumerals

    val gregDate = remember(ethDate) {
        EthiopianCalendarEngine.toGregorian(ethDate)
    }

    val dow = remember(ethDate) {
        EthiopianCalendarEngine.getDayOfWeek(ethDate)
    }

    val ethMonthName = EthiopianCalendarEngine.getEthiopianMonthName(ethDate.month, isAmharic)
    val gregMonthName = EthiopianCalendarEngine.getGregorianMonthName(gregDate.month, isAmharic)
    val evangelist = EthiopianCalendarEngine.getEvangelistName(ethDate.year, isAmharic)

    val holidays = remember(ethDate) {
        HolidaysData.getHolidaysForDay(ethDate)
    }

    val dateReminders = remember(reminders, ethDate) {
        reminders.filter {
            it.ethYear == ethDate.year && it.ethMonth == ethDate.month && it.ethDay == ethDate.day
        }
    }

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
        ) {
            // Header: Day of week & Close
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = if (isAmharic) dow.nameAm else dow.nameEn,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Primary Ethiopian Date
            Text(
                text = "$ethMonthName ${viewModel.formatNumber(ethDate.day, useGeez)}, ${viewModel.formatNumber(ethDate.year, useGeez)}",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Gregorian Equivalent
            Text(
                text = "$gregMonthName ${gregDate.day}, ${gregDate.year} GC",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = evangelist,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Action buttons row (Copy, Share, Convert in Converter)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val dateText = "$ethMonthName ${ethDate.day}, ${ethDate.year} ($gregMonthName ${gregDate.day}, ${gregDate.year})"
                        val clip = ClipData.newPlainText("Date", dateText)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, if (isAmharic) "ቀኑ ተቀድቷል!" else "Date copied!", Toast.LENGTH_SHORT).show()
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = if (isAmharic) "ቅዳ" else "Copy", fontSize = 12.sp)
                }

                OutlinedButton(
                    onClick = {
                        val shareText = "Ethiopian Date: $ethMonthName ${ethDate.day}, ${ethDate.year} (${dow.nameEn})\nGregorian Date: $gregMonthName ${gregDate.day}, ${gregDate.year}\nVia Zemen App"
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share Date"))
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = if (isAmharic) "አጋራ" else "Share", fontSize = 12.sp)
                }

                Button(
                    onClick = {
                        viewModel.updateEthConversionInput(ethDate.year, ethDate.month, ethDate.day)
                        viewModel.setTab(AppNavTab.CONVERTER)
                        onDismiss()
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.weight(1.2f)
                ) {
                    Icon(imageVector = Icons.Default.SwapHoriz, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = if (isAmharic) "ቀይር" else "Convert", fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Holidays section for this day
            if (holidays.isNotEmpty()) {
                Text(
                    text = if (isAmharic) "የቀኑ በዓላት" else "Holidays on this day",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                for (h in holidays) {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isAmharic) h.nameAm else h.nameEn,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                CategoryChip(category = h.category, isAmharic = isAmharic)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isAmharic) h.descriptionAm else h.descriptionEn,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Reminders section for this day
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isAmharic) "ማስታወሻዎች" else "Reminders",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                TextButton(
                    onClick = {
                        viewModel.openAddReminder(ethDate)
                        onDismiss()
                    }
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = if (isAmharic) "አዲስ ጨምር" else "Add New", fontSize = 12.sp)
                }
            }

            if (dateReminders.isEmpty()) {
                Text(
                    text = if (isAmharic) "ለዚህ ቀን ምንም ማስታወሻ አልተመዘገበም" else "No reminders scheduled for this day",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                for (r in dateReminders) {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = String.format("%02d:%02d", r.hour, r.minute),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = r.title,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp
                                )
                                if (r.notes.isNotBlank()) {
                                    Text(
                                        text = r.notes,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

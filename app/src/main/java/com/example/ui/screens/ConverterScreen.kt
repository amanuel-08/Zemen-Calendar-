package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calendar.EthiopianCalendarEngine
import com.example.calendar.EthiopianDate
import com.example.calendar.GregorianDate
import com.example.ui.ConverterDirection
import com.example.ui.MainViewModel
import com.example.ui.theme.ZemenEmerald
import com.example.ui.theme.ZemenGold

@Composable
fun ConverterScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val settings by viewModel.userSettings.collectAsState()
    val converterState by viewModel.converterState.collectAsState()

    val isAmharic = settings.language == "am"
    val useGeez = settings.useGeezNumerals

    val isEthToGreg = converterState.direction == ConverterDirection.ETHIOPIAN_TO_GREGORIAN

    val ethYear = converterState.ethYear
    val ethMonth = converterState.ethMonth
    val ethDay = converterState.ethDay

    val gregYear = converterState.gregYear
    val gregMonth = converterState.gregMonth
    val gregDay = converterState.gregDay

    val convertedGreg = converterState.convertedGreg
    val convertedEth = converterState.convertedEth
    val dayOfWeek = converterState.dayOfWeek

    val isEthLeap = EthiopianCalendarEngine.isEthiopianLeapYear(if (isEthToGreg) ethYear else convertedEth.year)
    val isGregLeap = EthiopianCalendarEngine.isGregorianLeapYear(if (isEthToGreg) convertedGreg.year else gregYear)

    val daysDiff = remember(converterState, viewModel.todayEth) {
        val targetEth = if (isEthToGreg) {
            EthiopianDate(ethYear, ethMonth, ethDay)
        } else {
            convertedEth
        }
        EthiopianCalendarEngine.daysBetween(viewModel.todayEth, targetEth)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .animateContentSize()
    ) {
        // Title banner
        Text(
            text = if (isAmharic) "የቀን መቀየሪያ" else "Date Converter",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = if (isAmharic) "በኢትዮጵያ እና በጎርጎሪዮሳዊ የቀን መቁጠሪያ መካከል በፍጥነት ቀይር"
            else "Instant Ethiopian ↔ Gregorian calendar conversion",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Direction Selector Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isAmharic) "የመቀየሪያ አቅጣጫ" else "Conversion Mode",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = if (isEthToGreg) {
                            if (isAmharic) "ኢትዮጵያ ➔ ጎርጎሪዮሳዊ (GC)" else "Ethiopian ➔ Gregorian (GC)"
                        } else {
                            if (isAmharic) "ጎርጎሪዮሳዊ (GC) ➔ ኢትዮጵያ" else "Gregorian (GC) ➔ Ethiopian"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Surface(
                    onClick = { viewModel.toggleConverterDirection() },
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(44.dp)
                        .testTag("btn_swap_direction")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.SwapVert,
                            contentDescription = "Swap Direction",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Input Fields (Year, Month, Day)
        Text(
            text = if (isEthToGreg) {
                if (isAmharic) "የኢትዮጵያ ቀን አስገባ" else "Enter Ethiopian Date"
            } else {
                if (isAmharic) "የጎርጎሪዮሳዊ ቀን አስገባ" else "Enter Gregorian Date"
            },
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (isEthToGreg) {
            // Ethiopian Date Selectors
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Year Selector
                NumberWheelSelector(
                    label = if (isAmharic) "ዓመት" else "Year",
                    value = ethYear,
                    min = 1900,
                    max = 2100,
                    modifier = Modifier.weight(1.2f),
                    onValueChange = { newY ->
                        viewModel.updateEthConversionInput(newY, ethMonth, ethDay)
                    }
                )

                // Month Selector
                EthiopianMonthDropdown(
                    label = if (isAmharic) "ወር" else "Month",
                    selectedMonth = ethMonth,
                    isAmharic = isAmharic,
                    modifier = Modifier.weight(1.8f),
                    onMonthSelected = { newM ->
                        viewModel.updateEthConversionInput(ethYear, newM, ethDay)
                    }
                )

                // Day Selector
                val maxEthDays = EthiopianCalendarEngine.getDaysInEthiopianMonth(ethYear, ethMonth)
                NumberWheelSelector(
                    label = if (isAmharic) "ቀን" else "Day",
                    value = ethDay,
                    min = 1,
                    max = maxEthDays,
                    modifier = Modifier.weight(1f),
                    onValueChange = { newD ->
                        viewModel.updateEthConversionInput(ethYear, ethMonth, newD)
                    }
                )
            }
        } else {
            // Gregorian Date Selectors
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Year Selector
                NumberWheelSelector(
                    label = if (isAmharic) "ዓመት (Year)" else "Year",
                    value = gregYear,
                    min = 1900,
                    max = 2100,
                    modifier = Modifier.weight(1.2f),
                    onValueChange = { newY ->
                        viewModel.updateGregConversionInput(newY, gregMonth, gregDay)
                    }
                )

                // Month Selector
                GregorianMonthDropdown(
                    label = if (isAmharic) "ወር (Month)" else "Month",
                    selectedMonth = gregMonth,
                    isAmharic = isAmharic,
                    modifier = Modifier.weight(1.8f),
                    onMonthSelected = { newM ->
                        viewModel.updateGregConversionInput(gregYear, newM, gregDay)
                    }
                )

                // Day Selector
                val maxGregDays = EthiopianCalendarEngine.getDaysInGregorianMonth(gregYear, gregMonth)
                NumberWheelSelector(
                    label = if (isAmharic) "ቀን (Day)" else "Day",
                    value = gregDay,
                    min = 1,
                    max = maxGregDays,
                    modifier = Modifier.weight(1f),
                    onValueChange = { newD ->
                        viewModel.updateGregConversionInput(gregYear, gregMonth, newD)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Result Card (Instant Conversion Result)
        ElevatedCard(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("converter_result_card")
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isAmharic) "የተቀየረው ውጤት" else "CONVERTED RESULT",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = if (isAmharic) dayOfWeek.nameAm else dayOfWeek.nameEn,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Converted Date text
                val resultDateString: String
                val subtitleDateString: String

                if (isEthToGreg) {
                    val gMonth = EthiopianCalendarEngine.getGregorianMonthName(convertedGreg.month, isAmharic)
                    resultDateString = "$gMonth ${convertedGreg.day}, ${convertedGreg.year}"
                    val eMonth = EthiopianCalendarEngine.getEthiopianMonthName(ethMonth, isAmharic)
                    subtitleDateString = "$eMonth ${viewModel.formatNumber(ethDay, useGeez)}, ${viewModel.formatNumber(ethYear, useGeez)}"
                } else {
                    val eMonth = EthiopianCalendarEngine.getEthiopianMonthName(convertedEth.month, isAmharic)
                    resultDateString = "$eMonth ${viewModel.formatNumber(convertedEth.day, useGeez)}, ${viewModel.formatNumber(convertedEth.year, useGeez)}"
                    val gMonth = EthiopianCalendarEngine.getGregorianMonthName(gregMonth, isAmharic)
                    subtitleDateString = "$gMonth $gregDay, $gregYear"
                }

                Text(
                    text = resultDateString,
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${if (isEthToGreg) "From: " else "ከ: "}$subtitleDateString",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Relative days difference badge & leap year status
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val diffText = when {
                        daysDiff == 0L -> if (isAmharic) "ዛሬ ነው" else "Today"
                        daysDiff == 1L -> if (isAmharic) "ነገ" else "Tomorrow"
                        daysDiff == -1L -> if (isAmharic) "ትላንት" else "Yesterday"
                        daysDiff > 1L -> if (isAmharic) "ከ $daysDiff ቀናት በኋላ" else "In $daysDiff days"
                        else -> if (isAmharic) "${-daysDiff} ቀናት በፊት" else "${-daysDiff} days ago"
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = diffText,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    if (isEthLeap) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = ZemenEmerald.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = if (isAmharic) "ዘመነ ሉቃስ (ጳጉሜን 6)" else "Eth Leap Year (Pagume 6)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ZemenEmerald,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons: Copy, Share, Add Reminder
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Date", "$resultDateString ($subtitleDateString)")
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, if (isAmharic) "ቀኑ ተቀድቷል!" else "Date copied!", Toast.LENGTH_SHORT).show()
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_copy_converted_date")
                    ) {
                        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = if (isAmharic) "ቅዳ" else "Copy", fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, "Ethiopian Date: $subtitleDateString\nGregorian Date: $resultDateString (${dayOfWeek.nameEn})\nVia Zemen App")
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Date"))
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_share_converted_date")
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = if (isAmharic) "አጋራ" else "Share", fontSize = 12.sp)
                    }

                    Button(
                        onClick = {
                            val ethToUse = if (isEthToGreg) EthiopianDate(ethYear, ethMonth, ethDay) else convertedEth
                            viewModel.openAddReminder(ethToUse)
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .weight(1.2f)
                            .testTag("btn_remind_converted_date")
                    ) {
                        Icon(imageVector = Icons.Default.AddAlert, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = if (isAmharic) "ማስታወሻ" else "Remind", fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun NumberWheelSelector(
    label: String,
    value: Int,
    min: Int,
    max: Int,
    modifier: Modifier = Modifier,
    onValueChange: (Int) -> Unit
) {
    OutlinedCard(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { if (value > min) onValueChange(value - 1) },
                    modifier = Modifier.size(28.dp)
                ) {
                    Text(text = "−", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Text(
                    text = value.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                IconButton(
                    onClick = { if (value < max) onValueChange(value + 1) },
                    modifier = Modifier.size(28.dp)
                ) {
                    Text(text = "+", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun EthiopianMonthDropdown(
    label: String,
    selectedMonth: Int,
    isAmharic: Boolean,
    modifier: Modifier = Modifier,
    onMonthSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val currentName = EthiopianCalendarEngine.getEthiopianMonthName(selectedMonth, isAmharic)

    Box(modifier = modifier) {
        OutlinedCard(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = currentName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            for (m in EthiopianCalendarEngine.ETHIOPIAN_MONTHS) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = if (isAmharic) "${m.index}. ${m.nameAm}" else "${m.index}. ${m.nameEn}",
                            fontWeight = if (m.index == selectedMonth) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    onClick = {
                        onMonthSelected(m.index)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun GregorianMonthDropdown(
    label: String,
    selectedMonth: Int,
    isAmharic: Boolean,
    modifier: Modifier = Modifier,
    onMonthSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val currentName = EthiopianCalendarEngine.getGregorianMonthName(selectedMonth, isAmharic)

    Box(modifier = modifier) {
        OutlinedCard(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = currentName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            for (m in 1..12) {
                val name = EthiopianCalendarEngine.getGregorianMonthName(m, isAmharic)
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "$m. $name",
                            fontWeight = if (m == selectedMonth) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    onClick = {
                        onMonthSelected(m)
                        expanded = false
                    }
                )
            }
        }
    }
}

package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calendar.EthiopianCalendarEngine
import com.example.calendar.EthiopianDate
import com.example.calendar.GregorianDate
import com.example.calendar.HolidaysData
import com.example.ui.MainViewModel
import com.example.ui.theme.ZemenCrimson
import com.example.ui.theme.ZemenEmerald
import com.example.ui.theme.ZemenGold

@Composable
fun CalendarScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val settings by viewModel.userSettings.collectAsState()
    val reminders by viewModel.reminders.collectAsState()
    val viewedYear by viewModel.viewedYear.collectAsState()
    val viewedMonth by viewModel.viewedMonth.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()

    val isAmharic = settings.language == "am"
    val useGeez = settings.useGeezNumerals
    val todayEth = viewModel.todayEth
    val firstDayOfWeek = settings.firstDayOfWeek // 0 = Sun, 1 = Mon

    val monthName = EthiopianCalendarEngine.getEthiopianMonthName(viewedMonth, isAmharic)
    val evangelist = EthiopianCalendarEngine.getEvangelistName(viewedYear, isAmharic)
    val isLeapYear = EthiopianCalendarEngine.isEthiopianLeapYear(viewedYear)
    val daysInMonth = EthiopianCalendarEngine.getDaysInEthiopianMonth(viewedYear, viewedMonth)

    // Holidays for this month
    val monthHolidays = remember(viewedYear, viewedMonth) {
        HolidaysData.getAllHolidaysForYear(viewedYear).filter { it.ethDate.month == viewedMonth }
    }

    // Reminders for this month
    val monthReminders = remember(reminders, viewedYear, viewedMonth) {
        reminders.filter { it.ethYear == viewedYear && it.ethMonth == viewedMonth }
    }

    // First weekday of the month (0 = Sun .. 6 = Sat)
    val firstDayIndex = remember(viewedYear, viewedMonth) {
        EthiopianCalendarEngine.getFirstDayOfWeekForMonth(viewedYear, viewedMonth)
    }

    // Adjust for first day of week setting (Sunday or Monday)
    val startingEmptyCells = remember(firstDayIndex, firstDayOfWeek) {
        if (firstDayOfWeek == 1) { // Monday first
            (firstDayIndex + 6) % 7
        } else { // Sunday first
            firstDayIndex
        }
    }

    // Weekday headers list
    val weekdayHeaders = remember(firstDayOfWeek, isAmharic) {
        val days = EthiopianCalendarEngine.DAYS_OF_WEEK
        if (firstDayOfWeek == 1) {
            // Mon..Sat, Sun
            days.drop(1) + days.take(1)
        } else {
            days
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Month & Year Navigation Header
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.previousMonth() },
                        modifier = Modifier.testTag("btn_prev_month")
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Previous Month"
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$monthName ${viewModel.formatNumber(viewedYear, useGeez)}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        val leapText = if (isLeapYear) {
                            if (isAmharic) "ዘመነ ሉቃስ (ተጫማሪ ቀን ጳጉሜን 6)" else "Leap Year (Pagume 6 days)"
                        } else {
                            evangelist
                        }
                        Text(
                            text = leapText,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    IconButton(
                        onClick = { viewModel.nextMonth() },
                        modifier = Modifier.testTag("btn_next_month")
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "Next Month"
                        )
                    }
                }

                // Month Selector Pills (Meskerem..Pagume)
                Spacer(modifier = Modifier.height(10.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    itemsIndexed(EthiopianCalendarEngine.ETHIOPIAN_MONTHS) { idx, m ->
                        val mIndex = idx + 1
                        val isSelected = mIndex == viewedMonth
                        val pillName = if (isAmharic) m.nameAm else m.nameEn

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surface,
                            modifier = Modifier
                                .clickable {
                                    // Jump to month
                                    while (viewModel.viewedMonth.value < mIndex) viewModel.nextMonth()
                                    while (viewModel.viewedMonth.value > mIndex) viewModel.previousMonth()
                                }
                                .testTag("month_pill_$mIndex")
                        ) {
                            Text(
                                text = pillName,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurface,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                // Quick Navigation Toolbar (Jump to Today, Prev/Next Year)
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = { viewModel.previousYear() },
                        modifier = Modifier.testTag("btn_prev_year")
                    ) {
                        Text(text = if (isAmharic) "‹ ያለፈው ዓመት" else "‹ Prev Year", fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = { viewModel.jumpToToday() },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("btn_today")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Today,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isAmharic) "ዛሬ" else "Today",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    TextButton(
                        onClick = { viewModel.nextYear() },
                        modifier = Modifier.testTag("btn_next_year")
                    ) {
                        Text(text = if (isAmharic) "ቀጣይ ዓመት ›" else "Next Year ›", fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Weekday Headers Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (w in weekdayHeaders) {
                Text(
                    text = if (isAmharic) w.shortAm else w.shortEn,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (w.index == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Calendar Days Grid (7 columns)
        val totalCells = startingEmptyCells + daysInMonth
        val rows = (totalCells + 6) / 7

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            for (row in 0 until rows) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (col in 0..6) {
                        val cellIndex = row * 7 + col
                        val dayNumber = cellIndex - startingEmptyCells + 1

                        if (dayNumber in 1..daysInMonth) {
                            val thisDate = EthiopianDate(viewedYear, viewedMonth, dayNumber)
                            val gregEquiv = remember(thisDate) {
                                EthiopianCalendarEngine.toGregorian(thisDate)
                            }
                            val isToday = thisDate.year == todayEth.year &&
                                    thisDate.month == todayEth.month &&
                                    thisDate.day == todayEth.day

                            val isSelected = selectedDate?.year == thisDate.year &&
                                    selectedDate?.month == thisDate.month &&
                                    selectedDate?.day == thisDate.day

                            val hasHoliday = monthHolidays.any { it.ethDate.day == dayNumber }
                            val hasReminder = monthReminders.any { it.ethDay == dayNumber }

                            CalendarDayCell(
                                ethDay = dayNumber,
                                formattedEthDay = viewModel.formatNumber(dayNumber, useGeez),
                                gregDay = gregEquiv.day,
                                isToday = isToday,
                                isSelected = isSelected,
                                hasHoliday = hasHoliday,
                                hasReminder = hasReminder,
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .testTag("day_cell_${viewedMonth}_$dayNumber")
                            ) {
                                viewModel.selectDate(thisDate)
                            }
                        } else {
                            // Empty placeholder box
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                            )
                        }
                    }
                }
            }
        }

        // Legend / Indicator notes
        Spacer(modifier = Modifier.height(14.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(ZemenEmerald)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isAmharic) "በዓል" else "Holiday",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(ZemenCrimson)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isAmharic) "ማስታወሻ" else "Reminder",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isAmharic) "ዛሬ" else "Today",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Holidays for this month section
        if (monthHolidays.isNotEmpty()) {
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = if (isAmharic) "የዚህ ወር በዓላት" else "Holidays this month",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            for (h in monthHolidays) {
                val gMonth = EthiopianCalendarEngine.getGregorianMonthName(h.gregDate.month, isAmharic)
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { viewModel.selectDate(h.ethDate) }
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
                                text = if (isAmharic) h.nameAm else h.nameEn,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "$monthName ${viewModel.formatNumber(h.ethDate.day, useGeez)} ($gMonth ${h.gregDate.day})",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        if (h.isPublicHoliday) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Text(
                                    text = if (isAmharic) "የሕዝብ በዓል" else "Public",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun CalendarDayCell(
    ethDay: Int,
    formattedEthDay: String,
    gregDay: Int,
    isToday: Boolean,
    isSelected: Boolean,
    hasHoliday: Boolean,
    hasReminder: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val bgColor = when {
        isSelected -> MaterialTheme.colorScheme.primaryContainer
        isToday -> MaterialTheme.colorScheme.surfaceVariant
        else -> MaterialTheme.colorScheme.surface
    }

    val borderColor = when {
        isToday -> MaterialTheme.colorScheme.primary
        isSelected -> MaterialTheme.colorScheme.primary
        else -> Color.Transparent
    }

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = bgColor,
        border = if (isToday || isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, borderColor) else null,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(3.dp)
        ) {
            // Main Ethiopian Day number
            Text(
                text = formattedEthDay,
                fontWeight = if (isToday || isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                fontSize = 13.sp,
                color = when {
                    isSelected -> MaterialTheme.colorScheme.onPrimaryContainer
                    isToday -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.onSurface
                },
                modifier = Modifier.align(Alignment.TopStart)
            )

            // Subtle Gregorian day equivalent in bottom-right
            Text(
                text = gregDay.toString(),
                fontSize = 9.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                modifier = Modifier.align(Alignment.BottomEnd)
            )

            // Indicators row at bottom-left
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier.align(Alignment.BottomStart)
            ) {
                if (hasHoliday) {
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(ZemenEmerald)
                    )
                }
                if (hasReminder) {
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(ZemenCrimson)
                    )
                }
            }
        }
    }
}

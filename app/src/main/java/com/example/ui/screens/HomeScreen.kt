package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.calendar.EthiopianCalendarEngine
import com.example.calendar.HolidaysData
import com.example.ui.AppNavTab
import com.example.ui.MainViewModel
import com.example.ui.components.CategoryChip
import com.example.ui.components.PublicHolidayBadge
import com.example.ui.theme.ZemenEmerald
import com.example.ui.theme.ZemenGold

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val settings by viewModel.userSettings.collectAsState()
    val reminders by viewModel.reminders.collectAsState()

    val isAmharic = settings.language == "am"
    val isEthFirst = settings.calendarPreference == "ethiopian_first"
    val useGeez = settings.useGeezNumerals

    val todayEth = viewModel.todayEth
    val todayGreg = viewModel.todayGreg
    val dayOfWeek = viewModel.todayDayOfWeek

    val ethMonthName = EthiopianCalendarEngine.getEthiopianMonthName(todayEth.month, isAmharic)
    val gregMonthName = EthiopianCalendarEngine.getGregorianMonthName(todayGreg.month, isAmharic)
    val evangelist = EthiopianCalendarEngine.getEvangelistName(todayEth.year, isAmharic)

    val upcomingHolidays = remember(todayEth) {
        HolidaysData.getUpcomingHolidays(todayEth, limit = 2)
    }

    val todayReminders = remember(reminders, todayEth) {
        reminders.filter {
            it.ethYear == todayEth.year && it.ethMonth == todayEth.month && it.ethDay == todayEth.day
        }
    }

    val upcomingRemindersList = remember(reminders, todayEth) {
        val todayRd = EthiopianCalendarEngine.ethToFixed(todayEth.year, todayEth.month, todayEth.day)
        reminders.filter { !it.isCompleted }.filter {
            val rRd = EthiopianCalendarEngine.ethToFixed(it.ethYear, it.ethMonth, it.ethDay)
            rRd >= todayRd
        }.take(3)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top App Bar row with Branding & quick Search / Settings
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(MaterialTheme.colorScheme.primary, ZemenEmerald)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isAmharic) "ዘ" else "Z",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (isAmharic) "ዘመን" else "Zemen",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = evangelist,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Row {
                    IconButton(
                        onClick = { viewModel.openSearch() },
                        modifier = Modifier.testTag("home_search_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = if (isAmharic) "ፈልግ" else "Search"
                        )
                    }
                    IconButton(
                        onClick = { viewModel.openSettings() },
                        modifier = Modifier.testTag("home_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = if (isAmharic) "ቅንብሮች" else "Settings"
                        )
                    }
                }
            }
        }

        // Hero Today Date Card
        item {
            ElevatedCard(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("home_hero_card")
                    .clickable {
                        viewModel.selectDate(todayEth)
                    }
            ) {
                Box {
                    // Decorative subtle banner top image
                    Image(
                        painter = painterResource(id = R.drawable.zemen_hero_banner),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    )

                    // Gradient overlay to fade smoothly into the card surface
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.85f),
                                        MaterialTheme.colorScheme.surfaceVariant
                                    )
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        // Header pill: Today & Weekday
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Text(
                                    text = if (isAmharic) "ዛሬ" else "TODAY",
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }

                            Text(
                                text = if (isAmharic) dayOfWeek.nameAm else dayOfWeek.nameEn,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Large Primary Date based on user preference
                        val formattedDay = viewModel.formatNumber(todayEth.day, useGeez)
                        val formattedYear = viewModel.formatNumber(todayEth.year, useGeez)

                        if (isEthFirst) {
                            Text(
                                text = "$ethMonthName $formattedDay, $formattedYear",
                                style = MaterialTheme.typography.displayMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${gregMonthName} ${todayGreg.day}, ${todayGreg.year}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            Text(
                                text = "${gregMonthName} ${todayGreg.day}, ${todayGreg.year}",
                                style = MaterialTheme.typography.displayMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "$ethMonthName $formattedDay, $formattedYear",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Footer row inside hero card
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(ZemenEmerald)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isAmharic) "ትክክለኛ የኢትዮጵያ ዘመን" else "Accurate Ethiopian Calendar",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Text(
                                text = if (isAmharic) "ዝርዝር እይ ›" else "View details ›",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }

        // Quick Actions Row (Convert, Add Reminder, View Calendar, Holidays)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionItem(
                    icon = Icons.Default.SwapHoriz,
                    label = if (isAmharic) "ቀን ቀይር" else "Convert",
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("action_convert")
                ) {
                    viewModel.setTab(AppNavTab.CONVERTER)
                }

                QuickActionItem(
                    icon = Icons.Default.Add,
                    label = if (isAmharic) "ማስታወሻ" else "Add Event",
                    containerColor = ZemenEmerald.copy(alpha = 0.15f),
                    contentColor = ZemenEmerald,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("action_add_reminder")
                ) {
                    viewModel.openAddReminder(todayEth)
                }

                QuickActionItem(
                    icon = Icons.Default.CalendarMonth,
                    label = if (isAmharic) "ቀን መቁጠሪያ" else "Calendar",
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("action_view_calendar")
                ) {
                    viewModel.setTab(AppNavTab.CALENDAR)
                }

                QuickActionItem(
                    icon = Icons.Default.Celebration,
                    label = if (isAmharic) "በዓላት" else "Holidays",
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("action_holidays")
                ) {
                    viewModel.setTab(AppNavTab.HOLIDAYS)
                }
            }
        }

        // Upcoming Holiday Card
        if (upcomingHolidays.isNotEmpty()) {
            item {
                val nextHoliday = upcomingHolidays.first()
                val daysUntil = remember(todayEth, nextHoliday) {
                    EthiopianCalendarEngine.daysBetween(todayEth, nextHoliday.ethDate)
                }

                OutlinedCard(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("home_upcoming_holiday_card")
                        .clickable {
                            viewModel.setTab(AppNavTab.HOLIDAYS)
                        }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Celebration,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isAmharic) "ቀጣይ በዓል" else "UPCOMING HOLIDAY",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            CategoryChip(
                                category = nextHoliday.category,
                                isAmharic = isAmharic
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = if (isAmharic) nextHoliday.nameAm else nextHoliday.nameEn,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        val holidayMonth = EthiopianCalendarEngine.getEthiopianMonthName(nextHoliday.ethDate.month, isAmharic)
                        val holidayGregMonth = EthiopianCalendarEngine.getGregorianMonthName(nextHoliday.gregDate.month, isAmharic)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "$holidayMonth ${viewModel.formatNumber(nextHoliday.ethDate.day, useGeez)} ($holidayGregMonth ${nextHoliday.gregDate.day})",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (daysUntil == 0L) MaterialTheme.colorScheme.primaryContainer
                                else MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                val countdownText = when {
                                    daysUntil == 0L -> if (isAmharic) "ዛሬ ነው!" else "Today!"
                                    daysUntil == 1L -> if (isAmharic) "ነገ" else "Tomorrow"
                                    else -> if (isAmharic) "ከ $daysUntil ቀናት በኋላ" else "In $daysUntil days"
                                }
                                Text(
                                    text = countdownText,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (daysUntil == 0L) MaterialTheme.colorScheme.onPrimaryContainer
                                    else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Reminders for Today & Upcoming
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isAmharic) "ማስታወሻዎች እና ክስተቶች" else "Reminders & Events",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = if (isAmharic) "ሁሉንም እይ" else "View all",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable { viewModel.setTab(AppNavTab.REMINDERS) }
                        .padding(4.dp)
                )
            }
        }

        if (upcomingRemindersList.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (isAmharic) "ምንም የተቀመጠ ማስታወሻ የለም" else "No upcoming reminders",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (isAmharic) "+ ማስታወሻ ወይም ክስተት ለመጨመር ንካ" else "+ Tap to add a reminder or event",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable {
                                viewModel.openAddReminder(todayEth)
                            }
                        )
                    }
                }
            }
        } else {
            items(upcomingRemindersList) { reminder ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("reminder_item_${reminder.id}")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { viewModel.toggleReminderComplete(reminder) },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = if (reminder.isCompleted) Icons.Default.CheckCircle
                                else Icons.Default.RadioButtonUnchecked,
                                contentDescription = "Toggle completed",
                                tint = if (reminder.isCompleted) ZemenEmerald
                                else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = reminder.title,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            val rMonth = EthiopianCalendarEngine.getEthiopianMonthName(reminder.ethMonth, isAmharic)
                            val timeStr = String.format("%02d:%02d", reminder.hour, reminder.minute)
                            Text(
                                text = "$rMonth ${reminder.ethDay} • $timeStr (${reminder.type})",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                text = reminder.type,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun QuickActionItem(
    icon: ImageVector,
    label: String,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = containerColor,
        modifier = modifier.height(86.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = label,
                color = contentColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

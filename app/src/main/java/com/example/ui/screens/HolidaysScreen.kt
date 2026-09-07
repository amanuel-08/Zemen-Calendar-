package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.calendar.HolidayCategory
import com.example.calendar.HolidayInstance
import com.example.calendar.HolidaysData
import com.example.ui.MainViewModel
import com.example.ui.components.CategoryChip
import com.example.ui.components.PublicHolidayBadge

@Composable
fun HolidaysScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val settings by viewModel.userSettings.collectAsState()
    val viewedYear by viewModel.viewedYear.collectAsState()
    val selectedCategory by viewModel.holidayCategoryFilter.collectAsState()
    val searchQuery by viewModel.holidaySearchQuery.collectAsState()

    val isAmharic = settings.language == "am"
    val useGeez = settings.useGeezNumerals

    // Compute holidays for the currently viewed Ethiopian year
    val allHolidays = remember(viewedYear) {
        HolidaysData.getAllHolidaysForYear(viewedYear)
    }

    // Filter by Category and Search Query
    val filteredHolidays = remember(allHolidays, selectedCategory, searchQuery) {
        allHolidays.filter { h ->
            val matchCategory = selectedCategory == null || h.category == selectedCategory
            val matchQuery = if (searchQuery.isBlank()) true else {
                val q = searchQuery.trim().lowercase()
                h.nameAm.lowercase().contains(q) ||
                        h.nameEn.lowercase().contains(q) ||
                        h.descriptionAm.lowercase().contains(q) ||
                        h.descriptionEn.lowercase().contains(q) ||
                        "${h.ethDate.month}/${h.ethDate.day}".contains(q) ||
                        "${h.gregDate.month}/${h.gregDate.day}".contains(q)
            }
            matchCategory && matchQuery
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isAmharic) "የኢትዮጵያ በዓላት" else "Ethiopian Holidays",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (isAmharic) "የ ${viewModel.formatNumber(viewedYear, useGeez)} ዓ.ም. ብሔራዊና ሃይማኖታዊ በዓላት"
                        else "National, Religious & Cultural Holidays for ${viewedYear} EC",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = "${allHolidays.size} " + if (isAmharic) "በዓላት" else "Holidays",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Search Field
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setHolidaySearchQuery(it) },
                placeholder = {
                    Text(
                        text = if (isAmharic) "በዓል ፈልግ (ምሳሌ፦ መስቀል፣ ገና...)" else "Search holidays (e.g., Meskel, Genna...)",
                        fontSize = 13.sp
                    )
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null, modifier = Modifier.size(20.dp))
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setHolidaySearchQuery("") }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("holiday_search_input")
            )
        }

        // Category Filter Chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // "All" chip
                item {
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = { viewModel.setHolidayCategory(null) },
                        label = { Text(if (isAmharic) "ሁሉም" else "All") },
                        shape = RoundedCornerShape(10.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier.testTag("filter_all_holidays")
                    )
                }

                // Individual Category chips
                items(HolidayCategory.values()) { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { viewModel.setHolidayCategory(if (selectedCategory == cat) null else cat) },
                        label = { Text(if (isAmharic) cat.titleAm else cat.titleEn) },
                        shape = RoundedCornerShape(10.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier.testTag("filter_holiday_${cat.name}")
                    )
                }
            }
        }

        // List of Holidays
        if (filteredHolidays.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (isAmharic) "ምንም በዓል አልተገኘም" else "No holidays found",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isAmharic) "እባክዎ ሌላ ቃል ፈልገው ይሞክሩ" else "Try a different search query or category",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(filteredHolidays) { holiday ->
                HolidayCardItem(
                    holiday = holiday,
                    isAmharic = isAmharic,
                    useGeez = useGeez,
                    onTap = { viewModel.selectDate(holiday.ethDate) },
                    onAddToReminders = {
                        viewModel.addHolidayAsReminder(holiday)
                        Toast.makeText(
                            context,
                            if (isAmharic) "${holiday.nameAm} ወደ ማስታወሻ ተጨምሯል!"
                            else "${holiday.nameEn} added to reminders!",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    modifier = Modifier.testTag("holiday_card_${holiday.id}")
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun HolidayCardItem(
    holiday: HolidayInstance,
    isAmharic: Boolean,
    useGeez: Boolean,
    onTap: () -> Unit,
    onAddToReminders: () -> Unit,
    modifier: Modifier = Modifier
) {
    val ethMonthName = EthiopianCalendarEngine.getEthiopianMonthName(holiday.ethDate.month, isAmharic)
    val gregMonthName = EthiopianCalendarEngine.getGregorianMonthName(holiday.gregDate.month, isAmharic)
    val dow = EthiopianCalendarEngine.getDayOfWeek(holiday.ethDate)

    ElevatedCard(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.5.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onTap() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CategoryChip(category = holiday.category, isAmharic = isAmharic)
                    if (holiday.isPublicHoliday) {
                        PublicHolidayBadge(isAmharic = isAmharic)
                    }
                }

                Text(
                    text = if (isAmharic) dow.shortAm else dow.shortEn,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Title
            Text(
                text = if (isAmharic) holiday.nameAm else holiday.nameEn,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            // Subtitle English/Amharic alternate
            val secondaryName = if (isAmharic) holiday.nameEn else holiday.nameAm
            Text(
                text = secondaryName,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Dates Row (Ethiopian and Gregorian)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$ethMonthName ${holiday.ethDate.day}, ${holiday.ethDate.year} ዓ.ም.",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "•  $gregMonthName ${holiday.gregDate.day}, ${holiday.gregDate.year}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Description
            Text(
                text = if (isAmharic) holiday.descriptionAm else holiday.descriptionEn,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Action: Add to reminders
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onAddToReminders,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("btn_add_holiday_reminder_${holiday.id}")
                ) {
                    Icon(imageVector = Icons.Default.AddAlert, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isAmharic) "ወደ ማስታወሻ አስገባ" else "Add to Reminders",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

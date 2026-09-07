package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsSheet(
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    val settings by viewModel.userSettings.collectAsState()
    val isAmharic = settings.language == "am"
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
                    text = if (isAmharic) "ቅንብሮች" else "Settings",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Language Section
            SettingsSectionHeader(
                icon = Icons.Default.Language,
                title = if (isAmharic) "ቋንቋ (Language)" else "Language"
            )

            OutlinedCard(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    SettingsRadioRow(
                        title = "አማርኛ (Amharic)",
                        subtitle = "የኢትዮጵያ ቀንን በዋናነት ይጠቀሙ",
                        selected = settings.language == "am",
                        onClick = { viewModel.updateLanguage("am") },
                        modifier = Modifier.testTag("setting_lang_am")
                    )

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                    SettingsRadioRow(
                        title = "English",
                        subtitle = "Use English UI with Ethiopian calendar",
                        selected = settings.language == "en",
                        onClick = { viewModel.updateLanguage("en") },
                        modifier = Modifier.testTag("setting_lang_en")
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Calendar Preference Section
            SettingsSectionHeader(
                icon = Icons.Default.ViewAgenda,
                title = if (isAmharic) "የቀን መቁጠሪያ ቅድሚያ" else "Calendar Priority"
            )

            OutlinedCard(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    SettingsRadioRow(
                        title = if (isAmharic) "የኢትዮጵያ ቀን በቅድሚያ" else "Ethiopian First",
                        subtitle = if (isAmharic) "የኢትዮጵያ ቀን ጎልቶ እንዲታይ" else "Highlight Ethiopian date in primary view",
                        selected = settings.calendarPreference == "ethiopian_first",
                        onClick = { viewModel.updateCalendarPreference("ethiopian_first") },
                        modifier = Modifier.testTag("setting_pref_eth")
                    )

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                    SettingsRadioRow(
                        title = if (isAmharic) "የጎርጎሪዮሳዊ ቀን በቅድሚያ" else "Gregorian First",
                        subtitle = if (isAmharic) "የፈረንጆች ቀን ጎልቶ እንዲታይ" else "Highlight Gregorian date in primary view",
                        selected = settings.calendarPreference == "gregorian_first",
                        onClick = { viewModel.updateCalendarPreference("gregorian_first") },
                        modifier = Modifier.testTag("setting_pref_greg")
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Ge'ez Numerals Toggle
            SettingsSectionHeader(
                icon = Icons.Default.Numbers,
                title = if (isAmharic) "የቁጥር አጻጻፍ" else "Numeral Style"
            )

            OutlinedCard(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isAmharic) "የግዕዝ ቁጥሮች (፩፣ ፪፣ ፫...)" else "Ge'ez Numerals (፩, ፪, ፫...)",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        )
                        Text(
                            text = if (isAmharic) "ቀናትን በግዕዝ ቁጥር አሳይ" else "Display days and years using Ge'ez numerals",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Switch(
                        checked = settings.useGeezNumerals,
                        onCheckedChange = { viewModel.toggleGeezNumerals(it) },
                        modifier = Modifier.testTag("switch_geez_numerals")
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // First Day of Week Section
            SettingsSectionHeader(
                icon = Icons.Default.Today,
                title = if (isAmharic) "የሳምንቱ የመጀመሪያ ቀን" else "First Day of Week"
            )

            OutlinedCard(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    SettingsRadioRow(
                        title = if (isAmharic) "እሑድ (Sunday)" else "Sunday",
                        subtitle = if (isAmharic) "ሳምንቱን በእሑድ ጀምር" else "Start week on Sunday",
                        selected = settings.firstDayOfWeek == 0,
                        onClick = { viewModel.updateFirstDayOfWeek(0) },
                        modifier = Modifier.testTag("setting_dow_sun")
                    )

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                    SettingsRadioRow(
                        title = if (isAmharic) "ሰኞ (Monday)" else "Monday",
                        subtitle = if (isAmharic) "ሳምንቱን በሰኞ ጀምር" else "Start week on Monday",
                        selected = settings.firstDayOfWeek == 1,
                        onClick = { viewModel.updateFirstDayOfWeek(1) },
                        modifier = Modifier.testTag("setting_dow_mon")
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Theme Mode Section
            SettingsSectionHeader(
                icon = Icons.Default.DarkMode,
                title = if (isAmharic) "ገጽታ (Theme)" else "Theme"
            )

            OutlinedCard(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    SettingsRadioRow(
                        title = if (isAmharic) "የስልክ ስርዓት (System)" else "System Default",
                        subtitle = if (isAmharic) "ከስልኩ ገጽታ ጋር የሚስማማ" else "Follow device system dark/light mode",
                        selected = settings.themeMode == "system",
                        onClick = { viewModel.updateThemeMode("system") }
                    )

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                    SettingsRadioRow(
                        title = if (isAmharic) "ጨለማ ገጽታ (Dark)" else "Dark Theme",
                        subtitle = if (isAmharic) "ለዓይን ምቹ የሆነ የጨለማ ቀለም" else "High-contrast obsidian theme",
                        selected = settings.themeMode == "dark",
                        onClick = { viewModel.updateThemeMode("dark") }
                    )

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                    SettingsRadioRow(
                        title = if (isAmharic) "ብርሃን ገጽታ (Light)" else "Light Theme",
                        subtitle = if (isAmharic) "ንጹህ የብርሃን ቀለም" else "Clean warm light theme",
                        selected = settings.themeMode == "light",
                        onClick = { viewModel.updateThemeMode("light") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // About Zemen card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isAmharic) "ስለ ዘመን (About Zemen)" else "About Zemen",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (isAmharic)
                            "ዘመን ዘመናዊ፣ ፈጣን እና ከመስመር ውጭ (Offline) የሚሰራ የኢትዮጵያ የቀን መቁጠሪያና ምርታማነት መተግበሪያ ነው። የ13 ወራት ትክክለኛ የዘመን ቀመር፣ የበዓላት ዝርዝር እና የቀን መቀየሪያን ያካትታል።"
                        else
                            "Zemen is a modern, offline-first Ethiopian calendar application featuring 13-month solar calendar precision, comprehensive holiday records, instant date conversion, and local event reminders.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Version 1.0.0 • Offline-First Local Storage",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsSectionHeader(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 6.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun SettingsRadioRow(
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Text(text = subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        RadioButton(selected = selected, onClick = onClick)
    }
}

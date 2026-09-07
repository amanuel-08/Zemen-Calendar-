package com.example.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.AddReminderSheet
import com.example.ui.screens.CalendarScreen
import com.example.ui.screens.ConverterScreen
import com.example.ui.screens.DateDetailSheet
import com.example.ui.screens.HolidaysScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.RemindersScreen
import com.example.ui.screens.SearchDialog
import com.example.ui.screens.SettingsSheet

data class NavItem(
    val tab: AppNavTab,
    val icon: ImageVector,
    val testTag: String
)

@Composable
fun ZemenApp(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val currentTab by viewModel.currentTab.collectAsState()
    val settings by viewModel.userSettings.collectAsState()
    val isDateDetailOpen by viewModel.isDateDetailOpen.collectAsState()
    val isAddReminderOpen by viewModel.isAddReminderOpen.collectAsState()
    val isSettingsOpen by viewModel.isSettingsOpen.collectAsState()
    val isSearchOpen by viewModel.isSearchOpen.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()

    val isAmharic = settings.language == "am"

    val navItems = listOf(
        NavItem(AppNavTab.TODAY, Icons.Default.Today, "nav_today"),
        NavItem(AppNavTab.CALENDAR, Icons.Default.CalendarMonth, "nav_calendar"),
        NavItem(AppNavTab.CONVERTER, Icons.Default.SwapHoriz, "nav_converter"),
        NavItem(AppNavTab.HOLIDAYS, Icons.Default.Celebration, "nav_holidays"),
        NavItem(AppNavTab.REMINDERS, Icons.Default.Notifications, "nav_reminders")
    )

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                modifier = Modifier.testTag("main_navigation_bar")
            ) {
                navItems.forEach { item ->
                    val isSelected = currentTab == item.tab
                    val label = if (isAmharic) item.tab.titleAm else item.tab.titleEn

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.setTab(item.tab) },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = label,
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        label = {
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.testTag(item.testTag)
                    )
                }
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Crossfade(
                targetState = currentTab,
                label = "tab_crossfade"
            ) { tab ->
                when (tab) {
                    AppNavTab.TODAY -> HomeScreen(viewModel = viewModel)
                    AppNavTab.CALENDAR -> CalendarScreen(viewModel = viewModel)
                    AppNavTab.CONVERTER -> ConverterScreen(viewModel = viewModel)
                    AppNavTab.HOLIDAYS -> HolidaysScreen(viewModel = viewModel)
                    AppNavTab.REMINDERS -> RemindersScreen(viewModel = viewModel)
                }
            }

            // Bottom Sheets / Overlays
            if (isDateDetailOpen && selectedDate != null) {
                DateDetailSheet(
                    ethDate = selectedDate!!,
                    viewModel = viewModel,
                    onDismiss = { viewModel.closeDateDetail() }
                )
            }

            if (isAddReminderOpen) {
                AddReminderSheet(
                    viewModel = viewModel,
                    onDismiss = { viewModel.closeAddReminder() }
                )
            }

            if (isSettingsOpen) {
                SettingsSheet(
                    viewModel = viewModel,
                    onDismiss = { viewModel.closeSettings() }
                )
            }

            if (isSearchOpen) {
                SearchDialog(
                    viewModel = viewModel,
                    onDismiss = { viewModel.closeSearch() }
                )
            }
        }
    }
}

package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendar.DayOfWeekInfo
import com.example.calendar.EthiopianCalendarEngine
import com.example.calendar.EthiopianDate
import com.example.calendar.GeezUtils
import com.example.calendar.GregorianDate
import com.example.calendar.HolidayCategory
import com.example.calendar.HolidayInstance
import com.example.calendar.HolidaysData
import com.example.data.AppDatabase
import com.example.data.entity.ReminderEntity
import com.example.data.entity.UserSettingsEntity
import com.example.data.repository.CalendarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AppNavTab(val titleAm: String, val titleEn: String) {
    TODAY("ዛሬ", "Today"),
    CALENDAR("ቀን መቁጠሪያ", "Calendar"),
    CONVERTER("መቀየሪያ", "Converter"),
    HOLIDAYS("በዓላት", "Holidays"),
    REMINDERS("ማስታወሻ", "Reminders")
}

enum class ConverterDirection {
    ETHIOPIAN_TO_GREGORIAN,
    GREGORIAN_TO_ETHIOPIAN
}

data class ConverterUiState(
    val direction: ConverterDirection = ConverterDirection.ETHIOPIAN_TO_GREGORIAN,
    val ethYear: Int = 2018,
    val ethMonth: Int = 13,
    val ethDay: Int = 3,
    val gregYear: Int = 2026,
    val gregMonth: Int = 9,
    val gregDay: Int = 8,
    val convertedGreg: GregorianDate = GregorianDate(2026, 9, 8),
    val convertedEth: EthiopianDate = EthiopianDate(2018, 13, 3),
    val dayOfWeek: DayOfWeekInfo = EthiopianCalendarEngine.DAYS_OF_WEEK[2]
)

data class SearchResult(
    val holidays: List<HolidayInstance> = emptyList(),
    val reminders: List<ReminderEntity> = emptyList(),
    val parsedDate: EthiopianDate? = null,
    val parsedGreg: GregorianDate? = null
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CalendarRepository

    init {
        val db = AppDatabase.getInstance(application)
        repository = CalendarRepository(db.reminderDao(), db.userSettingsDao())
    }

    val userSettings: StateFlow<UserSettingsEntity> = repository.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserSettingsEntity())

    val reminders: StateFlow<List<ReminderEntity>> = repository.allReminders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Navigation & Viewport State
    private val _currentTab = MutableStateFlow(AppNavTab.TODAY)
    val currentTab: StateFlow<AppNavTab> = _currentTab.asStateFlow()

    // Current real-world dates
    val todayEth = EthiopianCalendarEngine.getTodayEthiopian()
    val todayGreg = EthiopianCalendarEngine.getTodayGregorian()
    val todayDayOfWeek = EthiopianCalendarEngine.getDayOfWeek(todayEth)

    // Viewed Month/Year in Calendar
    private val _viewedYear = MutableStateFlow(todayEth.year)
    val viewedYear: StateFlow<Int> = _viewedYear.asStateFlow()

    private val _viewedMonth = MutableStateFlow(todayEth.month)
    val viewedMonth: StateFlow<Int> = _viewedMonth.asStateFlow()

    // Selected Date for detail panel
    private val _selectedDate = MutableStateFlow<EthiopianDate?>(todayEth)
    val selectedDate: StateFlow<EthiopianDate?> = _selectedDate.asStateFlow()

    // Converter State
    private val _converterState = MutableStateFlow(
        ConverterUiState(
            direction = ConverterDirection.ETHIOPIAN_TO_GREGORIAN,
            ethYear = todayEth.year,
            ethMonth = todayEth.month,
            ethDay = todayEth.day,
            gregYear = todayGreg.year,
            gregMonth = todayGreg.month,
            gregDay = todayGreg.day,
            convertedGreg = todayGreg,
            convertedEth = todayEth,
            dayOfWeek = todayDayOfWeek
        )
    )
    val converterState: StateFlow<ConverterUiState> = _converterState.asStateFlow()

    // Holiday filter state
    private val _holidayCategoryFilter = MutableStateFlow<HolidayCategory?>(null)
    val holidayCategoryFilter: StateFlow<HolidayCategory?> = _holidayCategoryFilter.asStateFlow()

    private val _holidaySearchQuery = MutableStateFlow("")
    val holidaySearchQuery: StateFlow<String> = _holidaySearchQuery.asStateFlow()

    // Global Search State
    private val _globalSearchQuery = MutableStateFlow("")
    val globalSearchQuery: StateFlow<String> = _globalSearchQuery.asStateFlow()

    private val _isSearchOpen = MutableStateFlow(false)
    val isSearchOpen: StateFlow<Boolean> = _isSearchOpen.asStateFlow()

    // Sheet / Dialog states
    private val _isDateDetailOpen = MutableStateFlow(false)
    val isDateDetailOpen: StateFlow<Boolean> = _isDateDetailOpen.asStateFlow()

    private val _isAddReminderOpen = MutableStateFlow(false)
    val isAddReminderOpen: StateFlow<Boolean> = _isAddReminderOpen.asStateFlow()

    private val _isSettingsOpen = MutableStateFlow(false)
    val isSettingsOpen: StateFlow<Boolean> = _isSettingsOpen.asStateFlow()

    private val _reminderToEdit = MutableStateFlow<ReminderEntity?>(null)
    val reminderToEdit: StateFlow<ReminderEntity?> = _reminderToEdit.asStateFlow()

    fun setTab(tab: AppNavTab) {
        _currentTab.value = tab
    }

    // Calendar Navigation
    fun nextMonth() {
        if (_viewedMonth.value == 13) {
            _viewedMonth.value = 1
            _viewedYear.value += 1
        } else {
            _viewedMonth.value += 1
        }
    }

    fun previousMonth() {
        if (_viewedMonth.value == 1) {
            _viewedMonth.value = 13
            _viewedYear.value -= 1
        } else {
            _viewedMonth.value -= 1
        }
    }

    fun nextYear() {
        _viewedYear.value += 1
    }

    fun previousYear() {
        if (_viewedYear.value > 1) {
            _viewedYear.value -= 1
        }
    }

    fun jumpToToday() {
        _viewedYear.value = todayEth.year
        _viewedMonth.value = todayEth.month
        _selectedDate.value = todayEth
    }

    fun selectDate(ethDate: EthiopianDate) {
        _selectedDate.value = ethDate
        _isDateDetailOpen.value = true
    }

    fun closeDateDetail() {
        _isDateDetailOpen.value = false
    }

    // Converter Actions
    fun toggleConverterDirection() {
        _converterState.update { current ->
            val newDir = if (current.direction == ConverterDirection.ETHIOPIAN_TO_GREGORIAN) {
                ConverterDirection.GREGORIAN_TO_ETHIOPIAN
            } else {
                ConverterDirection.ETHIOPIAN_TO_GREGORIAN
            }
            // Recalculate
            recalculateConverter(current.copy(direction = newDir))
        }
    }

    fun updateEthConversionInput(year: Int, month: Int, day: Int) {
        val maxDays = EthiopianCalendarEngine.getDaysInEthiopianMonth(year, month)
        val validDay = day.coerceIn(1, maxDays)
        _converterState.update { current ->
            recalculateConverter(current.copy(ethYear = year, ethMonth = month, ethDay = validDay))
        }
    }

    fun updateGregConversionInput(year: Int, month: Int, day: Int) {
        val maxDays = EthiopianCalendarEngine.getDaysInGregorianMonth(year, month)
        val validDay = day.coerceIn(1, maxDays)
        _converterState.update { current ->
            recalculateConverter(current.copy(gregYear = year, gregMonth = month, gregDay = validDay))
        }
    }

    private fun recalculateConverter(state: ConverterUiState): ConverterUiState {
        return if (state.direction == ConverterDirection.ETHIOPIAN_TO_GREGORIAN) {
            val eth = EthiopianDate(state.ethYear, state.ethMonth, state.ethDay)
            val greg = EthiopianCalendarEngine.toGregorian(eth)
            val dow = EthiopianCalendarEngine.getDayOfWeek(eth)
            state.copy(
                convertedGreg = greg,
                gregYear = greg.year,
                gregMonth = greg.month,
                gregDay = greg.day,
                dayOfWeek = dow
            )
        } else {
            val greg = GregorianDate(state.gregYear, state.gregMonth, state.gregDay)
            val eth = EthiopianCalendarEngine.toEthiopian(greg)
            val dow = EthiopianCalendarEngine.getDayOfWeek(greg)
            state.copy(
                convertedEth = eth,
                ethYear = eth.year,
                ethMonth = eth.month,
                ethDay = eth.day,
                dayOfWeek = dow
            )
        }
    }

    // Holiday filter actions
    fun setHolidayCategory(category: HolidayCategory?) {
        _holidayCategoryFilter.value = category
    }

    fun setHolidaySearchQuery(query: String) {
        _holidaySearchQuery.value = query
    }

    // Reminders & Events
    fun openAddReminder(prefillEthDate: EthiopianDate? = null, reminder: ReminderEntity? = null) {
        _reminderToEdit.value = reminder
        if (prefillEthDate != null) {
            _selectedDate.value = prefillEthDate
        }
        _isAddReminderOpen.value = true
    }

    fun closeAddReminder() {
        _isAddReminderOpen.value = false
        _reminderToEdit.value = null
    }

    fun saveReminder(
        title: String,
        ethDate: EthiopianDate,
        hour: Int,
        minute: Int,
        type: String,
        repeatMode: String,
        leadTimeMinutes: Int,
        notes: String
    ) {
        viewModelScope.launch {
            val gregDate = EthiopianCalendarEngine.toGregorian(ethDate)
            repository.saveReminder(
                title = title,
                ethDate = ethDate,
                gregDate = gregDate,
                hour = hour,
                minute = minute,
                type = type,
                repeatMode = repeatMode,
                leadTimeMinutes = leadTimeMinutes,
                notes = notes
            )
            closeAddReminder()
        }
    }

    fun addHolidayAsReminder(holiday: HolidayInstance) {
        viewModelScope.launch {
            repository.addHolidayAsReminder(holiday)
        }
    }

    fun toggleReminderComplete(reminder: ReminderEntity) {
        viewModelScope.launch {
            repository.toggleReminderComplete(reminder.id, !reminder.isCompleted)
        }
    }

    fun deleteReminder(reminderId: Long) {
        viewModelScope.launch {
            repository.deleteReminder(reminderId)
        }
    }

    // Settings actions
    fun openSettings() {
        _isSettingsOpen.value = true
    }

    fun closeSettings() {
        _isSettingsOpen.value = false
    }

    fun updateLanguage(lang: String) {
        viewModelScope.launch {
            val current = userSettings.value
            repository.updateSettings(current.copy(language = lang))
        }
    }

    fun updateCalendarPreference(pref: String) {
        viewModelScope.launch {
            val current = userSettings.value
            repository.updateSettings(current.copy(calendarPreference = pref))
        }
    }

    fun updateFirstDayOfWeek(firstDay: Int) {
        viewModelScope.launch {
            val current = userSettings.value
            repository.updateSettings(current.copy(firstDayOfWeek = firstDay))
        }
    }

    fun toggleGeezNumerals(enabled: Boolean) {
        viewModelScope.launch {
            val current = userSettings.value
            repository.updateSettings(current.copy(useGeezNumerals = enabled))
        }
    }

    fun updateThemeMode(theme: String) {
        viewModelScope.launch {
            val current = userSettings.value
            repository.updateSettings(current.copy(themeMode = theme))
        }
    }

    // Global Search
    fun openSearch() {
        _isSearchOpen.value = true
    }

    fun closeSearch() {
        _isSearchOpen.value = false
        _globalSearchQuery.value = ""
    }

    fun setGlobalSearchQuery(query: String) {
        _globalSearchQuery.value = query
    }

    fun formatNumber(number: Int, useGeez: Boolean = userSettings.value.useGeezNumerals): String {
        return if (useGeez) GeezUtils.toGeez(number) else number.toString()
    }
}

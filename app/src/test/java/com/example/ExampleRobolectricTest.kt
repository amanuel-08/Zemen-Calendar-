package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.calendar.EthiopianCalendarEngine
import com.example.calendar.EthiopianDate
import com.example.calendar.GeezUtils
import com.example.calendar.GregorianDate
import com.example.calendar.HolidaysData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Zemen", appName)
    }

    @Test
    fun `test Ethiopian to Gregorian conversion`() {
        // Enkutatash 2017 EC = September 11, 2024 GC
        val greg = EthiopianCalendarEngine.toGregorian(EthiopianDate(2017, 1, 1))
        assertEquals(2024, greg.year)
        assertEquals(9, greg.month)
        assertEquals(11, greg.day)

        // Pagume 3, 2018 EC = September 8, 2026 GC (PRD verification case)
        val greg2026 = EthiopianCalendarEngine.toGregorian(EthiopianDate(2018, 13, 3))
        assertEquals(2026, greg2026.year)
        assertEquals(9, greg2026.month)
        assertEquals(8, greg2026.day)
    }

    @Test
    fun `test Gregorian to Ethiopian conversion`() {
        // September 11, 2024 GC = Meskerem 1, 2017 EC
        val eth = EthiopianCalendarEngine.toEthiopian(GregorianDate(2024, 9, 11))
        assertEquals(2017, eth.year)
        assertEquals(1, eth.month)
        assertEquals(1, eth.day)

        // September 8, 2026 GC = Pagume 3, 2018 EC
        val eth2026 = EthiopianCalendarEngine.toEthiopian(GregorianDate(2026, 9, 8))
        assertEquals(2018, eth2026.year)
        assertEquals(13, eth2026.month)
        assertEquals(3, eth2026.day)
    }

    @Test
    fun `test Ethiopian Leap Year rules`() {
        // In Ethiopian calendar, leap year is when year % 4 == 3
        assertTrue(EthiopianCalendarEngine.isEthiopianLeapYear(2015))
        assertFalse(EthiopianCalendarEngine.isEthiopianLeapYear(2016))
        assertFalse(EthiopianCalendarEngine.isEthiopianLeapYear(2017))
        assertFalse(EthiopianCalendarEngine.isEthiopianLeapYear(2018))
        assertTrue(EthiopianCalendarEngine.isEthiopianLeapYear(2019))

        // Days in Pagume: 6 in leap year, 5 otherwise
        assertEquals(6, EthiopianCalendarEngine.getDaysInEthiopianMonth(2015, 13))
        assertEquals(5, EthiopianCalendarEngine.getDaysInEthiopianMonth(2016, 13))
        assertEquals(6, EthiopianCalendarEngine.getDaysInEthiopianMonth(2019, 13))
    }

    @Test
    fun `test Geez numerals conversion`() {
        assertEquals("፩", GeezUtils.toGeez(1))
        assertEquals("፯", GeezUtils.toGeez(7))
        assertEquals("፲", GeezUtils.toGeez(10))
        assertEquals("፲፱", GeezUtils.toGeez(19))
        assertEquals("፳፻፲፰", GeezUtils.toGeez(2018))
        assertEquals("፳፻፲፱", GeezUtils.toGeez(2019))
    }

    @Test
    fun `test Holidays calculation`() {
        val holidays2018 = HolidaysData.getAllHolidaysForYear(2018)
        assertTrue(holidays2018.isNotEmpty())

        val enkutatash = holidays2018.find { it.id.startsWith("enkutatash") }
        assertEquals(1, enkutatash?.ethDate?.month)
        assertEquals(1, enkutatash?.ethDate?.day)

        val meskel = holidays2018.find { it.id.startsWith("meskel") }
        assertEquals(1, meskel?.ethDate?.month)
        assertEquals(17, meskel?.ethDate?.day)
    }
}

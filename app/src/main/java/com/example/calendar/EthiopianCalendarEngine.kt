package com.example.calendar

data class EthiopianDate(
    val year: Int,
    val month: Int, // 1..13
    val day: Int    // 1..30 (or 1..5/6 for Pagume)
) {
    override fun toString(): String = String.format("%04d-%02d-%02d", year, month, day)
}

data class GregorianDate(
    val year: Int,
    val month: Int, // 1..12
    val day: Int    // 1..31
) {
    override fun toString(): String = String.format("%04d-%02d-%02d", year, month, day)
}

data class MonthInfo(
    val index: Int,
    val nameAm: String,
    val nameEn: String,
    val daysCount: Int
)

data class DayOfWeekInfo(
    val index: Int, // 0 = Sunday, 6 = Saturday
    val nameAm: String,
    val nameEn: String,
    val shortAm: String,
    val shortEn: String
)

object EthiopianCalendarEngine {
    // Meskerem 1, 1 EC in Rata Die (fixed days from 0001-01-01 Gregorian)
    // Meskerem 1, 1 EC = August 27, 8 CE Gregorian = RD 2796
    const val ETHIOPIAN_EPOCH_RD = 2796L

    val ETHIOPIAN_MONTHS = listOf(
        MonthInfo(1, "መስከረም", "Meskerem", 30),
        MonthInfo(2, "ጥቅምት", "Tikimt", 30),
        MonthInfo(3, "ኅዳር", "Hidar", 30),
        MonthInfo(4, "ታኅሣሥ", "Tahsas", 30),
        MonthInfo(5, "ጥር", "Tir", 30),
        MonthInfo(6, "የካቲት", "Yekatit", 30),
        MonthInfo(7, "መጋቢት", "Megabit", 30),
        MonthInfo(8, "ሚያዝያ", "Miazia", 30),
        MonthInfo(9, "ግንቦት", "Ginbot", 30),
        MonthInfo(10, "ሰኔ", "Sene", 30),
        MonthInfo(11, "ሐምሌ", "Hamle", 30),
        MonthInfo(12, "ነሐሴ", "Nehase", 30),
        MonthInfo(13, "ጳጉሜን", "Pagume", 5) // 6 in leap years
    )

    val GREGORIAN_MONTH_NAMES_EN = listOf(
        "", "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    val GREGORIAN_MONTH_NAMES_AM = listOf(
        "", "ጃንዩወሪ", "ፌብሩወሪ", "ማርች", "ኤፕሪል", "ሜይ", "ጁን",
        "ጁላይ", "ኦገስት", "ሴፕቴምበር", "ኦክቶበር", "ኖቬምበር", "ዲሴምበር"
    )

    val DAYS_OF_WEEK = listOf(
        DayOfWeekInfo(0, "እሑድ", "Sunday", "እሑ", "Sun"),
        DayOfWeekInfo(1, "ሰኞ", "Monday", "ሰኞ", "Mon"),
        DayOfWeekInfo(2, "ማክሰኞ", "Tuesday", "ማክ", "Tue"),
        DayOfWeekInfo(3, "ረቡዕ", "Wednesday", "ረቡ", "Wed"),
        DayOfWeekInfo(4, "ሐሙስ", "Thursday", "ሐሙ", "Thu"),
        DayOfWeekInfo(5, "ዓርብ", "Friday", "ዓር", "Fri"),
        DayOfWeekInfo(6, "ቅዳሜ", "Saturday", "ቅዳ", "Sat")
    )

    /**
     * An Ethiopian year is a leap year if (year % 4 == 3).
     * In leap years, the 13th month (Pagume) has 6 days instead of 5.
     */
    fun isEthiopianLeapYear(year: Int): Boolean {
        return (year % 4) == 3
    }

    /**
     * A Gregorian year is a leap year if (year % 4 == 0 and year % 100 != 0) or (year % 400 == 0).
     */
    fun isGregorianLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }

    fun getDaysInEthiopianMonth(year: Int, month: Int): Int {
        return when (month) {
            in 1..12 -> 30
            13 -> if (isEthiopianLeapYear(year)) 6 else 5
            else -> 30
        }
    }

    fun getDaysInGregorianMonth(year: Int, month: Int): Int {
        return when (month) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            4, 6, 9, 11 -> 30
            2 -> if (isGregorianLeapYear(year)) 29 else 28
            else -> 30
        }
    }

    fun getEthiopianMonthName(month: Int, isAmharic: Boolean = true): String {
        val safeMonth = month.coerceIn(1, 13)
        return if (isAmharic) ETHIOPIAN_MONTHS[safeMonth - 1].nameAm
        else ETHIOPIAN_MONTHS[safeMonth - 1].nameEn
    }

    fun getGregorianMonthName(month: Int, isAmharic: Boolean = false): String {
        val safeMonth = month.coerceIn(1, 12)
        return if (isAmharic) GREGORIAN_MONTH_NAMES_AM[safeMonth]
        else GREGORIAN_MONTH_NAMES_EN[safeMonth]
    }

    /**
     * Evangelist / Ethiopian year named according to the 4 Evangelists:
     * year % 4 == 0 -> John (ዮሐንስ)
     * year % 4 == 1 -> Matthew (ማቴዎስ)
     * year % 4 == 2 -> Mark (ማርቆስ)
     * year % 4 == 3 -> Luke (ሉቃስ - Leap year)
     */
    fun getEvangelistName(year: Int, isAmharic: Boolean = true): String {
        val rem = Math.floorMod(year, 4)
        return if (isAmharic) {
            when (rem) {
                0 -> "ዘመነ ዮሐንስ"
                1 -> "ዘመነ ማቴዎስ"
                2 -> "ዘመነ ማርቆስ"
                else -> "ዘመነ ሉቃስ"
            }
        } else {
            when (rem) {
                0 -> "Year of John"
                1 -> "Year of Matthew"
                2 -> "Year of Mark"
                else -> "Year of Luke"
            }
        }
    }

    // Fixed Days (Rata Die) conversion
    fun ethToFixed(year: Int, month: Int, day: Int): Long {
        return ETHIOPIAN_EPOCH_RD +
                365L * (year - 1) +
                Math.floorDiv((year).toLong(), 4L) +
                30L * (month - 1) +
                day - 1L
    }

    fun fixedToEth(rd: Long): EthiopianDate {
        val days = rd - ETHIOPIAN_EPOCH_RD
        val cycle = Math.floorDiv(days, 1461L)
        val rem = Math.floorMod(days, 1461L).toInt()

        val yOffset: Int
        val dayOfYear: Int
        when {
            rem < 365 -> {
                yOffset = 0
                dayOfYear = rem
            }
            rem < 730 -> {
                yOffset = 1
                dayOfYear = rem - 365
            }
            rem < 1096 -> {
                yOffset = 2
                dayOfYear = rem - 730
            }
            else -> {
                yOffset = 3
                dayOfYear = rem - 1096
            }
        }

        val year = (4L * cycle + yOffset + 1).toInt()
        val month = dayOfYear / 30 + 1
        val day = dayOfYear % 30 + 1
        return EthiopianDate(year, month, day)
    }

    fun gregToFixed(year: Int, month: Int, day: Int): Long {
        val a = (14 - month) / 12
        val y = year + 4800 - a
        val m = month + 12 * a - 3
        val jdn = day + (153 * m + 2) / 5 + 365L * y + y / 4 - y / 100 + y / 400 - 32045L
        return jdn - 1721425L
    }

    fun fixedToGregorian(rd: Long): GregorianDate {
        val jdn = rd + 1721425L
        val l = jdn + 68569L
        val n = (4L * l) / 146097L
        val l1 = l - (146097L * n + 3L) / 4L
        val i = (4000L * (l1 + 1L)) / 1461001L
        val l2 = l1 - (1461L * i) / 4L + 31L
        val j = (80L * l2) / 2447L
        val day = (l2 - (2447L * j) / 80L).toInt()
        val l3 = j / 11L
        val month = (j + 2L - 12L * l3).toInt()
        val year = (100L * (n - 49L) + i + l3).toInt()
        return GregorianDate(year, month, day)
    }

    // Public API conversion methods
    fun toGregorian(ethDate: EthiopianDate): GregorianDate {
        val rd = ethToFixed(ethDate.year, ethDate.month, ethDate.day)
        return fixedToGregorian(rd)
    }

    fun toEthiopian(gregDate: GregorianDate): EthiopianDate {
        val rd = gregToFixed(gregDate.year, gregDate.month, gregDate.day)
        return fixedToEth(rd)
    }

    /**
     * Day of week: 0 = Sunday, 1 = Monday ... 6 = Saturday
     */
    fun getDayOfWeek(ethDate: EthiopianDate): DayOfWeekInfo {
        val rd = ethToFixed(ethDate.year, ethDate.month, ethDate.day)
        val dayIndex = Math.floorMod(rd, 7L).toInt()
        return DAYS_OF_WEEK[dayIndex]
    }

    fun getDayOfWeek(gregDate: GregorianDate): DayOfWeekInfo {
        val rd = gregToFixed(gregDate.year, gregDate.month, gregDate.day)
        val dayIndex = Math.floorMod(rd, 7L).toInt()
        return DAYS_OF_WEEK[dayIndex]
    }

    /**
     * Calculates the weekday index of the first day of an Ethiopian month
     */
    fun getFirstDayOfWeekForMonth(year: Int, month: Int): Int {
        val rd = ethToFixed(year, month, 1)
        return Math.floorMod(rd, 7L).toInt()
    }

    /**
     * Today's Ethiopian Date based on Gregorian date
     */
    fun getTodayEthiopian(): EthiopianDate {
        val calendar = java.util.Calendar.getInstance()
        val gy = calendar.get(java.util.Calendar.YEAR)
        val gm = calendar.get(java.util.Calendar.MONTH) + 1
        val gd = calendar.get(java.util.Calendar.DAY_OF_MONTH)
        return toEthiopian(GregorianDate(gy, gm, gd))
    }

    fun getTodayGregorian(): GregorianDate {
        val calendar = java.util.Calendar.getInstance()
        val gy = calendar.get(java.util.Calendar.YEAR)
        val gm = calendar.get(java.util.Calendar.MONTH) + 1
        val gd = calendar.get(java.util.Calendar.DAY_OF_MONTH)
        return GregorianDate(gy, gm, gd)
    }

    /**
     * Validates date components
     */
    fun isValidEthiopianDate(year: Int, month: Int, day: Int): Boolean {
        if (year < 1 || month !in 1..13) return false
        val maxDays = getDaysInEthiopianMonth(year, month)
        return day in 1..maxDays
    }

    fun isValidGregorianDate(year: Int, month: Int, day: Int): Boolean {
        if (year < 1 || month !in 1..12) return false
        val maxDays = getDaysInGregorianMonth(year, month)
        return day in 1..maxDays
    }

    /**
     * Number of days between two Ethiopian dates
     */
    fun daysBetween(from: EthiopianDate, to: EthiopianDate): Long {
        val rdFrom = ethToFixed(from.year, from.month, from.day)
        val rdTo = ethToFixed(to.year, to.month, to.day)
        return rdTo - rdFrom
    }
}

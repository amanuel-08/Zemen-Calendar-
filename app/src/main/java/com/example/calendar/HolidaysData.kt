package com.example.calendar

enum class HolidayCategory(val titleAm: String, val titleEn: String) {
    NATIONAL("ብሔራዊ", "National"),
    RELIGIOUS("ሃይማኖታዊ", "Religious"),
    CULTURAL("ባህላዊ", "Cultural"),
    INTERNATIONAL("ዓለም አቀፍ", "International")
}

data class Holiday(
    val id: String,
    val nameAm: String,
    val nameEn: String,
    val ethMonth: Int,
    val ethDay: Int,
    val category: HolidayCategory,
    val isPublicHoliday: Boolean,
    val descriptionAm: String,
    val descriptionEn: String,
    val isMovable: Boolean = false
)

data class HolidayInstance(
    val id: String,
    val nameAm: String,
    val nameEn: String,
    val ethDate: EthiopianDate,
    val gregDate: GregorianDate,
    val category: HolidayCategory,
    val isPublicHoliday: Boolean,
    val descriptionAm: String,
    val descriptionEn: String
)

object HolidaysData {

    // Fixed Ethiopian holidays that occur on the same Ethiopian day every year
    val FIXED_HOLIDAYS = listOf(
        Holiday(
            id = "enkutatash",
            nameAm = "እንቁጣጣሽ (የኢትዮጵያ አዲስ ዓመት)",
            nameEn = "Enkutatash (Ethiopian New Year)",
            ethMonth = 1,
            ethDay = 1,
            category = HolidayCategory.NATIONAL,
            isPublicHoliday = true,
            descriptionAm = "የኢትዮጵያ አዲስ ዓመት በዓል፣ አሮጌው ዓመት አልፎ አዲሱ ዓመት የሚቀበልበት ታላቅ የደስታ ቀን።",
            descriptionEn = "Ethiopian New Year celebrated on Meskerem 1 marking the end of the rainy season and welcoming the spring."
        ),
        Holiday(
            id = "meskel",
            nameAm = "መስቀል (ደመራ)",
            nameEn = "Meskel (Finding of the True Cross)",
            ethMonth = 1,
            ethDay = 17,
            category = HolidayCategory.RELIGIOUS,
            isPublicHoliday = true,
            descriptionAm = "ደመራ ተደምሮ የሚከበረው የቅዱስ መስቀል መገኛ በዓል፤ በዩኔስኮ የተመዘገበ የማይዳሰስ ቅርስ።",
            descriptionEn = "Feast commemorating the discovery of the True Cross by Queen Helena, celebrated with Demera bonfires."
        ),
        Holiday(
            id = "hidar_tsion",
            nameAm = "ኅዳር ጽዮን",
            nameEn = "Hidar Tsion (St. Mary of Zion)",
            ethMonth = 3,
            ethDay = 21,
            category = HolidayCategory.RELIGIOUS,
            isPublicHoliday = false,
            descriptionAm = "ጽላተ ኪዳን ወደ ኢትዮጵያ አክሱም የገባችበትን መታሰቢያ በዓል።",
            descriptionEn = "Feast commemorating the arrival of the Ark of the Covenant at Axum Zion."
        ),
        Holiday(
            id = "genna",
            nameAm = "ገና (የጌታችን የኢየሱስ ክርስቶስ ልደት)",
            nameEn = "Genna (Ethiopian Christmas)",
            ethMonth = 4,
            ethDay = 29, // 28 in leap years handled dynamically
            category = HolidayCategory.RELIGIOUS,
            isPublicHoliday = true,
            descriptionAm = "የጌታችን የመድኃኒታችን የኢየሱስ ክርስቶስ የልደት በዓል በታላቅ ድምቀት የሚከበርበት ቀን።",
            descriptionEn = "Ethiopian Christmas celebrating the birth of Jesus Christ, accompanied by traditional Genna games."
        ),
        Holiday(
            id = "timkat",
            nameAm = "ጥምቀት (ኤጲፋንያ)",
            nameEn = "Timkat (Ethiopian Epiphany)",
            ethMonth = 5,
            ethDay = 11,
            category = HolidayCategory.RELIGIOUS,
            isPublicHoliday = true,
            descriptionAm = "የኢየሱስ ክርስቶስ የዮርዳኖስ ጥምቀት መታሰቢያ፤ ታቦታት በዑደት ወደ ጥምቀተ ባህር ወርደው በድምቀት የሚከበር።",
            descriptionEn = "Ethiopian Epiphany celebrating the baptism of Jesus in the Jordan River, famous for colorful tabot processions."
        ),
        Holiday(
            id = "kana_zegalila",
            nameAm = "ቃና ዘገሊላ",
            nameEn = "Kana Ze Galila (Miracle at Cana)",
            ethMonth = 5,
            ethDay = 12,
            category = HolidayCategory.RELIGIOUS,
            isPublicHoliday = false,
            descriptionAm = "ኢየሱስ ክርስቶስ በቃና ዘገሊላ ውኃውን ወደ ወይን ጠጅ የቀየረበት የመጀመርያው ተአምር መታሰቢያ።",
            descriptionEn = "Commemorating Jesus's first miracle of turning water into wine at the wedding in Cana."
        ),
        Holiday(
            id = "adwa",
            nameAm = "የዓድዋ ድል በዓል",
            nameEn = "Victory of Adwa Day",
            ethMonth = 6,
            ethDay = 23,
            category = HolidayCategory.NATIONAL,
            isPublicHoliday = true,
            descriptionAm = "በ1888 ዓ.ም. ኢትዮጵያ የጣሊያንን ወራሪ ኃይል ድል ያደረገችበት የአፍሪካ የነጻነት ምልክት ድል።",
            descriptionEn = "Commemorating Ethiopia's decisive victory over Italian colonial forces in 1896, a landmark of African sovereignty."
        ),
        Holiday(
            id = "patriots_day",
            nameAm = "የአርበኞች ቀን",
            nameEn = "Patriots' Victory Day",
            ethMonth = 8,
            ethDay = 27,
            category = HolidayCategory.NATIONAL,
            isPublicHoliday = true,
            descriptionAm = "የፋሺስት ጣሊያን 5 ዓመታት ወረራ ያበቃበትና የኢትዮጵያ ጀግኖች አርበኞች ድል መታሰቢያ።",
            descriptionEn = "Honoring the brave Ethiopian patriots who resisted Fascist Italian occupation from 1936 to 1941."
        ),
        Holiday(
            id = "ginbot_20",
            nameAm = "ግንቦት 20 (የደርግ ውድቀት ቀን)",
            nameEn = "Ginbot 20 (Downfall of Derg)",
            ethMonth = 9,
            ethDay = 20,
            category = HolidayCategory.NATIONAL,
            isPublicHoliday = true,
            descriptionAm = "በ1983 ዓ.ም. ወታደራዊው የደርግ አገዛዝ የወደቀበት ቀን።",
            descriptionEn = "Commemorating the downfall of the military Derg regime in 1991."
        ),
        Holiday(
            id = "buhe",
            nameAm = "ቡሄ (ደብረ ታቦር)",
            nameEn = "Buhe (Transfiguration / Debre Tabor)",
            ethMonth = 12,
            ethDay = 16,
            category = HolidayCategory.CULTURAL,
            isPublicHoliday = false,
            descriptionAm = "በደብረ ታቦር ተራራ ላይ የጌታችን ብርሃነ መለኮት የተገለጠበት፤ ጅራፍና ችቦ ተበራርቶ 'ሆያ ሆዬ' የሚዘመርበት በዓል።",
            descriptionEn = "Feast of the Transfiguration on Mount Tabor, celebrated with torches (chibo), whip cracking, and boys singing Hoya Hoye."
        ),
        Holiday(
            id = "filseta",
            nameAm = "ፍልሰታ ለማርያም",
            nameEn = "Filseta (Assumption of Mary)",
            ethMonth = 12,
            ethDay = 16,
            category = HolidayCategory.RELIGIOUS,
            isPublicHoliday = false,
            descriptionAm = "የእመቤታችን ቅድስት ድንግል ማርያም የትንሣኤዋና የእርገቷ በዓል።",
            descriptionEn = "Feast commemorating the dormition and bodily assumption of the Virgin Mary."
        ),
        Holiday(
            id = "peace_day",
            nameAm = "የሰላም ቀን (ጳጉሜን 3)",
            nameEn = "Peace Day (Pagume 3)",
            ethMonth = 13,
            ethDay = 3,
            category = HolidayCategory.NATIONAL,
            isPublicHoliday = false,
            descriptionAm = "በጳጉሜን ቀናት የሚከበረው የኢትዮጵያ ወጣቶች፣ እርቅና የአገራዊ ሰላም ማስተንተኛ ቀን።",
            descriptionEn = "National reflection day during Pagume dedicated to unity, reconciliation, and national peace."
        )
    )

    /**
     * Computes the Alexandrian Julian Easter Day (Fasika) for a given Ethiopian Year.
     * Uses the Gauss/Meeus computus for the Julian calendar, then converts to Ethiopian Date.
     */
    fun calculateFasika(ethYear: Int): EthiopianDate {
        // Ethiopian Year ethYear corresponds to Julian/Gregorian year:
        val julianYear = ethYear + 8
        val a = julianYear % 4
        val b = julianYear % 7
        val c = julianYear % 19
        val d = (19 * c + 15) % 30
        val e = (2 * a + 4 * b - d + 34) % 7
        val monthJulian = (d + e + 114) / 31 // 3 = March, 4 = April (Julian)
        val dayJulian = ((d + e + 114) % 31) + 1

        // Julian to Gregorian offset for centuries 20th-21st is 13 days
        val century = julianYear / 100
        val julianOffset = century - (century / 4) - 2
        val gregDate = addDaysToGregorian(julianYear, monthJulian, dayJulian, julianOffset)
        return EthiopianCalendarEngine.toEthiopian(gregDate)
    }

    private fun addDaysToGregorian(y: Int, m: Int, d: Int, days: Int): GregorianDate {
        val cal = java.util.Calendar.getInstance()
        cal.set(y, m - 1, d)
        cal.add(java.util.Calendar.DAY_OF_MONTH, days)
        return GregorianDate(
            cal.get(java.util.Calendar.YEAR),
            cal.get(java.util.Calendar.MONTH) + 1,
            cal.get(java.util.Calendar.DAY_OF_MONTH)
        )
    }

    private fun addDaysToEth(ethDate: EthiopianDate, days: Long): EthiopianDate {
        val rd = EthiopianCalendarEngine.ethToFixed(ethDate.year, ethDate.month, ethDate.day) + days
        return EthiopianCalendarEngine.fixedToEth(rd)
    }

    /**
     * Islamic Holidays for modern years (approximated accurately to the solar Ethiopian calendar)
     */
    private val ISLAMIC_HOLIDAYS_TABLE = mapOf(
        // ethYear to list of (nameAm, nameEn, ethMonth, ethDay, isPublic, descAm, descEn)
        2015 to listOf(
            IslamicEntry("ዒድ አልፈጥር", "Eid al-Fitr", 8, 13, true, "የረመዳን ጾም መጠናቀቂያ በዓል።", "Celebration marking the end of Ramadan."),
            IslamicEntry("ዒድ አል አድሃ (አረፋ)", "Eid al-Adha (Arefa)", 10, 21, true, "የመስዋዕት በዓል።", "Feast of Sacrifice commemorating Ibrahim's devotion."),
            IslamicEntry("መውሊድ", "Mawlid", 1, 16, true, "የነቢዩ ሙሐመድ የልደት በዓል።", "Birthday of the Prophet Muhammad.")
        ),
        2016 to listOf(
            IslamicEntry("ዒድ አልፈጥር", "Eid al-Fitr", 8, 2, true, "የረመዳን ጾም መጠናቀቂያ በዓል።", "Celebration marking the end of Ramadan."),
            IslamicEntry("ዒድ አል አድሃ (አረፋ)", "Eid al-Adha (Arefa)", 10, 10, true, "የመስዋዕት በዓል።", "Feast of Sacrifice commemorating Ibrahim's devotion."),
            IslamicEntry("መውሊድ", "Mawlid", 1, 6, true, "የነቢዩ ሙሐመድ የልደት በዓል።", "Birthday of the Prophet Muhammad.")
        ),
        2017 to listOf(
            IslamicEntry("ዒድ አልፈጥር", "Eid al-Fitr", 7, 22, true, "የረመዳን ጾም መጠናቀቂያ በዓል።", "Celebration marking the end of Ramadan."),
            IslamicEntry("ዒድ አል አድሃ (አረፋ)", "Eid al-Adha (Arefa)", 9, 29, true, "የመስዋዕት በዓል።", "Feast of Sacrifice commemorating Ibrahim's devotion."),
            IslamicEntry("መውሊድ", "Mawlid", 12, 26, true, "የነቢዩ ሙሐመድ የልደት በዓል።", "Birthday of the Prophet Muhammad.")
        ),
        2018 to listOf(
            IslamicEntry("ዒድ አልፈጥር", "Eid al-Fitr", 7, 11, true, "የረመዳን ጾም መጠናቀቂያ በዓል።", "Celebration marking the end of Ramadan."),
            IslamicEntry("ዒድ አል አድሃ (አረፋ)", "Eid al-Adha (Arefa)", 9, 19, true, "የመስዋዕት በዓል።", "Feast of Sacrifice commemorating Ibrahim's devotion."),
            IslamicEntry("መውሊድ", "Mawlid", 12, 16, true, "የነቢዩ ሙሐመድ የልደት በዓል።", "Birthday of the Prophet Muhammad.")
        ),
        2019 to listOf(
            IslamicEntry("ዒድ አልፈጥር", "Eid al-Fitr", 7, 1, true, "የረመዳን ጾም መጠናቀቂያ በዓል።", "Celebration marking the end of Ramadan."),
            IslamicEntry("ዒድ አል አድሃ (አረፋ)", "Eid al-Adha (Arefa)", 9, 8, true, "የመስዋዕት በዓል።", "Feast of Sacrifice commemorating Ibrahim's devotion."),
            IslamicEntry("መውሊድ", "Mawlid", 12, 5, true, "የነቢዩ ሙሐመድ የልደት በዓል።", "Birthday of the Prophet Muhammad.")
        ),
        2020 to listOf(
            IslamicEntry("ዒድ አልፈጥር", "Eid al-Fitr", 6, 21, true, "የረመዳን ጾም መጠናቀቂያ በዓል።", "Celebration marking the end of Ramadan."),
            IslamicEntry("ዒድ አል አድሃ (አረፋ)", "Eid al-Adha (Arefa)", 8, 28, true, "የመስዋዕት በዓል።", "Feast of Sacrifice commemorating Ibrahim's devotion."),
            IslamicEntry("መውሊድ", "Mawlid", 11, 24, true, "የነቢዩ ሙሐመድ የልደት በዓል።", "Birthday of the Prophet Muhammad.")
        )
    )

    private data class IslamicEntry(
        val nameAm: String,
        val nameEn: String,
        val ethMonth: Int,
        val ethDay: Int,
        val isPublic: Boolean,
        val descAm: String,
        val descEn: String
    )

    /**
     * Returns all holidays for an Ethiopian year (fixed, movable Christian, movable Islamic).
     */
    fun getAllHolidaysForYear(ethYear: Int): List<HolidayInstance> {
        val result = mutableListOf<HolidayInstance>()

        // 1. Fixed Holidays
        for (h in FIXED_HOLIDAYS) {
            val day = if (h.id == "genna") {
                // Genna is Tahsas 28 in Ethiopian leap year (when year % 4 == 3)
                if (EthiopianCalendarEngine.isEthiopianLeapYear(ethYear)) 28 else 29
            } else {
                h.ethDay
            }
            val ethDate = EthiopianDate(ethYear, h.ethMonth, day)
            val gregDate = EthiopianCalendarEngine.toGregorian(ethDate)
            result.add(
                HolidayInstance(
                    id = "${h.id}_$ethYear",
                    nameAm = h.nameAm,
                    nameEn = h.nameEn,
                    ethDate = ethDate,
                    gregDate = gregDate,
                    category = h.category,
                    isPublicHoliday = h.isPublicHoliday,
                    descriptionAm = h.descriptionAm,
                    descriptionEn = h.descriptionEn
                )
            )
        }

        // 2. Movable Christian Holidays based on Fasika
        val fasika = calculateFasika(ethYear)
        val siklet = addDaysToEth(fasika, -2) // Good Friday
        val hosanna = addDaysToEth(fasika, -7) // Palm Sunday
        val debreZeyt = addDaysToEth(fasika, -28) // Mount of Olives
        val lentStart = addDaysToEth(fasika, -55) // Great Lent
        val erget = addDaysToEth(fasika, 39) // Ascension
        val peraklit = addDaysToEth(fasika, 49) // Pentecost

        val movableChristian = listOf(
            HolidayInstance(
                id = "lent_start_$ethYear",
                nameAm = "ዓቢይ ጾም (ጾመ ሁርካል)",
                nameEn = "Great Lent (Tsome Hirkal)",
                ethDate = lentStart,
                gregDate = EthiopianCalendarEngine.toGregorian(lentStart),
                category = HolidayCategory.RELIGIOUS,
                isPublicHoliday = false,
                descriptionAm = "የጌታችን የ55 ቀናት የዐቢይ ጾም መጀመሪያ ቀን።",
                descriptionEn = "Beginning of the 55-day Great Lent fast in the Ethiopian Orthodox Church."
            ),
            HolidayInstance(
                id = "debre_zeyt_$ethYear",
                nameAm = "ደብረ ዘይት",
                nameEn = "Debre Zeyt (Mount of Olives)",
                ethDate = debreZeyt,
                gregDate = EthiopianCalendarEngine.toGregorian(debreZeyt),
                category = HolidayCategory.RELIGIOUS,
                isPublicHoliday = false,
                descriptionAm = "በዐቢይ ጾም እኩሌታ የሚከበረው የጌታችን በደብረ ዘይት ተራራ ላይ ያስተማረው ትምህርት መታሰቢያ።",
                descriptionEn = "Mid-Lent Sunday commemorating Jesus teaching on the Mount of Olives regarding the Second Coming."
            ),
            HolidayInstance(
                id = "hosanna_$ethYear",
                nameAm = "ሆሳዕና",
                nameEn = "Hosanna (Palm Sunday)",
                ethDate = hosanna,
                gregDate = EthiopianCalendarEngine.toGregorian(hosanna),
                category = HolidayCategory.RELIGIOUS,
                isPublicHoliday = false,
                descriptionAm = "ጌታችን ኢየሱስ ክርስቶስ በአህያ ውርንጫ ሆኖ በታላቅ ክብር ወደ ኢየሩሳሌም የገባበት በዓል።",
                descriptionEn = "Palm Sunday celebrating Jesus' triumphal entry into Jerusalem, celebrated with palm branches (Zembaba)."
            ),
            HolidayInstance(
                id = "siklet_$ethYear",
                nameAm = "ስቅለት (መልካም ዓርብ)",
                nameEn = "Siklet (Good Friday)",
                ethDate = siklet,
                gregDate = EthiopianCalendarEngine.toGregorian(siklet),
                category = HolidayCategory.RELIGIOUS,
                isPublicHoliday = true,
                descriptionAm = "የኢየሱስ ክርስቶስ የስቅለት መታሰቢያ፤ የሕዝብ ዕረፍት ቀን።",
                descriptionEn = "Good Friday commemorating the crucifixion of Jesus Christ; an official public holiday."
            ),
            HolidayInstance(
                id = "fasika_$ethYear",
                nameAm = "ፋሲካ (የጌታችን ትንሣኤ)",
                nameEn = "Fasika (Ethiopian Easter)",
                ethDate = fasika,
                gregDate = EthiopianCalendarEngine.toGregorian(fasika),
                category = HolidayCategory.RELIGIOUS,
                isPublicHoliday = true,
                descriptionAm = "የክርስቶስ የትንሣኤ በዓል፤ ከ55 ቀናት ጾም በኋላ የሚከበር ታላቅ በዓል።",
                descriptionEn = "Ethiopian Easter celebrating the Resurrection of Jesus Christ after the 55-day Great Fast."
            ),
            HolidayInstance(
                id = "erget_$ethYear",
                nameAm = "ዕርገት",
                nameEn = "Erget (Ascension of Jesus)",
                ethDate = erget,
                gregDate = EthiopianCalendarEngine.toGregorian(erget),
                category = HolidayCategory.RELIGIOUS,
                isPublicHoliday = false,
                descriptionAm = "ኢየሱስ ክርስቶስ ከትንሣኤው በ40ኛው ቀን ወደ ሰማይ ያረገበት መታሰቢያ።",
                descriptionEn = "Ascension of Jesus Christ celebrated 40 days after Easter."
            ),
            HolidayInstance(
                id = "peraklit_$ethYear",
                nameAm = "ጰራቅሊጦስ",
                nameEn = "Peraklitos (Pentecost)",
                ethDate = peraklit,
                gregDate = EthiopianCalendarEngine.toGregorian(peraklit),
                category = HolidayCategory.RELIGIOUS,
                isPublicHoliday = false,
                descriptionAm = "በሐዋርያት ላይ መንፈስ ቅዱስ የወረደበት የጰንጠቆስጤ በዓል።",
                descriptionEn = "Pentecost commemorating the descent of the Holy Spirit upon the Apostles."
            )
        )
        result.addAll(movableChristian)

        // 3. Islamic Holidays
        val islamic = ISLAMIC_HOLIDAYS_TABLE[ethYear] ?: emptyList()
        for ((idx, entry) in islamic.withIndex()) {
            val ethDate = EthiopianDate(ethYear, entry.ethMonth, entry.ethDay)
            result.add(
                HolidayInstance(
                    id = "islamic_${ethYear}_$idx",
                    nameAm = entry.nameAm,
                    nameEn = entry.nameEn,
                    ethDate = ethDate,
                    gregDate = EthiopianCalendarEngine.toGregorian(ethDate),
                    category = HolidayCategory.RELIGIOUS,
                    isPublicHoliday = entry.isPublic,
                    descriptionAm = entry.descAm,
                    descriptionEn = entry.descEn
                )
            )
        }

        // Sort by chronological order in the Ethiopian year
        return result.sortedWith(compareBy({ it.ethDate.month }, { it.ethDate.day }))
    }

    /**
     * Returns holidays falling on a specific Ethiopian date
     */
    fun getHolidaysForDay(ethDate: EthiopianDate): List<HolidayInstance> {
        return getAllHolidaysForYear(ethDate.year).filter {
            it.ethDate.month == ethDate.month && it.ethDate.day == ethDate.day
        }
    }

    /**
     * Returns the next upcoming holiday starting from a given Ethiopian date
     */
    fun getUpcomingHolidays(fromDate: EthiopianDate, limit: Int = 5): List<HolidayInstance> {
        val currentYearHolidays = getAllHolidaysForYear(fromDate.year)
        val nextYearHolidays = getAllHolidaysForYear(fromDate.year + 1)
        val combined = currentYearHolidays + nextYearHolidays

        val fromRd = EthiopianCalendarEngine.ethToFixed(fromDate.year, fromDate.month, fromDate.day)

        return combined.filter {
            val hRd = EthiopianCalendarEngine.ethToFixed(it.ethDate.year, it.ethDate.month, it.ethDate.day)
            hRd >= fromRd
        }.take(limit)
    }
}

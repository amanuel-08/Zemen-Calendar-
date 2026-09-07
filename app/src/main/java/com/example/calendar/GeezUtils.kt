package com.example.calendar

object GeezUtils {
    private val ONES = arrayOf("", "፩", "፪", "፫", "፬", "፭", "፮", "፯", "፰", "፱")
    private val TENS = arrayOf("", "፲", "፳", "፴", "፵", "፶", "፷", "፸", "፹", "፺")

    /**
     * Converts an integer (1..9999) to its Ge'ez numeral representation.
     * Examples:
     * 1 -> ፩
     * 3 -> ፫
     * 12 -> ፲፪
     * 30 -> ፴
     * 2018 -> ፳፻፲፰
     * 2019 -> ፳፻፲፱
     */
    fun toGeez(number: Int): String {
        if (number <= 0) return number.toString()
        if (number > 9999) return number.toString()

        val hundred = 100
        val quotient = number / hundred
        val remainder = number % hundred

        val sb = StringBuilder()

        if (quotient > 0) {
            val qTens = (quotient % 100) / 10
            val qOnes = quotient % 10
            if (quotient > 1) {
                sb.append(TENS[qTens])
                sb.append(ONES[qOnes])
            }
            sb.append("፻")
        }

        if (remainder > 0 || quotient == 0) {
            val rTens = remainder / 10
            val rOnes = remainder % 10
            sb.append(TENS[rTens])
            sb.append(ONES[rOnes])
        }

        return sb.toString()
    }
}

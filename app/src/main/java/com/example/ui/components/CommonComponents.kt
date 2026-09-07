package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calendar.HolidayCategory
import com.example.ui.theme.HolidayCultural
import com.example.ui.theme.HolidayInternational
import com.example.ui.theme.HolidayNational
import com.example.ui.theme.HolidayReligious

@Composable
fun CategoryChip(
    category: HolidayCategory,
    isAmharic: Boolean = true,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when (category) {
        HolidayCategory.NATIONAL -> HolidayNational.copy(alpha = 0.15f) to HolidayNational
        HolidayCategory.RELIGIOUS -> HolidayReligious.copy(alpha = 0.15f) to HolidayReligious
        HolidayCategory.CULTURAL -> HolidayCultural.copy(alpha = 0.15f) to HolidayCultural
        HolidayCategory.INTERNATIONAL -> HolidayInternational.copy(alpha = 0.15f) to HolidayInternational
    }

    val text = if (isAmharic) category.titleAm else category.titleEn

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun PublicHolidayBadge(
    isAmharic: Boolean = true,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
    ) {
        Text(
            text = if (isAmharic) "የሕዝብ በዓል" else "Public Holiday",
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

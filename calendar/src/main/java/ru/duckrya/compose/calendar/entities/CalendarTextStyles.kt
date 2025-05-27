package ru.duckrya.compose.calendar.entities

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.TextStyle

/**
 * Holds text styles for different parts of the calendar UI.
 *
 * @property textDay The text style applied to the day numbers in the grid.
 * @property dayOfWeek The text style used for the day-of-week header labels.
 *
 * You can provide your own styles or use [CalendarTextStyles.default] for a sensible default.
 */
class CalendarTextStyles(
    val dayOfWeek: TextStyle,
    val textDay: TextStyle
){
    companion object{

        @Composable
        fun default(
            dayOfWeek: TextStyle = MaterialTheme.typography.titleSmall,
            textDay: TextStyle = MaterialTheme.typography.bodyMedium
        ) = remember(
            dayOfWeek,
            textDay
        ) {
            CalendarTextStyles(
                dayOfWeek = dayOfWeek,
                textDay = textDay
            )
        }
    }
}
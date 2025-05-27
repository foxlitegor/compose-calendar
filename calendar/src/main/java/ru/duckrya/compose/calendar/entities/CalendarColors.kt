package ru.duckrya.compose.calendar.entities

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

/**
 * Defines the color scheme used throughout the calendar UI.
 *
 * This includes background colors, text colors for different states (selected, current, inactive, etc.),
 * and differentiation for weekends and weekdays.
 *
 * @property background The background color of the calendar container.
 * @property selectedDayBackground The background color for the selected day.
 * @property enabledDayBackground The background color for enabled selectable days (used when enabledDates is provided).
 * @property defaultTextColor The text color for regular days in the current month.
 * @property textDayOfWeek The text color for day-of-week headers (e.g., "Mon", "Tue").
 * @property inactiveDayColor The text color for days that are not part of the current month.
 * @property weekendDayColor The text color for weekend days (as defined in [CalendarProperties.weekEnds]).
 * @property currentDayColor The text color used to highlight the current (today’s) date.
 * @property selectedDayText The text color for the selected date.
 * @property enabledDayText The text color for dates that are selectable (when enabledDates is used).
 *
 * Use [CalendarColors.default] to obtain a default, theme-aware instance.
 */
class CalendarColors(
    val background: Color,
    val selectedDayBackground: Color,
    val enabledDayBackground: Color,
    val defaultTextColor: Color,
    val textDayOfWeek: Color,
    val inactiveDayColor: Color,
    val weekendDayColor: Color,
    val currentDayColor: Color,
    val selectedDayText: Color,
    val enabledDayText: Color
){
    companion object{

        @Composable
        fun default(
            background: Color = MaterialTheme.colorScheme.background,
            selectedDayBackground: Color = MaterialTheme.colorScheme.primary,
            enabledDayBackground: Color = MaterialTheme.colorScheme.tertiaryContainer,
            defaultTextColor: Color = MaterialTheme.colorScheme.onBackground,
            textDayOfWeek: Color = defaultTextColor,
            inactiveDayColor: Color = defaultTextColor.copy(alpha = 0.5f),
            weekendDayColor: Color = defaultTextColor,
            currentDayColor: Color = MaterialTheme.colorScheme.primary,
            selectedDayText: Color = MaterialTheme.colorScheme.onPrimary,
            enabledDayText: Color = MaterialTheme.colorScheme.onTertiaryContainer
        ) = remember(
            background,
            selectedDayBackground,
            enabledDayBackground,
            textDayOfWeek,
            defaultTextColor,
            inactiveDayColor,
            weekendDayColor,
            currentDayColor,
            selectedDayText,
            enabledDayText
        ){
            CalendarColors(
                background = background,
                selectedDayBackground = selectedDayBackground,
                enabledDayBackground = enabledDayBackground,
                textDayOfWeek = textDayOfWeek,
                defaultTextColor = defaultTextColor,
                inactiveDayColor = inactiveDayColor,
                weekendDayColor = weekendDayColor,
                currentDayColor = currentDayColor,
                selectedDayText = selectedDayText,
                enabledDayText = enabledDayText
            )
        }
    }
}

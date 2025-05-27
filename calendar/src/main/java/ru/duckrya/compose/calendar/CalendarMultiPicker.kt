package ru.duckrya.compose.calendar

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.duckrya.compose.calendar.entities.CalendarColors
import ru.duckrya.compose.calendar.entities.CalendarProperties
import ru.duckrya.compose.calendar.entities.CalendarTextStyles
import ru.duckrya.compose.calendar.entities.rememberCalendarProperties
import java.time.LocalDate
import java.time.YearMonth


/**
 * A component for choosing several dates with the possibility of limiting available dates.
 *
 * @param modifier The modifier applied to the root container of the calendar.
 * @param selectedDates A set of current selected dates.
 * @param onDateSelected The reverse call caused by changing the set of selected dates.
 * @param state The [CalendarState]. Allows you to manage the current displayed date
 * @param colors The color scheme of the calendar, including the color of the background, text, weekends, the current day and inactive days.
 * @param styles The style of text for the days of the week and days of the month.
 * @param properties The properties of the calendar, including the first day of the week, weekends, the initial, starting and final date.
 * @param shape Form (trimming of angles) for the calendar. By default - `MaterialTheme.shapes.medium`.
 * @param enabledDates List of dates available for choosing. If null, all dates are available.
 *
 * Example:
 * ```
 * var selectedDates by remember { mutableStateOf(setOf<LocalDate>()) }
 * CalendarMultiPicker(
 *     selectedDates = selectedDates,
 *     onDateSelected = { dates -> selectedDates = dates }
 * )
 * ```
 */
@Composable
fun CalendarMultiPicker(
    modifier: Modifier = Modifier,
    selectedDates: Set<LocalDate>,
    onDateSelected: (Set<LocalDate>) -> Unit,
    state: CalendarState = rememberCalendarState(),
    colors: CalendarColors = CalendarColors.default(),
    styles: CalendarTextStyles = CalendarTextStyles.default(),
    properties: CalendarProperties = rememberCalendarProperties(),
    shape: Shape = MaterialTheme.shapes.medium,
    enabledDates: Set<LocalDate>? = null
) = BaseCalendar(
    modifier = modifier,
    state = state,
    colors = colors,
    styles = styles,
    properties = properties,
    shape = shape,
    dayContent = { day ->
        val isCurrentMonth = YearMonth.from(day) == state.currentMonth
        val isEnabled = enabledDates?.contains(day)
        val isClickable = isCurrentMonth && ( isEnabled != false)
        val isSelected = day in selectedDates

        val backgroundColor by animateColorAsState(
            targetValue = when {
                isSelected -> colors.selectedDayBackground
                isEnabled == true -> colors.enabledDayBackground
                else -> Color.Transparent
            }
        )

        val textColor by animateColorAsState(
            targetValue = when {
                isSelected -> colors.selectedDayText
                isEnabled == true -> colors.enabledDayText
                else -> LocalContentColor.current
            }
        )

        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .clip(shape)
                .background(backgroundColor)
                .padding(4.dp)
                .clickable(
                    enabled = isClickable,
                    onClick = {
                        val newSelection = when(isSelected){
                            true -> selectedDates - day
                            false -> selectedDates + day
                        }
                        onDateSelected(newSelection)
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = day.dayOfMonth.toString(),
                style = LocalTextStyle.current,
                color = textColor
            )
        }
    }
)

@Preview
@Composable
fun PreviewMultiDatePicker() = Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center
){
    var selectedDates by remember { mutableStateOf(setOf<LocalDate>()) }

    CalendarMultiPicker(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        selectedDates = selectedDates,
        onDateSelected = { selectedDates = it },
        enabledDates = setOf(
            LocalDate.now().minusDays(2),
            LocalDate.now().minusDays(5),
            LocalDate.now().minusDays(11),
            LocalDate.now().minusDays(14)
        ),
        colors = CalendarColors.default(
            selectedDayBackground = MaterialTheme.colorScheme.primary,
            selectedDayText = MaterialTheme.colorScheme.onPrimary
        )
    )
}
package ru.duckrya.compose.calendar

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import java.time.format.TextStyle
import java.util.Locale


/**
 * Displays the calendar single picker widget with the possibility of horizontal scrolling for months.
 *
 * The calendar supports the setting of the color scheme, text styles, the initial, initial and final display date,
 *
 * @param modifier The modifier applied to the root container of the calendar.
 * @param selectedDate The current selected date
 * @param onDateSelected The reverse challenge caused when choosing a new date.
 * @param state The [CalendarState]. Allows you to manage the current displayed date
 * @param colors The color scheme of the calendar, including the color of the background, text, weekends, the current day and inactive days.
 * @param styles The style of text for the days of the week and days of the month.
 * @param properties The properties of the calendar, including the first day of the week, weekends, the initial, starting and final date.
 * @param shape Form (trimming of angles) for the calendar. By default - `MaterialTheme.shapes.medium`.
 * @param enabledDates List of dates available for choosing. If null, all dates are available.
 *
 * Example:
 * ```
 * var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
 * CalendarSinglePicker(
 *     selectedDate = selectedDate,
 *     onDateSelected = { date -> selectedDate = date },
 *     enabledDates = setOf(
 *         LocalDate.now().minusDays(1),
 *         LocalDate.now().minusDays(3)
 *     )
 * )
 * ```
 */
@Composable
fun CalendarSinglePicker(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
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
        val isSelected = day == selectedDate

        val backgroundColor by animateColorAsState(when{
            isSelected -> colors.selectedDayBackground
            isEnabled == true -> colors.enabledDayBackground
            else -> Color.Transparent
        })

        val textColor by animateColorAsState(when{
            isSelected -> colors.selectedDayText
            isEnabled == true -> colors.enabledDayText
            else -> LocalContentColor.current
        })
        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .clip(shape)
                .background(backgroundColor)
                .padding(4.dp)
                .clickable(
                    enabled = isClickable,
                    onClick = { onDateSelected(day) }
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
fun PreviewSingleDatePicker() = Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
) {
    val calendarState = rememberCalendarState()
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    CalendarSinglePicker(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        state = calendarState,
        selectedDate = selectedDate,
        onDateSelected = {
            selectedDate = it.takeUnless{ it == selectedDate }
        },
        enabledDates = setOf(
            LocalDate.now().minusDays(1),
            LocalDate.now().minusDays(3),
            LocalDate.now().minusDays(5),
            LocalDate.now().minusDays(29),
        ),
        colors = CalendarColors.default(
            selectedDayBackground = MaterialTheme.colorScheme.primary,
            selectedDayText = MaterialTheme.colorScheme.onPrimary
        )
    )
}
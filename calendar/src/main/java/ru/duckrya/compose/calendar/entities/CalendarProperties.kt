package ru.duckrya.compose.calendar.entities

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.time.DayOfWeek
import java.time.YearMonth
import java.util.Locale


/**
 * Represents configuration settings for the calendar.
 *
 * This class defines display behavior such as the range of selectable months,
 * the starting visible month, the first day of the week, weekends, and the locale used
 * for rendering day names.
 *
 * @property firstDayOfWeek The first day of the week (e.g., [DayOfWeek.MONDAY] or [DayOfWeek.SUNDAY]). Defaults to Monday.
 * @property weekEnds A set of days considered as weekends. Defaults to Saturday and Sunday.
 * @property locale The locale used to localize day-of-week labels (e.g., "Mon", "Tue"). Defaults to the system locale.
 * @property initDate The initially visible [YearMonth] when the calendar is first displayed.
 * @property startDate The earliest [YearMonth] the user can scroll to. Must not be after [endDate].
 * @property endDate The latest [YearMonth] the user can scroll to. Must not be before [startDate].
 *
 * @throws IllegalArgumentException if [startDate] is after [endDate].
 *
 * @constructor Ensures that the scrollable range is valid and provides helper functionality.
 *
 * @see isWeekend for determining whether a given [DayOfWeek] is considered a weekend.
 */
data class CalendarProperties(
    val firstDayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
    val weekEnds: Set<DayOfWeek> = setOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY),
    val locale: Locale = Locale.getDefault(),
    val initDate: YearMonth = YearMonth.now(),
    val startDate: YearMonth = initDate.minusYears(100),
    val endDate: YearMonth = initDate.plusYears(100)
){
    init {
        require(!startDate.isAfter(endDate)) {
            "startDate must be before or equal to endDate"
        }
    }

    fun isWeekend(day: DayOfWeek): Boolean = day in weekEnds

}

@Composable
fun rememberCalendarProperties(
    firstDayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
    weekEnds: Set<DayOfWeek> = setOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY),
    initDate: YearMonth = YearMonth.now(),
    startDate: YearMonth = initDate.minusYears(100),
    endDate: YearMonth = initDate.plusYears(100)
): CalendarProperties = remember(
    firstDayOfWeek,
    weekEnds,
    initDate,
    startDate,
    endDate
) {
    val safeStart = minOf(startDate, endDate)
    val safeEnd = maxOf(startDate, endDate)
    val safeInit = initDate.coerceIn(safeStart, safeEnd)
    CalendarProperties(
        firstDayOfWeek = firstDayOfWeek,
        weekEnds = weekEnds,
        initDate = safeInit,
        startDate = safeStart,
        endDate = safeEnd
    )
}
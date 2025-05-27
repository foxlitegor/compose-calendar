package ru.duckrya.compose.calendar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import ru.duckrya.compose.calendar.entities.CalendarProperties
import java.time.YearMonth

/**
 *
 * The condition of the calendar, which allows you to control the current displayed date.
 *
 */
@Stable
class CalendarState internal constructor() {

    private var updateListener: ((YearMonth) -> Unit)? = null
    private var initDate by mutableStateOf<YearMonth?>(null)
    private var startDate by mutableStateOf<YearMonth?>(null)
    private var endDate by mutableStateOf<YearMonth?>(null)

    var currentMonth by mutableStateOf<YearMonth?>(null)
        internal set

    internal fun onChangeCurrent(
        callback: (YearMonth) -> Unit
    ){ updateListener = callback }

    /**
     * Switchs the calendar for the next month.
     */
    fun nextMonth() {
        currentMonth?.let {
            goToMonth(it.plusMonths(1))
        }
    }

    /**
     * It switches the calendar to the previous month.
     */
    fun prevMonth() {
        currentMonth?.let {
            goToMonth(it.minusMonths(1))
        }
    }

    /**
     * Moves the calendar for the specified month. Consider the range of startDate and endDate.
     */
    fun goToMonth(month: YearMonth) {
        val targetMonth = month.coerceIn(startDate, endDate)
        updateListener?.invoke(targetMonth)
    }

    internal fun initState(
        properties: CalendarProperties
    ){
        initDate = properties.initDate
        startDate = properties.startDate
        endDate = properties.endDate
        currentMonth = properties.initDate.coerceIn(startDate, endDate)
    }
}

@Composable
fun rememberCalendarState(): CalendarState =
    remember { CalendarState() }
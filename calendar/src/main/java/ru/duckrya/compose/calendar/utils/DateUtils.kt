package ru.duckrya.compose.calendar.utils

import java.time.DayOfWeek
import java.time.YearMonth
import java.time.temporal.ChronoUnit

internal fun orderedDaysOfWeek(firstDay: DayOfWeek): List<DayOfWeek> {
    val allDays = DayOfWeek.entries
    val startIndex = allDays.indexOf(firstDay)
    return allDays.drop(startIndex) + allDays.take(startIndex)
}

internal fun calculateTotalMonths(
    startDate: YearMonth,
    endDate: YearMonth
): Int = maxOf(1, ChronoUnit.MONTHS.between(startDate, endDate).toInt() + 1)


internal fun calculateStartIndex(
    startDate: YearMonth,
    initDate: YearMonth,
    totalMonths: Int
): Int {
    if (totalMonths <= 0) return 0
    val monthsBetween = ChronoUnit.MONTHS.between(startDate, initDate).toInt()
    return monthsBetween.coerceIn(0, totalMonths - 1)
}
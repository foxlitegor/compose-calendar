package ru.duckrya.compose.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.duckrya.compose.calendar.entities.CalendarColors
import ru.duckrya.compose.calendar.entities.CalendarProperties
import ru.duckrya.compose.calendar.entities.CalendarTextStyles
import ru.duckrya.compose.calendar.entities.rememberCalendarProperties
import ru.duckrya.compose.calendar.utils.calculateStartIndex
import ru.duckrya.compose.calendar.utils.calculateTotalMonths
import ru.duckrya.compose.calendar.utils.orderedDaysOfWeek
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.util.Locale
import java.time.format.TextStyle as JavaTextStyle

private typealias DayContent = @Composable BoxScope.(LocalDate) -> Unit


/**
 * Displays the basic calendar widget with the possibility of horizontal scrolling for months.
 *
 * The calendar supports the setting of the color scheme, text styles, the initial, initial and final display date,
 * And also provides the possibility of customization of the content of cells with days of the month.
 *
 * @param modifier The modifier applied to the root container of the calendar.
 * @param state The [CalendarState]. Allows you to manage the current displayed date
 * @param colors The color scheme of the calendar, including the color of the background, text, weekends, the current day and inactive days.
 * @param styles The style of text for the days of the week and days of the month.
 * @param properties The properties of the calendar, including the first day of the week, weekends, the initial, starting and final date.
 * @param shape Form (trimming of angles) for the calendar. By default - `MaterialTheme.shapes.medium`.
 * @param dayContent Content displayed inside each calendar cell (day). By default, the number of the day is displayed.
 *
 * Example:
 * ```
 * BaseCalendar(
 *     modifier = Modifier.fillMaxWidth(),
 *     properties = rememberCalendarProperties(
 *         initDate = YearMonth.of(2024, 5),
 *         startDate = YearMonth.of(2023, 1),
 *         endDate = YearMonth.of(2025, 12)
 *     ),
 *     dayContent = { date ->
 *         Text(text = date.dayOfMonth.toString())
 *     }
 * )
 * ```
 */
@Composable
fun BaseCalendar(
    modifier: Modifier = Modifier,
    state: CalendarState = rememberCalendarState(),
    colors: CalendarColors = CalendarColors.default(),
    styles: CalendarTextStyles = CalendarTextStyles.default(),
    properties: CalendarProperties = rememberCalendarProperties(),
    shape: Shape = MaterialTheme.shapes.medium,
    dayContent: DayContent = { DefaultDayContent(it) }
) = Column(
    modifier = modifier
        .clip(shape)
        .background(colors.background)
){
    val scope = rememberCoroutineScope()

    val totalMonths = remember(properties){
        calculateTotalMonths(
            startDate = properties.startDate,
            endDate = properties.endDate
        )
    }

    val initialPage = remember(properties, totalMonths){
        calculateStartIndex(
            startDate = properties.startDate,
            initDate = properties.initDate,
            totalMonths = totalMonths
        )
    }

    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { totalMonths }
    )

    LaunchedEffect(properties) {
        state.initState(properties)
    }
    LaunchedEffect(Unit) {
        state.onChangeCurrent { newMonth ->
            scope.launch {
                pagerState.animateScrollToPage(
                    calculateStartIndex(properties.startDate, newMonth, totalMonths)
                )
            }
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        val month = properties.startDate.plusMonths(pagerState.currentPage.toLong())
        state.currentMonth = month
    }

    HorizontalPager(
        state = pagerState
    ) { pageIndex ->
        val yearMonth = properties.startDate.plusMonths(pageIndex.toLong())
        Column {
            DayOfWeekGrid(
                modifier = Modifier.fillMaxWidth(),
                textStyle = styles.dayOfWeek,
                textColor = colors.textDayOfWeek,
                firstDayOfWeek = properties.firstDayOfWeek,
                locale = properties.locale
            )
            MonthDaysGrid(
                modifier = Modifier.fillMaxWidth(),
                yearMonth = yearMonth,
                colors = colors,
                styles = styles,
                properties = properties,
                dayContent = dayContent
            )
        }
    }
}

@Composable
private fun DayOfWeekGrid(
    modifier: Modifier = Modifier,
    textStyle: TextStyle,
    textColor: Color,
    firstDayOfWeek: DayOfWeek,
    locale: Locale
) = Row(
    modifier = modifier
        .heightIn(48.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
){
    orderedDaysOfWeek(firstDayOfWeek).forEach { dayOfWeek ->
        Text(
            modifier = Modifier.weight(1f),
            text = dayOfWeek.getDisplayName(
                JavaTextStyle.SHORT_STANDALONE,
                locale
            ).replaceFirstChar { it.uppercase() },
            style = textStyle,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MonthDaysGrid(
    modifier: Modifier = Modifier,
    yearMonth: YearMonth,
    colors: CalendarColors,
    styles: CalendarTextStyles,
    properties: CalendarProperties,
    dayContent: @Composable BoxScope.(LocalDate) -> Unit
) = FlowRow(
    modifier = modifier,
    maxItemsInEachRow = DayOfWeek.entries.size,
    horizontalArrangement = Arrangement.SpaceBetween
){
    val currentDay = remember { LocalDate.now() }
    val firstDayOfMonth = remember(yearMonth){ yearMonth.atDay(1) }
    val firstDayWeekValue = remember(firstDayOfMonth){ firstDayOfMonth.dayOfWeek.value }
    val firstDayOfWeekValue = remember(properties){ properties.firstDayOfWeek.value }

    val startPadding = remember(firstDayWeekValue, firstDayOfWeekValue){
        (firstDayWeekValue - firstDayOfWeekValue + 7) % 7
    }

    val totalDays = remember(startPadding, yearMonth){ startPadding + yearMonth.lengthOfMonth() }
    val endPadding = remember(totalDays){ (7 - (totalDays % 7)) % 7 }

    val startDay = remember(firstDayOfMonth, startPadding){ firstDayOfMonth.minusDays(startPadding.toLong()) }
    val calendarDays = remember(yearMonth, startPadding, endPadding){
        yearMonth.lengthOfMonth() + startPadding + endPadding
    }

    repeat(calendarDays){
        val day = remember(startDay){ startDay.plusDays(it.toLong()) }
        val isCurrentDay = day == currentDay
        val isCurrentMonth = YearMonth.from(day) == yearMonth
        val isWeekend = properties.isWeekend(day.dayOfWeek)
        CompositionLocalProvider(
            LocalContentColor provides when{
                isCurrentMonth.not() -> colors.inactiveDayColor
                isCurrentDay -> colors.currentDayColor
                isWeekend -> colors.weekendDayColor
                else -> colors.defaultTextColor
            },
            LocalTextStyle provides styles.textDay
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(1f / DayOfWeek.entries.size)
                    .aspectRatio(1f)
                    .padding(8.dp),
                contentAlignment = Alignment.Center,
                content = { dayContent(day) }
            )
        }
    }
}

@Composable
private fun BoxScope.DefaultDayContent(day: LocalDate) = Text(
    modifier = Modifier.align(Alignment.Center),
    text = day.dayOfMonth.toString(),
    style = LocalTextStyle.current,
    color = LocalContentColor.current
)
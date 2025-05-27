# 📅 Compose Calendar

A flexible and modern Jetpack Compose calendar component for Android with support for:

- Single or multiple date selection  
- Custom day cell content  
- Paging by months  
- Configurable appearance and constraints  
- Disabled and highlightable dates  
- Locale and first-day-of-week customization  

---

## 🚀 Features

- 📆 Horizontal month paging  
- 🎨 Fully customizable colors and text styles  
- 🔘 Single and multi-date selection out of the box  
- 🚫 Disable selection of dates outside the current month or a specific list  
- 🌐 Locale support for weekdays  
- 🔧 Calendar state management (`nextMonth()`, `prevMonth()`, `goToMonth()`)

---

## 🛠️ Installation

This library is available via [JitPack](https://jitpack.io/#foxlitegor/compose-calendar).

### Step 1 – Add the JitPack repository

In your **`settings.gradle.kts`** or **root `build.gradle.kts`**:

```kotlin
dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```
Or in Groovy:
```groovy
allprojects {
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```
### Step 2 – Add the dependency
```kotlin
dependencies {
    implementation("com.github.foxlitegor:compose-calendar:Tag")
}
```

## ✨ Usage
### Basic Calendar
```kotlin
BaseCalendar(
    modifier = Modifier.fillMaxWidth(),
    properties = rememberCalendarProperties(
        initDate = YearMonth.of(2024, 6),
        startDate = YearMonth.of(2023, 1),
        endDate = YearMonth.of(2026, 1)
    )
)
```
### Single Date Picker
```kotlin
var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

CalendarSinglePicker(
    selectedDate = selectedDate,
    onDateSelected = { date -> selectedDate = date },
    enabledDates = setOf(LocalDate.now(), LocalDate.now().plusDays(1))
)
```
### Multi Date Picker
```kotlin
var selectedDates by remember { mutableStateOf(setOf<LocalDate>()) }

CalendarMultiPicker(
    selectedDates = selectedDates,
    onDateSelected = { date ->
        selectedDates = selectedDates.toggle(date)
    }
)

fun Set<LocalDate>.toggle(date: LocalDate): Set<LocalDate> =
    if (contains(date)) this - date else this + date
```

## 📘 License
MIT License © 2025 [foxlitegor](https://github.com/foxlitegor/compose-calendar)

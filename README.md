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
- 🔧 Calendar state management (e.g., `nextMonth()`, `prevMonth()`, `goToMonth()`)

---

## 🛠️ Installation

This library is available via [JitPack](https://jitpack.io).

1. Add the JitPack repository to your **root** `build.gradle` or `settings.gradle`:

```kotlin
dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

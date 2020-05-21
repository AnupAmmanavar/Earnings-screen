@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.kinley.earnings

import com.kinley.earnings.entities.DailyReport
import com.kinley.earnings.entities.WeeklyReport
import kotlinx.coroutines.flow.MutableStateFlow

data class AppState(
    val _weeks: MutableStateFlow<List<WeeklyReport>> = MutableStateFlow(arrayListOf()),
    val _selectedWeek: MutableStateFlow<WeeklyReport?> = MutableStateFlow(null),
    val _selectedDay: MutableStateFlow<DailyReport?> = MutableStateFlow(null)
)

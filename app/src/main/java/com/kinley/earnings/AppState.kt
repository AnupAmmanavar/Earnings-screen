@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.kinley.earnings

import com.kinley.earnings.entities.DailyReport
import com.kinley.earnings.entities.WeeklyReport
import kotlinx.coroutines.flow.MutableStateFlow

data class AppState(
    val weeks: MutableStateFlow<List<WeeklyReport>> = MutableStateFlow(arrayListOf()),
    val selectedWeek: MutableStateFlow<WeeklyReport?> = MutableStateFlow(null),
    val selectedDay: MutableStateFlow<DailyReport?> = MutableStateFlow(null)
)

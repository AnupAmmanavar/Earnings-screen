@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.kinley.earnings

import com.kinley.earnings.entities.DailyReport
import com.kinley.earnings.entities.WeeklyReport
import kotlinx.coroutines.flow.MutableStateFlow

data class AppState(
    val weeklyReports: MutableStateFlow<List<WeeklyReport>> = MutableStateFlow(arrayListOf()),
    val selectedWeeklyReport: MutableStateFlow<WeeklyReport?> = MutableStateFlow(null),
    val selectedDailyReport: MutableStateFlow<DailyReport?> = MutableStateFlow(null)
)

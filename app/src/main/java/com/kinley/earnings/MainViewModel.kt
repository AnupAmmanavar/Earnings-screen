package com.kinley.earnings

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.kinley.earnings.entities.DailyReport
import com.kinley.earnings.entities.WeeklyReport
import com.kinley.ui.weekcomponent.WeekUiModel
import com.kinley.ui.weekdaycomponent.WeekdayUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

@Suppress("EXPERIMENTAL_API_USAGE")
class MainViewModel : ViewModel(), LifecycleObserver {

    private val repository = Repository()

    // App State
    private val _weeks = MutableStateFlow<List<WeeklyReport>>(arrayListOf())
    private val _selectedWeek = MutableStateFlow<WeeklyReport?>(null)

    private val _selectedDay = MutableStateFlow<DailyReport?>(null)
    private val _days = _selectedWeek.map { it?.dailyReports }

    val weeksUiModel: MutableStateFlow<List<WeekUiModel>> = MutableStateFlow(arrayListOf())
    val daysUiModel: MutableStateFlow<List<WeekdayUiModel>> = MutableStateFlow(arrayListOf())

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onResume() {
        _weeks.value = repository.fetchWeeks()

        _weeks.combine(_selectedWeek) { list: List<WeeklyReport>, selectedWeeklyReport: WeeklyReport? ->
            list.map {
                WeekUiModel(it.week, it.earnings, it == selectedWeeklyReport)
            }.let { weeksUiModel.value = it }
        }

        _days.combine(_selectedDay) { list: List<DailyReport>?, selectedDailyReport: DailyReport? ->
            list
                ?.map { WeekdayUiModel(it.day, it.date, it.earning, it == selectedDailyReport) }
                ?.let {
                    daysUiModel.value = it
                }
        }
    }
}


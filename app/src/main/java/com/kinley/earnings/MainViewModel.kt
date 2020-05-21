package com.kinley.earnings

import androidx.lifecycle.*
import com.kinley.earnings.entities.DailyReport
import com.kinley.earnings.entities.WeeklyReport
import com.kinley.ui.weekcomponent.WeekUiModel
import com.kinley.ui.weekdaycomponent.WeekdayUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Suppress("EXPERIMENTAL_API_USAGE")
class MainViewModel : ViewModel(), LifecycleObserver {

    private val repository = Repository()

    // App State
    private val _weeks = MutableStateFlow<List<WeeklyReport>>(arrayListOf())
    private val _selectedWeek = MutableStateFlow<WeeklyReport?>(null)
    private val _selectedDay = MutableStateFlow<DailyReport?>(null)

    // UI State
    val weeksUiModel: MutableStateFlow<List<WeekUiModel>> = MutableStateFlow(arrayListOf())
    val daysUiModel: MutableStateFlow<List<WeekdayUiModel>> = MutableStateFlow(arrayListOf())

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onResume() {
        _weeks.value = repository.fetchWeeks()

        viewModelScope.launch { defineWeeksUiModel() }
        viewModelScope.launch { defineDaysUiModel() }

    }

    /**
     * UI is a function of state
     * WeeksUIState = f(list of weeks, selected week).
     * The change in the any of the dependent values will trigger a change in UIState
     */
    private suspend fun defineWeeksUiModel() {
        _weeks
            .combine(_selectedWeek) { list: List<WeeklyReport>, selectedWeeklyReport: WeeklyReport? ->
                list.map { WeekUiModel(it.week, it.earnings, it == selectedWeeklyReport) }
            }
            .collect { weeksUiModel.value = it }
    }

    /**
     * UI is a function of state
     * WeekdaysUIState = f(selectedWeek, selectedWeekDay)
     */
    private suspend fun defineDaysUiModel() {
        _selectedWeek
            .combine(_selectedDay) { selected: WeeklyReport?, selectedDailyReport: DailyReport? ->
                val list: List<DailyReport>? = selected?.dailyReports
                list?.map { WeekdayUiModel(it.day, it.date, it.earning, it == selectedDailyReport) }
                    ?: arrayListOf()
            }
            .collect { daysUiModel.value = it }
    }

    /**
     * If you consider the ViewModel as store then, this function is an action to change
     *  the state of the App.
     */
    fun updateWeek(week: WeekUiModel) {
        _selectedWeek.value = _weeks.value.firstOrNull { it.week == week.week }
    }
}


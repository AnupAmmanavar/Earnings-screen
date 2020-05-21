@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.kinley.earnings

import androidx.lifecycle.*
import com.kinley.earnings.entities.DailyReport
import com.kinley.earnings.entities.WeeklyReport
import com.kinley.ui.weekcomponent.WeekUiModel
import com.kinley.ui.weekdaycomponent.WeekdayUiModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@Suppress("EXPERIMENTAL_API_USAGE")
class MainViewModel : ViewModel(), LifecycleObserver {

    private val repository = Repository()

    // App State
    private val appState = AppState()

    // UI State
    val uiState = UIState()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onResume() {
        appState._weeks.value = repository.fetchWeeks()

        viewModelScope.launch { defineWeeksUiModel() }
        viewModelScope.launch { defineDaysUiModel() }

    }

    /**
     * UI is a function of state
     * WeeksUIState = f(list of weeks, selected week).
     * The change in the any of the dependent values will trigger a change in UIState
     */
    private suspend fun defineWeeksUiModel() {
        appState._weeks
            .combine(appState._selectedWeek) { list: List<WeeklyReport>, selectedWeeklyReport: WeeklyReport? ->
                list.map { WeekUiModel(it.week, it.earnings, it == selectedWeeklyReport) }
            }
            .collect { uiState.weeksUiModel.value = it }
    }

    /**
     * UI is a function of state
     * WeekdaysUIState = f(selectedWeek, selectedWeekDay)
     */
    private suspend fun defineDaysUiModel() {
        appState._selectedWeek
            .combine(appState._selectedDay) { selectedWeek: WeeklyReport?, selectedDailyReport: DailyReport? ->
                selectedWeek?.dailyReports?.map { WeekdayUiModel(it.day, it.date, it.earning, it == selectedDailyReport) }
                    ?: arrayListOf()
            }
            .collect { uiState.daysUiModel.value = it }
    }

    /**
     * If you consider the ViewModel as store then, this function is an action to change
     *  the state of the App.
     *  Ideally it should act as a Mediator to take events from different components and act as a passage
     */
    fun updateWeek(week: WeekUiModel) {
        appState._selectedWeek.value = appState._weeks.value.firstOrNull { it.week == week.week }
    }
}


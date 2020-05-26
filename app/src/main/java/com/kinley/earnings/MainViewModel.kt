@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.kinley.earnings

import androidx.lifecycle.*
import com.kinley.earnings.entities.DailyReport
import com.kinley.earnings.entities.WeeklyReport
import kotlinx.coroutines.flow.*

class MainViewModel : ViewModel(), LifecycleObserver {

    private val repository = Repository()
    private val uiMapper = UiMapper()

    // App State
    private val appState = AppState()

    // UI State
    val uiState = UIState()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onResume() {
        appState.weeklyReports.value = repository.fetchWeeks()

        defineWeeksUiModel()
        defineDaysUiModel()
        defineEarningsUiModel()

        defineStateDependencies()

    }

    private fun defineWeeksUiModel() {

        /**
         * UI to state relation
         * WeeksUIState = f(list of weeks, selected week)
         * The change in the any of the dependent values(list of weeks and selected week) will trigger a change in UIState
         */
        with(appState) {

                combine(weeklyReports, selectedWeeklyReport) { weeklyReports: List<WeeklyReport>, selectedWeeklyReport: WeeklyReport? ->
                    uiMapper.toWeeksUiModels(weeklyReports, selectedWeeklyReport)
                }
                .onEach { uiState.weeksUiModel.value = it }
                .launchIn(viewModelScope)
        }
    }


    private fun defineDaysUiModel() {
        /**
         * UI to state relation
         * WeekdaysUIState = f(selectedWeek, selectedWeekDay)
         */

        with(appState) {

            combine(selectedWeeklyReport, selectedDailyReport) { selectedWeeklyReport: WeeklyReport?, selectedDailyReport: DailyReport? ->
                uiMapper.toWeekdayUiModels(selectedWeeklyReport, selectedDailyReport)
            }
             .onEach { uiState.weekdaysUiModel.value = it }
             .launchIn(viewModelScope)
        }
    }

    private fun defineEarningsUiModel() {
        /**
         * Change of selected day should set the earnings view to that day's earning
         */
        with(appState) {

            selectedDailyReport
                .map {
                    uiMapper.toEarningUiModels(it)
                }
                .onEach { uiState.earningsUiModel.value = it }
                .launchIn(viewModelScope)
        }

    }

    /**
     * Some of the variables in the state are inter-dependent
     * For example -  a change in the SelectedWeek should update SelectedDay
     */
    private fun defineStateDependencies() {

        // State relation - A change in week should set the current day to the first day of the week
        with(appState) {

            selectedWeeklyReport
                .map { weeklyReport ->
                    weeklyReport?.dailyReports?.getOrNull(0)  // By default the first day of the week is selected
                }
                .onEach { selectedDailyReport.value = it }
                .launchIn(viewModelScope)
        }

    }


    /**
     * If you consider the ViewModel as store then, this function is an action to change
     *  the state of the App - StateReducer
     *  Improvement - Make it an interface so that different components can include it(loosely coupled)
     *  Ideally it should act as a Mediator to take events from different components and act as a passage
     */
    fun updateWeek(selectedWeekId: String) {
        with(appState) {
            selectedWeeklyReport.value = weeklyReports.value.firstOrNull { it.id == selectedWeekId }
        }
    }


    fun updateDay(selectedWeekdayId: String) {
        with(appState) {
            selectedDailyReport.value = selectedWeeklyReport.value?.dailyReports?.firstOrNull { it.id == selectedWeekdayId }
        }
    }
}


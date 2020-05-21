@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.kinley.earnings

import androidx.lifecycle.*
import com.kinley.earnings.entities.DailyReport
import com.kinley.earnings.entities.Earning
import com.kinley.earnings.entities.WeeklyReport
import com.kinley.ui.earningcomponent.EarningUiModel
import com.kinley.ui.weekcomponent.WeekUiModel
import com.kinley.ui.weekdaycomponent.WeekdayUiModel
import kotlinx.coroutines.flow.*

class MainViewModel : ViewModel(), LifecycleObserver {

    private val repository = Repository()

    // App State
    private val appState = AppState()

    // UI State
    val uiState = UIState()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onResume() {
        appState.weeks.value = repository.fetchWeeks()

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
            weeks
                .combine(selectedWeek) { weeks: List<WeeklyReport>, selectedWeeklyReport: WeeklyReport? ->
                    toWeeksUiModels(weeks, selectedWeeklyReport)
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

            selectedWeek
                .combine(selectedDay) { selectedWeek: WeeklyReport?, selectedDailyReport: DailyReport? ->
                    toWeekdayUiModels(selectedWeek, selectedDailyReport)
                }
                .onEach { uiState.daysUiModel.value = it }
                .launchIn(viewModelScope)
        }
    }

    private fun defineEarningsUiModel() {
        /**
         * Change of selected day should set the earnings view to that day's earning
         */
        with(appState) {

            selectedDay
                .map {
                    it?.earnings?.map(this@MainViewModel::toEarningUiModel) ?: arrayListOf()
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

            selectedWeek
                .onEach { weeklyReport ->
                    // By default the first day of the week is selected
                    selectedDay.value = weeklyReport?.dailyReports?.getOrNull(0)
                }
                .launchIn(viewModelScope)
        }

    }


    /**
     * UI-Mappers. Domain to UiModels
     * Having separated out lets you define the dependencies clearly.
     * More of a readability purpose. Can be moved out to a separate class
     */
    private fun toWeeksUiModels(weeks: List<WeeklyReport>, selectedWeeklyReport: WeeklyReport?): List<WeekUiModel> {
        return weeks.map { WeekUiModel(it.id, it.week, it.earnings, it == selectedWeeklyReport) }
    }

    private fun toWeekdayUiModels(selectedWeek: WeeklyReport?, selectedDailyReport: DailyReport?) : List<WeekdayUiModel> {
        return selectedWeek?.dailyReports?.map {
            WeekdayUiModel(it.id, it.day, it.date, it.earningAmount, it == selectedDailyReport)
        } ?: arrayListOf()
    }

    private fun toEarningUiModel(earning: Earning) : EarningUiModel {
        return EarningUiModel(earning.id, earning.earningType, earning.amount)
    }

    /**
     * If you consider the ViewModel as store then, this function is an action to change
     *  the state of the App.
     *  Ideally it should act as a Mediator to take events from different components and act as a passage
     */
    fun updateWeek(selectedWeekId: String) {
        with(appState) {
            selectedWeek.value = weeks.value.firstOrNull { it.id == selectedWeekId }
        }
    }


    fun updateDay(selectedWeekdayId: String) {
        with(appState) {
            selectedDay.value =
                selectedWeek.value?.dailyReports?.firstOrNull { it.id == selectedWeekdayId }
        }

    }
}


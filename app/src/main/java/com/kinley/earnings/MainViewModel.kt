@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.kinley.earnings

import androidx.lifecycle.*
import com.kinley.earnings.entities.DailyReport
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
        appState._weeks.value = repository.fetchWeeks()

        defineWeeksUiModel()
        defineDaysUiModel()
        defineEarningsUiModel()

    }

    /**
     * UI is a function of state
     * WeeksUIState = f(list of weeks, selected week).
     * The change in the any of the dependent values will trigger a change in UIState
     */
    private fun defineWeeksUiModel() {
        appState._weeks
            .combine(appState._selectedWeek) { list: List<WeeklyReport>, selectedWeeklyReport: WeeklyReport? ->
                list.map { WeekUiModel(it.week, it.earnings, it == selectedWeeklyReport) }
            }
            .onEach { uiState.weeksUiModel.value = it }
            .launchIn(viewModelScope)
    }

    /**
     * UI is a function of state
     * WeekdaysUIState = f(selectedWeek, selectedWeekDay)
     */
    private fun defineDaysUiModel() {
        appState._selectedWeek
            .combine(appState._selectedDay) { selectedWeek: WeeklyReport?, selectedDailyReport: DailyReport? ->
                selectedWeek?.dailyReports?.map {
                    WeekdayUiModel(
                        it.day,
                        it.date,
                        it.earningAmount,
                        it == selectedDailyReport
                    )
                }
                    ?: arrayListOf()
            }
            .onEach { uiState.daysUiModel.value = it }
            .launchIn(viewModelScope)
    }

    private fun defineEarningsUiModel() {
        /**
         * Change of selected day should set the earnings view to that day's earning
         */
        appState._selectedDay
            .map {
                it?.earnings?.map { earning ->
                    EarningUiModel(earning.earningType, earning.amount)
                } ?: arrayListOf()
            }
            .onEach {
                uiState.earningsUiModel.value = it
            }
            .launchIn(viewModelScope)


        /**
         * A new week should result in resetting the earnings view
         */
        appState._selectedWeek
            .onEach {
                uiState.earningsUiModel.value = arrayListOf()
            }
            .launchIn(viewModelScope)

    }

    /**
     * If you consider the ViewModel as store then, this function is an action to change
     *  the state of the App.
     *  Ideally it should act as a Mediator to take events from different components and act as a passage
     */
    fun updateWeek(week: WeekUiModel) {
        with(appState) {
            _selectedWeek.value = _weeks.value.firstOrNull { it.week == week.week }
        }
    }


    fun updateDay(weekDay: String) {
        with(appState) {
            _selectedDay.value =
                _selectedWeek.value?.dailyReports?.firstOrNull { it.day == weekDay }
        }

    }
}


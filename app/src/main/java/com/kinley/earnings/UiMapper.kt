package com.kinley.earnings

import com.kinley.earnings.entities.DailyReport
import com.kinley.earnings.entities.WeeklyReport
import com.kinley.ui.earningcomponent.EarningUiModel
import com.kinley.ui.weekcomponent.WeekUiModel
import com.kinley.ui.weekdaycomponent.WeekdayUiModel

/**
 * UI-Mappers. AppState to UiModels
 * Having separated out lets you define the dependencies clearly. More of a readability purpose.
 */
class UiMapper {

    /**
     * Read as [WeekUiModel] is dependent on list of [WeeklyReport] and selected [WeeklyReport]
     */
    fun toWeeksUiModels(
        weeklyReports: List<WeeklyReport>,
        selectedWeeklyReport: WeeklyReport?
    ): List<WeekUiModel> {
        return weeklyReports.map {
            WeekUiModel(
                it.id,
                it.week,
                it.earnings,
                it == selectedWeeklyReport
            )
        }
    }

    /**
     * Read as [WeekdayUiModel] is dependent on selected [WeeklyReport] and selected [DailyReport]
     */
    fun toWeekdayUiModels(
        selectedWeeklyReport: WeeklyReport?,
        selectedDailyReport: DailyReport?
    ): List<WeekdayUiModel> {
        return selectedWeeklyReport?.dailyReports?.map {
            WeekdayUiModel(
                it.id,
                it.day,
                it.date,
                it.earningAmount,
                it == selectedDailyReport
            )
        } ?: arrayListOf()
    }

    /**
     * Read as [EarningUiModel] is dependent on [DailyReport]
     */
    fun toEarningUiModels(selectedDailyReport: DailyReport?): List<EarningUiModel> {

        val earnings = selectedDailyReport?.earnings ?: arrayListOf()
        return earnings.map { earning ->
            EarningUiModel(
                earning.id,
                earning.earningType,
                earning.amount
            )
        }

    }
}

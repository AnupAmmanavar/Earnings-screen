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
        weeks: List<WeeklyReport>,
        selectedWeeklyReport: WeeklyReport?
    ): List<WeekUiModel> {
        return weeks.map {
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
        selectedWeek: WeeklyReport?,
        selectedDailyReport: DailyReport?
    ): List<WeekdayUiModel> {
        return selectedWeek?.dailyReports?.map {
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
    fun toEarningUiModels(dailyReport: DailyReport): List<EarningUiModel> {

        val earnings = dailyReport.earnings
        return earnings.map { earning ->
            EarningUiModel(
                earning.id,
                earning.earningType,
                earning.amount
            )
        }

    }
}

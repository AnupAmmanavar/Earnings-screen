package com.kinley.earnings

import com.kinley.earnings.entities.DailyReport
import com.kinley.earnings.entities.Earning
import com.kinley.earnings.entities.WeeklyReport
import com.kinley.ui.earningcomponent.EarningUiModel
import com.kinley.ui.weekcomponent.WeekUiModel
import com.kinley.ui.weekdaycomponent.WeekdayUiModel

/**
 * UI-Mappers. Domain to UiModels
 * Having separated out lets you define the dependencies clearly.
 * More of a readability purpose.
 */
class UiMapper {

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

    fun toEarningUiModel(earning: Earning): EarningUiModel {
        return EarningUiModel(
            earning.id,
            earning.earningType,
            earning.amount
        )
    }
}

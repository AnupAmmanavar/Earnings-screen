package com.kinley.earnings.data

import com.kinley.earnings.entities.DailyReport
import com.kinley.earnings.entities.Earning
import com.kinley.earnings.entities.WeeklyReport

typealias df = DataFactory

object DataProvider {

    fun getWeeklyReport(): List<WeeklyReport> = (0..df.randomInt())
        .map {
            val dailyReports = getDailyReports("$it")
            WeeklyReport("Week $it", "Week $it", dailyReports, dailyReports.sumByDouble { it.earningAmount })
        }

    private fun getDailyReports(weekId: String): List<DailyReport> =
        arrayListOf("Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat")
            .mapIndexed { index, day ->
                val weekdayId = "${weekId}_$day"
                val earnings = getEarnings(weekdayId)
                DailyReport(weekdayId, day, index, earnings.sumByDouble { it.amount }, earnings)
            }

    private fun getEarnings(weekdayId: String): List<Earning> = (0..df.randomInt())
        .map {
            Earning(
                id = "${weekdayId}_$it",
                earningType = getEarningType(),
                amount = df.randomDouble()
            )
        }

    private fun getEarningType(): String = if (df.randomInt() % 2 == 0) "Trip" else "Incentive"
}

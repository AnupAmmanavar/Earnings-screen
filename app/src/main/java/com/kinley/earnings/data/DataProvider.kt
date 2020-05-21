package com.kinley.earnings.data

import com.kinley.earnings.entities.DailyReport
import com.kinley.earnings.entities.Earning
import com.kinley.earnings.entities.WeeklyReport

typealias df = DataFactory

object DataProvider {

    fun getWeeklyReport(): List<WeeklyReport> = (0..df.randomInt())
        .map {
            val dailyReports = getDailyReports()
            WeeklyReport(
                week = "Week $it",
                dailyReports = dailyReports,
                earnings = dailyReports.sumByDouble { it.earningAmount }
            )
        }

    private fun getDailyReports(): List<DailyReport> =
        arrayListOf("Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat")
            .mapIndexed { index, day ->
                val earnings = getEarnings()
                DailyReport(day, index, earnings.sumByDouble { it.amount }, earnings)
            }

    private fun getEarnings(): List<Earning> = (0..df.randomInt())
        .map {
            Earning(getEarningType(), df.randomDouble())
        }

    private fun getEarningType(): String = if (df.randomInt() % 2 == 0) "Trip" else "Incentive"
}

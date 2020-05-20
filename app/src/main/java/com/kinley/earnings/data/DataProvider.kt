package com.kinley.earnings.data

import com.kinley.earnings.entities.DailyReport
import com.kinley.earnings.entities.WeeklyReport

typealias df = DataFactory

object DataProvider {

    fun getWeeklyReport(): List<WeeklyReport> = (0..df.randomInt())
        .map {
            val dailyReports = getDailyReports()
            WeeklyReport(
                week = "Week $it",
                dailyReports = dailyReports,
                earnings = dailyReports.sumByDouble { it.earning }
            )
        }

    private fun getDailyReports(): List<DailyReport> =
        arrayListOf("Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat")
            .mapIndexed { index, day ->
                DailyReport(day, index, df.randomDouble())
            }
}

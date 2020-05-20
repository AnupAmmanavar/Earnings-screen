package com.kinley.earnings

import com.kinley.earnings.data.DataProvider
import com.kinley.earnings.entities.WeeklyReport

class Repository {

    fun fetchWeeks(): List<WeeklyReport> {
        return DataProvider.getWeeklyReport()
    }
}

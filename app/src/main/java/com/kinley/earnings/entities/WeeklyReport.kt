package com.kinley.earnings.entities

data class WeeklyReport(
    val id: String,
    val week: String,
    val dailyReports: List<DailyReport>,
    val earnings: Double
)

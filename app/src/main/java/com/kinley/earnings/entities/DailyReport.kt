package com.kinley.earnings.entities

data class DailyReport(
    val day: String,
    val date: Int,
    val earningAmount: Double,
    val earnings: List<Earning>
)

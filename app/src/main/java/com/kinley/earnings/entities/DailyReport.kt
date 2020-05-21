package com.kinley.earnings.entities

data class DailyReport(
    val id: String,
    val day: String,
    val date: Int,
    val earningAmount: Double,
    val earnings: List<Earning>
)

package com.kinley.earnings.features.earnings

import com.kinley.earnings.entities.DailyReport

interface EarningDataChangeListener {
    fun onSelectedDayChanges(selectedDailyReport: DailyReport?)
}

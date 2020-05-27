package com.kinley.earnings.features.earnings

import com.kinley.earnings.entities.DailyReport
import kotlinx.coroutines.flow.MutableStateFlow

data class EarningComponentDependentData(
    val selectedDailyReport: MutableStateFlow<DailyReport?> = MutableStateFlow(
        null
    )
)

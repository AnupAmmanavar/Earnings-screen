@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.kinley.earnings

import com.kinley.ui.earningcomponent.EarningUiModel
import com.kinley.ui.weekcomponent.WeekUiModel
import com.kinley.ui.weekdaycomponent.WeekdayUiModel
import kotlinx.coroutines.flow.MutableStateFlow

data class UIState(
    val weeksUiModel: MutableStateFlow<List<WeekUiModel>> = MutableStateFlow(arrayListOf()),
    val daysUiModel: MutableStateFlow<List<WeekdayUiModel>> = MutableStateFlow(arrayListOf()),
    val earningsUiModel: MutableStateFlow<List<EarningUiModel>> = MutableStateFlow(arrayListOf())
)

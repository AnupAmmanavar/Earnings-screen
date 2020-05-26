@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.kinley.earnings

import com.kinley.ui.earningcomponent.EarningUXComponent
import com.kinley.ui.weekcomponent.HorizontalWeekUXComponent
import com.kinley.ui.weekcomponent.WeekUXComponent
import com.kinley.ui.weekdaycomponent.WeekdayUXComponent
import kotlinx.coroutines.flow.MutableStateFlow

data class ComponentHolder(
    val weekdayUXComponent: MutableStateFlow<WeekdayUXComponent?> = MutableStateFlow(null),
    val weekUXComponent: MutableStateFlow<HorizontalWeekUXComponent?> = MutableStateFlow(null),
    val earningUXComponent: MutableStateFlow<EarningUXComponent?> = MutableStateFlow(null)
)

@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.kinley.earnings.features.earnings

import com.kinley.earnings.UiMapper
import com.kinley.earnings.entities.DailyReport
import com.kinley.ui.earningcomponent.EarningUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

/**
 * This is a view-model class responsible for holding the data
 */
class EarningFeatureViewModel(data: EarningComponentDependentData) : EarningDataChangeListener {

    // Encapsulate in a UiState if the number of models increase
    var uiModels: MutableStateFlow<List<EarningUiModel>> = MutableStateFlow(arrayListOf())

    var dependentData = data

    private val uiMapper = UiMapper()

    init {
        defineRelation()
    }

    private fun defineRelation() {
        with(dependentData) {
            selectedDailyReport
                .map { uiMapper.toEarningUiModels(it) }
                .onEach {
                    uiModels.value = it
                }
        }
    }

    override fun onSelectedDayChanges(selectedDailyReport: DailyReport?) {
        dependentData.selectedDailyReport.value = selectedDailyReport
    }

}

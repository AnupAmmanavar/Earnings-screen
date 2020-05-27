@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.kinley.earnings.features

import com.airbnb.epoxy.EpoxyRecyclerView
import com.kinley.earnings.UiMapper
import com.kinley.earnings.entities.DailyReport
import com.kinley.earnings.entities.Earning
import com.kinley.ui.EarningsBindingModel_
import com.kinley.ui.earningcomponent.EarningUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

/**
 * TODO:  Move to a separate module. Define use cases for receiving the data and interfaces for publishing the events
 */
class EarningFeatureComponent(
    private val coroutineScope: CoroutineScope,
    private val data: EarningComponentDependentData,
    private val eventDispatcher: EarningFeatureEventDispatcher
) : EarningDataChangeListener, CoroutineScope by coroutineScope {

    // TODO Can be moved to a separate Dataholder class - then use it as a Flow
    private var uiModels: List<EarningUiModel> = arrayListOf()

    /**
     * Adding this as normal function, not as a constructor.
     * Reason being it can be constructed in the ViewModel.
     *
     * Also the fact that it is a normal function, it can be called multiple times.
     * This should be avoided.
     */
    fun include(epoxyRecyclerView: EpoxyRecyclerView) { // Constraint layout or a ViewGroup can be used. Each feature has its own UI

        val uiMapper = UiMapper()

        epoxyRecyclerView.withModels {

            uiModels.map {
                EarningsBindingModel_()
                    .id(it.id)
                    .onClick { _ ->
                        eventDispatcher.onEarningClick(it.id)
                    }
                    .uiModel(it)
                    .addTo(this)
            }

        }

        // Defining the relation AppState variables to UiModel
        with(data) {
            selectedDailyReport
                .map { uiMapper.toEarningUiModels(it) }
                .onEach {
                    uiModels = it
                    epoxyRecyclerView.requestModelBuild()
                }
                .launchIn(coroutineScope)
        }
    }

    override fun onChange(data: DailyReport?) {
        this.data.selectedDailyReport.value = data
    }

}


data class EarningComponentDependentData(
    val selectedDailyReport: MutableStateFlow<DailyReport?> = MutableStateFlow(null)
)

interface EarningFeatureEventDispatcher : EventDispatcher {
    fun onEarningClick(id: String)
}

interface EarningDataChangeListener : ComponentDataChangeListener<DailyReport?>

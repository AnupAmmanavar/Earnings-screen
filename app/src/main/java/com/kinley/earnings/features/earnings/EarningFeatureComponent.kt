@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.kinley.earnings.features.earnings

import com.airbnb.epoxy.EpoxyRecyclerView
import com.kinley.ui.EarningsBindingModel_
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * TODO:  Move to a separate module. Define use cases for receiving the data and interfaces for publishing the events
 */
class EarningFeatureComponent(
    private val coroutineScope: CoroutineScope,
    private val eventDispatcher: EarningFeatureEventDispatcher,
    private val vm: EarningFeatureViewModel
) : EarningDataChangeListener by vm, CoroutineScope by coroutineScope {

    companion object Builder {

        fun build(
            coroutineScope: CoroutineScope,
            data: EarningComponentDependentData,
            eventDispatcher: EarningFeatureEventDispatcher
        ): EarningFeatureComponent {
            return EarningFeatureComponent(
                coroutineScope,
                eventDispatcher,
                EarningFeatureViewModel(data)
            )
        }
    }

    /**
     * Adding this as normal function, not as a constructor.
     * Reason being it can be constructed in the ViewModel.
     *
     * Also the fact that it is a normal function, it can be called multiple times.
     * This should be avoided.
     */
    fun include(epoxyRecyclerView: EpoxyRecyclerView) { // Constraint layout or a ViewGroup can be used. Each feature has its own UI

        epoxyRecyclerView.withModels {

            // TODO Should not be able to get a MutableStateFlow
            vm.uiModels.value.map {
                EarningsBindingModel_()
                    .id(it.id)
                    .onClick { _ ->
                        eventDispatcher.onEarningClick(it.id)
                    }
                    .uiModel(it)
                    .addTo(this)
            }

        }

        vm.uiModels.onEach { epoxyRecyclerView.requestModelBuild() }.launchIn(coroutineScope)
    }

}

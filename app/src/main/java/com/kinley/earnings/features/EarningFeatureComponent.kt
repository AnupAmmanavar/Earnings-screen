@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.kinley.earnings.features

import com.airbnb.epoxy.EpoxyRecyclerView
import com.kinley.ui.EarningsBindingModel_
import com.kinley.ui.earningcomponent.EarningUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * TODO:  Move to a separate module. Define use cases for receiving the data and interfaces for publishing the events
 */
class EarningFeatureComponent(
    private val coroutineScope: CoroutineScope,
    private val data: EarningComponentData,
    private val eventDispatcher: EarningFeatureEventDispatcher
) : EarningDataChangeListener, CoroutineScope by coroutineScope {

    /**
     * Adding this as normal function, not as a constructor.
     * Reason being it can be constructed in the ViewModel.
     *
     * Also the fact that it is a normal function, it can be called multiple times.
     * This should be avoided.
     */
    fun include(epoxyRecyclerView: EpoxyRecyclerView) {

        data.earnings.value?.map {

            EarningsBindingModel_()
                .id(it.id)
                .uiModel(it)
        }

        data.earnings.onEach { epoxyRecyclerView.requestModelBuild() }.launchIn(coroutineScope)
    }

    /**
     * Listen for the data changes affecting it
     */
    override fun onChange(data: List<EarningUiModel>?) {
        this.data.earnings.value = data
    }

}


data class EarningComponentData(
    val earnings: MutableStateFlow<List<EarningUiModel>?> = MutableStateFlow(null)
)

interface EarningFeatureEventDispatcher : EventDispatcher {
    fun onEarningClick(id: String)
}

/**
 * TODO: An open point whether it should resolve the domain entities or only deal with the Ui Models
 */
interface EarningDataChangeListener : ComponentDataChangeListener<List<EarningUiModel>?>

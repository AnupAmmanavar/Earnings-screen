package com.kinley.ui.earningcomponent

import com.airbnb.epoxy.EpoxyModel
import com.kinley.ui.EarningsBindingModel_
import com.kinley.ui.uxcomponent.UIDelegate
import com.kinley.ui.uxcomponent.UXComponent
import com.kinley.ui.uxcomponent.VMDelegate

class EarningUXComponent(
    private val earnings: List<EarningUiModel>,
    override val vmDelegate: EarningVmDelegate
) : UXComponent<EarningVmDelegate, EarningUiDelegate> {


    override fun render(uiDelegate: EarningUiDelegate): List<EpoxyModel<*>> {
        return earnings.map {
            // TODO Add click listener
            EarningsBindingModel_()
                .id(it.id)
                .uiModel(it)
        }
    }

}

interface EarningUiDelegate : UIDelegate {
    fun onEarningClick(earningId: String)
}

interface EarningVmDelegate : VMDelegate

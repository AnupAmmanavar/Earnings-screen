package com.kinley.ui.weekdaycomponent

import com.airbnb.epoxy.EpoxyModel
import com.kinley.ui.WeekdayBindingModel_
import com.kinley.ui.uxcomponent.UIDelegate
import com.kinley.ui.uxcomponent.UXComponent
import com.kinley.ui.uxcomponent.VMDelegate

class WeekdayUXComponent(
    private val uiModels: List<WeekdayUiModel>,
    override val vmDelegate: WeekdayVMDelegate
) : UXComponent<WeekdayVMDelegate, WeekdayUIDelegate> {

    override fun render(uiDelegate: WeekdayUIDelegate): List<EpoxyModel<*>> =
        uiModels.map { uiModel ->
            WeekdayBindingModel_()
                .id(uiModel.id)
                .weekdayUiModel(uiModel)
                .onClick { v ->
                    vmDelegate.onWeekdaySelected(uiModel)
                }
        }
}


interface WeekdayVMDelegate : VMDelegate {
    fun onWeekdaySelected(weekdayUModel: WeekdayUiModel)
}

interface WeekdayUIDelegate : UIDelegate

data class WeekdayUiModel(
    val id: String,
    val day: String, // Sun
    val date: Int, // 21
    val earning: Double, //200.0
    val isSelected: Boolean
)


fun <T> EpoxyModel<T>.toList(): List<EpoxyModel<T>> {
    return arrayListOf(this)
}

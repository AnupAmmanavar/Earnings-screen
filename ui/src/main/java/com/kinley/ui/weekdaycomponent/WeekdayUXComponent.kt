package com.kinley.ui.weekdaycomponent

import com.airbnb.epoxy.CarouselModel_
import com.airbnb.epoxy.EpoxyModel
import com.kinley.ui.WeekdayBindingModel_
import com.kinley.ui.uxcomponent.UIDelegate
import com.kinley.ui.uxcomponent.UXComponent
import com.kinley.ui.uxcomponent.VMDelegate

// TODO put identifier in UXComponent
class HorizontalWeekdayUXComponent(
    identifier: String,
    uiModels: List<WeekdayUiModel>,
    override val vmDelegate: WeekdayVMDelegate
) : UXComponent<WeekdayVMDelegate, WeekdayUIDelegate> {

    private val weekDayUXComponent = WeekdayUXComponent(identifier, uiModels, vmDelegate)

    override fun render(uiDelegate: WeekdayUIDelegate): List<EpoxyModel<*>> {

        val models = weekDayUXComponent.render(uiDelegate)
        return CarouselModel_()
            .id("")
            .models(models)
            .toList()
    }
}

class WeekdayUXComponent(
    private val identifier: String,
    private val uiModels: List<WeekdayUiModel>,
    override val vmDelegate: WeekdayVMDelegate
) : UXComponent<WeekdayVMDelegate, WeekdayUIDelegate> {

    override fun render(uiDelegate: WeekdayUIDelegate): List<EpoxyModel<*>> =
        uiModels.map { uiModel ->
            WeekdayBindingModel_()
                .id(identifier)
                .weekdayUiModel(uiModel)
                .onClick { v ->
                    vmDelegate.onWeekdaySelected(uiModel)
                }
        }
}


interface WeekdayVMDelegate : VMDelegate {
    fun onWeekdaySelected(weekdayUModel: WeekdayUiModel)
}

interface WeekdayUIDelegate : UIDelegate {
    fun onWeekdaySelected(weekdayUModel: WeekdayUiModel)
}

data class WeekdayUiModel(
    val day: String, // Sun
    val date: Int, // 21
    val earning: Double, //200.0
    val isSelected: Boolean
)


fun <T> EpoxyModel<T>.toList(): List<EpoxyModel<T>> {
    return arrayListOf(this)
}

package com.kinley.ui.weekcomponent

import com.airbnb.epoxy.CarouselModel_
import com.airbnb.epoxy.EpoxyModel
import com.kinley.ui.WeekBindingModel_
import com.kinley.ui.uxcomponent.UIDelegate
import com.kinley.ui.uxcomponent.UXComponent
import com.kinley.ui.uxcomponent.VMDelegate
import com.kinley.ui.weekdaycomponent.toList

class WeekUXComponent(
    private val identifier: String,
    private val uiModel: WeekUiModel,
    override val vmDelegate: WeekVMDelegate
) :
    UXComponent<WeekVMDelegate, WeekUIDelegate> {

    override fun render(uiDelegate: WeekUIDelegate): List<EpoxyModel<*>> {

        return WeekBindingModel_()
            .id(identifier)
            .weekUiModel(uiModel)
            .onClick { v ->
                vmDelegate.onWeekSelected(uiModel)
            }
            .toList()
    }
}

class HorizontalWeekUXComponent(
    private val identifier: String,
    private val uiModels: List<WeekUiModel>,
    override val vmDelegate: WeekVMDelegate
) : UXComponent<WeekVMDelegate, WeekUIDelegate> {

    override fun render(uiDelegate: WeekUIDelegate): List<EpoxyModel<*>> {
        val models = uiModels
            .map { WeekUXComponent(it.week, it, vmDelegate).render(uiDelegate) }
            .reduce { acc, list -> acc + list }

        return CarouselModel_()
            .id(identifier)
            .models(models)
            .toList()
    }

}

interface WeekVMDelegate : VMDelegate {
    fun onWeekSelected(week: WeekUiModel)
}

interface WeekUIDelegate : UIDelegate {
    fun onWeekSelected(week: WeekUiModel)
}


data class WeekUiModel(
    val week: String, // Week 21
    val earning: Double // 200
)

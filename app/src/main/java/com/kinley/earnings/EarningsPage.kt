@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.kinley.earnings

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyModel
import com.kinley.ui.EarningsBindingModel_
import com.kinley.ui.WeekBindingModel_
import com.kinley.ui.WeekdayBindingModel_
import com.kinley.ui.earningcomponent.EarningUiDelegate
import com.kinley.ui.weekcomponent.WeekUIDelegate
import com.kinley.ui.weekcomponent.WeekUiModel
import com.kinley.ui.weekdaycomponent.WeekdayUIDelegate
import com.kinley.ui.weekdaycomponent.WeekdayUiModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class EarningsPage : AppCompatActivity(), WeekUIDelegate, WeekdayUIDelegate, EarningUiDelegate {

    private val vm: EarningsPageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycle.addObserver(vm)

        // Defining the view of weeks
        rv_weeks.withModels { createWeekModels(this@EarningsPage).render(this) }

        // Defining the view of weekdays
        rv_weekdays.withModels { createWeekdaysModels(this@EarningsPage).render(this) }

        // Defining the views of earnings
        rv_earnings.withModels { createEarningsModels(this@EarningsPage).render(this) }


        // Observe for the changes in each of the components and re-build their UI

        with(vm.components) {
            weekUXComponent.onEach { rv_weeks.requestModelBuild() }.launchIn(lifecycleScope)
            weekdayUXComponent.onEach { rv_weekdays.requestModelBuild() }.launchIn(lifecycleScope)
            earningUXComponent.onEach { rv_earnings.requestModelBuild() }.launchIn(lifecycleScope)
        }

    }

    override fun onWeekSelected(week: WeekUiModel) {

    }

    override fun onWeekdaySelected(weekdayUModel: WeekdayUiModel) {

    }


    // ENHANCEMENT : Move the creation of Views outside of this class
    private fun createWeekModels(uiDelegate: WeekUIDelegate): List<EpoxyModel<*>> {
        return vm.components.weekUXComponent.value?.render(uiDelegate) ?: arrayListOf()
    }

    private fun createWeekdaysModels(uiDelegate: WeekdayUIDelegate): List<EpoxyModel<*>> {
        return vm.components.weekdayUXComponent.value?.render(uiDelegate) ?: arrayListOf()
    }

    private fun createEarningsModels(earningUiDelegate: EarningUiDelegate): List<EpoxyModel<*>> {
        return vm.components.earningUXComponent.value?.render(earningUiDelegate) ?: arrayListOf()
    }

    override fun onEarningClick(earningId: String) {

    }
}

fun <T : EpoxyModel<*>> List<T>.render(controller: EpoxyController) {
    this.forEach { it.addTo(controller) }
}



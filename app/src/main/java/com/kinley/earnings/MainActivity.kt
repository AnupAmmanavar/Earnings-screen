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
import com.kinley.ui.weekcomponent.WeekUIDelegate
import com.kinley.ui.weekcomponent.WeekUiModel
import com.kinley.ui.weekdaycomponent.WeekdayUIDelegate
import com.kinley.ui.weekdaycomponent.WeekdayUiModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), WeekUIDelegate, WeekdayUIDelegate {

    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycle.addObserver(vm)

        // Defining the view of weeks
        rv_weeks.withModels { createWeekModels(this@MainActivity).render(this) }

        // Defining the view of weekdays
        rv_weekdays.withModels { createWeekdaysModels(this@MainActivity).render(this) }

        // Defining the views of earnings
        rv_earnings.withModels { createEarningsModels().render(this) }


        // Observe for the changes in each of the components and re-build their UI
        lifecycleScope.launch {
            vm.uiState.weeksUiModel.onEach { rv_weeks.requestModelBuild() }.launchIn(this)
            vm.uiState.daysUiModel.onEach { rv_weekdays.requestModelBuild() }.launchIn(this)
            vm.uiState.earningsUiModel.onEach { rv_earnings.requestModelBuild() }.launchIn(this)
        }

    }

    override fun onWeekSelected(week: WeekUiModel) {
        vm.updateWeek(week.id)
    }

    override fun onWeekdaySelected(weekdayUModel: WeekdayUiModel) {
        vm.updateDay(weekdayUModel.id)
    }


    // ENHANCEMENT : Move the creation of Views outside of this class
    private fun createWeekModels(uiDelegate: WeekUIDelegate): List<EpoxyModel<*>> {
        val list = vm.uiState.weeksUiModel.value

        return list.map {
            WeekBindingModel_()
                .id(it.id)
                .weekUiModel(it)
                .onClick { _ ->
                    uiDelegate.onWeekSelected(it)
                }
        }

    }

    private fun createWeekdaysModels(uiDelegate: WeekdayUIDelegate): List<EpoxyModel<*>> {
        val list = vm.uiState.daysUiModel.value

        return list.map {
            WeekdayBindingModel_()
                .id(it.id)
                .weekdayUiModel(it)
                .onClick { v ->
                    uiDelegate.onWeekdaySelected(it)
                }

        }
    }

    private fun createEarningsModels(): List<EpoxyModel<*>> {
        val list = vm.uiState.earningsUiModel.value

        return list.map {
            EarningsBindingModel_()
                .id(it.id)
                .uiModel(it)

        }
    }
}

fun <T : EpoxyModel<*>> List<T>.render(controller: EpoxyController) {
    this.forEach { it.addTo(controller) }
}



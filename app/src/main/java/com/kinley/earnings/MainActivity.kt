@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.kinley.earnings

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kinley.ui.EarningsBindingModel_
import com.kinley.ui.WeekBindingModel_
import com.kinley.ui.WeekdayBindingModel_
import com.kinley.ui.weekcomponent.WeekUiModel
import com.kinley.ui.weekcomponent.WeekVMDelegate
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), WeekVMDelegate {

    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycle.addObserver(vm)

        // Defining the view of weeks
        rv_weeks.withModels {
            val list = vm.uiState.weeksUiModel.value

            list.map {
                WeekBindingModel_()
                    .id(it.week)
                    .weekUiModel(it)
                    .onClick { _ ->
                        onWeekSelected(it)
                    }
                    .addTo(this)
            }

        }

        // Defining the view of weekdays
        rv_weekdays.withModels {
            val list = vm.uiState.daysUiModel.value

            list.map {
                WeekdayBindingModel_()
                    .id(it.day)
                    .weekdayUiModel(it)
                    .onClick { v ->
                        vm.updateDay(it.day)
                    }
                    .addTo(this)
            }
        }

        rv_earnings.withModels {
            val list = vm.uiState.earningsUiModel.value

            list.map {
                EarningsBindingModel_()
                    .id("${it.earningType} ${it.amount}") // Add identifier
                    .uiModel(it)
                    .addTo(this)
            }
        }


        // Observe for the changes in each of the components and re-build their UI
        lifecycleScope.launch {
            vm.uiState.weeksUiModel.onEach { rv_weeks.requestModelBuild() }.launchIn(this)
            vm.uiState.daysUiModel.onEach { rv_weekdays.requestModelBuild() }.launchIn(this)
            vm.uiState.earningsUiModel.onEach { rv_earnings.requestModelBuild() }.launchIn(this)
        }

    }

    override fun onWeekSelected(week: WeekUiModel) {
        vm.updateWeek(week)
    }
}



@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.kinley.earnings

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kinley.ui.WeekBindingModel_
import com.kinley.ui.WeekdayBindingModel_
import com.kinley.ui.weekcomponent.WeekUiModel
import com.kinley.ui.weekcomponent.WeekVMDelegate
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), WeekVMDelegate {

    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycle.addObserver(vm)

        // Defining the view of weeks
        rv_weeks.withModels {
            val list = vm.weeksUiModel.value

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
            val list = vm.daysUiModel.value

            list.map {
                WeekdayBindingModel_()
                    .id(it.day)
                    .weekdayUiModel(it)
                    .addTo(this)
            }
        }


        // Observe for the changes in weeks
        lifecycleScope.launch {
            vm.weeksUiModel.collect { rv_weeks.requestModelBuild() }
        }

        // Observe for the changes in weekdays
        lifecycleScope.launch {
            vm.daysUiModel.collect { rv_weekdays.requestModelBuild() }
        }

    }

    override fun onWeekSelected(week: WeekUiModel) {
        vm.updateWeek(week)
    }
}



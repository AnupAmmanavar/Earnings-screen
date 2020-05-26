@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.kinley.earnings

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyModel
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
        rv_weeks.withModels {
            (vm.components.weekUXComponent.value?.render(this@EarningsPage)
                ?: arrayListOf()).update(this)
        }

        // Defining the view of weekdays
        rv_weekdays.withModels {
            (vm.components.weekdayUXComponent.value?.render(this@EarningsPage)
                ?: arrayListOf()).update(this)
        }

        // Defining the views of earnings
        rv_earnings.withModels {
            (vm.components.earningUXComponent.value?.render(this@EarningsPage)
                ?: arrayListOf()).update(this)
        }


        // Observe for the changes in each of the components and re-build their UI

        with(vm.components) {
            weekUXComponent.onEach { rv_weeks.requestModelBuild() }.launchIn(lifecycleScope)
            weekdayUXComponent.onEach { rv_weekdays.requestModelBuild() }.launchIn(lifecycleScope)
            earningUXComponent.onEach { rv_earnings.requestModelBuild() }.launchIn(lifecycleScope)
        }

    }

    override fun onEarningClick(earningId: String) {
        // Navigation
    }
}

fun <T : EpoxyModel<*>> List<T>.update(controller: EpoxyController) {
    this.forEach { it.addTo(controller) }
}



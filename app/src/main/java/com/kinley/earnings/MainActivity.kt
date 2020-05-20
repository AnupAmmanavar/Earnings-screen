@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.kinley.earnings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycle.addObserver(vm)

        lifecycleScope.launch {
            vm.daysUiModel.collect {
                Log.d("Colected", it.toString())
                rv_weekdays.requestModelBuild()
            }
        }

        lifecycleScope.launch {
            vm.weeksUiModel.collect {
                Log.d("Colected", it.toString())
                rv_weeks.requestModelBuild()
            }
        }

        rv_weeks.withModels {
            val list = vm.weeksUiModel.value

        }

        rv_weekdays.withModels {
            val list = vm.daysUiModel.value
        }
    }
}



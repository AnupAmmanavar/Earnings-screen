package com.kinley.earnings.features.earnings

import com.kinley.earnings.features.EventDispatcher

interface EarningFeatureEventDispatcher :
    EventDispatcher {
    fun onEarningClick(id: String)
}

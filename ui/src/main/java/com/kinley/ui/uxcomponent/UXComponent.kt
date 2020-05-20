package com.kinley.ui.uxcomponent

import com.airbnb.epoxy.EpoxyModel

interface UXComponent<VM : VMDelegate, UI : UIDelegate> {
    val vmDelegate: VM
    fun render(uiDelegate: UI): List<EpoxyModel<*>>
}

interface VMDelegate

interface UIDelegate

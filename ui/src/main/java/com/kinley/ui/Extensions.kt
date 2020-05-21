package com.kinley.ui

import android.graphics.Typeface
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("textBold")
fun TextView.setBold(isBold: Boolean) {
    typeface =
        if (isBold) Typeface.DEFAULT_BOLD
        else Typeface.DEFAULT
}

package com.kinley.earnings.features

interface ComponentDataChangeListener<T> {
    fun onChange(data: T)
}

package com.example.counter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val countValue = MutableLiveData<Int>()

    internal fun getValue(): LiveData<Int> = countValue

    internal fun incrementValue(displayedValue: Int) {
        countValue.value = displayedValue + 1
    }
}
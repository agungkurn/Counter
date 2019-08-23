package com.example.counter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel : ViewModel() {
    // Coroutines put in here
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    // LiveDatas put in here
    private val countRepValue = MutableLiveData<Int>()
    private val stopwatchValue = MutableLiveData<String>()
    private val isStopwatchStarted = MutableLiveData<Boolean>()

    // Object vars put in here
    private lateinit var stopwatchCounter: Job

    // Standard vars put in here

    // Getters put in here
    internal fun getValue(): LiveData<Int> = countRepValue

    internal fun getStopwatchValue(): LiveData<String> = stopwatchValue
    internal fun isStopwatchStarted(): LiveData<Boolean> = isStopwatchStarted

    init {
        isStopwatchStarted.value = false
    }

    internal fun incrementValue(displayedValue: Int) {
        countRepValue.value = displayedValue + 1
    }

    internal fun controlStopwatch() {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)

//        var handler: Handler? = Handler(Looper.getMainLooper())

        Log.d(MainActivity.TAG, "Stopwatch started?: ${isStopwatchStarted.value}")

        stopwatchCounter = viewModelScope.launch {
            while (isStopwatchStarted.value == true) {
                val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(cal.time)
                stopwatchValue.value = currentTime
                Log.d(MainActivity.TAG, "Stopwatch started ($currentTime)")
                cal.add(Calendar.SECOND, 1)
                delay(1000L)
            }
        }

        if (isStopwatchStarted.value == false) {
            isStopwatchStarted.value = true
            stopwatchCounter.start()
        } else {
            isStopwatchStarted.value = false
            stopwatchCounter.cancel()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
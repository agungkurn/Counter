package com.example.counter

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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
    private val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    // Standard vars put in here
    private var secondInMillis = 0L

    // Getters put in here
    internal fun getValue(): LiveData<Int> = countRepValue

    internal fun getStopwatchValue(): LiveData<String> = stopwatchValue
    internal fun isStopwatchStarted(): LiveData<Boolean> = isStopwatchStarted

    internal fun incrementValue(displayedValue: Int) {
        countRepValue.value = displayedValue + 1
    }

    internal fun controlStopwatch() {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)

        var handler: Handler? = null

        if (isStopwatchStarted.value == true) {
            handler = null
            isStopwatchStarted.value = false
        }

        if (handler == null) {
            isStopwatchStarted.value = true
            handler = Handler(Looper.getMainLooper())
            handler.postDelayed(object : Runnable {
                override fun run() {
                    val currentTime =
                        SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(cal.time)
                    stopwatchValue.postValue(currentTime)
                    Log.d(MainActivity.TAG, "Current time: $currentTime")
                    handler.postDelayed(this, 1000)
                    cal.add(Calendar.SECOND, 1)
                }
            }, 10)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
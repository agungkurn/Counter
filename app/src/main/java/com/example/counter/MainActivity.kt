package com.example.counter

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        internal val TAG = MainActivity::class.java.simpleName
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(bar)

        viewModel.getValue().observe(this, Observer {
            tv_count.text = "$it"
        })
    }

    fun incrementValue(v: View) {
        viewModel.incrementValue(tv_count.text.toString().toInt())
    }

    fun resetValue(v: View) {
        tv_count.text = "0"
    }

    fun controlStopwatch(v: View) {
        viewModel.getStopwatchValue().observe(this, Observer {
            fab_stopwatch.text = it
        })
        viewModel.isStopwatchStarted().observe(this, Observer { started ->
            if (started) {
                fab_stopwatch.icon = getDrawable(R.drawable.ic_stop_black_24dp)
            } else {
                fab_stopwatch.icon = getDrawable(R.drawable.ic_play_arrow_black_24dp)
            }
        })
        viewModel.controlStopwatch()
    }
}

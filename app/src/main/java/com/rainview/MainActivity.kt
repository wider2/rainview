package com.rainview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val STATE = "state"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        var width = displayMetrics.widthPixels
        var height = displayMetrics.heightPixels
        rain_drop_view.setWidth(width)
        rain_drop_view.setHeight(height)

        savedInstanceState?.let {
            it.getParcelableArrayList<RainDrop>(STATE)?.let {
                rain_drop_view.rainDropList = it
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (rain_drop_view.rainDropList.isNotEmpty()) {
            outState.putParcelableArrayList(STATE, rain_drop_view.rainDropList as ArrayList)
        }
    }

    override fun onResume() {
        super.onResume()
    }
}

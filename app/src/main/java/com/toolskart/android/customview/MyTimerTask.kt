package com.toolskart.android.customview

import android.os.Handler
import java.util.*

class MyTimerTask (val handler: Handler, val runnable: Runnable) : TimerTask() {
    override fun run() {
        handler.post(runnable)

    }
}
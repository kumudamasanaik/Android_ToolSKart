package com.toolskart.android.utils

import android.util.Log
import com.toolskart.android.BuildConfig

class MyLogUtils {
    companion object {
        var isDebug = BuildConfig.DEBUG

        fun d(tag: String, msg: String) {
            if (isDebug) {
                if (tag.isNotEmpty() && msg.isNotEmpty())
                    Log.d(tag, msg)
            }
        }

        fun e(tag: String, msg: String) {
            if (isDebug) {
                if (tag.isNotEmpty() && msg.isNotEmpty())
                    Log.e(tag, msg)
            }
        }

        fun w(tag: String, msg: String, tr: Throwable) {
            if (isDebug) {
                if (tag.isNotEmpty() && msg.isNotEmpty())
                    Log.w(tag, msg, tr)
            }
        }

        fun i(tag: String, msg: String) {
            if (isDebug) {
                if (tag.isNotEmpty() && msg.isNotEmpty())
                    Log.i(tag, msg)
            }
        }
    }
}
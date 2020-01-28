package com.toolskart.android.utils

import android.support.v7.app.AppCompatActivity
import android.widget.Toast

fun AppCompatActivity.showToastMsg(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, duration).show()
}
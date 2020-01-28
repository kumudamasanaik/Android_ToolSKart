package com.toolskart.android.ui

interface BaseView {

    fun init()

    fun showMsg(msg: String?)

    fun showLoader()

    fun hideLoader()

    fun showViewState(state: Int)
}
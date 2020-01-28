package com.toolskart.android.ui.aboutus

import com.toolskart.android.model.AboutUsRes
import com.toolskart.android.ui.BasePresenter
import com.toolskart.android.ui.BaseView

interface AboutUsContract {
    interface View : BaseView {
        fun callAboutUsScreenApi()
        fun setAboutUsApiRes(response: AboutUsRes)
    }

    interface Presenter : BasePresenter<View> {
        fun callAboutUsAPi()
    }
}
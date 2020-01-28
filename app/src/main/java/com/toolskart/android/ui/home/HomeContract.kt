package com.toolskart.android.ui.home

import com.toolskart.android.model.CustomerRes
import com.toolskart.android.model.HomeResponse
import com.toolskart.android.ui.BasePresenter
import com.toolskart.android.ui.BaseView

interface HomeContract {

    interface View : BaseView {
        fun callHomeBoardApi()
        fun setHomeApiResp(homeResp: HomeResponse)
    }

    interface Presenter : BasePresenter<View> {
        fun callDashBoardApi()

    }
}
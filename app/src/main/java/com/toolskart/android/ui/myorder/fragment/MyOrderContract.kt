package com.toolskart.android.ui.myorder.fragment

import com.toolskart.android.model.MyOrderRes
import com.toolskart.android.ui.BasePresenter
import com.toolskart.android.ui.BaseView

interface MyOrderContract {
    interface View : BaseView {
        fun callMyOrder()
        fun setMyOrderResp(res: MyOrderRes)
        fun setCancelApiRes(res: MyOrderRes)
    }

    interface Presenter : BasePresenter<View> {
        fun callMyOrderApi(status:String)
        fun callCancelApi(status: String,order_no:String)
    }
}
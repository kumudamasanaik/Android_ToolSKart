package com.toolskart.android.ui.orderdetails

import android.view.View
import com.google.gson.JsonArray
import com.toolskart.android.model.CommonRes
import com.toolskart.android.model.OrderDetailRes
import com.toolskart.android.ui.BasePresenter
import com.toolskart.android.ui.BaseView

interface OrderDetailContract {
    interface view : BaseView {
        fun callOrderDetails()
        fun setOrderDetailsResp(res: OrderDetailRes)
        fun setReturnApiRes(res: CommonRes)
    }

    interface Presenter : BasePresenter<View> {
        fun callMyOrderDetailsApi(order_no: String)
        fun callReturnApi(id: String, order_id: String, return_reason: String, jsonArray: JsonArray)
    }
}
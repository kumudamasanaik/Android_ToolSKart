package com.toolskart.android.ui.viewallproduct

import com.toolskart.android.model.Customer
import com.toolskart.android.model.FeaturedProductListRes
import com.toolskart.android.ui.BasePresenter
import com.toolskart.android.ui.BaseView

interface ViewAllProductContract {
    interface view : BaseView {
        fun callProductList()
        fun setProductListApiRes(res: FeaturedProductListRes)
    }

    interface presenter : BasePresenter<view> {
        fun callProductListApi()
    }
}
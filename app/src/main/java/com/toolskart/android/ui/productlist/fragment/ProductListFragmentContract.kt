package com.toolskart.android.ui.productlist.fragment

import com.toolskart.android.model.ProductListResponse
import com.toolskart.android.ui.BasePresenter
import com.toolskart.android.ui.BaseView

interface ProductListFragmentContract {
    interface View : BaseView {
        fun callProductList()
        fun setProductListApiRes(productlistRes: ProductListResponse)
    }

    interface Presenter : BasePresenter<View> {
        fun callProductListApi(lowCatId: String)
    }
}
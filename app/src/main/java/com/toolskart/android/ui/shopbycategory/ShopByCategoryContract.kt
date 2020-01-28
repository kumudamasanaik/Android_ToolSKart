package com.toolskart.android.ui.shopbycategory

import com.toolskart.android.model.ShopByCategoryRes
import com.toolskart.android.model.test.Test
import com.toolskart.android.ui.BasePresenter
import com.toolskart.android.ui.BaseView

interface ShopByCategoryContract {
    interface View : BaseView {
        fun callShopByCategory()
        fun setShopByCategoryApiRes(response: Test)
    }

    interface Presenter : BasePresenter<View> {
        fun callShopByCategoryApi()
    }
}
package com.toolskart.android.ui.search

import com.toolskart.android.model.inputmodel.SearchInput
import com.toolskart.android.model.SearchProductsRes
import com.toolskart.android.ui.BasePresenter
import com.toolskart.android.ui.BaseView

interface SearchContract {
    interface View : BaseView {
        fun searchProducts()
        fun setSearchProductsRes(res: SearchProductsRes)
    }

    interface Presenter : BasePresenter<View> {
        fun callSearch(input: SearchInput)
    }
}
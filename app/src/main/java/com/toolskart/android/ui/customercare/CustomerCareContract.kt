package com.toolskart.android.ui.customercare

import com.toolskart.android.model.Customer
import com.toolskart.android.model.inputmodel.CustomerCareIp
import com.toolskart.android.ui.BasePresenter
import com.toolskart.android.ui.BaseView

interface CustomerCareContract {
    interface view : BaseView {
        fun callCustomerCare()
        fun setCustomerCareApiRes(res: Customer)
    }

    interface presenter : BasePresenter<view> {
        fun validateInputs(inputs:CustomerCareIp)
        fun callCustomerCareApi()
    }
}
package com.toolskart.android.ui.customercare

import com.toolskart.android.api.ApiResponsePresenter
import com.toolskart.android.api.IResponseInterface
import com.toolskart.android.model.inputmodel.CustomerCareIp
import retrofit2.Call
import retrofit2.Response

class CustomerCarePresenter(val view: CustomerCareContract.view) : CustomerCareContract.presenter, IResponseInterface {

    val iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    override fun callCustomerCareApi() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun validateInputs(inputs: CustomerCareIp) {
        /*if (input.name.isNullOrEmpty()) {
            view.showDeliveryAddressValidateErrorMsg("1")
            return false
        } */
    }
}
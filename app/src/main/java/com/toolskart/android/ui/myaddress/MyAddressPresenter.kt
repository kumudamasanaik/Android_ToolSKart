package com.toolskart.android.ui.myaddress

import com.toolskart.android.api.ApiRequestParam
import com.toolskart.android.api.ApiResponsePresenter
import com.toolskart.android.api.ApiType
import com.toolskart.android.api.IResponseInterface
import com.toolskart.android.customview.ScreenStateView
import com.toolskart.android.model.inputmodel.AddressInput
import com.toolskart.android.model.AddAddressRes
import com.toolskart.android.model.DeleteAddressResp
import com.toolskart.android.model.GetAddressResp
import com.toolskart.android.model.UpdateAddress
import com.toolskart.android.utils.MyApplication
import com.toolskart.android.utils.Validation
import retrofit2.Call
import retrofit2.Response

class MyAddressPresenter(var View: MyAddressContract.View) : MyAddressContract.Presenter, IResponseInterface {
    var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)

    override fun validate(input: AddressInput): Boolean {
        if (input.name.isNullOrEmpty()) {
            View.showDeliveryAddressValidateErrorMsg("1")
            return false
        }
        if (!Validation.isValidMobileNumber(input.phone)) {
            View.showDeliveryAddressValidateErrorMsg("2")
            return false
        }
        if (input.address1.isNullOrEmpty()) {
            View.showDeliveryAddressValidateErrorMsg("3")
            return false
        }
        if (input.pincode!!.isEmpty()) {
            View.showDeliveryAddressValidateErrorMsg("4")
            return false
        }
        return true
    }

    override fun callSelectDeliveryAddressApi(op: String) {
        iResponseInterface.callApi(MyApplication.service.getAddress(ApiRequestParam.getSelectAddressRequestParameters(op)), ApiType.GET_ADDRESS)
    }

    override fun callUpdateAddressApi(input: AddressInput) {
        iResponseInterface.callApi(MyApplication.service.getUpdateAddress(input), ApiType.UPDATEADDRESS)
    }

    override fun callDeleteAddressApi(input: AddressInput) {
        iResponseInterface.callApi(MyApplication.service.getDeleteAddress(input), ApiType.DELETEADDRESS)
    }

    override fun callAddDeliveryAddressApi(input: AddressInput) {
        iResponseInterface.callApi(MyApplication.service.getAddAddress(input), ApiType.ADD_ADDRESS)
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        View.hideLoader()
        if (response.isSuccessful && Validation.isValidObject(response)) {
            when (reqType) {
                ApiType.GET_ADDRESS ->
                    View.setSelectDeliveryAddressResp(response.body() as GetAddressResp)

                ApiType.UPDATEADDRESS ->
                    View.setUpdateDeliveryAddressResp(response.body() as UpdateAddress)

                ApiType.DELETEADDRESS ->
                    View.setDeleteAddressResp(response.body() as DeleteAddressResp)

                ApiType.ADD_ADDRESS ->
                    View.setAddDeliveryAddressResp(response.body() as AddAddressRes)
            }
        }
    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        View.hideLoader()
        View.showMsg(responseError.message)
        when (reqType) {
            ApiType.GET_ADDRESS ->
                View.showViewState(ScreenStateView.VIEW_STATE_ERROR)

            ApiType.UPDATEADDRESS ->
                View.showViewState(ScreenStateView.VIEW_STATE_ERROR)

            ApiType.DELETEADDRESS ->
                View.showViewState(ScreenStateView.VIEW_STATE_ERROR)

            ApiType.ADD_ADDRESS ->
                View.showViewState(ScreenStateView.VIEW_STATE_ERROR)
        }
    }
}
package com.toolskart.android.ui.orderdetails

import com.google.gson.JsonArray
import com.toolskart.android.api.ApiRequestParam
import com.toolskart.android.api.ApiResponsePresenter
import com.toolskart.android.api.ApiType
import com.toolskart.android.api.IResponseInterface
import com.toolskart.android.customview.ScreenStateView
import com.toolskart.android.model.OrderDetailRes
import com.toolskart.android.utils.MyApplication
import com.toolskart.android.utils.Validation
import retrofit2.Call
import retrofit2.Response

class OrderDetailPresenter(view: OrderDetailContract.view) : OrderDetailContract.Presenter, IResponseInterface {

    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: OrderDetailContract.view? = view

    override fun callMyOrderDetailsApi(order_no: String) {
        iResponseInterface.callApi(MyApplication.service.getOrderDetailsList(ApiRequestParam.getMyOrderDetailsParameters(order_no)), ApiType.ORDER_DETAILS)
    }

    override fun callReturnApi(id: String, order_id: String, return_reason: String, jsonArray: JsonArray) {

    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()

        if (response.isSuccessful && Validation.isValidObject(response)) {
            when (reqType) {
                ApiType.ORDER_DETAILS ->
                    view!!.setOrderDetailsResp(response.body() as OrderDetailRes)
            }
        } else
            view!!.showViewState(ScreenStateView.VIEW_STATE_EMPTY)
    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        view?.showMsg(responseError.message)
        when (reqType) {
            ApiType.ORDER_DETAILS -> {
                view!!.showViewState(ScreenStateView.VIEW_STATE_ERROR)
            }

            /* ApiType.RETURN_ORDER -> {
                 view!!.showViewState(MultiStateView.VIEW_STATE_ERROR)
             }*/
        }
    }
}
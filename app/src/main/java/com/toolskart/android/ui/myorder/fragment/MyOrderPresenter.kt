package com.toolskart.android.ui.myorder.fragment

import com.toolskart.android.api.ApiRequestParam
import com.toolskart.android.api.ApiResponsePresenter
import com.toolskart.android.api.ApiType
import com.toolskart.android.api.IResponseInterface
import com.toolskart.android.customview.ScreenStateView
import com.toolskart.android.model.MyOrderRes
import com.toolskart.android.utils.MyApplication
import com.toolskart.android.utils.Validation
import retrofit2.Call
import retrofit2.Response

class MyOrderPresenter(view: MyOrderContract.View) : MyOrderContract.Presenter, IResponseInterface {
    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: MyOrderContract.View? = view

    override fun callMyOrderApi(status: String) {
        iResponseInterface.callApi(MyApplication.service.getMyOrdersList(ApiRequestParam.getMyOrderParameters(status)), ApiType.MY_ORDER)
    }

    override fun callCancelApi(status: String, order_no: String) {
        iResponseInterface.callApi(MyApplication.service.getCancelOrder(ApiRequestParam.geCancelOrderParameters(status, order_no)), ApiType.CANCEL_ORDER)
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful && Validation.isValidObject(response)) {
            when (reqType) {
                ApiType.MY_ORDER ->
                    view!!.setMyOrderResp(response.body() as MyOrderRes)

                ApiType.CANCEL_ORDER ->
                    view!!.setCancelApiRes(response.body() as MyOrderRes)
            }
        } else
            view!!.showViewState(ScreenStateView.VIEW_STATE_EMPTY)
    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        view?.showMsg(responseError.message)
        when (reqType) {
            ApiType.MY_ORDER -> {
                view!!.showViewState(ScreenStateView.VIEW_STATE_ERROR)
            }

            ApiType.CANCEL_ORDER -> {
                view!!.showViewState(ScreenStateView.VIEW_STATE_ERROR)
            }
        }
    }
}
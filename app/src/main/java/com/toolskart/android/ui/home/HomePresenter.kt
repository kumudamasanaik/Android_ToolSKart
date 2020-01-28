package com.toolskart.android.ui.home

import com.toolskart.android.api.ApiRequestParam
import com.toolskart.android.api.ApiResponsePresenter
import com.toolskart.android.api.ApiType
import com.toolskart.android.api.IResponseInterface
import com.toolskart.android.customview.ScreenStateView
import com.toolskart.android.model.CustomerRes
import com.toolskart.android.model.HomeResponse
import com.toolskart.android.utils.MyApplication
import com.toolskart.android.utils.Validation
import retrofit2.Call
import retrofit2.Response

class HomePresenter(view: HomeContract.View?) : HomeContract.Presenter, IResponseInterface {

    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: HomeContract.View? = view

    override fun callDashBoardApi() {
        iResponseInterface.callApi(MyApplication.service.getHomeApi(ApiRequestParam.getHomeParameters()), ApiType.HOME)
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful && Validation.isValidObject(response)) {
            when (reqType) {
                ApiType.HOME ->
                    view!!.setHomeApiResp(response.body() as HomeResponse)
            }
        } else
            view!!.showViewState(ScreenStateView.VIEW_STATE_EMPTY)
    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        view?.showMsg(responseError.message)
        when (reqType) {
            ApiType.HOME -> {
                view!!.showViewState(ScreenStateView.VIEW_STATE_ERROR)
            }
        }
    }
}
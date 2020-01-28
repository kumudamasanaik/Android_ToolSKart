package com.toolskart.android.ui.aboutus

import com.toolskart.android.api.ApiResponsePresenter
import com.toolskart.android.api.ApiType
import com.toolskart.android.api.IResponseInterface
import com.toolskart.android.customview.ScreenStateView
import com.toolskart.android.model.AboutUsRes
import com.toolskart.android.utils.MyApplication
import retrofit2.Call
import retrofit2.Response

class AboutUsPresenter(view: AboutUsContract.View) : AboutUsContract.Presenter, IResponseInterface {
    private var view: AboutUsContract.View? = view
    private var iResponseInterface: ApiResponsePresenter

    init {
        this.view = view
        this.iResponseInterface = ApiResponsePresenter(this)
    }

    override fun callAboutUsAPi() {
        iResponseInterface.callApi(MyApplication.service.getAboutUsScreen(), ApiType.ABOUT_US)
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful) {
            when (reqType) {
                ApiType.ABOUT_US ->
                    view?.setAboutUsApiRes(response.body() as AboutUsRes)
            }
        } else view?.showViewState(ScreenStateView.VIEW_STATE_ERROR)
    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        when (reqType) {
            ApiType.ABOUT_US ->
                view?.showMsg(responseError.message)
        }
    }
}
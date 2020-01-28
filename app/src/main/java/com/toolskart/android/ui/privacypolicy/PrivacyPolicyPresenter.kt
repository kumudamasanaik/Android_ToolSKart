package com.toolskart.android.ui.privacypolicy

import com.toolskart.android.api.ApiResponsePresenter
import com.toolskart.android.api.ApiType
import com.toolskart.android.api.IResponseInterface
import com.toolskart.android.customview.ScreenStateView
import com.toolskart.android.model.PrivacyPolicyRes
import com.toolskart.android.utils.MyApplication
import retrofit2.Call
import retrofit2.Response

class PrivacyPolicyPresenter(val view: PrivacyPolicyContract.View) : PrivacyPolicyContract.Presenter, IResponseInterface {
    val iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)

    override fun callPrivacyPolicyAPi() {
        iResponseInterface.callApi(MyApplication.service.getPrivacuPolicy(), ApiType.PRIVACY_POLICY)
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view.hideLoader()
        if (response.isSuccessful) {
            when (reqType) {
                ApiType.PRIVACY_POLICY ->
                    view.setPrivacyPolicyApiRes(response.body() as PrivacyPolicyRes)
            }
        } else view.showViewState(ScreenStateView.VIEW_STATE_ERROR)
    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view.hideLoader()
        when (reqType) {
            ApiType.PRIVACY_POLICY ->
                view.showMsg(responseError.message)
        }
    }
}
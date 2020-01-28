package com.toolskart.android.ui.notification

import com.toolskart.android.api.ApiRequestParam
import com.toolskart.android.api.ApiResponsePresenter
import com.toolskart.android.api.ApiType
import com.toolskart.android.api.IResponseInterface
import com.toolskart.android.model.CustomerRes
import com.toolskart.android.utils.MyApplication
import retrofit2.Call
import retrofit2.Response

class NotificationPresenter(view: NotificationContract.View) : NotificationContract.Presenter, IResponseInterface {
    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: NotificationContract.View? = view

    override fun callNotificationApi() {
        iResponseInterface.callApi(MyApplication.service.getNotification(ApiRequestParam.getNotificationData()), ApiType.NOTIFICATIONLIST)
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful) {
            when (reqType) {
                ApiType.NOTIFICATIONLIST ->
                    view?.setNotificationResp(response.body() as CustomerRes)
            }
        }
    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        when (reqType) {
            ApiType.NOTIFICATIONLIST ->
                view?.showMsg(responseError.message)
        }
    }
}
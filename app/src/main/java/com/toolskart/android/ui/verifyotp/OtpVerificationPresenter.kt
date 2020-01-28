package com.toolskart.android.ui.verifyotp

import com.toolskart.android.api.ApiRequestParam
import com.toolskart.android.api.ApiResponsePresenter
import com.toolskart.android.api.ApiType
import com.toolskart.android.api.IResponseInterface
import com.toolskart.android.model.CustomerRes
import com.toolskart.android.utils.MyApplication
import retrofit2.Call
import retrofit2.Response

class OtpVerificationPresenter(view: OtpVerificationContract.View) : OtpVerificationContract.Presenter, IResponseInterface {

    private var iRequestInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: OtpVerificationContract.View? = view

    override fun verifyOtp(otp: String, mobile: String) {
        iRequestInterface.callApi(MyApplication.service.verifyOtp(ApiRequestParam.verifyOtp(otp, mobile)), ApiType.OTP)
    }

    override fun resendOtp(mobile:String) {
        iRequestInterface.callApi(MyApplication.service.resendOtp(ApiRequestParam.resendOtp(mobile)), ApiType.RESEND_OTP)
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful) {
            when (reqType) {
                ApiType.OTP ->
                    view?.setOtpRes(response.body() as CustomerRes)
                ApiType.RESEND_OTP ->
                    view?.showResendOtpRes(response.body() as CustomerRes)
            }
        }
    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()

        when (reqType) {
            ApiType.OTP ->
                view?.showMsg(responseError.message)
            ApiType.RESEND_OTP ->
                view?.showMsg(responseError.message)
        }
    }
}
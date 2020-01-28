package com.toolskart.android.ui.login

import com.toolskart.android.api.ApiRequestParam
import com.toolskart.android.api.ApiResponsePresenter
import com.toolskart.android.api.ApiType
import com.toolskart.android.api.IResponseInterface
import com.toolskart.android.model.CustomerRes
import com.toolskart.android.model.inputmodel.SignUpInput
import com.toolskart.android.utils.MyApplication
import com.toolskart.android.utils.Validation
import retrofit2.Call
import retrofit2.Response

class LoginPresenter(view: LoginContract.View) : LoginContract.Presenter, IResponseInterface {

    var view: LoginContract.View? = view
    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)

    override fun validateLogin(emailormobile: String?): Boolean {
        if (emailormobile.isNullOrEmpty()) {
            view?.invalidEmailPhone()
            return false
        }
        return true
    }

    override fun validate(input: SignUpInput): Boolean {
        if (input.name.isEmpty()) {
            view!!.showSignupValidateErrorMsg("1")
            return false
        }
        if (!Validation.isValidEmail(input.email!!)) {
            view!!.showSignupValidateErrorMsg("2")
            return false
        }
        if (!Validation.isValidMobileNumber(input.mobile)) {
            view!!.showSignupValidateErrorMsg("3")
            return false
        }
        return true
    }

    override fun doLogin(emailormobile: String) {
        iResponseInterface.callApi(MyApplication.service.doLogin(ApiRequestParam.login(emailormobile)), ApiType.LOGIN)
    }

    override fun callRegister(input: SignUpInput) {
        iResponseInterface.callApi(MyApplication.service.doRegister(input), ApiType.REGISTER)
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful) {
            when (reqType) {
                ApiType.LOGIN ->
                    view?.setLoginResp(response.body() as CustomerRes)

                ApiType.REGISTER ->
                    view?.setRegsiterRes(response.body() as CustomerRes)
            }
        }
    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        when (reqType) {
            ApiType.LOGIN ->
                view?.showMsg(responseError.message)

            ApiType.REGISTER ->
                view?.showMsg(responseError.message)
        }
    }
}
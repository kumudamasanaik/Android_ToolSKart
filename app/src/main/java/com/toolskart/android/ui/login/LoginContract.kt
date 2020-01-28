package com.toolskart.android.ui.login

import com.toolskart.android.model.CustomerRes
import com.toolskart.android.model.inputmodel.SignUpInput
import com.toolskart.android.ui.BasePresenter
import com.toolskart.android.ui.BaseView

interface LoginContract {
    interface View : BaseView {
        /*sign_in*/
        fun doLogin()
        fun invalidEmailPhone()
        fun setLoginResp(res: CustomerRes)

        /*sign_up*/
        fun doRegister()
        fun setRegsiterRes(res: CustomerRes)
        fun showSignupValidateErrorMsg(msg: String)

    }

    interface Presenter : BasePresenter<View> {
        /*sign_in*/
        fun validateLogin(emailormobile: String?): Boolean
        fun doLogin(emailormobile: String)

        /*sign_up*/
        fun validate(input: SignUpInput): Boolean
        fun callRegister(input: SignUpInput)
    }
}
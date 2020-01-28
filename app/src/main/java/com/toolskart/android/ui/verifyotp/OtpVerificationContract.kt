package com.toolskart.android.ui.verifyotp

import com.toolskart.android.model.CommonRes
import com.toolskart.android.model.CustomerRes
import com.toolskart.android.ui.BasePresenter
import com.toolskart.android.ui.BaseView

interface OtpVerificationContract {

    interface View : BaseView {
        fun verifyOtp()
        fun setOtpRes(res: CustomerRes)
        fun showResendOtpRes(res: CommonRes)
    }

    interface Presenter : BasePresenter<View> {
        fun verifyOtp(otp: String,mobile:String)
        fun resendOtp(mobile:String)
    }
}
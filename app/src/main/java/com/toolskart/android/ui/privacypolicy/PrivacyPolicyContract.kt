package com.toolskart.android.ui.privacypolicy

import com.toolskart.android.model.PrivacyPolicyRes
import com.toolskart.android.ui.BasePresenter
import com.toolskart.android.ui.BaseView

interface PrivacyPolicyContract {
    interface View : BaseView {
        fun callPrivacyPolicyScreenApi()
        fun setPrivacyPolicyApiRes(response: PrivacyPolicyRes)
    }

    interface Presenter : BasePresenter<View> {
        fun callPrivacyPolicyAPi()
    }
}
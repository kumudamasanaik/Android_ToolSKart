package com.toolskart.android.ui.notification

import com.toolskart.android.model.CustomerRes
import com.toolskart.android.ui.BasePresenter
import com.toolskart.android.ui.BaseView

interface NotificationContract {
    interface View : BaseView {
        fun callNotification()
        fun setNotificationResp(res: CustomerRes)
    }

    interface Presenter : BasePresenter<View> {
        fun callNotificationApi()
    }
}
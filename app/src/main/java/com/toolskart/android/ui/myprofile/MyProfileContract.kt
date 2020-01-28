package com.toolskart.android.ui.myprofile

import com.toolskart.android.model.MyProfileRes
import com.toolskart.android.model.UpdateProfileRes
import com.toolskart.android.ui.BasePresenter
import com.toolskart.android.ui.BaseView

interface MyProfileContract {
    interface View : BaseView {
        fun callMyProfile()
        fun updateMyProfile()
        fun setMyProfileResp(response: MyProfileRes)
        fun setUpdateMyProfileResp(updatedMyProfileRes: UpdateProfileRes)
    }

    interface Presenter : BasePresenter<View> {
        fun callMyProfileApi()
        fun callUpdateMyProfileApi(address:String,pic:String)
    }
}
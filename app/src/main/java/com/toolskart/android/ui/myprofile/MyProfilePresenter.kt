package com.toolskart.android.ui.myprofile

import com.toolskart.android.api.ApiRequestParam
import com.toolskart.android.api.ApiResponsePresenter
import com.toolskart.android.api.ApiType
import com.toolskart.android.api.IResponseInterface
import com.toolskart.android.model.MyProfileRes
import com.toolskart.android.model.UpdateProfileRes
import com.toolskart.android.utils.MyApplication
import com.toolskart.android.utils.Validation
import retrofit2.Call
import retrofit2.Response

class MyProfilePresenter(view: MyProfileContract.View) : MyProfileContract.Presenter, IResponseInterface {

    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    private var view: MyProfileContract.View? = view

    override fun callMyProfileApi() {
        iResponseInterface.callApi(MyApplication.service.getMyProfile(ApiRequestParam.myProfileRequest()), ApiType.MY_PROFILE)
    }

    override fun callUpdateMyProfileApi(address:String,pic:String) {
        iResponseInterface.callApi(MyApplication.service.getUpdatedProfile(ApiRequestParam.updatedProfile(address,pic)), ApiType.UPDATE_MY_PROFILE)
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful && Validation.isValidObject(response)) {
            when (reqType) {
                ApiType.MY_PROFILE -> {
                    view?.setMyProfileResp(response.body() as MyProfileRes)
                }
                ApiType.UPDATE_MY_PROFILE -> {
                    view?.setUpdateMyProfileResp(response.body() as UpdateProfileRes)
                }
            }
        }
    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        when (reqType) {
            ApiType.MY_PROFILE -> {
                view?.showMsg(responseError.message)
            }
            ApiType.UPDATE_MY_PROFILE -> {
                view?.showMsg(responseError.message)
            }
        }
    }
}
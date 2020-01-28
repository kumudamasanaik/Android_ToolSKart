package com.toolskart.android.ui.shopbycategory

import com.toolskart.android.api.ApiResponsePresenter
import com.toolskart.android.api.ApiType
import com.toolskart.android.api.IResponseInterface
import com.toolskart.android.model.PrivacyPolicyRes
import com.toolskart.android.model.ShopByCategoryRes
import com.toolskart.android.model.test.Test
import com.toolskart.android.utils.MyApplication
import retrofit2.Call
import retrofit2.Response

class ShopByCategoryPresenter(val view: ShopByCategoryContract.View) : ShopByCategoryContract.Presenter, IResponseInterface {
    val iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)

    override fun callShopByCategoryApi() {
        iResponseInterface.callApi(MyApplication.service.getShopByCategory(), ApiType.SHOP_BY_CATEGORY)
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view.hideLoader()
        when (reqType) {
            ApiType.SHOP_BY_CATEGORY -> {
                view.setShopByCategoryApiRes(response.body() as Test)
            }
        }
    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view.hideLoader()
        when (reqType) {
            ApiType.SHOP_BY_CATEGORY -> {
                view.showMsg(responseError.message)
            }
        }
    }
}
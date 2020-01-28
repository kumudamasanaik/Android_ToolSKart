package com.toolskart.android.ui.viewallproduct

import com.toolskart.android.api.ApiResponsePresenter
import com.toolskart.android.api.ApiType
import com.toolskart.android.api.IResponseInterface
import com.toolskart.android.customview.ScreenStateView
import com.toolskart.android.model.FeaturedProductListRes
import com.toolskart.android.utils.MyApplication
import retrofit2.Call
import retrofit2.Response

class ViewAllProductPresenter(val view: ViewAllProductContract.view) : ViewAllProductContract.presenter, IResponseInterface {
    val iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)

    override fun callProductListApi() {
        iResponseInterface.callApi(MyApplication.service.getProductList(), ApiType.FEATURED_PRODUCT_LIST)
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view.hideLoader()
        if (response.isSuccessful) {
            when (reqType) {
                ApiType.FEATURED_PRODUCT_LIST ->
                    view.setProductListApiRes(response.body() as FeaturedProductListRes)
            }
        } else view.showViewState(ScreenStateView.VIEW_STATE_ERROR)
    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view.hideLoader()
        when (reqType) {
            ApiType.FEATURED_PRODUCT_LIST ->
                view.showMsg(responseError.message)
        }
    }
}
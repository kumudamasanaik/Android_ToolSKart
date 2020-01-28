package com.toolskart.android.ui.productlist.fragment

import com.toolskart.android.api.ApiRequestParam
import com.toolskart.android.api.ApiResponsePresenter
import com.toolskart.android.api.ApiType
import com.toolskart.android.api.IResponseInterface
import com.toolskart.android.customview.ScreenStateView
import com.toolskart.android.model.ProductListResponse
import com.toolskart.android.utils.MyApplication
import com.toolskart.android.utils.Validation
import retrofit2.Call
import retrofit2.Response

class ProductListFragmentPresenter(var view: ProductListFragmentContract.View?) : ProductListFragmentContract.Presenter, IResponseInterface {
    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)

    override fun callProductListApi(lowCatId: String) {
        iResponseInterface.callApi(MyApplication.service.getChildCategoryForViewPageHeaders(ApiRequestParam.getProductListParameters(lowCatId)), ApiType.PRODUCT_LIST)
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful && Validation.isValidObject(response)) {
            when (reqType) {
                ApiType.PRODUCT_LIST ->
                    view!!.setProductListApiRes(response.body() as ProductListResponse)
            }
        }
        else
            view!!.showViewState(ScreenStateView.VIEW_STATE_EMPTY)
    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        view?.showMsg(responseError.message)
        when (reqType) {
            ApiType.PRODUCT_LIST -> {
                view!!.showViewState(ScreenStateView.VIEW_STATE_ERROR)
            }
        }
    }
}
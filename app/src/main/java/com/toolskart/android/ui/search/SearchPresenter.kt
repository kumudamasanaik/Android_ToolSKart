package com.toolskart.android.ui.search

import com.toolskart.android.api.ApiResponsePresenter
import com.toolskart.android.api.ApiType
import com.toolskart.android.api.IResponseInterface
import com.toolskart.android.model.inputmodel.SearchInput
import com.toolskart.android.model.SearchProductsRes
import com.toolskart.android.utils.MyApplication
import retrofit2.Call
import retrofit2.Response

class SearchPresenter(view: SearchContract.View) : SearchContract.Presenter, IResponseInterface {
    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: SearchContract.View? = view

    override fun callSearch(input: SearchInput) {
        iResponseInterface.callApi(MyApplication.service.getSearchResult(input), ApiType.SEARCH_PRODUCTS)
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful) {
            when (reqType) {
                ApiType.SEARCH_PRODUCTS ->
                    view?.setSearchProductsRes(response.body() as SearchProductsRes)
            }
        }
    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        when (reqType) {
            ApiType.SEARCH_PRODUCTS ->
                view?.showMsg(responseError.message)
        }
    }
}
package com.toolskart.android.ui.wishlist

import com.toolskart.android.api.ApiRequestParam
import com.toolskart.android.api.ApiResponsePresenter
import com.toolskart.android.api.ApiType
import com.toolskart.android.api.IResponseInterface
import com.toolskart.android.customview.ScreenStateView
import com.toolskart.android.model.CartRes
import com.toolskart.android.model.WishListResp
import com.toolskart.android.model.inputmodel.ModifyCartIp
import com.toolskart.android.utils.MyApplication
import com.toolskart.android.utils.Validation
import retrofit2.Call
import retrofit2.Response

class WishListPresenter(view: WishListContract.View?) : WishListContract.Presenter, IResponseInterface {

    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: WishListContract.View? = view

    override fun callWishListApi(op: String) {
        iResponseInterface.callApi(MyApplication.service.getWishList(ApiRequestParam.getWishListParameters(op)), ApiType.GET_WISHLIST)
    }

    override fun modifyCart(modifyCartIn: ModifyCartIp) {
        iResponseInterface.callApi(MyApplication.service.getModifyCart(modifyCartIn), ApiType.MODIFY_CART)
    }

    override fun callModifyWishList(op: String, productId: String) {
        iResponseInterface.callApi(MyApplication.service.getWishList(ApiRequestParam.getWishListParameters(op, productId)), ApiType.MODIFY_WISH_LIST)
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful && Validation.isValidObject(response)) {
            when (reqType) {
                ApiType.GET_WISHLIST ->
                    view!!.setWishListApiRes(response.body() as WishListResp)

                ApiType.MODIFY_CART ->
                    view!!.modifycartApiRes(response.body() as CartRes)

                ApiType.MODIFY_WISH_LIST -> {
                     view!!.setWishListModifiedRes(response.body() as WishListResp)
                }
            }
        } else
            view!!.showViewState(ScreenStateView.VIEW_STATE_EMPTY)
    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        view?.showMsg(responseError.message)
        when (reqType) {
            ApiType.GET_WISHLIST -> {
                view!!.showViewState(ScreenStateView.VIEW_STATE_ERROR)
            }

            ApiType.MY_WISH_LIST -> {
                view!!.showMsg(null)
            }

            ApiType.MODIFY_CART -> {
                view!!.showMsg(null)
            }
        }
    }
}
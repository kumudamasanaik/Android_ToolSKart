package com.toolskart.android.ui.mycart

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

class CartPresenter(view: CartContract.View) : CartContract.Presenter, IResponseInterface {
    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: CartContract.View? = view

    override fun callCartApi(op: String) {
        iResponseInterface.callApi(MyApplication.service.getCart(ApiRequestParam.getCartParameters(op)), ApiType.CART)
    }

    override fun modifyCart(modifyCartIn: ModifyCartIp) {
        iResponseInterface.callApi(MyApplication.service.getModifyCart(modifyCartIn), ApiType.MODIFY_CART)
    }

    override fun modifyWishList(op: String, productId: String) {
        iResponseInterface.callApi(MyApplication.service.getWishList(ApiRequestParam.getWishListParameters(op, productId)), ApiType.CTEATE_WISH_LIST)
    }

    override fun callRemoveCartApi(modifyCartIn: ModifyCartIp) {
        iResponseInterface.callApi(MyApplication.service.removeCart(modifyCartIn), ApiType.REMOVE_CART)
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful && Validation.isValidObject(response)) {
            when (reqType) {
                ApiType.CART ->
                    view!!.setCartApiResp(response.body() as CartRes)

                ApiType.CTEATE_WISH_LIST ->
                    view!!.setWishListApiRes(response.body() as WishListResp)

                ApiType.MODIFY_CART ->
                    view!!.modifycartApiRes(response.body() as CartRes)

                ApiType.REMOVE_CART ->
                    view!!.setRemoveCartApiRes(response.body() as CartRes)
            }
        } else
            view!!.showViewState(ScreenStateView.VIEW_STATE_EMPTY)
    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        view?.showMsg(responseError.message)
        when (reqType) {
            ApiType.CART -> {
                view!!.showViewState(ScreenStateView.VIEW_STATE_ERROR)
            }
            ApiType.GET_WISHLIST -> {
                view!!.showMsg(null)
            }
            ApiType.MODIFY_CART -> {
                view!!.showMsg(null)
            }
            ApiType.REMOVE_CART -> {
                view!!.showMsg(null)
            }
        }
    }
}
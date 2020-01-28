package com.toolskart.android.ui.productdetails

import com.toolskart.android.api.ApiRequestParam
import com.toolskart.android.api.ApiResponsePresenter
import com.toolskart.android.api.ApiType
import com.toolskart.android.api.IResponseInterface
import com.toolskart.android.model.CartRes
import com.toolskart.android.model.ProductDetailResp
import com.toolskart.android.model.WishListResp
import com.toolskart.android.model.inputmodel.ModifyCartIp
import com.toolskart.android.utils.MyApplication
import com.toolskart.android.utils.Validation
import retrofit2.Call
import retrofit2.Response

class ProductDetailsPresenter(view: ProductDetailsContract.View) : ProductDetailsContract.Presenter, IResponseInterface {


    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: ProductDetailsContract.View? = view

    override fun callProductDetailApi(product_id: String) {
        iResponseInterface.callApi(MyApplication.service.getProductDetail(ApiRequestParam.getProductDetailParameters(product_id)), ApiType.PRODUCT_DETAIL)
    }

    override fun callCreateWishList(op: String, productId: String) {
        iResponseInterface.callApi(MyApplication.service.getModifyWishList(ApiRequestParam.getWishListParameters(op, productId)), ApiType.CTEATE_WISH_LIST)
    }

    override fun callDeleteWishList(op: String, productId: String) {
        iResponseInterface.callApi(MyApplication.service.getModifyWishList(ApiRequestParam.getWishListParameters(op, productId)), ApiType.DELETE_WISH_LIST)
    }

    override fun callAddToCart(modifyCartIp: ModifyCartIp) {
        iResponseInterface.callApi(MyApplication.service.getModifyCart(modifyCartIp), ApiType.MODIFY_CART)
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful && Validation.isValidObject(response)) {
            when (reqType) {
                ApiType.PRODUCT_DETAIL ->
                    view?.setProductDetailRes(response.body() as ProductDetailResp)

                ApiType.CTEATE_WISH_LIST ->
                    view?.setCreateWishListResp(response.body() as WishListResp)

                ApiType.DELETE_WISH_LIST ->
                    view?.setDeleteWishListResp(response.body() as WishListResp)

                ApiType.MODIFY_CART ->
                    view?.setAddTOCartResp(response.body() as CartRes)

            }
        }
    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        when (reqType) {
            ApiType.PRODUCT_DETAIL ->
                view?.showMsg(responseError.message)
        }
    }
}
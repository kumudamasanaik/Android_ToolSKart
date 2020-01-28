package com.toolskart.android.ui.productdetails

import com.toolskart.android.model.CartRes
import com.toolskart.android.model.ProductDetailResp
import com.toolskart.android.model.WishListResp
import com.toolskart.android.model.inputmodel.ModifyCartIp
import com.toolskart.android.ui.BasePresenter
import com.toolskart.android.ui.BaseView

interface ProductDetailsContract {
    interface View : BaseView {
        fun callProductDetailScreenApi()
        fun setProductDetailRes(responce: ProductDetailResp)

        fun setCreateWishListResp(responce: WishListResp)
        fun setDeleteWishListResp(responce: WishListResp)

        fun setAddTOCartResp(res: CartRes)
    }

    interface Presenter : BasePresenter<View> {
        fun callProductDetailApi(product_id:String)

        fun callCreateWishList(op:String,productId:String)
        fun callDeleteWishList(op:String,productId:String)

        fun callAddToCart(modifyCartIp: ModifyCartIp)
    }
}
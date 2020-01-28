package com.toolskart.android.ui.mycart

import com.toolskart.android.model.CartRes
import com.toolskart.android.model.WishListResp
import com.toolskart.android.model.inputmodel.ModifyCartIp
import com.toolskart.android.ui.BasePresenter
import com.toolskart.android.ui.BaseView

interface CartContract {
    interface View : BaseView {
        fun callCartApi()
        fun setCartApiResp(cartres: CartRes)
        fun setRemoveCartApiRes(res: CartRes?)

        fun modifycartApiRes(cartres: CartRes?)
        fun setWishListApiRes(wishlistRes: WishListResp)
    }

    interface Presenter : BasePresenter<View> {
        fun callCartApi(op: String)
        fun modifyCart(modifyCartIn: ModifyCartIp)
        fun modifyWishList(op: String, productId: String)
        fun callRemoveCartApi(modifyCartIn: ModifyCartIp)
    }
}
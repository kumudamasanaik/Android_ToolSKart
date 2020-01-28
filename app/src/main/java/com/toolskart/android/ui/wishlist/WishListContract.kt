package com.toolskart.android.ui.wishlist

import com.toolskart.android.model.CartRes
import com.toolskart.android.model.WishListResp
import com.toolskart.android.model.inputmodel.ModifyCartIp
import com.toolskart.android.ui.BasePresenter
import com.toolskart.android.ui.BaseView

interface WishListContract {
    interface View : BaseView {
        fun callWishListApi()
        fun setWishListApiRes(res: WishListResp)
        fun modifycartApiRes(cartRes: CartRes?)
        fun setWishListModifiedRes(ModifiedWishListRes: WishListResp)
    }

    interface Presenter : BasePresenter<View> {
        fun callWishListApi(op: String)
        fun callModifyWishList(op:String,productId:String)
        fun modifyCart(modifyCartIn: ModifyCartIp)
    }
}
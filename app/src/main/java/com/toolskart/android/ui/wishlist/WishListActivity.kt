package com.toolskart.android.ui.wishlist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import com.toolskart.android.R
import com.toolskart.android.api.LiveNetworkMonitor
import com.toolskart.android.constants.Constants
import com.toolskart.android.customview.ScreenStateView
import com.toolskart.android.interfaces.IAdapterClickListener
import com.toolskart.android.model.CartRes
import com.toolskart.android.model.Product
import com.toolskart.android.model.WishListResp
import com.toolskart.android.ui.base.BaseActivity
import com.toolskart.android.ui.commonadapter.WishListAdapter
import com.toolskart.android.ui.mycart.MyCartActivity
import com.toolskart.android.utils.AppSharedPreference
import com.toolskart.android.utils.CommonUtils
import com.toolskart.android.utils.Validation
import com.toolskart.android.utils.showToastMsg
import kotlinx.android.synthetic.main.activity_wish_list.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*

class WishListActivity : BaseActivity(), View.OnClickListener, IAdapterClickListener, WishListContract.View {

    private lateinit var wishListAdapter: WishListAdapter
    private var mContext: Context? = null
    private val TAG = "WishListActivity"
    private lateinit var source: String
    private lateinit var snackbbar: View
    private lateinit var mWishlistRes: WishListResp
    private lateinit var presenter: WishListPresenter
    private var modifiedProd: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_wish_list, fragmentLayout)
        mContext = this
        init()
    }

    override fun init() {
        setUpRecyClerViewData()
        tvTitle?.text = getString(R.string.wish_list)
        snackbbar = this.wish_list_snack_bar
        presenter = WishListPresenter(this)
        empty_button.setOnClickListener { callWishListApi() }
        error_button.setOnClickListener { callWishListApi() }
        iv_cart!!.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        callWishListApi()
    }

    override fun callWishListApi() {
        if (LiveNetworkMonitor(this).isConnected()) {
            showViewState(ScreenStateView.VIEW_STATE_LOADING)
            presenter.callWishListApi(Constants.GET)
        } else {
            showMsg(getString(R.string.error_no_internet))
            showViewState(ScreenStateView.VIEW_STATE_ERROR)
        }
    }

    override fun setWishListApiRes(res: WishListResp) {
        if (Validation.isValidObject(res)) {
            showViewState(ScreenStateView.VIEW_STATE_CONTENT)
            mWishlistRes = res
            setData()
        } else
            showViewState(ScreenStateView.VIEW_STATE_ERROR)
    }

    private fun setData() {
        mWishlistRes.result.apply {
            if (mWishlistRes.result!!.isNotEmpty()) {
                wishListAdapter.addList(mWishlistRes.result!!)
            } else
                showViewState(ScreenStateView.VIEW_STATE_EMPTY)
        }
    }

    private fun setUpRecyClerViewData() {
        val linearLayout = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        wishListAdapter = WishListAdapter(context = this, adapterType = Constants.TYPE_WISH_LIST_ADAPTER, adapterClickListener = this)
        rv_wish_list.apply {
            layoutManager = linearLayout
            adapter = wishListAdapter
        }
    }

    override fun modifycartApiRes(cartRes: CartRes?) {
        hideLoader()
        if (Validation.isValidStatus(cartRes)) {
            AppSharedPreference.saveCartData(cartRes!!.summary)
            wishListAdapter.showModifiedRes(Constants.RES_SUCCESS)
        } else
            wishListAdapter.showModifiedRes(Constants.RES_FAILED)
    }

    override fun setWishListModifiedRes(ModifiedWishListRes: WishListResp) {
        hideLoader()
        if (Validation.isValidStatus(ModifiedWishListRes)) {
            AppSharedPreference.saveCartData(ModifiedWishListRes.summary)
            callWishListApi()
        }
    }

    override fun showMsg(msg: String?) {
        showToastMsg(msg!!)
    }

    override fun showLoader() {
        CommonUtils.showLoading(this, "", true)
    }

    override fun hideLoader() {
        CommonUtils.hideLoading()
    }

    override fun showViewState(state: Int) {
        if (wishList_multiStateView != null)
            wishList_multiStateView.viewState = state
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_cart -> {
                startActivity(Intent(this, MyCartActivity::class.java))
            }
        }
    }

    override fun onclick(data: Any, pos: Int, type: String, op: String) {
        modifiedProd = data as Product
        if (data._id != null) {
            when (op) {
                Constants.WISHLIST -> {
                    if (modifiedProd!!.isWishListEnabled()) {
                        if (LiveNetworkMonitor(this).isConnected()) {
                            showLoader()
                            presenter.callModifyWishList(Constants.DELETE, data._id)
                        } else
                            showMsg(getString(R.string.error_no_internet))
                    }
                }

                Constants.OP_MODIFY_CART -> {
                    if (LiveNetworkMonitor(this).isConnected()) {
                        showLoader()
                        presenter.modifyCart(data.modifyCartIp!!)
                    } else {
                        showMsg(getString(R.string.error_no_internet))
                    }
                }
            }
        }
    }
}
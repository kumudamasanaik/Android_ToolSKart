package com.toolskart.android.ui.mycart

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
import com.toolskart.android.ui.payment.PaymentActivity
import com.toolskart.android.utils.AppSharedPreference
import com.toolskart.android.utils.CommonUtils
import com.toolskart.android.utils.Validation
import com.toolskart.android.utils.showToastMsg
import kotlinx.android.synthetic.main.activity_my_cart.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*

class MyCartActivity : BaseActivity(), IAdapterClickListener, CartContract.View, View.OnClickListener {

    private val TAG = "CartActivity"
    private lateinit var source: String
    private lateinit var snackbbar: View
    private lateinit var presenter: CartPresenter
    private lateinit var cartList: CartRes
    private lateinit var cartAdapter: WishListAdapter
    private var mContext: Context? = null
    private var modifiedProd: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_my_cart, fragmentLayout)
        mContext = this
        init()
    }

    override fun init() {
        snackbbar = cart_snackbar
        tvTitle?.text = getString(R.string.my_cart)
        empty_button.setOnClickListener { callCartApi() }
        error_button.setOnClickListener { callCartApi() }
        btn_checkout.setOnClickListener(this)
        presenter = CartPresenter(this)
        hideCartView()
        setUpRecyclerViewData()
        setToolbarScrollFlags(false)
        callCartApi()
    }

    private fun setUpRecyclerViewData() {
        val linearLayout = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        cartAdapter = WishListAdapter(context = this, adapterType = Constants.TYPE_CART_ADAPTER, adapterClickListener = this)
        rv_cart.apply {
            layoutManager = linearLayout
            adapter = cartAdapter
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_checkout -> {
                startActivity(Intent(this, PaymentActivity::class.java))
            }
        }
    }

    override fun callCartApi() {
        if (LiveNetworkMonitor(this).isConnected()) {
            showViewState(ScreenStateView.VIEW_STATE_LOADING)
            presenter.callCartApi(Constants.GET)
        } else {
            showMsg(getString(R.string.error_no_internet))
            showViewState(ScreenStateView.VIEW_STATE_ERROR)
        }
    }

    override fun setCartApiResp(cartres: CartRes) {
        if (Validation.isValidObject(cartres)) {
            showViewState(ScreenStateView.VIEW_STATE_CONTENT)
            cartList = cartres
            setData()
        } else
            showViewState(ScreenStateView.VIEW_STATE_ERROR)
    }

    private fun setData() {
        cartList.cart?.apply {
            if (cartList.cart!!.isNotEmpty()) {
                cartAdapter.addList(cartList.cart!!)
            } else
                showViewState(ScreenStateView.VIEW_STATE_EMPTY)
        }
        val cart_summary = cartList.summary
        cart_summary.apply {
            AppSharedPreference.saveCartData(cart_summary)
        }
        price.text = CommonUtils.getRupeesSymbol(this, cart_summary!!.selling_price!!)
        tv_no_of_items.text = "(" + cart_summary.cart_count + ")"
    }

    override fun modifycartApiRes(cartRes: CartRes?) {
        hideLoader()
        if (Validation.isValidStatus(cartRes)) {
            AppSharedPreference.saveCartData(cartRes!!.summary)
            cartAdapter.showModifiedRes(Constants.RES_SUCCESS)
        } else
            cartAdapter.showModifiedRes(Constants.RES_FAILED)
    }

    override fun setRemoveCartApiRes(res: CartRes?) {
        hideLoader()
        if (Validation.isValidStatus(res)) {
            AppSharedPreference.saveCartData(res!!.summary)
            callCartApi()
        }
    }

    override fun setWishListApiRes(wishlistRes: WishListResp) {
        hideLoader()
        if (Validation.isValidStatus(wishlistRes)) {
            cartAdapter.showModifiedWishListRes(Constants.RES_SUCCESS)
        } else
            cartAdapter.showModifiedWishListRes(Constants.RES_FAILED)
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
        if (cart_multiStateView != null)
            cart_multiStateView.viewState = state
    }

    override fun onclick(data: Any, pos: Int, type: String, op: String) {
        modifiedProd = data as Product
        if (data.selSku?._id != null) {
            when (op) {
                Constants.OP_REMOVE_CART -> {
                    if (LiveNetworkMonitor(this).isConnected()) {
                        showLoader()
                        presenter.callRemoveCartApi(data.modifyCartIp!!)
                    } else
                        showMsg(getString(R.string.error_no_internet))
                }

                Constants.OP_MODIFY_CART -> {
                    if (LiveNetworkMonitor(this).isConnected()) {
                        showLoader()
                        presenter.modifyCart(data.modifyCartIp!!)
                    } else {
                        showMsg(getString(R.string.error_no_internet))
                    }
                }

                Constants.WISHLIST -> {
                    if (LiveNetworkMonitor(this).isConnected()) {
                        showLoader()
                        presenter.modifyWishList(if (modifiedProd?.wishlist == 0) Constants.CREATE else Constants.DELETE, data.selSku!!.id_product!!)
                    } else
                        showMsg(getString(R.string.error_no_internet))
                }
            }
        }
    }
}
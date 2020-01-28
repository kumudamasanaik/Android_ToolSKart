package com.toolskart.android.ui.viewallproduct

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.MenuItem
import android.view.View
import com.toolskart.android.R
import com.toolskart.android.api.LiveNetworkMonitor
import com.toolskart.android.constants.Constants
import com.toolskart.android.customview.ScreenStateView
import com.toolskart.android.interfaces.IAdapterClickListener
import com.toolskart.android.model.FeaturedProductListRes
import com.toolskart.android.model.Product
import com.toolskart.android.ui.base.BaseActivity
import com.toolskart.android.ui.base.adapter.BaseRecAdapter
import com.toolskart.android.ui.mycart.MyCartActivity
import com.toolskart.android.ui.productdetails.ProductDetailsActivity
import com.toolskart.android.utils.CommonUtils
import com.toolskart.android.utils.Validation
import com.toolskart.android.utils.showToastMsg
import com.toolskart.android.utils.strikeThr
import kotlinx.android.synthetic.main.activity_view_all_product.*
import kotlinx.android.synthetic.main.item_offer_details.*

class ViewAllProductActivity : BaseActivity(), ViewAllProductContract.view, IAdapterClickListener, View.OnClickListener {


    private var mContext: Context? = null
    private lateinit var productDetailAdapter: BaseRecAdapter
    private lateinit var source: String
    private lateinit var presenter: ViewAllProductPresenter
    private lateinit var mResult: FeaturedProductListRes
    private var modifiedProd: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_view_all_product, fragmentLayout)

        mContext = this
        if (intent != null) {
            source = intent.getStringExtra(Constants.SOURCE)
        }
        init()
    }

    override fun init() {
        presenter = ViewAllProductPresenter(this)
        updateUI()
        iv_cart!!.setOnClickListener(this)
        showBack()
        setUpViewAllProductDetails()
    }

    private fun updateUI() {
        source.apply {
            if (source.contentEquals(Constants.FEATURED_PRODUCTS)) {
                tvTitle?.text = getString(R.string.featured_products)
                callProductList()
            } else {
                tvTitle?.text = getString(R.string.offer_and_deals)
            }
        }
    }

    private fun setUpViewAllProductDetails() {
        productDetailAdapter = BaseRecAdapter(mContext!!, R.layout.item_offer_details, adapterClickListener = this, requestType = Constants.FEATURED_PRODUCTS)
        rv_view_all_products.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        rv_view_all_products.adapter = productDetailAdapter
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_cart -> {
                navigateToCartActivity()
            }
        }
    }

    override fun callProductList() {
        if (LiveNetworkMonitor(this).isConnected()) {
            showViewState(ScreenStateView.VIEW_STATE_LOADING)
            presenter.callProductListApi()
        } else {
            showMsg(getString(R.string.error_no_internet))
            showViewState(ScreenStateView.VIEW_STATE_ERROR)
        }
    }

    override fun setProductListApiRes(res: FeaturedProductListRes) {
        if (Validation.isValidObject(res)) {
            showViewState(ScreenStateView.VIEW_STATE_CONTENT)
            mResult = res
            setdata()
        } else
            showViewState(ScreenStateView.VIEW_STATE_ERROR)
    }

    private fun setdata() {
        mResult.product?.apply {
            if (mResult.product!!.isNotEmpty()) {
                productDetailAdapter.addList(mResult.product!!)
            } else
                showViewState(ScreenStateView.VIEW_STATE_EMPTY)
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

    }

    private fun navigateToCartActivity() {
        startActivity(Intent(this, MyCartActivity::class.java))
    }

    override fun onclick(data: Any, pos: Int, type: String, op: String) {
        if (data is Product) {
            modifiedProd = data
            when (type) {
                Constants.FEATURED_PRODUCTS -> {
                    if (LiveNetworkMonitor(this).isConnected()) {
                        val intent = Intent(mContext, ProductDetailsActivity::class.java)
                        intent.putExtra(Constants.SOURCE, Constants.PRODUCT_LIST_FRAGMENT)
                        intent.putExtra(Constants.PRODUCT_LIST, data._id)
                        startActivity(intent)
                    } else
                        showMsg(getString(R.string.error_no_internet))
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

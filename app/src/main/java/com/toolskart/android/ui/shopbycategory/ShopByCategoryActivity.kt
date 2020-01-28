package com.toolskart.android.ui.shopbycategory

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.widget.LinearLayout
import com.toolskart.android.R
import com.toolskart.android.api.LiveNetworkMonitor
import com.toolskart.android.customview.ScreenStateView
import com.toolskart.android.interfaces.IAdapterClickListener
import com.toolskart.android.model.ShopByCategoryRes
import com.toolskart.android.model.test.Test
import com.toolskart.android.ui.base.BaseActivity
import com.toolskart.android.ui.base.adapter.BaseRecAdapter
import com.toolskart.android.utils.CommonUtils
import com.toolskart.android.utils.Validation
import com.toolskart.android.utils.showToastMsg
import kotlinx.android.synthetic.main.activity_shop_by_category.*

class ShopByCategoryActivity : BaseActivity(), ShopByCategoryContract.View, IAdapterClickListener {
   // private lateinit var shopbycategoryAdapter: BaseRecAdapter
    private var mContext: Context? = null
    private lateinit var presenter: ShopByCategoryPresenter
    private lateinit var mResult: Test
    private lateinit var shopbycategoryAdapter: BaseRecAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_shop_by_category, fragmentLayout)
        mContext = this
        init()
    }

    override fun init() {
        presenter = ShopByCategoryPresenter(this)
        tvTitle?.text = getString(R.string.shop_by_category)
        hideCartView()
        // setUpRecyclerViewData()
        callShopByCategory()
        setUpCategories()
    }

    private fun setUpCategories() {
        rv_shop_by_category.layoutManager = LinearLayoutManager(this)
        rv_shop_by_category!!.setHasFixedSize(true)
        shopbycategoryAdapter = BaseRecAdapter(this, R.layout.category_item, adapterClickListener = this)
        rv_shop_by_category.adapter = shopbycategoryAdapter
    }

    private fun setUpRecyclerViewData() {
        val linearLayout = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        shopbycategoryAdapter = BaseRecAdapter(mContext!!, R.layout.items_shop_by_category, adapterClickListener = this)
        rv_shop_by_category.apply {
            layoutManager = linearLayout
            adapter = shopbycategoryAdapter
        }
    }

    override fun callShopByCategory() {
        if (LiveNetworkMonitor(this).isConnected()) {
            showViewState(ScreenStateView.VIEW_STATE_LOADING)
            presenter.callShopByCategoryApi()
        } else {
            showMsg(getString(R.string.error_no_internet))
            showViewState(ScreenStateView.VIEW_STATE_ERROR)
        }
    }

    override fun setShopByCategoryApiRes(response: Test) {
        if (Validation.isValidObject(response)) {
            showViewState(ScreenStateView.VIEW_STATE_CONTENT)
            mResult = response
            setupUI()
        } else
            showViewState(ScreenStateView.VIEW_STATE_ERROR)
    }

    private fun setupUI() {
        mResult.result?.apply {
            /*if (mResult.result!!.isNotEmpty()) {
                shopbycategoryAdapter.addList(mResult.result!!)
            } else
                showViewState(ScreenStateView.VIEW_STATE_EMPTY)*/
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

    override fun onclick(data: Any, pos: Int, type: String, op: String) {

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
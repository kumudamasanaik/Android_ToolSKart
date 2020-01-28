package com.toolskart.android.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.support.v7.widget.GridLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import com.toolskart.android.R
import com.toolskart.android.api.LiveNetworkMonitor
import com.toolskart.android.constants.Constants
import com.toolskart.android.interfaces.IAdapterClickListener
import com.toolskart.android.model.Product
import com.toolskart.android.model.inputmodel.SearchInput
import com.toolskart.android.model.SearchProductsRes
import com.toolskart.android.ui.base.adapter.BaseRecAdapter
import com.toolskart.android.ui.productdetails.ProductDetailsActivity
import com.toolskart.android.utils.CommonUtils
import com.toolskart.android.utils.Validation
import com.toolskart.android.utils.showToastMsg
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_search.*
import java.util.*

class SearchActivity : DaggerAppCompatActivity(), SearchContract.View, IAdapterClickListener {
    internal lateinit var input: String
    internal lateinit var searchChar: String
    private lateinit var searchListAdapter: BaseRecAdapter
    private var mContext: Context? = null
    lateinit var presenter: SearchPresenter
    private lateinit var searchRes: SearchProductsRes
    private var modifiedProd: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        init()
    }

    override fun init() {
        presenter = SearchPresenter(this)
        mContext = this
        et_text.addTextChangedListener(object : TextWatcher {
            private var timer = Timer()
            private val DELAY: Long = 750 // milliseconds

            override fun beforeTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun onTextChanged(searchChar: CharSequence, start: Int, count: Int, after: Int) {
                timer.cancel()
                timer = Timer()
                timer.schedule(
                        object : TimerTask() {
                            override fun run() {
                                Looper.prepare()
                                if (searchChar.toString().isNotEmpty()) {
                                    this@SearchActivity.searchChar = searchChar.toString()
                                    input = searchChar.toString()
                                    searchProducts()
                                }
                                Looper.loop()
                            }
                        },
                        DELAY
                )
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
    }

    override fun searchProducts() {
        val input = SearchInput(_id = CommonUtils.getCustomerID(), _session = CommonUtils.getSession(),
                lang = "en", search = input, count = 50, start = 0)
        if (LiveNetworkMonitor(this).isConnected()) {
            presenter.callSearch(input)
        } else
            showMsg(getString(R.string.error_no_internet))
    }

    override fun setSearchProductsRes(res: SearchProductsRes) {
        if (Validation.isValidStatus(res)) {
            searchRes = res
            setupListProductsRecyclerView()
        } else
            showMsg("Products are not available")
    }

    private fun setupListProductsRecyclerView() {
        searchListAdapter = BaseRecAdapter(mContext!!, R.layout.product_list_item, adapterClickListener = this, requestType = Constants.SEARCH_PRODUCTS)
        rc_search.layoutManager = GridLayoutManager(mContext, 2)
        rc_search.adapter = searchListAdapter
        if (Validation.isValidList(searchRes.product!!))
            searchListAdapter.addList(searchRes.product!!)
    }

    override fun onclick(data: Any, pos: Int, type: String, op: String) {
        if (data is Product) {
            modifiedProd = data
            when (type) {
                Constants.PRODUCT_LIST -> {
                    if (LiveNetworkMonitor(this).isConnected()) {
                        val intent = Intent(mContext, ProductDetailsActivity::class.java)
                        intent.putExtra(Constants.SOURCE, Constants.SEARCH_PRODUCTS)
                        intent.putExtra(Constants.PRODUCT_LIST, data._id)
                        startActivity(intent)
                    } else
                        showMsg(getString(R.string.error_no_internet))
                }
            }
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
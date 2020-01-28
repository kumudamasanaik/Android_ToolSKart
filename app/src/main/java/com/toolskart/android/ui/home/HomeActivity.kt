package com.toolskart.android.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.toolskart.android.R
import com.toolskart.android.api.LiveNetworkMonitor
import com.toolskart.android.constants.Constants
import com.toolskart.android.customview.MyTimerTask
import com.toolskart.android.customview.ScreenStateView
import com.toolskart.android.interfaces.IAdapterClickListener
import com.toolskart.android.model.FeaturedProduct
import com.toolskart.android.model.Home
import com.toolskart.android.model.HomeResponse
import com.toolskart.android.model.Subcategory
import com.toolskart.android.ui.base.BaseActivity
import com.toolskart.android.ui.base.adapter.BaseRecAdapter
import com.toolskart.android.ui.home.adapter.BannerAdapter
import com.toolskart.android.ui.mycart.MyCartActivity
import com.toolskart.android.ui.productdetails.ProductDetailsActivity
import com.toolskart.android.ui.productlist.ProductListActivity
import com.toolskart.android.ui.search.SearchActivity
import com.toolskart.android.ui.viewallproduct.ViewAllProductActivity
import com.toolskart.android.utils.AppSharedPreference
import com.toolskart.android.utils.CommonUtils
import com.toolskart.android.utils.Validation
import com.toolskart.android.utils.schedulers.BaseScheduler
import com.toolskart.android.utils.showToastMsg
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.view_pager_banner_layout.*
import java.util.*
import javax.inject.Inject

class HomeActivity : BaseActivity(), IAdapterClickListener, View.OnClickListener, ViewPager.OnPageChangeListener, HomeContract.View {

    @Inject
    lateinit var baseScheduler: BaseScheduler
    lateinit var mainBannerAdapter: BannerAdapter
    private lateinit var featuredAdapter: BaseRecAdapter
    private lateinit var offerdealsAdapter: BaseRecAdapter
    private lateinit var hotCategoryAdapter: BaseRecAdapter
    var dummybanner: ArrayList<String>? = ArrayList()
    private var mainBannerCurrentPage = 0
    private var handler: Handler? = null
    private lateinit var snackbbar: View
    private lateinit var homePresenter: HomePresenter
    private lateinit var home: Home
    private var mContext: Context? = null
    private var selPos: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_home, fragmentLayout)
        showMenu()
        mContext = this@HomeActivity

        init()
    }

    override fun init() {
        homePresenter = HomePresenter(this)
        tvTitle?.text = getString(R.string.products)
        snackbbar = snack_barview

        setupBannerViewHolder()
        setupFeaturesProducts()
        setupOfferAndDeals()
        setupHotCategories()

        tv_featured_view_all.setOnClickListener(this)
        tv_offers_deals_view_all.setOnClickListener(this)
        et_search_products.setOnClickListener(this)
        iv_cart!!.setOnClickListener(this)

        empty_button.setOnClickListener { callHomeBoardApi() }
        error_button.setOnClickListener { callHomeBoardApi() }
    }

    override fun onResume() {
        super.onResume()
        callHomeBoardApi()
    }

    private fun setupBannerViewHolder() {
        mainBannerAdapter = BannerAdapter(this, R.layout.item_only_image_banner)
        main_banner_pager.adapter = mainBannerAdapter
        tab_layout.setupWithViewPager(main_banner_pager)
        setupViewPagerAutoRatate()
    }

    private fun setupFeaturesProducts() {
        rv_featured.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_featured.setHasFixedSize(true)
        featuredAdapter = BaseRecAdapter(this, R.layout.item_featured_product, adapterClickListener = this, requestType = Constants.FEATURED_PRODUCTS)
        rv_featured.adapter = featuredAdapter
    }

    private fun setupOfferAndDeals() {
        rv_offers_deals.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_offers_deals.setHasFixedSize(true)
        offerdealsAdapter = BaseRecAdapter(this, R.layout.item_featured_product, adapterClickListener = this, requestType = Constants.OFFER_DEALS)
        rv_offers_deals.adapter = offerdealsAdapter
    }

    private fun setupHotCategories() {
        rv_hot_categories.layoutManager = LinearLayoutManager(this)
        rv_hot_categories!!.setHasFixedSize(true)
        hotCategoryAdapter = BaseRecAdapter(this, R.layout.category_item, adapterClickListener = this)
        rv_hot_categories.adapter = hotCategoryAdapter
    }

    override fun callHomeBoardApi() {
        if (LiveNetworkMonitor(this).isConnected()) {
            showViewState(ScreenStateView.VIEW_STATE_LOADING)
            homePresenter.callDashBoardApi()
        } else {
            showMsg(getString(R.string.error_no_internet))
            showViewState(ScreenStateView.VIEW_STATE_ERROR)
        }
    }

    override fun setHomeApiResp(homeResp: HomeResponse) {
        if (Validation.isValidStatus(homeResp) && Validation.isValidObject(homeResp.result)) {
            home = homeResp.result
            showViewState(ScreenStateView.VIEW_STATE_CONTENT)
            AppSharedPreference.saveCartData(homeResp.summary)
            setData()
        } else
            showViewState(ScreenStateView.VIEW_STATE_ERROR)
    }

    private fun setData() {
        home.category?.apply {
            AppSharedPreference.saveCategoryListData(home.category!!)
        }
        if (home.banner!!.isNotEmpty())
            mainBannerAdapter.addList(home.banner!!)

        if (home.featured_products!!.isNotEmpty())
            featuredAdapter.addList(home.featured_products!!)

        if (home.category!!.isNotEmpty())
            hotCategoryAdapter.addList(home.category!!)

        if (home.offers!!.isNotEmpty())
            offerdealsAdapter.addList(home.offers!!)
    }

    override fun onclick(data: Any, pos: Int, type: String, op: String) {
        when (type) {
            Constants.CATEGORY -> {
                if (data is Subcategory) {
                    val intent = Intent(this, ProductListActivity::class.java)
                    intent.apply {
                        putExtra(Constants.SOURCE, Constants.HOME)
                        putExtra(Constants.LOWCATEGORY, data)
                    }
                    startActivity(intent)
                }
            }
            Constants.FEATURED -> {

                if (data is FeaturedProduct) {
                    val intent = Intent(this, ProductDetailsActivity::class.java)
                    intent.apply {
                        putExtra(Constants.SOURCE, Constants.HOME)
                        putExtra(Constants.FEATURED_PRODUCTS, data._id)
                    }
                    startActivity(intent)
                }
            }
        }
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.tv_featured_view_all -> {
                val intent = Intent(this, ViewAllProductActivity::class.java)
                intent.apply {
                    putExtra(Constants.SOURCE, Constants.FEATURED_PRODUCTS)
                }
                startActivity(intent)
            }

            R.id.tv_offers_deals_view_all -> {
                val intent = Intent(this, ViewAllProductActivity::class.java)
                intent.apply {
                    putExtra(Constants.SOURCE, Constants.OFFER_DEALS)
                }
                startActivity(intent)
            }

            R.id.iv_cart -> {
                navigateToCartActivity()
            }

            R.id.et_search_products -> {
                navigateToSearchActivity()
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
        if (multiStateView != null) {
            multiStateView.viewState = state
        }
    }

    private fun navigateToSearchActivity() {
        startActivity(Intent(this, SearchActivity::class.java))
    }

    private fun setupViewPagerAutoRatate() {
        handler = Handler()
        Timer().schedule(MyTimerTask(handler!!, MyRunnableThread()), 2000, 2000)
        main_banner_pager.addOnPageChangeListener(this)
    }

    override fun onPageScrollStateChanged(p0: Int) {

    }

    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

    }

    override fun onPageSelected(p0: Int) {

    }

    private fun navigateToCartActivity() {
        startActivity(Intent(this, MyCartActivity::class.java))
    }

    inner class MyRunnableThread : Runnable {
        override fun run() {
            if (mainBannerCurrentPage == mainBannerAdapter.count) {
                mainBannerCurrentPage = 0
            }
            main_banner_pager.setCurrentItem(mainBannerCurrentPage++, true)
        }
    }
}
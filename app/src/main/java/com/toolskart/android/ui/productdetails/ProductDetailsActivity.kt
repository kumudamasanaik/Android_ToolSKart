package com.toolskart.android.ui.productdetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import com.toolskart.android.R
import com.toolskart.android.api.LiveNetworkMonitor
import com.toolskart.android.constants.Constants
import com.toolskart.android.customview.ScreenStateView
import com.toolskart.android.interfaces.IAdapterClickListener
import com.toolskart.android.model.*
import com.toolskart.android.model.inputmodel.ModifyCartIp
import com.toolskart.android.ui.base.BaseActivity
import com.toolskart.android.ui.base.adapter.BaseRecAdapter
import com.toolskart.android.ui.mycart.MyCartActivity
import com.toolskart.android.ui.review.ReviewActivity
import com.toolskart.android.utils.AppSharedPreference
import com.toolskart.android.utils.CommonUtils
import com.toolskart.android.utils.Validation
import com.toolskart.android.utils.showToastMsg
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.product_details_activity.*
import kotlinx.android.synthetic.main.rating_button.*

class ProductDetailsActivity : BaseActivity(), IAdapterClickListener, View.OnClickListener, ProductDetailsContract.View {

    private lateinit var reviewsAdapter: BaseRecAdapter
    private lateinit var relatedImagesAdapter: BaseRecAdapter
    private lateinit var productDetailAdapter: BaseRecAdapter
    private var mContext: Context? = null
    private lateinit var presenter: ProductDetailsPresenter
    private lateinit var snackbbar: View
    var productRespData: ArrayList<Product>? = null
    var cartSummary: CartSummary? = null
    private var productId: String = ""
    private lateinit var source: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.product_details_activity, fragmentLayout)
        mContext = this

        if (intent != null) {
            source = intent.getStringExtra(Constants.SOURCE)
            if (source.contentEquals(Constants.HOME)) {
                productId = intent.getStringExtra(Constants.FEATURED_PRODUCTS)
            } else if (source.contentEquals(Constants.PRODUCT_LIST_FRAGMENT)) {
                productId = intent.getStringExtra(Constants.PRODUCT_LIST)
            }
        }
        init()
    }

    override fun init() {
        snackbbar = product_detail_snack_bar
        tvTitle?.text = getString(R.string.product_details)
        presenter = ProductDetailsPresenter(this)
        empty_button.setOnClickListener { callProductDetailScreenApi() }
        error_button.setOnClickListener { callProductDetailScreenApi() }
        showBack()
        setUpRelatedProductImages()
        setUpReviewsDetails()
        btn_review.setOnClickListener(this)
        iv_cart!!.setOnClickListener(this)
        image_wish_list.setOnClickListener(this)
        btn_add_to_cart.setOnClickListener(this)
        btn_go_to_cart.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        callProductDetailScreenApi()
    }

    private fun setUpRelatedProductImages() {
        val linearLayout = LinearLayoutManager(this, LinearLayout.HORIZONTAL, false)
        relatedImagesAdapter = BaseRecAdapter(mContext!!, R.layout.product_pictures, adapterClickListener = this)
        product_dummy_images.apply {
            layoutManager = linearLayout
            adapter = relatedImagesAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun setUpReviewsDetails() {
        val linearLayout = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        reviewsAdapter = BaseRecAdapter(mContext!!, R.layout.item_view_all_reviews_rec, adapterClickListener = this)
        rv_review_all.apply {
            layoutManager = linearLayout
            adapter = reviewsAdapter
            isNestedScrollingEnabled = false
        }
    }

    override fun callProductDetailScreenApi() {
        if (LiveNetworkMonitor(this).isConnected()) {
            showViewState(ScreenStateView.VIEW_STATE_LOADING)
            presenter.callProductDetailApi(productId)
        } else {
            showMsg(getString(R.string.error_no_internet))
            showViewState(ScreenStateView.VIEW_STATE_ERROR)
        }
    }

    override fun setProductDetailRes(responce: ProductDetailResp) {
        if (Validation.isValidStatus(responce) && Validation.isValidObject(responce)) {
            productRespData = responce.product
            cartSummary = responce.cart!!
            showViewState(ScreenStateView.VIEW_STATE_CONTENT)
            AppSharedPreference.saveCartData(cartSummary)
            if (productRespData!!.isNotEmpty()) {
                setUpWishListDetail(productRespData, image_wish_list)
                setData()
            } else
                showViewState(ScreenStateView.VIEW_STATE_EMPTY)
        } else
            showViewState(ScreenStateView.VIEW_STATE_ERROR)
    }

    private fun setData() {
        if (productRespData!![0].review!!.isNotEmpty())
            reviewsAdapter.addList(this.productRespData!![0].review!!)

        if (productRespData!![0].pic!!.isNotEmpty())
            relatedImagesAdapter.addList(this.productRespData!![0].pic!!)

        if (productRespData!![0].sku!![0].mycart!! > 0.toString()) {
            btn_go_to_cart.visibility = View.VISIBLE
        } else btn_add_to_cart.visibility = View.VISIBLE

        productRespData?.apply {
            //ImageLoader.setImage(product_image, productRespData!![0].pic!![0]!!.pic!!)
            CommonUtils.setUserImage(this@ProductDetailsActivity, pro_image, productRespData!![0].pic!![0]!!.pic!!)

            tv_product_tittle.text = productRespData!![0].name
            rating_star.text = productRespData!![0].avg_rating.toString()
            tv_total_rating.text = productRespData!![0].total_rating.toString()
            if (productRespData!![0].sku != null) {
                tv_price.text = CommonUtils.getRupeesSymbol(mContext!!, productRespData!![0].sku!![0]!!.mrp.toString())
            } else {
                tv_price.visibility = View.GONE
            }
        }
        setRatingBar()
    }

    private fun setRatingBar() {
        rating_bar.rating = productRespData!![0].avg_rating!!.toFloat()
        rating_bar.isIndicator = true
        tv_no_of_customers.text = productRespData!![0].total_rating!!.toString()
        tv_five_star.text = productRespData!![0].ratingtest!!.value5.toString() + "%"
        tv_four_star.text = productRespData!![0].ratingtest!!.value4.toString() + "%"
        tv_three_star.text = productRespData!![0].ratingtest!!.value3.toString() + "%"
        tv_two_star.text = productRespData!![0].ratingtest!!.value2.toString() + "%"
        tv_one_star.text = productRespData!![0].ratingtest!!.value1.toString() + "%"

        pb_five_star.progress = productRespData!![0].ratingtest!!.value5!!
        pb_four_star.progress = productRespData!![0].ratingtest!!.value4!!
        pb_three_star.progress = productRespData!![0].ratingtest!!.value3!!
        pb_two_star.progress = productRespData!![0].ratingtest!!.value2!!
        pb_one_star.progress = productRespData!![0].ratingtest!!.value1!!
        tx_no_of_reviews.text = "(" + productRespData!![0].total_rating.toString() + ")"
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_review -> {
                if (CommonUtils.isUserLogin()) {
                    if (LiveNetworkMonitor(this.mContext!!).isConnected()) {
                        val intent = Intent(mContext, ReviewActivity::class.java)
                        intent.putExtra(Constants.SOURCE, Constants.PRODUCT_DETAIL)
                        intent.putExtra(Constants.PRODUCT_ID, this.productRespData!![0]._id)
                        startActivity(intent)
                    } else
                        showMsg(getString(R.string.error_no_internet))
                } else {
                    showMsg(getString(R.string.Please_login_to_the_application))
                }
            }

            R.id.iv_cart -> {
                if (CommonUtils.isUserLogin())
                    startActivity(Intent(this, MyCartActivity::class.java))
                else
                    showMsg(getString(R.string.Please_login_to_the_application))
            }

            R.id.image_wish_list -> {
                if (CommonUtils.isUserLogin()) {
                    if (LiveNetworkMonitor(this.mContext!!).isConnected()) {
                        showLoader()
                        if (productRespData!![0].wishlist == 0)
                            presenter.callCreateWishList(Constants.CREATE, productRespData!![0].id_product!!)
                        else
                            presenter.callDeleteWishList(Constants.DELETE, productRespData!![0].id_product!!)
                    } else
                        showMsg(getString(R.string.error_no_internet))
                } else
                    showMsg(getString(R.string.Please_login_to_the_application))
            }

            R.id.btn_add_to_cart -> {
                if (CommonUtils.isUserLogin()) {
                    if (LiveNetworkMonitor(this.mContext!!).isConnected()) {
                        showLoader()
                        presenter.callAddToCart(
                                ModifyCartIp(
                                        id_product = productRespData!![0].id_product,
                                        _id = CommonUtils.getCustomerID(),
                                        _session = CommonUtils.getSession(),
                                        id_sku = productRespData!![0].sku!![0]._id,
                                        op = Constants.CREATE,
                                        wh_pincode = "",
                                        quantity = cartSummary!!.cart_count + 1,
                                        lang = "en"))
                    } else
                        showMsg(getString(R.string.error_no_internet))
                } else
                    showMsg(getString(R.string.Please_login_to_the_application))
            }

            R.id.btn_go_to_cart ->
                startActivity(Intent(mContext, MyCartActivity::class.java))
        }
    }

    override fun setCreateWishListResp(responce: WishListResp) {
        hideLoader()
        if (Validation.isValidStatus(responce)) {
            productRespData = responce.result
            setUpWishListDetail(productRespData, image_wish_list)
            callProductDetailScreenApi()
        } else
            showViewState(ScreenStateView.VIEW_STATE_ERROR)
    }

    override fun setDeleteWishListResp(responce: WishListResp) {
        hideLoader()
        if (Validation.isValidStatus(responce)) {
            productRespData = responce.result
            image_wish_list!!.setImageResource(R.drawable.ic_wish_list)
            callProductDetailScreenApi()
        } else
            showViewState(ScreenStateView.VIEW_STATE_ERROR)
    }

    override fun setAddTOCartResp(res: CartRes) {
        hideLoader()
        if (Validation.isValidStatus(res)) {
            btn_add_to_cart.visibility = View.GONE
            callProductDetailScreenApi()
        } else
            showViewState(ScreenStateView.VIEW_STATE_ERROR)
    }

    private fun setUpWishListDetail(item: ArrayList<Product>?, image_wish_list: AppCompatImageView?) {
        if (item!![0].wishlist == 1) image_wish_list!!.setImageResource(R.drawable.ic_wish_list_selected)
        else image_wish_list!!.setImageResource(R.drawable.ic_wish_list)
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
        if (product_detail_multiStateView != null)
            product_detail_multiStateView.viewState = state
    }

    override fun onclick(data: Any, pos: Int, type: String, op: String) {
        if (data is Pic)
        when (op) {
            Constants.SIMILAR_PRODUCT_IMAGE -> {
                CommonUtils.setUserImage(this@ProductDetailsActivity, pro_image, productRespData!![0].pic!![pos]!!.pic!!)//todo check this by adding more products to pic array and set that image in the view
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
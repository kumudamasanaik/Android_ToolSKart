package com.toolskart.android.ui.review

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.toolskart.android.R
import com.toolskart.android.api.LiveNetworkMonitor
import com.toolskart.android.constants.Constants
import com.toolskart.android.customview.ScreenStateView
import com.toolskart.android.model.CommonRes
import com.toolskart.android.model.inputmodel.RatingFeedbackInput
import com.toolskart.android.ui.base.BaseActivity
import com.toolskart.android.utils.CommonUtils
import com.toolskart.android.utils.Validation
import com.toolskart.android.utils.showToastMsg
import kotlinx.android.synthetic.main.activity_review.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*

class ReviewActivity : BaseActivity(), ReviewContract.view, View.OnClickListener {

    private var mContext: Context? = null
    private lateinit var source: String
    private lateinit var presenter: ReviewPresenter
    private lateinit var snackbbar: View
    private var productId: String = ""
    var ratingFeedbackInput = RatingFeedbackInput()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_review, fragmentLayout)
        mContext = this
        if (intent != null) {
            source = intent.getStringExtra(Constants.SOURCE)
            productId = intent.getStringExtra(Constants.PRODUCT_ID)
        }
        init()
    }

    override fun init() {
        snackbbar = review_snack_bar
        tvTitle?.text = getString(R.string.review)
        empty_button.setOnClickListener { callReview() }
        error_button.setOnClickListener { callReview() }
        showBack()
        hideCartView()
        presenter = ReviewPresenter(this)
        btn_submit.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_submit -> {
                callReview()
            }
        }
    }

    override fun callReview() {
        ratingFeedbackInput = RatingFeedbackInput(_id = CommonUtils.getCustomerID(),id_product = productId,
                rating = rating_bar.rating.toString(), review = ed_review.text.toString())

        if (LiveNetworkMonitor(this).isConnected()) {
            showViewState(ScreenStateView.VIEW_STATE_LOADING)
            presenter.callReviewApi(ratingFeedbackInput)
        } else {
            showMsg(getString(R.string.error_no_internet))
            showViewState(ScreenStateView.VIEW_STATE_ERROR)
        }
    }

    override fun setcallReviewRes(response: CommonRes) {
        if (Validation.isValidStatus(response)) {
            showViewState(ScreenStateView.VIEW_STATE_CONTENT)
            showMsg(response.message)
            finish()
        } else {
            showViewState(ScreenStateView.VIEW_STATE_EMPTY)
            showViewState(ScreenStateView.VIEW_STATE_ERROR)
        }
    }

    override fun showInValidFiledMsg(msg: String) {
        when (msg) {
            getString(R.string.err_please_enter_valid_message) -> showMsg(msg)
        }
    }

    override fun getContext(): Context = this

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
        if (review_multiStateView != null)
            review_multiStateView.viewState = state
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
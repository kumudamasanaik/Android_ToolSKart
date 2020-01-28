package com.toolskart.android.ui.orderdetails

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.toolskart.android.R
import com.toolskart.android.api.LiveNetworkMonitor
import com.toolskart.android.constants.Constants
import com.toolskart.android.customview.ScreenStateView
import com.toolskart.android.model.CommonRes
import com.toolskart.android.model.OrderDetailRes
import com.toolskart.android.ui.base.BaseActivity
import com.toolskart.android.utils.CommonUtils
import com.toolskart.android.utils.Validation
import kotlinx.android.synthetic.main.activity_view_order_details.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class OrderDetailsActivity : BaseActivity(), OrderDetailContract.view {
    private val TAG = "OrderDetailsActivity"
    private var mContext: Context? = null
    private lateinit var source: String
    private lateinit var order_id: String
    private lateinit var presenter: OrderDetailPresenter
    private lateinit var cur_order: OrderDetailRes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_view_order_details, fragmentLayout)
        mContext = this
        if (intent != null) {
            source = intent.getStringExtra(Constants.SOURCE)
            order_id = intent.getStringExtra(Constants.ORDER_ID)
        }
        init()
    }

    override fun init() {
        presenter = OrderDetailPresenter(this)
        tvTitle?.text = getString(R.string.view_order_detail)
        showBack()
        hideCartView()
        updateUI()
        callOrderDetails()
    }

    private fun updateUI() {
        source.apply {
            if (source.contentEquals(Constants.PLACED_FRAGMENT)) {
                btn_return.visibility = View.GONE
            } else if (source.contentEquals(Constants.DELIVERED_FRAGMENT)) {
                btn_cancel_order.visibility = View.GONE
            } else {
                btn_return.visibility = View.GONE
                btn_cancel_order.visibility = View.GONE
            }
        }
    }

    override fun callOrderDetails() {
        if (LiveNetworkMonitor(this.mContext!!).isConnected()) {
            showViewState(ScreenStateView.VIEW_STATE_LOADING)
            presenter.callMyOrderDetailsApi(order_id)
        } else {
            showMsg(getString(R.string.error_no_internet))
            showViewState(ScreenStateView.VIEW_STATE_ERROR)
        }
    }

    override fun setOrderDetailsResp(res: OrderDetailRes) {
        if (Validation.isValidObject(res)) {
            showViewState(ScreenStateView.VIEW_STATE_CONTENT)
            cur_order = res
            setOrderData()// todo api response is has to change then setorderdata()
        } else
            showViewState(ScreenStateView.VIEW_STATE_ERROR)
    }

    private fun setOrderData() {
        // item_name.text = cur_order.order!!.first_name
        // tv_delivery_estimate_date.text = cur_order.order!!.delivery_slot_time
        setDate()
        tv_price.text = CommonUtils.getRupeesSymbol(this, "\t" + cur_order.order!!.total_selling_price!!)
        tv_quantity_ordered.text = cur_order.order!!.total_items
        //tv_order_date.text = cur_order.order!!.placed_on
        tv_item_order_id.text = cur_order.order!!.order_no
        tv_total_price.text = CommonUtils.getRupeesSymbol(this, "\t" + cur_order.order!!.total_selling_price)
        tv_no_of_items.text = "(" + cur_order.order!!.total_items + ")"
        tv_total_Price.text = cur_order.order!!.total_selling_price
        tv_shipping.text = cur_order.order!!.total_mrp
        tv_order_total.text = cur_order.order!!.total_paid
        // tv_addresss.text = cur_order.address!!.address1
    }

    private fun setDate() {
        if (cur_order.order!!.delivery_slot_time!!.isNotEmpty()) {
            val input = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val output = SimpleDateFormat("MM/dd/yyyy")
            var d: Date? = null
            try {
                d = input.parse(cur_order.order!!.delivery_slot_time)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            val formatted = output.format(d)
            Log.i("DATE", "" + formatted)
            tv_delivery_estimate_date.text = formatted
        }
    }

    override fun setReturnApiRes(res: CommonRes) {

    }

    override fun showMsg(msg: String?) {

    }

    override fun showLoader() {
        CommonUtils.showLoading(this.mContext!!, "", true)
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
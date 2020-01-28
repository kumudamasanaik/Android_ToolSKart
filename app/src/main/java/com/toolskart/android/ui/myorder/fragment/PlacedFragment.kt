package com.toolskart.android.ui.myorder.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.toolskart.android.R
import com.toolskart.android.api.LiveNetworkMonitor
import com.toolskart.android.constants.Constants
import com.toolskart.android.customview.ScreenStateView
import com.toolskart.android.interfaces.IAdapterClickListener
import com.toolskart.android.model.MyOrderRes
import com.toolskart.android.model.Order
import com.toolskart.android.ui.base.adapter.BaseRecAdapter
import com.toolskart.android.ui.orderdetails.OrderDetailsActivity
import com.toolskart.android.utils.CommonUtils
import com.toolskart.android.utils.Validation
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.my_order_list_fragment.*

class PlacedFragment : Fragment(), IAdapterClickListener, MyOrderContract.View {

    private var mContext: Context? = null
    private lateinit var orderListAdapter: BaseRecAdapter
    private lateinit var snackbbar: View
    private lateinit var presenter: MyOrderPresenter
    private lateinit var orderlist: MyOrderRes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.my_order_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun init() {
        snackbbar = this.my_order_snackbar
        presenter = MyOrderPresenter(this)

        empty_button.setOnClickListener { callMyOrder() }
        error_button.setOnClickListener { callMyOrder() }
        setupOrderListFragmentRecyclerView()
        callMyOrder()
    }

    private fun setupOrderListFragmentRecyclerView() {
        val linearLayout = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        orderListAdapter = BaseRecAdapter(mContext!!, R.layout.items_my_order, adapterClickListener = this,requestType = Constants.PLACED_FRAGMENT)
        rv_my_orders.apply {
            layoutManager = linearLayout
            adapter = orderListAdapter
        }
    }

    override fun callMyOrder() {
        if (LiveNetworkMonitor(this.mContext!!).isConnected()) {
            showViewState(ScreenStateView.VIEW_STATE_LOADING)
            presenter.callMyOrderApi(Constants.PLACED)
        } else {
            showMsg(getString(R.string.error_no_internet))
            showViewState(ScreenStateView.VIEW_STATE_ERROR)
        }
    }

    override fun setMyOrderResp(res: MyOrderRes) {
        if (Validation.isValidStatus(res) && Validation.isValidObject(res)) {
            showViewState(ScreenStateView.VIEW_STATE_CONTENT)
            orderlist = res
            setData()
        } else
            showViewState(ScreenStateView.VIEW_STATE_ERROR)
    }

    private fun setData() {
        orderlist.result?.apply {
            if (orderlist.result!!.isNotEmpty()) {
                orderListAdapter.addList(orderlist.result!!)
            } else
                showViewState(ScreenStateView.VIEW_STATE_EMPTY)
        }
    }

    private fun cancelMyOrder() {
        if (LiveNetworkMonitor(this.mContext!!).isConnected()) {
            showViewState(ScreenStateView.VIEW_STATE_LOADING)
            presenter.callCancelApi(Constants.CANCELLED, orderlist.result!![0].order_id!!)
        } else {
            showMsg(getString(R.string.error_no_internet))
            showViewState(ScreenStateView.VIEW_STATE_ERROR)
        }
    }

    override fun setCancelApiRes(res: MyOrderRes) {
        if (Validation.isValidStatus(res)) {
            callMyOrder()
        } else
            showViewState(ScreenStateView.VIEW_STATE_EMPTY)
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onclick(data: Any, pos: Int, type: String, op: String) {
        if (Validation.isValidObject(data) && data is Order) {
            when (type) {
                Constants.CANCEL -> {
                    CommonUtils.showConfirmationDialogPopup(this.context!!, this, getString(R.string.do_you_want_to_cancel_order), data, pos)
                }

                Constants.ORDER_CANCELLED_CONFIRMATION -> {
                    cancelMyOrder()
                }

                Constants.PLACED -> {
                    val intent = Intent(mContext, OrderDetailsActivity::class.java)
                    intent.putExtra(Constants.SOURCE, Constants.PLACED_FRAGMENT)
                    intent.putExtra(Constants.ORDER_ID, orderlist.result!![pos].order_id)
                    startActivity(intent)
                }
            }
        }
    }
}
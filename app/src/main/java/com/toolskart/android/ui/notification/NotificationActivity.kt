package com.toolskart.android.ui.notification

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import com.toolskart.android.R
import com.toolskart.android.api.LiveNetworkMonitor
import com.toolskart.android.customview.ScreenStateView
import com.toolskart.android.interfaces.IAdapterClickListener
import com.toolskart.android.model.CustomerRes
import com.toolskart.android.ui.base.BaseActivity
import com.toolskart.android.ui.base.adapter.BaseRecAdapter
import com.toolskart.android.utils.CommonUtils
import com.toolskart.android.utils.Validation
import com.toolskart.android.utils.showToastMsg
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_notification.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*

class NotificationActivity : BaseActivity(), IAdapterClickListener, NotificationContract.View {

    private lateinit var productDetailAdapter: BaseRecAdapter
    private var mContext: Context? = null
    private lateinit var presenter: NotificationPresenter
    private lateinit var notificationListRes: CustomerRes
    private lateinit var snackbbar: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_notification, fragmentLayout)
        mContext = this
        init()
    }

    override fun init() {
        tvTitle?.text = getString(R.string.notifications)
        presenter = NotificationPresenter(this)
        snackbbar = notification_snack_bar

        empty_button.setOnClickListener { callNotification() }
        error_button.setOnClickListener { callNotification() }
        hideCartView()
        setUpRecyclerViewData()
        //callNotification()
    }

    private fun setUpRecyclerViewData() {
        val linearLayout = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        productDetailAdapter = BaseRecAdapter(mContext!!, R.layout.items_notification, adapterClickListener = this)
        rv_notification.apply {
            layoutManager = linearLayout
            adapter = productDetailAdapter
            isNestedScrollingEnabled = false
        }
    }

    override fun callNotification() {
        if (LiveNetworkMonitor(this).isConnected()) {
            showViewState(ScreenStateView.VIEW_STATE_LOADING)
            presenter.callNotificationApi()
        } else {
            showMsg(getString(R.string.error_no_internet))
            showViewState(ScreenStateView.VIEW_STATE_ERROR)
        }
    }

    override fun setNotificationResp(res: CustomerRes) {
        if (Validation.isValidStatus(res) && Validation.isValidObject(res)) {
            showViewState(ScreenStateView.VIEW_STATE_CONTENT)
            notificationListRes = res
            if (Validation.isValidList(res.result)) {
                setupData()
            } else
                showViewState(ScreenStateView.VIEW_STATE_EMPTY)
        } else
            showViewState(ScreenStateView.VIEW_STATE_ERROR)
    }

    private fun setupData() {

    }

    override fun onclick(data: Any, pos: Int, type: String, op: String) {

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
}
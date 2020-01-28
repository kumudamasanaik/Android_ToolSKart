package com.toolskart.android.ui.transactionhistory

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.widget.LinearLayout
import com.toolskart.android.R
import com.toolskart.android.interfaces.IAdapterClickListener
import com.toolskart.android.ui.base.BaseActivity
import com.toolskart.android.ui.base.adapter.BaseRecAdapter
import kotlinx.android.synthetic.main.activity_transaction_history.*

class TransactionHistoryActivity : BaseActivity(), IAdapterClickListener {

    private lateinit var transactionHistoryAdapter: BaseRecAdapter
    private var mContext: Context? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_transaction_history, fragmentLayout)
        mContext = this
        init()
    }

    private fun init() {
        showBack()
        tvTitle?.text = getString(R.string.transaction_history)
        hideCartView()
        setUpRecyclerViewData()
    }

    private fun setUpRecyclerViewData() {
        val linearLayout = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        transactionHistoryAdapter = BaseRecAdapter(mContext!!, R.layout.item_transaction_history_rec, adapterClickListener = this)
        rv_transaction_history.apply {
            layoutManager = linearLayout
            adapter = transactionHistoryAdapter
        }
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
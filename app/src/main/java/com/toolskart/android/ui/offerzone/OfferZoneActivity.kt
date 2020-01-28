package com.toolskart.android.ui.offerzone

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.MenuItem
import android.view.View
import com.toolskart.android.R
import com.toolskart.android.interfaces.IAdapterClickListener
import com.toolskart.android.ui.base.BaseActivity
import com.toolskart.android.ui.base.adapter.BaseRecAdapter
import com.toolskart.android.ui.mycart.MyCartActivity
import kotlinx.android.synthetic.main.activity_offer_zone.*

class OfferZoneActivity : BaseActivity(), View.OnClickListener, IAdapterClickListener {

    private var mContext: Context? = null
    private lateinit var offerZoneAdapter: BaseRecAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_offer_zone, fragmentLayout)
        mContext = this
        init()
    }

    private fun init() {
        tvTitle?.text = getString(R.string.offer_and_deals)
        iv_cart!!.setOnClickListener(this)
        setUprecyclerViewData()
        showBack()

    }

    private fun setUprecyclerViewData() {
        offerZoneAdapter = BaseRecAdapter(mContext!!, R.layout.partial_offer_zone_details, adapterClickListener = this)
        rv_offer_zone.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        rv_offer_zone.adapter = offerZoneAdapter

    }

    override fun onclick(data: Any, pos: Int, type: String, op: String) {


    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_cart -> {
                startActivity(Intent(this, MyCartActivity::class.java))
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
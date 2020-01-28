package com.toolskart.android.ui.myorder

import android.content.Context
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View
import com.toolskart.android.R
import com.toolskart.android.ui.base.BaseActivity
import com.toolskart.android.ui.base.adapter.ViewPagerAdapter
import com.toolskart.android.ui.myorder.fragment.CancelledFragment
import com.toolskart.android.ui.myorder.fragment.DeliveredFragment
import com.toolskart.android.ui.myorder.fragment.PlacedFragment
import kotlinx.android.synthetic.main.activity_my_order.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.partial_tab_layout.*

class MyOrderActivity : BaseActivity(), ViewPager.OnPageChangeListener {

    private lateinit var mContext: Context
    private var cur_pos: Int = 0
    private var first: Boolean = true
    private lateinit var pagerAdapetr: ViewPagerAdapter
    private lateinit var snackbbar: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_my_order, fragmentLayout)
        init()
        mContext = this
    }

    private fun init() {
        tvTitle?.text = getString(R.string.my_orders)
        snackbbar = myorder_snackbar
        empty_button.setOnClickListener { }
        error_button.setOnClickListener { }
        setupViewPager()
        hideCartView()
    }

    private fun setupViewPager() {
        pagerAdapetr = ViewPagerAdapter(supportFragmentManager)
        setupTabsHeaders()
        cat_pager.adapter = pagerAdapetr
        tab_layout.setupWithViewPager(cat_pager)
        cat_pager.currentItem = cur_pos
        cat_pager.addOnPageChangeListener(this)
    }

    private fun setupTabsHeaders() {
        pagerAdapetr.apply {
            addList(PlacedFragment(), getString(R.string.placed))
            addList(DeliveredFragment(), getString(R.string.delivered))
            addList(CancelledFragment(), getString(R.string.cancelled))
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(p0: Int) {

    }

    override fun onPageScrollStateChanged(p0: Int) {

    }
}
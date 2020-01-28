package com.toolskart.android.ui.productlist

import android.content.Context
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.MenuItem
import android.view.View
import com.toolskart.android.R
import com.toolskart.android.constants.Constants
import com.toolskart.android.model.Lowcategory
import com.toolskart.android.model.Subcategory
import com.toolskart.android.ui.base.BaseActivity
import com.toolskart.android.ui.base.adapter.ViewPagerAdapter
import com.toolskart.android.ui.productlist.fragment.ProductListFragment
import com.toolskart.android.utils.withNotNullNorEmpty
import kotlinx.android.synthetic.main.activity_offer_supplies.*
import kotlinx.android.synthetic.main.partial_tab_layout.*

class ProductListActivity : BaseActivity(), ViewPager.OnPageChangeListener {

    private var cur_pos: Int = 0
    private lateinit var mContext: Context
    private lateinit var pagerAdapetr: ViewPagerAdapter
    private var source: String = ""
    private lateinit var snackbbar: View
    var mLow_Catgory: ArrayList<Lowcategory>? = null
    var mSub_Category: Subcategory? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_offer_supplies, fragmentLayout)
        mContext = this
        init()
    }

     fun init() {
        showBack()
        snackbbar = product_list_snack_barview


        getIntentData()
        tvTitle?.text = mSub_Category?.name_en

        hideCartView()
        setupViewPager()
    }

    private fun getIntentData() {
        intent?.apply {
            intent.extras?.apply {
                source = intent.getStringExtra(Constants.SOURCE)
                mSub_Category = getParcelableExtra(Constants.LOWCATEGORY)
            }
        }
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
        mSub_Category?.lowcategory.withNotNullNorEmpty {
            mLow_Catgory = filterNotNull() as ArrayList<Lowcategory>
            mLow_Catgory = filter { it.name_en != null && it._id != null } as ArrayList<Lowcategory>
            mLow_Catgory.withNotNullNorEmpty {
                for (tabHeader in this) {
                    pagerAdapetr.addList(ProductListFragment.newInstance(tabHeader), tabHeader.name_en!!)
                }
            }
        }
    }


    override fun onPageScrollStateChanged(p0: Int) {

    }

    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

    }

    override fun onPageSelected(position: Int) {
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
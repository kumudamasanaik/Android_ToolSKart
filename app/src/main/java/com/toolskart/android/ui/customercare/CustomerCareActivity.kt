package com.toolskart.android.ui.customercare

import android.content.Context
import android.os.Bundle
import android.support.v7.content.res.AppCompatResources
import android.view.View
import com.toolskart.android.R
import com.toolskart.android.interfaces.IAdapterClickListener
import com.toolskart.android.ui.base.BaseActivity
import com.toolskart.android.ui.base.adapter.BaseRecAdapter
import kotlinx.android.synthetic.main.activity_customer_care.*

class CustomerCareActivity : BaseActivity(), IAdapterClickListener, View.OnClickListener {
// todo still Api call has to do.......
    private lateinit var customercareAdapter: BaseRecAdapter
    private var mContext: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_customer_care, fragmentLayout)
        init()
    }

    private fun init() {
        mContext = this
        tvTitle?.text = getString(R.string.customer_care)
        hideCartView()
        category.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(this, R.drawable.ic_down_arrow), null)
        //setUpRecyclerView()
        category.setOnClickListener(this)
        btn_submit.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.category -> {
                if (grievance_category_layout.visibility == View.GONE) {
                    grievance_category_layout.visibility = View.VISIBLE
                    category.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(this, R.drawable.ic_down_arrow), null)
                } else {
                    grievance_category_layout.visibility = View.GONE
                    category.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(this, R.drawable.ic_up_arrow), null)
                    view_layout.visibility = View.GONE
                }
            }
            R.id.btn_submit->{

            }
        }
    }

    /*private fun setUpRecyclerView() {
        val linearLayout = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        customercareAdapter = BaseRecAdapter(mContext!!, R.layout.partial_drop_down_layout, adapterClickListener = this, requestType = Constants.ADDRESS)
        rv_grievance_category.apply {
            layoutManager = linearLayout
            adapter = customercareAdapter
        }
    }*/

    override fun onclick(data: Any, pos: Int, type: String, op: String) {

    }

}

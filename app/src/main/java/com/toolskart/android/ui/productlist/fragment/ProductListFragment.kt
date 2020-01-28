package com.toolskart.android.ui.productlist.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toolskart.android.R
import com.toolskart.android.api.LiveNetworkMonitor
import com.toolskart.android.constants.Constants
import com.toolskart.android.customview.ScreenStateView
import com.toolskart.android.interfaces.IAdapterClickListener
import com.toolskart.android.interfaces.OnFragmentInteractionListener
import com.toolskart.android.model.Lowcategory
import com.toolskart.android.model.Product
import com.toolskart.android.model.ProductListResponse
import com.toolskart.android.ui.productdetails.ProductDetailsActivity
import com.toolskart.android.ui.productlist.adapter.ProductListAdapter
import com.toolskart.android.utils.CommonUtils
import com.toolskart.android.utils.Validation
import com.toolskart.android.utils.strikeThr
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.partial_grid_product_list_item.*
import kotlinx.android.synthetic.main.product_list_fragment.*
import kotlinx.android.synthetic.main.product_list_item.*

class ProductListFragment : Fragment(), IAdapterClickListener, ProductListFragmentContract.View, View.OnClickListener {
    private var mActivity: FragmentActivity? = null
    private var mContext: Context? = null
    private var listener: OnFragmentInteractionListener? = null
    //private lateinit var productListAdapter: BaseRecAdapter
    private lateinit var productListAdapter: ProductListAdapter
    private lateinit var productListFragmentPresenter: ProductListFragmentPresenter
    var productRespData: ArrayList<Product>? = null
    private var lowCatId: String? = null
    private var modifiedProd: Product? = null
    private var isGridView: Boolean = false
    private lateinit var fragmentView: View
    private var gridLayoutManager: GridLayoutManager? = null
    private var linearLayoutManager: LinearLayoutManager? = null

    companion object {
        @JvmStatic
        fun newInstance(lowCat: Lowcategory): ProductListFragment {
            val bundle = Bundle()
            bundle.putParcelable(Constants.SUBCAT_ID, lowCat)
            bundle.putString(Constants.LOW_CAT_ID, lowCat._id)
            val fragment = ProductListFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            lowCatId = getString(Constants.LOW_CAT_ID, lowCatId)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContext = context
        mActivity = requireActivity()
        return inflater.inflate(R.layout.product_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun init() {
        productListFragmentPresenter = ProductListFragmentPresenter(this)
        error_button.setOnClickListener { callProductList() }
        empty_button.setOnClickListener { callProductList() }
        filter_layout.setOnClickListener(this)
        listing_image.setBackgroundResource(R.drawable.ic_grid)

        // setUpRecyclerViewData()
        iv_switch_menu.setOnClickListener(this)
        setupProductList()

    }

    override fun onResume() {
        super.onResume()
        callProductList()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.filter_layout -> {

            }

            R.id.iv_switch_menu -> {
                isGridView = !isGridView
                listing_text.text = mContext!!.getString(if (isGridView) R.string.list else R.string.grid)
                if (listing_text.text == getString(R.string.grid))
                    listing_image.setBackgroundResource(R.drawable.ic_grid)
                else
                    listing_image.setBackgroundResource(R.drawable.ic_list)

                updateProductRecyclerview()
            }
        }
    }

    private fun updateProductRecyclerview() {
        initRecyclerView()
        callProductList()
    }

    private fun initRecyclerView() {
        productListAdapter = ProductListAdapter(this.mContext!!,
                if (isGridView) R.layout.partial_grid_product_list_item else R.layout.partial_product_listview_item, this,
                if (isGridView) Constants.GRID_LAYOUT else Constants.LIST_VIEW)
        rv_offer_supplies.layoutManager = if (isGridView) gridLayoutManager else linearLayoutManager
        rv_offer_supplies.adapter = productListAdapter
    }

    private fun setupProductList() {
        linearLayoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        gridLayoutManager = GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false)
        initRecyclerView()
    }

    /* private fun setUpRecyclerViewData() {
         productListAdapter = BaseRecAdapter(mContext!!, R.layout.product_list_item, adapterClickListener = this,requestType = Constants.PRODUCT_LIST_FRAGMENT)
         rv_offer_supplies.layoutManager = GridLayoutManager(mContext, 2)
         rv_offer_supplies.adapter = productListAdapter
     }
 */

    override fun callProductList() {
        if (LiveNetworkMonitor(this.context!!).isConnected()) {
            showViewState(ScreenStateView.VIEW_STATE_LOADING)
            productListFragmentPresenter.callProductListApi(lowCatId!!)
        } else {
            showMsg(getString(R.string.error_no_internet))
            showViewState(ScreenStateView.VIEW_STATE_ERROR)
        }
    }

    override fun setProductListApiRes(productlistRes: ProductListResponse) {
        if (Validation.isValidStatus(productlistRes) && productlistRes.product!!.isNotEmpty()) {
            productRespData = productlistRes.product
            showViewState(ScreenStateView.VIEW_STATE_CONTENT)
            if (productRespData!!.isNotEmpty())
                productListAdapter.addList(productRespData!!)
        } else
            showViewState(ScreenStateView.VIEW_STATE_EMPTY)
    }

    override fun onclick(data: Any, pos: Int, type: String, op: String) {
        if (data is Product) {
            modifiedProd = data
            when (type) {
                Constants.PRODUCT_LIST -> {
                    if (LiveNetworkMonitor(this.context!!).isConnected()) {
                        val intent = Intent(mContext, ProductDetailsActivity::class.java)
                        intent.putExtra(Constants.SOURCE, Constants.PRODUCT_LIST_FRAGMENT)
                        intent.putExtra(Constants.PRODUCT_LIST, data._id)
                        startActivity(intent)
                    } else
                        showMsg(getString(R.string.error_no_internet))
                }
            }
        }
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
        if (multiStateView != null)
            multiStateView.viewState = state
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
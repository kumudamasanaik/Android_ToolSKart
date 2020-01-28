package com.toolskart.android.ui.base.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.toolskart.android.R
import com.toolskart.android.interfaces.IAdapterClickListener
import com.toolskart.android.ui.base.viewholder.*
import com.toolskart.android.ui.customercare.viewholder.CustomerCareViewHolder
import com.toolskart.android.ui.myaddress.viewholder.AddressViewHolder
import com.toolskart.android.ui.productdetails.viewholder.PicViewHolder
import com.toolskart.android.ui.productdetails.viewholder.ReviewsViewHolder
import com.toolskart.android.ui.productlist.viewholder.ProductListViewHolder
import com.toolskart.android.utils.inflate
import com.toolskart.android.utils.withNotNullNorEmpty

class BaseRecAdapter(context: Context, type: Int, var adapterClickListener: IAdapterClickListener? = null, var requestType: String = "none") : RecyclerView.Adapter<BaseViewholder>() {
    var context = context
    var type = type

    var list: ArrayList<*>? = null

    fun addList(list: ArrayList<*>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewholder {
        var view = parent.inflate(type)
        lateinit var holder: BaseViewholder
        when (type) {
            R.layout.item_left_menu -> holder = LeftMenuViewHolder(view, adapterClickListener)
            R.layout.item_featured_product -> holder = FeaturedViewHolder(view, adapterClickListener, requstType = requestType)
            R.layout.category_item -> holder = CategoryViewHolder(view, adapterClickListener)
            R.layout.product_pictures -> holder = PicViewHolder(view, adapterClickListener)
            R.layout.item_view_all_reviews_rec -> holder = ReviewsViewHolder(view, adapterClickListener)
            R.layout.item_offer_details -> holder = ProductListViewHolder(view, adapterClickListener,requestType=requestType)
            R.layout.items_my_order -> holder = MyOrderViewHolder(view, adapterClickListener,requstType = requestType)
            R.layout.items_notification -> holder = FeaturedViewHolder(view, adapterClickListener)
            R.layout.items_shop_by_category -> holder = ShopByCategoryViewHolder(view, adapterClickListener)
            R.layout.sub_categories_items -> holder = SubCategoryViewHolder(view, adapterClickListener)
            R.layout.partial_offer_zone_details -> holder = FeaturedViewHolder(view, adapterClickListener)
            R.layout.item_transaction_history_rec -> holder = TransactionHistoryViewHolder(view, adapterClickListener)
            R.layout.partial_item_transaction_history_rec -> holder = FeaturedViewHolder(view, adapterClickListener)
            R.layout.product_list_item -> holder = ProductListViewHolder(view, adapterClickListener,requestType=requestType)
            R.layout.partial_address_list_item -> holder = AddressViewHolder(view, adapterClickListener)
            R.layout.partial_drop_down_layout -> holder = CustomerCareViewHolder(view, adapterClickListener)

            else -> {
                holder = LeftMenuViewHolder(view, adapterClickListener)
            }
        }
        return holder
    }

    override fun getItemCount(): Int {
        return if (list != null && list!!.size > 0) list!!.size else 0
    }

    override fun onBindViewHolder(holder: BaseViewholder, position: Int) {
        list.withNotNullNorEmpty {
            holder.bind(context, list!![position], position)
            return
        }
        holder.bind(context, holder, position)
    }
}
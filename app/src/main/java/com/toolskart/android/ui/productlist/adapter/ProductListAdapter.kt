package com.toolskart.android.ui.productlist.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.toolskart.android.R
import com.toolskart.android.constants.Constants
import com.toolskart.android.interfaces.IAdapterClickListener
import com.toolskart.android.model.Product
import com.toolskart.android.utils.*
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.partial_grid_product_list_item.*
import kotlinx.android.synthetic.main.partial_grid_product_list_item.view.*
import kotlinx.android.synthetic.main.partial_product_listview_item.view.*
import kotlinx.android.synthetic.main.product_list_item.*
import java.util.*

class ProductListAdapter(var context: Context,
                         var type: Int,
                         var clickListener: IAdapterClickListener,
                         var adapterType: String = "common") : RecyclerView.Adapter<ProductListAdapter.ProductListViewHolder>() {
    internal var list = ArrayList<Product>()
    var modifyProd: Product? = null

    fun addList(list: ArrayList<Product>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListViewHolder {
        val view = parent.inflate(type)
        return ProductListViewHolder(view, clickListener, adapterType)
    }

    override fun getItemCount(): Int = if (list.size > 0) list.size else 0

    override fun onBindViewHolder(holder: ProductListViewHolder, position: Int) {
        if (Validation.isValidObject(list)) holder.bind(context, list[position], position)
    }

    inner class ProductListViewHolder(override val containerView: View, var adapterClickListener: IAdapterClickListener?,
                                      var adapterType: String = "common") : RecyclerView.ViewHolder(containerView),
            LayoutContainer {
        fun bind(context: Context, item: Any, pos: Int) {
            if (Validation.isValidObject(item) && item is Product) {
                when (type) {
                    R.layout.partial_product_listview_item -> {
                        tx_pro_selling_price.strikeThr()
                        if (!(item as Product).pic!![0]?.pic!!?.isBlank() && (item as Product).pic!!?.isNotEmpty())
                            CommonUtils.setUserImage(context, itemView.image_product, (item as Product).pic!![0].pic!!)
                        else
                            ImageLoader.setImage(itemView.image_product, "")

                        if (!item.name.isNullOrEmpty())
                            low_cat_prod_name.text = item.name!!

                        if (!item.brand.isNullOrEmpty())
                            tx_pro_brand.text = item.brand!!

                        if (item.sku != null) {
                            tx_pro_price.text = CommonUtils.getRupeesSymbol(context, item.sku!![0]!!.mrp.toString())
                            tx_pro_selling_price.text = CommonUtils.getRupeesSymbol(context, item.sku!![0]!!.selling_price.toString())
                        } else {
                            tx_pro_price.visibility = View.GONE
                            tx_pro_selling_price.visibility = View.GONE
                        }

                        itemView.setOnClickListener {
                            adapterClickListener?.onclick(item, pos, Constants.PRODUCT_LIST, "")
                        }
                    }
                    R.layout.partial_grid_product_list_item -> {
                        tx_pro_price_grid.strikeThr()
                        if (!(item as Product).pic!![0]?.pic!!?.isBlank() && (item as Product).pic!!?.isNotEmpty())
                            CommonUtils.setUserImage(context, itemView.image_product_grid, (item as Product).pic!![0].pic!!)
                        else
                            ImageLoader.setImage(itemView.image_product_grid, "")

                        if (!item.name.isNullOrEmpty())
                            low_cat_prod_name_grid.text = item.name!!

                        if (!item.brand.isNullOrEmpty())
                            tx_pro_brand_grid.text = item.brand!!

                        if (item.sku != null) {
                            tx_pro_price_grid.text = CommonUtils.getRupeesSymbol(context, item.sku!![0]!!.mrp.toString())
                            tx_pro_selling_price_grid.text = CommonUtils.getRupeesSymbol(context, item.sku!![0]!!.selling_price.toString())
                        } else {
                            tx_pro_price_grid.visibility = View.GONE
                            tx_pro_selling_price_grid.visibility = View.GONE
                        }

                        itemView.setOnClickListener {
                            adapterClickListener?.onclick(item, pos, Constants.PRODUCT_LIST, "")
                        }
                    }
                }
            }
        }
    }
}
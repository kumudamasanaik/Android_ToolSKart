package com.toolskart.android.ui.productlist.viewholder

import android.content.Context
import android.view.View
import com.toolskart.android.constants.Constants
import com.toolskart.android.interfaces.IAdapterClickListener
import com.toolskart.android.model.Product
import com.toolskart.android.model.Sku
import com.toolskart.android.ui.base.viewholder.BaseViewholder
import com.toolskart.android.utils.CommonUtils
import com.toolskart.android.utils.ImageLoader
import com.toolskart.android.utils.strikeThr
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_offer_details.*
import kotlinx.android.synthetic.main.item_offer_details.view.*
import kotlinx.android.synthetic.main.offer.*
import kotlinx.android.synthetic.main.product_list_item.*
import kotlinx.android.synthetic.main.product_list_item.view.*

class ProductListViewHolder(override val containerView: View, var adapterClickListener: IAdapterClickListener?, var requestType: String = "") : BaseViewholder(containerView), LayoutContainer {
    override fun bind(context: Context, item: Any, pos: Int) {
        if (requestType == Constants.PRODUCT_LIST_FRAGMENT || requestType == Constants.SEARCH_PRODUCTS) {
            if (item is Product) {

                if (!(item as Product).pic!![0]?.pic!!?.isBlank() && (item as Product).pic!!?.isNotEmpty())
                    CommonUtils.setUserImage(context, itemView.item_img, (item as Product).pic!![0].pic!!)
                else
                    ImageLoader.setImage(itemView.item_img, "")

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
        }

        if (requestType == Constants.FEATURED_PRODUCTS) {
            if (item is Product) {
                pro_price.strikeThr()
              //  offersAndSavings(item.selSku)
                if (!(item as Product).pic!![0]?.pic!!?.isBlank() && (item as Product).pic!!?.isNotEmpty())
                    CommonUtils.setUserImage(context, itemView.img_pro, (item as Product).pic!![0].pic!!)
                else
                    ImageLoader.setImage(itemView.img_pro, "")

                if (!item.name.isNullOrEmpty())
                    tv_pro_name.text = item.name!!

                if (!item.brand.isNullOrEmpty())
                    brand_name.text = item.brand

                if (item.sku != null) {
                    pro_price.text = CommonUtils.getRupeesSymbol(context, item.sku!![0]!!.mrp.toString())
                    selling_price.text = CommonUtils.getRupeesSymbol(context, item.sku!![0]!!.selling_price.toString())
                } else {
                    pro_price.visibility = View.GONE
                    selling_price.visibility = View.GONE
                }

                /*if (!item.selSku!!.pack_size!!.isNotEmpty())
                    pack_size.text = item.selSku!!.pack_size
*/
                /* if (!(item.selling_price!!.toString().isEmpty() && item.mrp!!.toString().isEmpty())) {
                     saving_amount_layout.visibility = View.VISIBLE
                     val mrp=item.mrp!!.toString()
                     val selling_price=item.selling_price!!.toString()
                     val savings = mrp-selling_price
                     tx_savings.text = savings.toString()
                 } else {
                     saving_amount_layout.visibility = View.GONE
                 }
 */


                itemView.setOnClickListener {
                    adapterClickListener?.onclick(item, pos, Constants.FEATURED_PRODUCTS, "")
                }
            }
        }
    }

    //todo need to check offer
   /* private fun offersAndSavings(item: Sku?) {
        item.apply {
            val mrp = item?.mrp
            val sellingPrice = item?.selling_price
            if (mrp != null && mrp > 0 && sellingPrice != null) {
                val savingsPer = (((mrp - sellingPrice) / mrp) * 100)
                val savings = mrp - sellingPrice
                assignProdAmt(sellingPrice, mrp, savings, savingsPer)
            }
        }
    }

    private fun assignProdAmt(sellingPrice: Float, mrp: Float, savings: Float, savingsPer: Float) {
        offer.apply {
            text = CommonUtils.getRupeesSymbol(context, "%Of".format(savingsPer))
        }
        tx_savings.apply {
            text = CommonUtils.getRupeesSymbol(context, "%of".format(savings))
        }
    }*/
}
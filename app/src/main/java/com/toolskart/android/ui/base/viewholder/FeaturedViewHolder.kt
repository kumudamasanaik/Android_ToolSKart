package com.toolskart.android.ui.base.viewholder

import android.content.Context
import android.view.View
import com.toolskart.android.constants.Constants
import com.toolskart.android.interfaces.IAdapterClickListener
import com.toolskart.android.model.FeaturedProduct
import com.toolskart.android.model.Offer
import com.toolskart.android.utils.ImageLoader
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_featured_product.*
import kotlinx.android.synthetic.main.item_featured_product.view.*

class FeaturedViewHolder(override val containerView: View, var adapterClickListener: IAdapterClickListener?, var requstType: String = "") : BaseViewholder(containerView), LayoutContainer {
    override fun bind(context: Context, item: Any, pos: Int) {

        if (requstType == Constants.FEATURED_PRODUCTS) {
            if (item is FeaturedProduct) {
                if (item.pic!!.isNotEmpty())
                    ImageLoader.setImage(itemView.iv_product_image, item.pic[0]!!.pic!!)

                if (!item.name.isNullOrEmpty())
                    tv_cat_name.text = item.name!!

                if (!item.description.isNullOrEmpty())
                    tv_product_desc.text = item.description!!

                itemView.setOnClickListener {
                    adapterClickListener?.onclick(item, pos, Constants.FEATURED, "")
                }

            }
        } else if (requstType == Constants.OFFER_DEALS) {
            if (item is Offer) {
                if (!item.image!!.isNullOrEmpty())
                    ImageLoader.setImage(itemView.iv_product_image, item.image)

                if (!item.title.isNullOrEmpty())
                    itemView.tv_cat_name.text = item.title!!

                itemView.setOnClickListener {
                    adapterClickListener?.onclick(item, pos, Constants.OFFER_DEALS, "")
                }
            }
        }
    }
}
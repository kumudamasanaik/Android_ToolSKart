package com.toolskart.android.ui.base.viewholder

import android.content.Context
import android.view.View
import com.toolskart.android.interfaces.IAdapterClickListener
import com.toolskart.android.model.Offer
import com.toolskart.android.utils.ImageLoader
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.category_item.*
import kotlinx.android.synthetic.main.category_item.view.*

class OfferslViewHolder(override val containerView: View, var adapterClickListener: IAdapterClickListener?) : BaseViewholder(containerView), LayoutContainer {
    override fun bind(context: Context, item: Any, pos: Int) {
        if (item is Offer) {
            if (item.image!!.isNotEmpty())
                ImageLoader.setImage(itemView.image, item.image)

            if (!item.title.isNullOrEmpty())
                tv_category_name.text = item.title!!

            itemView.setOnClickListener {

            }
        }
    }
}
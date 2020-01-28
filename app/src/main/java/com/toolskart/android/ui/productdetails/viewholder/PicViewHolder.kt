package com.toolskart.android.ui.productdetails.viewholder

import android.content.Context
import android.view.View
import com.toolskart.android.constants.Constants
import com.toolskart.android.interfaces.IAdapterClickListener
import com.toolskart.android.model.Pic
import com.toolskart.android.ui.base.viewholder.BaseViewholder
import com.toolskart.android.utils.CommonUtils
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.product_pictures.view.*

class PicViewHolder(override val containerView: View, var adapterClickListener: IAdapterClickListener?) : BaseViewholder(containerView), LayoutContainer {
    override fun bind(context: Context, item: Any, pos: Int) {
        if (item is Pic) {
            if (item.pic!!.isNotEmpty())
            // ImageLoader.setImage(itemView.related_images, item.pic)
                CommonUtils.setUserImage(context, itemView.related_images, item.pic)
        }
        itemView.setOnClickListener {
            adapterClickListener?.onclick(data=item,pos= pos, op=Constants.SIMILAR_PRODUCT_IMAGE, type = "")
        }
    }
}
package com.toolskart.android.ui.base.viewholder

import android.content.Context
import android.view.View
import com.toolskart.android.interfaces.IAdapterClickListener
import com.toolskart.android.model.ShopByCategory
import com.toolskart.android.utils.ImageLoader
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.items_shop_by_category.*
import kotlinx.android.synthetic.main.items_shop_by_category.view.*

class ShopByCategoryViewHolder(override val containerView: View, var adapterClickListener: IAdapterClickListener?) : BaseViewholder(containerView), LayoutContainer {
    override fun bind(context: Context, item: Any, pos: Int) {
            if (item is ShopByCategory) {
                if (item.pic!!.isNotEmpty())
                    ImageLoader.setImage(itemView.img_shop_by_cat, item.pic)
                if (item.name!!.isNotEmpty())
                    name_shop_by_cat.text = item.name
            }
    }
}
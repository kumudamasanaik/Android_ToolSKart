package com.toolskart.android.ui.base.viewholder

import android.content.Context
import android.view.View
import com.toolskart.android.constants.Constants
import com.toolskart.android.interfaces.IAdapterClickListener
import com.toolskart.android.model.Subcategory
import com.toolskart.android.utils.ImageLoader
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.sub_categories_items.*
import kotlinx.android.synthetic.main.sub_categories_items.view.*

class SubCategoryViewHolder(override val containerView: View, var adapterClickListener: IAdapterClickListener?) : BaseViewholder(containerView), LayoutContainer {
    override fun bind(context: Context, item: Any, pos: Int) {
        if (item is Subcategory) {
            if (item.pic!!.isNotEmpty())
                ImageLoader.setImage(itemView.hot_category_child_image, item.pic)

            if (!item.name_en.isNullOrEmpty())
                tv_hot_category_child_name.text = item.name_en

            sub_category_data.setOnClickListener {
                adapterClickListener?.onclick(item, pos, Constants.CATEGORY, "")
            }
        }
    }
}
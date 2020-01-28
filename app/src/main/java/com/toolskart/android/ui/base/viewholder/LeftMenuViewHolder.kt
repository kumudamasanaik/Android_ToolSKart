
package com.toolskart.android.ui.base.viewholder

import android.content.Context
import android.view.View
import com.toolskart.android.interfaces.IAdapterClickListener
import com.toolskart.android.ui.navigationdrawer.MenuItemModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_left_menu.*

class LeftMenuViewHolder(override val containerView: View, var adapterClickListener: IAdapterClickListener?) : BaseViewholder(containerView), LayoutContainer {
    override fun bind(context: Context, item: Any, pos: Int) {
        if (item is MenuItemModel) {
            image.setImageResource(item.pic)
            tv_name.text = item.name
            itemView.setOnClickListener {
                adapterClickListener!!.onclick(item, pos, item.name, "")
            }
        }
    }
}
package com.toolskart.android.ui.base.viewholder

import android.content.Context
import android.view.View
import com.toolskart.android.interfaces.IAdapterClickListener
import kotlinx.android.extensions.LayoutContainer

class HotSubChildCategoryViewHolder (override val containerView: View, var adapterClickListener: IAdapterClickListener?) : BaseViewholder(containerView), LayoutContainer {
    override fun bind(context: Context, item: Any, pos: Int) {

        itemView.setOnClickListener {

        }
    }
}
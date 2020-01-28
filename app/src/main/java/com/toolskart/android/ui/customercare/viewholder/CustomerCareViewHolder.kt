package com.toolskart.android.ui.customercare.viewholder

import android.content.Context
import android.view.View
import com.toolskart.android.interfaces.IAdapterClickListener
import com.toolskart.android.ui.base.viewholder.BaseViewholder
import kotlinx.android.extensions.LayoutContainer

class CustomerCareViewHolder(override val containerView: View, var adapterClickListener: IAdapterClickListener?, var requestType: String = "") : BaseViewholder(containerView), LayoutContainer {
    override fun bind(context: Context, item: Any, pos: Int) {
    }
}
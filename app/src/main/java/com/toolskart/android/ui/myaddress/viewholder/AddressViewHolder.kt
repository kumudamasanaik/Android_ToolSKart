package com.toolskart.android.ui.myaddress.viewholder

import android.content.Context
import android.view.View
import com.toolskart.android.constants.Constants
import com.toolskart.android.interfaces.IAdapterClickListener
import com.toolskart.android.model.MyAddress
import com.toolskart.android.ui.base.viewholder.BaseViewholder
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.partial_address_list_item.*

class AddressViewHolder(override val containerView: View, var adapterClickListener: IAdapterClickListener?, var requestType: String = "") : BaseViewholder(containerView), LayoutContainer {
    override fun bind(context: Context, item: Any, pos: Int) {
        if (item is MyAddress) {
            tv_customer_name.text = item.name
            tv_delivery_address.text = item.getAddressString()
        }
        btn_edit.setOnClickListener {
            adapterClickListener?.onclick(item, pos, type=Constants.EDIT_ADDRESS, op = "")
        }

        btn_delete.setOnClickListener {
            adapterClickListener?.onclick(item, pos, type=Constants.DELETE_ADDRESS, op = "")
        }
    }
}
package com.toolskart.android.ui.base.viewholder

import android.content.Context
import android.util.Log
import android.view.View
import com.toolskart.android.constants.Constants
import com.toolskart.android.interfaces.IAdapterClickListener
import com.toolskart.android.model.Order
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.items_my_order.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MyOrderViewHolder(override val containerView: View, var adapterClickListener: IAdapterClickListener?, var requstType: String = "") : BaseViewholder(containerView), LayoutContainer {
    override fun bind(context: Context, item: Any, pos: Int) {
        if (requstType == Constants.CANCELLED_FRAGMENT) {
            btn_cancel.visibility = View.GONE
        } else
            btn_cancel.visibility = View.VISIBLE

        if (item is Order) {
            tv_order_id.text = item.order_id
            tv_quantity.text = item.quantity
            tv_status.text = item.status

            if (item.estimated_delivery!!.isNotEmpty() && item.placed_on!!.isNotEmpty()) {
                val input = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val output = SimpleDateFormat("MM/dd/yyyy")
                var d: Date? = null
                var date: Date? = null
                try {
                    d = input.parse(item.estimated_delivery)
                    date = input.parse(item.placed_on)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
                val formatted = output.format(d)
                val formatted1 = output.format(date)
                Log.i("DATE", "" + formatted)
                tv_estimated_date.text = formatted
                tv_placed_date.text = formatted1
            }
        }

        itemView.setOnClickListener {
            adapterClickListener?.onclick(pos = pos, data = item, type = Constants.PLACED)
            adapterClickListener?.onclick(pos = pos, data = item, type = Constants.DELIVERED)
            adapterClickListener?.onclick(pos = pos, data = item, type = Constants.CANCELLED)
        }

        btn_cancel.setOnClickListener {
            adapterClickListener?.onclick(pos = pos, data = item, type = Constants.CANCEL)
        }
    }
}
package com.toolskart.android.ui.base.viewholder

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import com.toolskart.android.R
import com.toolskart.android.interfaces.IAdapterClickListener
import com.toolskart.android.ui.base.adapter.BaseRecAdapter
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_transaction_history_rec.*

class TransactionHistoryViewHolder(override val containerView: View, var adapterClickListener: IAdapterClickListener?) : BaseViewholder(containerView), LayoutContainer {
    override fun bind(context: Context, item: Any, pos: Int) {
        rv_partial_transaction_history.apply {
            val linearLayout = LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false)
            val transactionHistoryAdapter = BaseRecAdapter(getContext(), R.layout.partial_item_transaction_history_rec, adapterClickListener = adapterClickListener)
            rv_partial_transaction_history.apply {
                layoutManager = linearLayout
                adapter = transactionHistoryAdapter
            }
        }
        /* itemView.setOnClickListener {

         }*/
    }
}
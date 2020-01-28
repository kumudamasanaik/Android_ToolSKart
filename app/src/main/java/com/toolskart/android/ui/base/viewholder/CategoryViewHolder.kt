package com.toolskart.android.ui.base.viewholder

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import com.toolskart.android.R
import com.toolskart.android.interfaces.IAdapterClickListener
import com.toolskart.android.model.Category
import com.toolskart.android.ui.base.adapter.BaseRecAdapter
import com.toolskart.android.utils.ImageLoader
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.category_item.*
import kotlinx.android.synthetic.main.category_item.view.*


class CategoryViewHolder(override val containerView: View, var adapterClickListener: IAdapterClickListener?) : BaseViewholder(containerView), LayoutContainer {
    override fun bind(context: Context, item: Any, pos: Int) {

        if (item is Category) {
            if (item.pic!!.isNotEmpty())
                ImageLoader.setImage(itemView.image, item.pic)

            if (!item.name.isNullOrEmpty())
                tv_category_name.text = item.name!!

            if (item.subcategory!!.isEmpty()) {
                image_drop.visibility = View.GONE
            } else {
                itemView.setOnClickListener {
                    if (recyclerView.visibility == View.GONE && item.subcategory.isNotEmpty()) {
                        recyclerView.visibility = View.VISIBLE
                        image_drop.setImageResource(R.drawable.ic_up_arrow)
                        recyclerView.apply {
                            val linearLayout = LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false)
                            val subCategoryAdapter = BaseRecAdapter(getContext(), R.layout.sub_categories_items, adapterClickListener = adapterClickListener)
                            recyclerView.apply {
                                layoutManager = linearLayout
                                adapter = subCategoryAdapter
                                subCategoryAdapter.addList(item.subcategory)
                            }
                        }
                    }
                    else {
                        recyclerView.visibility = View.GONE
                        image_drop.setImageResource(R.drawable.ic_down_arrow)
                    }
                }
                image_drop.visibility = View.VISIBLE
            }
        }
    }
}
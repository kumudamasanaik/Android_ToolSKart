package com.toolskart.android.ui.home.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toolskart.android.R
import com.toolskart.android.interfaces.IAdapterClickListener
import com.toolskart.android.model.Banner
import com.toolskart.android.utils.ImageLoader
import com.toolskart.android.utils.Validation
import kotlinx.android.synthetic.main.item_only_image_banner.view.*
import java.util.*


class BannerAdapter(var context: Context, var type: Int, var clickListener: IAdapterClickListener? = null) : PagerAdapter() {

    var itemList: ArrayList<*>? = null
    lateinit var itemView: View

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun getCount(): Int {
        return if (itemList != null && itemList!!.size > 0) itemList!!.size else 4
    }

    fun addList(itemList: ArrayList<*>) {
        this.itemList = itemList
        notifyDataSetChanged()
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        itemList.apply {
            when (type) {
                R.layout.item_only_image_banner -> {
                    itemView = LayoutInflater.from(context).inflate(type, container, false)
                    if (Validation.isValidList(itemList)) {
                        val banner = this!![position] as Banner
                        if (!banner.pic.isNullOrBlank())
                            ImageLoader.setImage(itemView.image_banner, banner.pic!!)
                    }
                }
            }
        }
        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
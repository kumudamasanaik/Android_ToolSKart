package com.toolskart.android.ui.productdetails.viewholder

import android.content.Context
import android.util.Log
import android.view.View
import com.toolskart.android.interfaces.IAdapterClickListener
import com.toolskart.android.model.Review
import com.toolskart.android.ui.base.viewholder.BaseViewholder
import com.toolskart.android.utils.CommonUtils
import com.toolskart.android.utils.Validation
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_view_all_reviews_rec.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ReviewsViewHolder(override val containerView: View, var adapterClickListener: IAdapterClickListener?) : BaseViewholder(containerView), LayoutContainer {
    override fun bind(context: Context, item: Any, pos: Int) {
        if (item is Review && item != "") {
            if (item.pic!!.isNotEmpty())
                CommonUtils.setCircularUserImage(context, im_reviewers_image, item.pic)

            if (item.first_name!!.isNotEmpty())
                tv_first_name.text = item.first_name

            if (item.dateadded!!.isNotEmpty()) {
                val input = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val output = SimpleDateFormat("MM/dd/yyyy")
                var d: Date? = null
                try {
                    d = input.parse(item.dateadded)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
                val formatted = output.format(d)
                Log.i("DATE", "" + formatted)
                tv_date.text = formatted
            }
            if (item.review!!.isNotEmpty())
                tv_review.text = item.review

            if (Validation.isValidString(item.first_name))
                no_of_stars.rating = item.rating!!.toFloat()
            no_of_stars.isIndicator = true
        }
    }
}
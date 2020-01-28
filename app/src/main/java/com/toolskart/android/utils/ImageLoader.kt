package com.toolskart.android.utils

import android.support.v7.widget.AppCompatImageView
import com.squareup.picasso.Picasso
import com.toolskart.android.R
import com.toolskart.android.api.ApiConstants

object ImageLoader {

    fun setImage(imageView: AppCompatImageView, imageUrl: String) {
        Picasso.get()
                .load(ApiConstants.IMAGE_BASE_URL + imageUrl)
                .placeholder(R.drawable.dummy)
                .fit()
                .error(R.drawable.dummy)
                .into(imageView)
    }


    fun loadImagesWithoutBaseUrl(imageView: AppCompatImageView, imageUrl: String) {
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.dummy)
                .fit()
                .error(R.drawable.dummy)
                .into(imageView)
    }
}
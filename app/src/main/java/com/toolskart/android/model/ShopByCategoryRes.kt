package com.toolskart.android.model

data class ShopByCategoryRes(
        val result: ArrayList<ShopByCategory>?
) :CommonRes()
    data class ShopByCategory(
            val _id: String?,
            val description: String?,
            val name: String?,
            val pic: String?,
            val updated: String?
    )
package com.toolskart.android.model

import com.toolskart.android.model.CartSummary
import com.toolskart.android.model.CommonRes
import com.toolskart.android.model.Product

data class SearchProductsRes(
        val _session: String?,
        val id_customer: String?,
        val product: ArrayList<Product>?,
        val summary: CartSummary?
) :CommonRes()


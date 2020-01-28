package com.toolskart.android.model.test

import android.os.Parcelable
import com.toolskart.android.model.Category
import kotlinx.android.parcel.Parcelize

data class Test(
        val _session: String?,
        val id_customer: String?,
        val message: String?,
        val result: Result?,
        val status: String?
)

@Parcelize
data class Result(
        val category: ArrayList<Category?>?
) : Parcelable
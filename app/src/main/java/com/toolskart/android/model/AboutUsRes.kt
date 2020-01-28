package com.toolskart.android.model

data class AboutUsRes(
        val result: Result?
):CommonRes()
    data class Result(val aboutus: ArrayList<Aboutus>?)
        data class Aboutus(
                val _id: String?,
                val name: String?,
                val type: String?
        )
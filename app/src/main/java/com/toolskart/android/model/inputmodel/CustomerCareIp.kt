package com.toolskart.android.model.inputmodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize

class CustomerCareIp(
        var _id: String? = null,
        val op: String?,
        val id_address: String? = null,
        val selected: String?=null,
        val name: String?=null,
        val phone: String?=null
) : Parcelable
package com.toolskart.android.model.inputmodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize

class AddressInput(
        var _id: String? = null,
        val op: String?,
        val id_address: String? = null,
        val selected: String?=null,
        val name: String?=null,
        val phone: String?=null,
        val address1: String?=null,
        val pincode: String?=null
) : Parcelable